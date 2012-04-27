Play Framework 2.0 Web Crawler Snapshot Module
==============================================

This module implements the snapshotting process described by Google in:

https://developers.google.com/webmasters/ajax-crawling/docs/html-snapshot

It uses HtmlUnit to do the snapshotting.  Snapshotting is done using Akka, so that request threads aren't taken up
during the snapshot process.

Installation
------------

Add a dependency on ``"net.vz.play.snapshot" %% "play-snapshot" % "1.0.0-rc.2"``.  For example, in ``Build.scala``:

    val appDendencies = Seq(@"net.vz.play.snapshot" %% "play-snapshot" % "1.0.0-rc.2")

Usage in Java
-------------

Simply annotate your controller methods or classes with ``@Snapshot``.  For example:

    @Snapshot
    public static Result index() {
        return ok();
    }

You can apply a few configuration parameters:

    @Snapshot(waitForJavascriptMs = 1000, browserVersion = BrowserVersion.FIREFOX_3_6)
    public static Result index() {
        return ok();
    }

Usage in Scala
--------------

Simply compose your action using the ``Snapshot`` action.  For example:

    val index = Snapshot {
      Action {
        ok
      }
    }

You can apply a few configuration parameters:

    val index = Snapshot(waitForJavascriptMs = 1000, browserVersion = BrowserVersion.FIREFOX_3_6) {
      Action {
        ok
      }
    }

Configuration
-------------

You can override the default values for all of the above configuration in ``application.conf``:

- ``snapshot.waitForJavascriptMs`` - This configures the default time to wait for Javascript to execute.  The default is 2000.
- ``snapshot.browserVersion`` - This configures the default browser version to use to snapshot the page.  It should be an HtmlUnit ``BrowserVersion`` field.  The default is ``FIREFOX_3_6``.
