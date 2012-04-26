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

import play.mvc.With;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Indicates that this action should honour the #! web crawling policy, as described in the third section of this page:
 * <p/>
 * https://developers.google.com/webmasters/ajax-crawling/docs/html-snapshot
 * <p/>
 * Uses HtmlUnit to visit the #! version of the site when a web crawler requests it
 */
@With(SnapshotAction.class)
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD, ElementType.TYPE})
public @interface Snapshot {
    /**
     * How long to wait for Javascript to finish executing.  Default is to use the configuration property
     * <code>snapshot.waitForJavascriptMs</code>, which defaults to 2000.  The value you configure should give HtmlUnit
     * enough time to render the page, and make any AJAX calls it needs to make in order to serve the page.
     *
     * @return The timeout in ms
     */
    int waitForJavascriptMs() default -1;

    /**
     * Which HtmlUnit browser version to use.  Default is to use the configuration property
     * <code>snapshot.browserVersion</code>, which defaults to FIREFOX_3_6.
     *
     * @return The HtmlUnit browser version
     */
    BrowserVersion browserVersion() default BrowserVersion.DEFAULT;
}
