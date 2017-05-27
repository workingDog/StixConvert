package com.kodekutters

import com.kodekutters.stix._
import scala.language.implicitConversions

/**
  * converts Stix-2.1 objects and relationships into GraphML format
  *
  * @author R. Wathelet May 2017
  *
  *         ref: https://github.com/workingDog/scalastix
  *         ref: http://graphml.graphdrawing.org/index.html
  */
object GraphMLConverter {
  def apply() = new GraphMLConverter()
}

/**
  * converts Stix-2.1 objects (nodes) and relationships (edges) into GraphML format
  */
class GraphMLConverter extends StixConverter {

  // the output file extension to use
  val outputExt = ".graphml"

  /**
    * convert the bundle to GraphML (string) format
    */
  def convert(bundle: Bundle): String = {

    val nodesXml = for (stix <- bundle.objects.filter(obj => obj.isInstanceOf[SDO] || obj.isInstanceOf[SDO2]))
      yield {
        // name is in all SDO except ObservedData
        if (stix.isInstanceOf[ObservedData]) {
          val b = stix.asInstanceOf[ObservedData]
          <node id={b.id.toString()}>
            <data key="n1"><stix:type>{b.`type`}</stix:type></data>
            <data key="n2"><stix:created>{b.created.time}</stix:created></data>
            <data key="n3"><stix:modified>{b.modified.time}</stix:modified></data>
            <data key="n4"><stix:created_by_ref>{b.created_by_ref.getOrElse("").toString}</stix:created_by_ref></data>
            <data key="n5"><stix:revoked>{b.revoked.getOrElse("").toString}</stix:revoked></data>
            <data key="n6"><stix:confidence>{b.confidence.getOrElse("").toString}</stix:confidence></data>
            <data key="n7"><stix:name>"observed-data"</stix:name></data>
          </node>
        } else {
        val b = stix.asInstanceOf[SDO]
        <node id={b.id.toString()}>
          <data key="n1"><stix:type>{b.`type`}</stix:type></data>
          <data key="n2"><stix:created>{b.created.time}</stix:created></data>
          <data key="n3"><stix:modified>{b.modified.time}</stix:modified></data>
          <data key="n4"><stix:created_by_ref>{b.created_by_ref.getOrElse("").toString}</stix:created_by_ref></data>
          <data key="n5"><stix:revoked>{b.revoked.getOrElse("").toString}</stix:revoked></data>
          <data key="n6"><stix:confidence>{b.confidence.getOrElse("").toString}</stix:confidence></data>
          <data key="n7"><stix:name>{b.name}</stix:name></data>
        </node>
      }}

    val edgesXml = for (e <- bundle.objects.filter(_.isInstanceOf[SRO]))
      yield {
      if (e.isInstanceOf[Relationship]) {
      val b = e.asInstanceOf[Relationship]
      <edge directed="true" id={b.id.toString()} source={b.source_ref.toString()} target={b.target_ref.toString()}>
        <data key="e1"><stix:type>{b.relationship_type}</stix:type></data>
        <data key="e2"><stix:created>{b.created.time}</stix:created></data>
        <data key="e3"><stix:modified>{b.modified.time}</stix:modified></data>
        <data key="e4"><stix:description>{b.description.getOrElse("").toString}</stix:description></data>
        <data key="e5"><stix:revoked>{b.revoked.getOrElse("").toString}</stix:revoked></data>
        <data key="e6"><stix:confidence>{b.confidence.getOrElse("").toString}</stix:confidence></data>
        </edge>
      }
     else {  // must be a Sighting
        val b = e.asInstanceOf[Sighting]
        // create an edge for every where_sighted_refs, if none
        // do a self reference with the sighting_of_ref
        for(ref <- b.where_sighted_refs.getOrElse(List(b.sighting_of_ref))) yield {
          <edge directed="true" id={b.id.toString()} source={ref.toString} target={b.sighting_of_ref.toString()}>
            <data key="e1"><stix:type>"sighting"</stix:type></data>
            <data key="e2"><stix:created>{b.created.time}</stix:created></data>
            <data key="e3"><stix:modified>{b.modified.time}</stix:modified></data>
            <data key="e4"><stix:description>{b.description.getOrElse("").toString}</stix:description></data>
            <data key="e5"><stix:revoked>{b.revoked.getOrElse("").toString}</stix:revoked></data>
            <data key="e6"><stix:confidence>{b.confidence.getOrElse("").toString}</stix:confidence></data>
            <data key="e7"><stix:count>{b.count.getOrElse("")}</stix:count></data>
          </edge>
        }
       }
      }

    val xmlDoc = <graphml xmlns="http://graphml.graphdrawing.org/xmlns"
                 xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
                 xmlns:stix="https://www.oasis-open.org/committees/tc_home.php?wg_abbrev=cti-stix"
                 xsi:schemaLocation="http://graphml.graphdrawing.org/xmlns http://graphml.graphdrawing.org/xmlns/1.0/graphml.xsd">
          <key for="node" id="n1" stix.name="type" stix.type="string"/>
          <key for="node" id="n2" stix.name="created" stix.type="string"/>
          <key for="node" id="n3" stix.name="modified" stix.type="string"/>
          <key for="node" id="n4" stix.name="created_by_ref" stix.type="string"/>
          <key for="node" id="n5" stix.name="revoked" stix.type="boolean"/>
          <key for="node" id="n6" stix.name="confidence" stix.type="integer"/>
          <key for="node" id="n7" stix.name="name" stix.type="string"/>

          <key for="edge" id="e1" stix.name="type" stix.type="string"/>
          <key for="edge" id="e2" stix.name="created" stix.type="string"/>
          <key for="edge" id="e3" stix.name="modified" stix.type="string"/>
          <key for="edge" id="e4" stix.name="description" stix.type="string"/>
          <key for="edge" id="e5" stix.name="revoked" stix.type="boolean"/>
          <key for="edge" id="e6" stix.name="confidence" stix.type="integer"/>
          <key for="edge" id="e7" stix.name="count" stix.type="integer"/>

          <graph id="StixGraph" edgedefault="directed">
            {nodesXml}
            {edgesXml}
          </graph>
        </graphml>

    """<?xml version="1.0" encoding="UTF-8"?>""" + "\n" + xmlDoc.toString()
  }

}

