/*
 * Copyright 2012 VZ Netzwerke
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package play.modules.snapshot

import api.SnapshotRouter.{encode, decode, EscapedFragment}
import api.Snapshotter
import play.mvc.Results.AsyncResult
import play.libs.Akka
import java.util.concurrent.Callable
import play.mvc.Http.{Request, Context}
import play.mvc.{Result, Action}

/**
 * Snapshot action for use in Java
 */
class SnapshotAction extends Action[Snapshot] {

  def call(ctx: Context) = {
    if (ctx.request().method() == "GET" && ctx.request().queryString().containsKey(EscapedFragment)) {
      new AsyncResult(
        Akka.future(
          new Callable[Result]() {
            def call() = {
              val waitForJavascriptMs = Some(configuration.waitForJavascriptMs()) filter { _ != -1 } getOrElse(Snapshotter.defaultWaitForJavascriptMs)
              val browserVersion = Some(configuration.browserVersion()) filter { _ != BrowserVersion.DEFAULT } map {
                _.browserVersion } getOrElse(Snapshotter.defaultBrowserVersion)
              ScalaResult(Snapshotter.getSnapshot(url = buildUrl(ctx.request()), waitForJavascriptMs = waitForJavascriptMs,
                  browserVersion = browserVersion))
            }
          }
        )
      )
    } else {
      delegate.call(ctx)
    }
  }

  def buildUrl(req: Request): String = {
    import scala.collection.JavaConversions._
    val escapedFragment = req.queryString.get(EscapedFragment)(0)
    val qs = req.queryString - EscapedFragment
    // Use HTTP
    val sb = new StringBuilder("http://")
    // Go to the same host the crawler is visiting
    sb.append(req.host()).append(req.path())
    if (!qs.isEmpty) {
      sb.append("?")
      for ((key: String, values: Array[String]) <- qs) {
        values.foreach { value => sb.append(encode(key)).append("=").append(encode(value)).append("&") }
      }
    }
    sb.append("#").append(decode(escapedFragment))
    sb.toString()
  }
}

case class ScalaResult(wrappedResult: play.api.mvc.Result) extends Result {
  def getWrappedResult = wrappedResult
}
