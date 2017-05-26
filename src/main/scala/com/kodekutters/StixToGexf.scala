package com.kodekutters

/**
  * converts a Stix json file containing a STIX bundle, or
  * a Stix zip file containing one or more of bundle files,
  * into a Gexf representation
  *
  * @author R. Wathelet May 2017
  *
  *         ref: https://github.com/workingDog/scalastix
  *         ref: https://gephi.org/gexf/format/
  */
object StixToGexf {

  val transformer = new Transformer(GexfConverter())

  /**
    * converts a Stix json file containing a STIX bundle, or
    * a Stix zip file containing one or more of bundle files,
    * into a Gexf representation
    */
  def main(args: Array[String]) {
    val usage = """Usage: java -jar stixtogexf-1.0.jar stix_file.json gexf_file.gexf""".stripMargin
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
