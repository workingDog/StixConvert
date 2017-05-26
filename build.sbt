organization := "com.github.workingDog"

name := "stixconvert"

version := (version in ThisBuild).value

scalaVersion := "2.12.2"

crossScalaVersions := Seq("2.12.2")

libraryDependencies ++= Seq(
  "org.scala-lang.modules" %% "scala-xml" % "1.0.6"
//  "com.github.workingDog" %% "scalastix" % "1.0"
)

homepage := Some(url("https://github.com/workingDog/StixConvert"))

licenses := Seq("Apache 2" -> url("http://www.apache.org/licenses/LICENSE-2.0"))

mainClass in(Compile, run) := Some("com.kodekutters.StixConvert")

mainClass in assembly := Some("com.kodekutters.StixConvert")

assemblyJarName in assembly := "stixconvert-1.0.jar"
