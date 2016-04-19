package com.boldradius.sdf.akka

import scala.util.Random
import Session._
import System.{currentTimeMillis => now}
import scala.concurrent.duration._


class Session {

  // These are consistent throughout the session
  val id = Math.abs(Random.nextLong())
  val referrer = randomReferrer
  val browser = randomBrowser
  val start = now
  val duration = randomSessionTime    // For simplicity, we know a priori the session duration

  // Produce a new request to a random URL
  def request: Request =
    new Request(id, now, randomUrl, referrer, browser)

  override def toString = s"Session($id, $referrer, $browser)"
}

object Session {

  private val longVisit  = 5 minutes
  private val shortVisit = 10 seconds
  private val urlMap    = Map("/" -> 4, "/about" -> 2, "/store" -> 2, "/blog" -> 1, "/help" -> 1)
  private val urls      = distributedList(urlMap)
  private val browsers  = distributedList(Map("chrome" -> 5, "firefox" -> 3, "ie" -> 2))
  private val referrers = distributedList(Map("google" -> 8, "twitter" -> 1, "facebook" -> 2))

  private def random[A](list: List[A]): A = list(Random.nextInt(list.length))
  private def withProbability[A](probability: Double, positive: A, negative: A): A =
    if(Random.nextDouble() < probability){
      positive
    } else {
      negative
    }

  def randomUrl:      String  = random(urls)
  def randomBrowser:  String  = random(browsers)
  def randomReferrer: String  = withProbability(0.98, random(referrers), Random.nextString(10))

  // Lazy way of creating a list with a skewed distribution - repeat elements
  def distributedList[A](map: Map[A,Int]): List[A] = {
    for {
      (value, prob) <- map.toList
      i <- 1 to prob
    } yield value
  }

  // For more interesting data, we insert some deviation in our numbers
  def deviate(duration: FiniteDuration):FiniteDuration = {
    val newDuration = duration * (1.5 - Random.nextDouble)
    FiniteDuration(newDuration.toMillis, MILLISECONDS)
  }

  // 80% of users stay for a long visit, the rest bounce off
  def randomSessionTime = withProbability(0.8, deviate(longVisit), deviate(shortVisit))

  // Some pages are visited for longer than others
  def randomPageTime(url: String) = {
    val weight = 1.0 / urlMap(url)
    val s = weight * 10
    deviate(s seconds)
  }


}
