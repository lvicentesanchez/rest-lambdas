package io.github.lvicentesanchez.lambdas.rest

import argonaut.{ Context => _, _ }, Argonaut._
import com.amazonaws.services.lambda.runtime._
import java.io.{ InputStream, OutputStream }
import java.nio.channels.Channels
import jawn.Parser
import jawn.support.argonaut.Parser.facade

final class GetHelloWorld extends RequestStreamHandler {
  def handleRequest(input: InputStream, output: OutputStream, context: Context): Unit = {
    context.getLogger().log(s"Received request with Id: ${context.getAwsRequestId}.")
    output.write("""{ "hello" : "world!" }""".getBytes)
  }
}

final class PostHelloWorld extends RequestStreamHandler {
  def handleRequest(input: InputStream, output: OutputStream, context: Context): Unit = {
    val content: String =
      Parser.parseFromChannel[Json](Channels.newChannel(input))
        .getOrElse(jEmptyObject)
        .as[HelloName]
        .fold((_, _) => "- empty -", _.name)
    context.getLogger().log(s"Received request with Id: ${context.getAwsRequestId} and content $content")
    output.write(s"""{ "hello" : "$content" }""".getBytes)
  }
}

object HelloWorld {
}