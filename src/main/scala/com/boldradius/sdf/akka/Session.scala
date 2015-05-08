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
  val urlMap    = Map("/" -> 4, "/about" -> 2, "/store" -> 2, "/blog" -> 1, "/help" -> 1)
  val urls      = distributedList(urlMap)
  val browsers  = distributedList(Map("chrome" -> 5, "firefox" -> 3, "ie" -> 2))
  val referrers = distributedList(Map("google" -> 8, "twitter" -> 1, "facebook" -> 2))

  def random[A](list: List[A]): A = list(Random.nextInt(list.length))
  def randomUrl      = random(urls)
  def randomBrowser  = random(browsers)
  def randomReferrer =
    if(Random.nextInt(100) < 98)
      random(referrers)
    else
      Random.nextString(10)

  // Lazy way of creating a list with a skewed distribution - repeat elements
  def distributedList[A](map: Map[A,Int]): List[A] = {
    for {
      (value, prob) <- map.toList
      i <- 1 to prob
    } yield value
  }

  val longVisit  = 5 minutes
  val shortVisit = 10 seconds

  // For more interesting data, we insert some deviation in our numbers
  def deviate(duration: FiniteDuration):FiniteDuration = {
    val newDuration = duration * (1.5 - Random.nextDouble)
    FiniteDuration(newDuration.toMillis, MILLISECONDS)
  }

  // 80% of users stay for a long visit, the rest bounce off
  def randomSessionTime =
    if(Random.nextInt(100) < 80) deviate(longVisit)
    else deviate(shortVisit)

  // Some pages are visited for longer than others
  def randomPageTime(url: String) = {
    val weight = 1.0 / urlMap(url)
    val s = weight * 10
    deviate(s seconds)
  }


}