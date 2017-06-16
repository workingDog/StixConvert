## Convert STIX-2.1 to GraphML or GEXF format 

This application **StixConvert**, converts STIX-2.1 json and zip files into a GraphML or GEXF representation. 

The Structured Threat Information Expression [STIX-2.1](https://docs.google.com/document/d/1yvqWaPPnPW-2NiVCLqzRszcx91ffMowfT5MmE9Nsy_w/edit#) 
is a language for expressing cyber threat and observable information.

[GraphML](http://graphml.graphdrawing.org/) is a common XML format for graphs and similarly [GEXF](https://gephi.org/gexf/format/) is 
a format used by the popular [Gephi](https://gephi.org/) graph visualization tool.
 
This application uses the [ScalaStix](https://github.com/workingDog/scalastix) library
to convert Stix domain objects (SDO) and relationships (SRO) to GraphML and GEXF formats. 

The aim is to make Stix graph structures available to GraphML and GEXF visualisation tools.
 
### Another tool
[StixToNeoDB](https://github.com/workingDog/StixToNeoDB) in this repository,
loads Stix objects into a [Neo4j](https://neo4j.com/) graph database which can then be exported into graphML, 
Cypher statements, CSV and binary formats using  [export tools](https://github.com/jexp/neo4j-shell-tools). 
  
  
### Stix to GraphML and GEXF mapping

With **StixConvert** only a small number of Stix attributes are converted. The following Stix SDO properties are represented in GraphML and GEXF nodes:

    type, created, modified, created_by_ref, revoked, confidence and name (when available)

In addition, the following Stix SRO properties are represented in GraphML and GEXF edges:
           
    relationship_type, created, modified, description, revoked and confidence
               
### References
 
1) [GraphML](http://graphml.graphdrawing.org/)

2) [GEXF](https://gephi.org/gexf/format/)

3) [STIX-2.1](https://docs.google.com/document/d/1yvqWaPPnPW-2NiVCLqzRszcx91ffMowfT5MmE9Nsy_w/edit#) 

4) [ScalaStix](https://github.com/workingDog/scalastix)

### Dependencies

Depends on the scala [ScalaStix](https://github.com/workingDog/scalastix) library
(included in the "lib" directory) and the [scala-xml](https://github.com/scala/scala-xml) library.

Java 8 is also required.

### Installation and packaging

The easiest way to compile and package the application from source is to use [SBT](http://www.scala-sbt.org/).
To assemble the application and all its dependencies into a single jar file type:

    sbt assembly

This will produce "stixconvert-1.0.jar" in the "./target/scala-2.12" directory.

For convenience a **"stixconvert-1.0.jar"** file is in the "distrib" directory.

### Usage

Once you have the jar file, simply type at the prompt:
 
    java -jar stixconvert-1.0.jar --graphml stix_file.json out_file.graphml
    or
    java -jar stixconvert-1.0.jar --gexf stix_file.json out_file.gexf
 
where "--graphml" or"--gexf" determines the conversion format, "stix_file.json" is the Stix file containing a 
bundle of Stix objects you want to convert, and "out_file.graphml" or "out_file.gexf" is the destination file 
with the new format results. If the output file is absent, the output is directed to the console.
 
If the input file is a zip file with one or more files containing bundles of Stix objects,
the output file will also be a zip file with one or more files of GraphML or GEXF format results.
 
Note: on macOS, when using "Compress" from the "Finder" menu, the resulting zip file may contain 
  extra "__MACOSX/" directories that should be removed by typing in a terminal:
  
      zip -d the_file_name.zip __MACOSX/\*
 
### Status

not fully tested.

Using Scala 2.12, Java 8 and SBT-0.13.15.


