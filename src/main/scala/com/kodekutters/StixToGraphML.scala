package com.kodekutters

import scala.language.implicitConversions
import scala.language.postfixOps

/**
  * converts a Stix json file containing a STIX bundle, or
  * a Stix zip file containing one or more of bundle files,
  * into a GraphML representation
  *
  * @author R. Wathelet May 2017
  *
  *         ref: https://github.com/workingDog/scalastix
  *         ref: http://graphml.graphdrawing.org/index.html
  */
object StixToGraphML {

  val transformer = new Transformer(GraphMLConverter())

  /**
    * converts a Stix json file containing a STIX bundle, or
    * a Stix zip file containing one or more of bundle files,
    * into a GraphML representation
    */
  def main(args: Array[String]) {
    val usage = """Usage: java -jar stixtographml-1.0.jar stix_file.json graphml_file.graphml""".stripMargin
    if (args.isEmpty)
      println(usage)
    else {
      val outFile = if (args.length == 2) args(1) else ""
      // the input file
      args(0).toLowerCase match {
        case inFile if inFile.endsWith(".json") => transformer.stixConvertion(inFile, outFile)
        case inFile if inFile.endsWith(".zip") => transformer.stixConvertionZip(inFile, outFile)
        case inFile => println("Error --> input file \"" + inFile + "\" must have extension .json or .zip")
      }
    }
  }

}

