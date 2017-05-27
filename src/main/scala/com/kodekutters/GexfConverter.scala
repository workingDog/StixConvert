package com.kodekutters

import com.kodekutters.stix._

/**
  * converts Stix-2.1 objects and relationships into Gexf format
  *
  * @author R. Wathelet May 2017
  *
  *         ref: https://github.com/workingDog/scalastix
  *         ref: https://gephi.org/gexf/format/
  */
object GexfConverter {
  def apply() = new GexfConverter()
}

/**
  * converts Stix-2.1 objects (nodes) and relationships (edges) into Gexf format
  */
class GexfConverter extends StixConverter {

  // the output file extension to use
  val outputExt = ".gexf"

  /**
    * convert the bundle to Gexf (string) format
    */
  def convert(bundle: Bundle): String = {

    val nodesXml = for (stix <- bundle.objects.filter(obj => obj.isInstanceOf[SDO] || obj.isInstanceOf[SDO2]))
      yield {
        // name is in all SDO except ObservedData
        if (stix.isInstanceOf[ObservedData]) {
          val b = stix.asInstanceOf[ObservedData]
          <node id={b.id.toString()} label="observed-data">
            <attvalues>
              <attvalue for="n1" value={b.`type`}/>
              <attvalue for="n2" value={b.created.time}/>
              <attvalue for="n3" value={b.modified.time}/>
              <attvalue for="n4" value={b.created_by_ref.getOrElse("").toString}/>
              <attvalue for="n5" value={b.revoked.getOrElse("").toString}/>
              <attvalue for="n6" value={b.confidence.getOrElse("").toString}/>
              <attvalue for="n7" value="observed-data"/>
            </attvalues>
          </node>
        } else {
          val b = stix.asInstanceOf[SDO]
          <node id={b.id.toString()} label={b.name}>
            <attvalues>
              <attvalue for="n1" value={b.`type`}/>
              <attvalue for="n2" value={b.created.time}/>
              <attvalue for="n3" value={b.modified.time}/>
              <attvalue for="n4" value={b.created_by_ref.getOrElse("").toString}/>
              <attvalue for="n5" value={b.revoked.getOrElse("").toString}/>
              <attvalue for="n6" value={b.confidence.getOrElse("").toString}/>
              <attvalue for="n7" value={b.name}/>
            </attvalues>
          </node>
        }
      }

    val edgesXml = for (e <- bundle.objects.filter(_.isInstanceOf[SRO]))
      yield {
        if (e.isInstanceOf[Relationship]) {
          val b = e.asInstanceOf[Relationship]
          <edge id={b.id.toString()} source={b.source_ref.toString()} target={b.target_ref.toString()}>
            <attvalues>
              <attvalue for="e1" value={b.relationship_type}/>
              <attvalue for="e2" value={b.created.time}/>
              <attvalue for="e3" value={b.modified.time}/>
              <attvalue for="e4" value={b.created_by_ref.getOrElse("").toString}/>
              <attvalue for="e5" value={b.revoked.getOrElse("").toString}/>
              <attvalue for="e6" value={b.confidence.getOrElse("").toString}/>
            </attvalues>
          </edge>
        }
        else { // must be a Sighting
          val b = e.asInstanceOf[Sighting]
          // create an edge for every where_sighted_refs, if none
          // do a self reference with the sighting_of_ref
          for (ref <- b.where_sighted_refs.getOrElse(List(b.sighting_of_ref))) yield
            <edge id={b.id.toString()} source={ref.toString} target={b.sighting_of_ref.toString()}>
              <attvalues>
                <attvalue for="e1" value="sighting"/>
                <attvalue for="e2" value={b.created.time}/>
                <attvalue for="e3" value={b.modified.time}/>
                <attvalue for="e4" value={b.created_by_ref.getOrElse("").toString}/>
                <attvalue for="e5" value={b.revoked.getOrElse("").toString}/>
                <attvalue for="e6" value={b.confidence.getOrElse("").toString}/>
                <attvalue for="e7" value={b.count.getOrElse("").toString}/>
              </attvalues>
            </edge>
        }
      }

    val xmlDoc = <gexf xmlns="http://www.gexf.net/1.2draft"
                       xmlns:xsi="http://www.w3.org/2001/XMLSchema−instance" xsi:schemaLocation="http://www.gexf.net/1.2draft http://www.gexf.net/1.2draft/gexf.xsd"
                       version="1.2">
      <graph mode="static" defaultedgetype="directed">
        <attributes class="node">
          <attribute id="n1" title="type" type="string"/>
          <attribute id="n2" title="created" type="string"/>
          <attribute id="n3" title="modified" type="string"/>
          <attribute id="n4" title="created_by_ref" type="string"/>
          <attribute id="n5" title="revoked" type="boolean"/>
          <attribute id="n6" title="confidence" type="integer"/>
          <attribute id="n7" title="name" type="string"/>
        </attributes>
        <attributes class="edge">
          <attribute id="e1" title="type" type="string"/>
          <attribute id="e2" title="created" type="string"/>
          <attribute id="e3" title="modified" type="string"/>
          <attribute id="e4" title="created_by_ref" type="string"/>
          <attribute id="e5" title="revoked" type="boolean"/>
          <attribute id="e6" title="confidence" type="integer"/>
          <attribute id="e7" title="count" type="integer"/>
        </attributes>
        <nodes>{nodesXml}</nodes>
        <edges>{edgesXml}</edges>
      </graph>
    </gexf>

    """<?xml version="1.0" encoding="UTF-8"?>""" + "\n" + xmlDoc.toString()

  }

}

