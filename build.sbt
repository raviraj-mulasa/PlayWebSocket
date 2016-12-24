import Dependencies._

// factor out common settings into a sequence
lazy val buildSettings = Seq(
  organization := "net.geekscore",
  version := "1.0",
  scalaVersion := "2.11.6"
)

lazy val db = (project in file("db")).
  settings(buildSettings: _*).
  settings(
    libraryDependencies ++= loggingDeps,
    libraryDependencies ++= databaseDeps
  )

lazy val server = (project in file("server")).
  dependsOn(db).
  settings(buildSettings: _*).
  settings(
    libraryDependencies ++= loggingDeps
  )


lazy val playwebsocket = (project in file(".")).
  aggregate(server).
  dependsOn(server).
  settings(buildSettings: _*).
  settings(

    // set the location of the JDK to use for compiling Java code.
    // if 'fork' is true, this is used for 'run' as well
    javaHome := Some(file("/Library/Java/JavaVirtualMachines/jdk1.8.0_45.jdk/Contents/Home")),

    // Use Scala from a directory on the filesystem instead of retrieving from a repository
    scalaHome := Some(file("/Users/ravirajmulasa/scala-2.11.6/")),

    // append several options to the list of options passed to the Java compiler
    javacOptions ++= Seq("-source", "1.8", "-target", "1.8"),

    // add a JVM option to use when forking a JVM for 'run'
    javaOptions += "-Xmx2G",

    // append -deprecation to the options passed to the Scala compiler
    scalacOptions ++= Seq(
      "-deprecation",
      "-feature",
      "-language:reflectiveCalls"
    ),

    // set the initial commands when entering 'console' or 'consoleQuick', but not 'consoleProject'
    initialCommands in console := "import net.geekscore._",

    // set the main class for packaging the main jar
    // 'run' will still auto-detect and prompt
    // change Compile to Test to set it for the test jar
    mainClass in (Compile, packageBin) := Some("net.geekscore.db.Main"),

    // set the main class for the main 'run' task
    // change Compile to Test to set it for 'test:run'
    mainClass in (Compile, run) := Some("net.geekscore.db.Main"),


    // only use a single thread for building
    parallelExecution := false,

    // Execute tests in the current project serially
    //   Tests from other projects may still run concurrently.
    parallelExecution in Test := false,



    // don't aggregate clean (See FullConfiguration for aggregation details)
//    aggregate in clean := false,

    // only show warnings and errors on the screen for compilations.
    //  this applies to both test:compile and compile and is Info by default
    logLevel in compile := Level.Debug,

    // only show warnings and errors on the screen for all tasks (the default is Info)
    //  individual tasks can then be more verbose using the previous setting
    logLevel := Level.Debug,

    // only store messages at info and above (the default is Debug)
    //   this is the logging level for replaying logging with 'last'
    persistLogLevel := Level.Debug,

    // add SWT to the unmanaged classpath
//    unmanagedJars in Compile += Attributed.blank(file("/usr/share/java/swt.jar")),

    // publish test jar, sources, and docs
//    publishArtifact in Test := false,

    // disable publishing of main docs
//    publishArtifact in (Compile, packageDoc) := false

    // change the classifier for the docs artifact
//    artifactClassifier in packageDoc := Some("doc"),

//     Copy all managed dependencies to <build-root>/lib_managed/
//    This is essentially a project-local cache and is different
//    from the lib_managed/ in sbt 0.7.x.  There is only one
//       lib_managed/ in the build root (not per-project).
    retrieveManaged := true

    /* Specify a file containing credentials for publishing. The format is:
    realm=Sonatype Nexus Repository Manager
    host=nexus.scala-tools.org
    user=admin
    password=admin123
    */
//    credentials += Credentials(Path.userHome / ".ivy2" / ".credentials"),

    // Directly specify credentials for publishing.
//    credentials += Credentials("Sonatype Nexus Repository Manager", "nexus.scala-tools.org", "admin", "admin123"),

  )


enablePlugins(JavaAppPackaging)