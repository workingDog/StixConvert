package com.kodekutters

import scala.language.implicitConversions
import scala.language.postfixOps

/**
  * converts a Stix json file containing a STIX bundle, or
  * a Stix zip file containing one or more of bundle files,
  * into either GraphML or GEXF format.
  *
  * @author R. Wathelet May 2017
  *
  *         ref: https://github.com/workingDog/scalastix
  */
object StixConvert {

  val usage =
    """Usage:
       java -jar stixconvert-1.0.jar --graphml stix_file.json out_file.graphml
      or
       java -jar stixconvert-1.0.jar --gexf stix_file.json out_file.gexf """.stripMargin

  /**
    * converts a Stix json file containing a STIX bundle, or
    * a Stix zip file containing one or more of bundle files,
    * into a GraphML or GEXF format
    */
  def main(args: Array[String]) {
    if (args.isEmpty)
      println(usage)
    else {
      args(0) match {
        case "--graphml" => doTransform(args, new Transformer(GraphMLConverter()))
        case "--gexf" => doTransform(args, new Transformer(GexfConverter()))
        case x => println("unknown format: " + x + "\n"); println(usage)
      }
    }
  }

  def doTransform(args: Array[String], transformer: Transformer) = {
    val outFile: String = if (args.length == 3) args(2) else ""
    args(1).toLowerCase match {
      case inFile if inFile.endsWith(".json") => transformer.stixConvertion(inFile, outFile)
      case inFile if inFile.endsWith(".zip") => transformer.stixConvertionZip(inFile, outFile)
      case inFile => println("Error --> input file \"" + inFile + "\" must have extension .json or .zip")
    }
  }

}


