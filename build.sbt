// sbt-assembly

import sbt.Keys._
import sbtassembly.Plugin._
import AssemblyKeys._

// sbt-buildinfo
import sbtbuildinfo.Plugin._

// sbt-dependecy-graph
import net.virtualvoid.sbt.graph.Plugin._

// sbt-scalariform
import com.typesafe.sbt.SbtScalariform._
import scalariform.formatter.preferences._

// Resolvers
resolvers ++= Seq(
)

// Dependencies

val testDependencies = Seq (
)

val rootDependencies = Seq(
  "com.amazonaws"          %  "aws-lambda-java-core"   % "1.0.0",
  "com.amazonaws"          %  "aws-lambda-java-events" % "1.0.0",
  "org.scala-lang.modules" %% "scala-java8-compat"     % "0.5.0",
  "org.spire-math"         %% "argonaut-support"       % "0.8.0",
  "org.spire-math"         %% "jawn-parser"            % "0.8.0"
)

val dependencies =
  rootDependencies ++
  testDependencies

// Settings
//
val forkedJvmOption = Seq(
  "-server",
  "-Dfile.encoding=UTF8",
  "-Duser.timezone=GMT",
  "-Xss1m",
  "-Xms2048m",
  "-Xmx2048m",
  "-XX:+CMSClassUnloadingEnabled",
  "-XX:+DoEscapeAnalysis",
  "-XX:+UseConcMarkSweepGC",
  "-XX:+UseParNewGC",
  "-XX:+UseCodeCacheFlushing",
  "-XX:+UseCompressedOops",
  "-Xmn512m"
)

val buildSettings = Seq(
  name := "lambdas-rest",
  organization := "io.github.lvicentesanchez",
  scalaVersion := "2.11.7"
)

val compileSettings = Seq(
  scalacOptions ++= Seq(
    "-deprecation",
    "-encoding", "UTF-8",
    "-feature",
    "-language:_",
    "-unchecked",
    "-Xlint",
    "-target:jvm-1.8",
    "-Ybackend:GenBCode",
    "-Ydelambdafy:method"
    //"-Xfuture"
    //"-Yno-predef",
    //"-Yno-imports"
  )
)

val formatting =
  FormattingPreferences()
    .setPreference(AlignParameters, true)
    .setPreference(AlignSingleLineCaseStatements, false)
    .setPreference(AlignSingleLineCaseStatements.MaxArrowIndent, 40)
    .setPreference(CompactControlReadability, false)
    .setPreference(CompactStringConcatenation, false)
    .setPreference(DoubleIndentClassDeclaration, true)
    .setPreference(FormatXml, true)
    .setPreference(IndentLocalDefs, false)
    .setPreference(IndentPackageBlocks, true)
    .setPreference(IndentSpaces, 2)
    .setPreference(IndentWithTabs, false)
    .setPreference(MultilineScaladocCommentsStartOnFirstLine, false)
    .setPreference(PlaceScaladocAsterisksBeneathSecondAsterisk, false)
    .setPreference(PreserveSpaceBeforeArguments, false)
    .setPreference(PreserveDanglingCloseParenthesis, true)
    .setPreference(RewriteArrowSymbols, false)
    .setPreference(SpaceBeforeColon, false)
    .setPreference(SpaceInsideBrackets, false)
    .setPreference(SpaceInsideParentheses, false)
    .setPreference(SpacesWithinPatternBinders, true)

val pluginsSettings =
  assemblySettings ++
  buildInfoSettings ++
  compileSettings ++
  buildSettings ++
  graphSettings ++
  scalariformSettings

val settings = Seq(
  libraryDependencies ++= dependencies,
  fork in run := true,
  fork in Test := true,
  fork in testOnly := true,
  connectInput in run := true,
  javaOptions in run ++= forkedJvmOption,
  javaOptions in Test ++= forkedJvmOption,
  // assembly
  //
  jarName in assembly <<= (name, version) map ( (n, v) => s"$n-$v.jar" ),
  // build info
  //
  sourceGenerators in Compile <+= buildInfo,
  buildInfoKeys := Seq[BuildInfoKey](name, version),
  buildInfoPackage := "io.github.lvicentesanchez.lambdas.rest.info",
  // formatting
  //
  ScalariformKeys.preferences := formatting
) ++ addArtifact(artifact in (Compile, assembly), assembly)

lazy val main =
  project
    .in(file("."))
    .settings(
      pluginsSettings ++ settings:_*
    )
