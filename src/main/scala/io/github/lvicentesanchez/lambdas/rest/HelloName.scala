package io.github.lvicentesanchez.lambdas.rest

import argonaut._, Argonaut._

case class HelloName(name: String)

object HelloName {
  implicit val codec: CodecJson[HelloName] = casecodec1(HelloName.apply, HelloName.unapply)("name")
}
