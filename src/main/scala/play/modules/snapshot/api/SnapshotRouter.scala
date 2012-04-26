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
package play.modules.snapshot.api

import play.api.mvc._
import java.net.{URLDecoder, URLEncoder}
import play.api.libs.concurrent.Akka
import com.gargoylesoftware.htmlunit.BrowserVersion

/**
 * Implements the third approach in Googles AJAX crawling guidelines
 *
 * https://developers.google.com/webmasters/ajax-crawling/docs/html-snapshot
 *
 * @author James Roper
 */
object SnapshotRouter {
  val EscapedFragment = "_escaped_fragment_"

  import play.api.Play.current

  def handle(waitForJavascriptMs: Int, browserVersion: BrowserVersion): PartialFunction[RequestHeader, Result] = {
    case req: RequestHeader if req.method == "GET" && req.queryString.get(EscapedFragment).isDefined => {
      AsyncResult {
        Akka.future {
          Snapshotter.getSnapshot(url = buildUrl(req), waitForJavascriptMs = waitForJavascriptMs,
            browserVersion = browserVersion)
        }
      }
    }
  }

  def buildUrl(req: RequestHeader): String = {
    val escapedFragment = req.queryString(EscapedFragment)(0)
    val qs = req.queryString - EscapedFragment
    // Use HTTP
    val sb = new StringBuilder("http://")
    // Go to the same host the crawler is visiting
    sb.append(req.host).append(req.path)
    if (!qs.isEmpty) {
      sb.append("?")
      for ((key: String, values: Seq[String]) <- qs) {
        values.foreach {
          value => sb.append(encode(key)).append("=").append(encode(value)).append("&")
        }
      }
    }
    sb.append("#").append(decode(escapedFragment))
    sb.toString()
  }

  def encode(f: => String) = {
    URLEncoder.encode(f, "UTF-8")
  }

  def decode(f: => String) = {
    URLDecoder.decode(f, "UTF-8")
  }
}

/**
 * A snapshot action.
 *
 * @param waitForJavascriptMs The number of ms to wait for the Javascript to finish executing. Defaults to 2000.
 * @param browserVersion The browser version to snapshot the page with. Defaults to Firefox 3.6.
 * @param action The action to wrap
 */
case class Snapshot[A](waitForJavascriptMs: Int = Snapshotter.defaultWaitForJavascriptMs,
                       browserVersion: BrowserVersion = Snapshotter.defaultBrowserVersion)
                       (action: Action[A]) extends Action[A] {
  val router = SnapshotRouter.handle(waitForJavascriptMs, browserVersion)

  def apply(request: Request[A]): Result = {
    if (router.isDefinedAt(request)) {
      router(request)
    } else {
      action.apply(request)
    }
  }

  lazy val parser = action.parser
}
