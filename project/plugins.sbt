credentials += Credentials(Path.userHome / ".ivy2" / ".credentials")

resolvers += Resolver.url("Rally Plugin Releases", url("https://artifacts.werally.in/artifactory/ivy-plugins-release"))(Resolver.ivyStylePatterns)

addSbtPlugin("com.rallyhealth" %% "rally-sbt-plugin" % "0.1.0")
