import sbt._
import Keys._

object Dependencies {


//  Testing related
  val scalatestVersion  = "3.0.1"
  val scalatest         = "org.scalatest" %% "scalatest" % scalatestVersion % "test"
  val junitVersion      = "4.12"
  val junit             = "junit" % "junit" % junitVersion % "test"
  lazy val testingDeps  = Seq(
    junit,
    scalatest
  )

//  Logging related
  val slf4jVersion      = "1.7.22"
  val logbackVersion    = "1.1.8"
  val slf4j             = "org.slf4j" % "slf4j-api" % slf4jVersion
  val logbackCore       = "ch.qos.logback" % "logback-core"     % logbackVersion
  val logbackClassic    = "ch.qos.logback" % "logback-classic"  % logbackVersion
  val jul2Slf4j         = "org.slf4j" % "jul-to-slf4j" % slf4jVersion
  val jclOverSlf4j      = "org.slf4j" % "jcl-over-slf4j" % slf4jVersion
  val log4jOverSlf4j    = "org.slf4j" % "log4j-over-slf4j" % slf4jVersion
  lazy val loggingDeps  = Seq(
    slf4j,
    jclOverSlf4j,
    jul2Slf4j,
    log4jOverSlf4j,
    logbackCore,
    logbackClassic
  )

  val commonDeps        = (testingDeps ++ loggingDeps)

//  Database related
  val slickVersion              = "3.1.1"
  val postgresqlJDBCVersion     = "9.4.1212.jre7"
  val hikariCPVersion           = "2.5.1"
  val flywayVersion             = "4.0.3"
  val slick                     = "org.slf4j" % "log4j-over-slf4j" % slf4jVersion
  val postgresqlJDBC            = "org.postgresql" % "postgresql" % postgresqlJDBCVersion
  val hikariCP                  = "com.zaxxer" % "HikariCP" % hikariCPVersion
  val flyway                    = "org.flywaydb" % "flyway-core" % flywayVersion % "test"
  lazy val databaseDeps         = Seq(
    slick,
    hikariCP,
    postgresqlJDBC,
    flyway
  )

}