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

import scala.util.control.Exception._
import com.gargoylesoftware.htmlunit.{WebClient, BrowserVersion}
import com.gargoylesoftware.htmlunit.html.HtmlPage
import play.api.mvc.{ResponseHeader, SimpleResult, Result}
import play.api.libs.iteratee.Enumerator
import play.api.{Logger, Play}

/**
 * Responsible for snapshotting pages
 *
 * @author James Roper
 */
object Snapshotter {

  import play.api.Play.current;

  lazy val defaultBrowserVersion = Play.configuration.getString("snapshot.browserVersion").map { browserString =>
      allCatch opt { classOf[BrowserVersion].getField(browserString).get(null).asInstanceOf[BrowserVersion] } getOrElse {
        throw new IllegalArgumentException(browserString + " is not a valid browser version")
      }
  } getOrElse BrowserVersion.FIREFOX_3_6 // Firefox 3.6 makes a good default because it works on all OSes
  lazy val defaultWaitForJavascriptMs = Play.configuration.getInt("snapshot.waitForJavascriptMs").getOrElse(2000)

  /**
   * Get a snapshot of the given URL
   *
   * @param url The URL to get the snapshot of
   * @param waitForJavascriptMs How long to wait for the Javascript to finish executing
   * @param browserVersion Which browser version to snapshot the page with
   * @return
   */
  def getSnapshot(url: String,
                  waitForJavascriptMs: Int = defaultWaitForJavascriptMs,
                  browserVersion: BrowserVersion = defaultBrowserVersion): Result = {
    Logger.debug("Snapshotting " + url)
    val webClient = new WebClient(browserVersion)
    val page = webClient.getPage[HtmlPage](url)
    webClient.waitForBackgroundJavaScript(waitForJavascriptMs)
    val content = page.asXml()
    val result = SimpleResult(
      header = ResponseHeader(200, Map("Content-Type" -> "text/html; charset=UTF-8")),
      body = Enumerator(content)
    )
    webClient.closeAllWindows()
    result
  }
}
