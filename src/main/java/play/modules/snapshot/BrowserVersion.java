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
package play.modules.snapshot;

/**
 * The HtmlUnit browser version to use
 */
public enum BrowserVersion {
    DEFAULT(com.gargoylesoftware.htmlunit.BrowserVersion.getDefault()),
    FIREFOX_3_6(com.gargoylesoftware.htmlunit.BrowserVersion.FIREFOX_3_6),
    INTERNET_EXPLORER_6(com.gargoylesoftware.htmlunit.BrowserVersion.INTERNET_EXPLORER_6),
    INTERNET_EXPLORER_7(com.gargoylesoftware.htmlunit.BrowserVersion.INTERNET_EXPLORER_7),
    INTERNET_EXPLORER_8(com.gargoylesoftware.htmlunit.BrowserVersion.INTERNET_EXPLORER_8);

    public final com.gargoylesoftware.htmlunit.BrowserVersion browserVersion;

    private BrowserVersion(com.gargoylesoftware.htmlunit.BrowserVersion browserVersion) {
        this.browserVersion = browserVersion;
    }
}
