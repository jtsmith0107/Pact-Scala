credentials += Credentials(Path.userHome / ".ivy2" / ".credentials")

addSbtPlugin("com.typesafe.play" % "sbt-plugin" % "2.3.9") // where version is the current Play version, i.e. "2.4.0"

addSbtPlugin("com.eed3si9n" % "sbt-buildinfo" % "0.5.0")