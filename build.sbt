// Project info

name := "play-snapshot"

// So that we can publish it into Sonatype

organization := "net.vz.play.snapshot"

// The version comes from version.sbt, and is generated by the release plugin

scalaVersion := "2.9.1"

// Dependencies

libraryDependencies ++= Seq(
    "play" %% "play" % "2.0",
    "net.sourceforge.htmlunit" % "htmlunit" % "2.9" % "compile"
)

// Test dependencies

libraryDependencies ++= Seq(
    "org.specs2" %% "specs2" % "1.9" % "test",
    "play" %% "play-test" % "2.0" % "test"
)

// Configuration required for deploying to sonatype

publishMavenStyle := true

publishTo <<= version { (v: String) =>
  val nexus = "https://oss.sonatype.org/"
  if (v.trim.endsWith("SNAPSHOT"))
    Some("snapshots" at nexus + "content/repositories/snapshots")
  else
    Some("releases"  at nexus + "service/local/staging/deploy/maven2")
}

pomIncludeRepository := { _ => false }

publishArtifact in Test := false

pomExtra := (
  <url>http://github.com/vznet/play-snapshot</url>
  <inceptionYear>2012</inceptionYear>
  <licenses>
    <license>
      <name>Apache 2</name>
      <url>http://www.apache.org/licenses/LICENSE-2.0.txt</url>
      <distribution>repo</distribution>
      <comments>A business-friendly OSS license</comments>
    </license>
  </licenses>
  <scm>
    <url>git@github.com:vznet/play-snapshot.git</url>
    <connection>scm:git:git@github.com:vznet/play-snapshot.git</connection>
  </scm>
  <developers>
    <developer>
      <name>James Roper</name>
      <email>james@jazzy.id.au</email>
      <url>http://jazzy.id.au</url>
      <roles>
        <role>Author</role>
      </roles>
      <organization>VZ Netzwerke</organization>
    </developer>
  </developers>)
