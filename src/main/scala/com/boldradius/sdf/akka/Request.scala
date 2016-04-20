package com.boldradius.sdf.akka

case class Request(
  sessionId: Long,
  timestamp: Long,
  url: String,
  referrer: String,
  browser: String,
  language: String)
