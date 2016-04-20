package com.boldradius.sdf.akka

import scala.util.Random
import Session._
import System.{currentTimeMillis => now}
import scala.concurrent.duration._


class Session(shortVisit: FiniteDuration, longVisit: FiniteDuration)
  extends ProbabilityUtils{

  // These are consistent throughout the session
  val id:       Long   = now
  val start:    Long   = now
  val referrer: String = randomReferrer
  val browser:  String = randomBrowser
  val language: String = randomLanguage

  // For simplicity, we know a priori the session duration
  private[akka] val duration: FiniteDuration =
    withProbability(0.8, deviate(longVisit), deviate(shortVisit))

  // Produce a new request to a random URL
  def request: Request =
    new Request(id, now, randomUrl, referrer, browser, language)

  override def toString = s"Session($id, $referrer, $browser)"
}

object Session extends ProbabilityUtils {

  private val urlMap     = Map("/" -> 4, "/about" -> 2, "/store" -> 2, "/blog" -> 1, "/help" -> 1)
  private val urls       = distributedList(urlMap)
  private val browsers   = distributedList(Map("chrome" -> 5, "firefox" -> 3, "ie" -> 2))
  private val referrers  = distributedList(Map("google" -> 8, "twitter" -> 1, "facebook" -> 2))
  private val languages  = distributedList(Map("en" -> 5, "fr" -> 5, "es" -> 1))

  private def random[A](list: List[A]): A = list(Random.nextInt(list.length))

  def randomUrl:      String = random(urls)
  def randomBrowser:  String = random(browsers)
  def randomReferrer: String = withProbability(0.98, random(referrers), Random.nextString(10))
  def randomLanguage: String = random(languages)

  // Lazy way of creating a list with a skewed distribution - repeat elements
  def distributedList[A](map: Map[A,Int]): List[A] = {
    for {
      (value, prob) <- map.toList
      i <- 1 to prob
    } yield value
  }

  // Some pages are visited for longer than others
  def randomPageTime(url: String) = {
    val weight = 1.0 / urlMap(url)
    val s = weight * 10
    deviate(s seconds)
  }
}
