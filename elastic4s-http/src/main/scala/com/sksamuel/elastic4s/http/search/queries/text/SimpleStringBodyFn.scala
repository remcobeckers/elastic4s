package com.sksamuel.elastic4s.http.search.queries.text

import com.sksamuel.elastic4s.http.EnumConversions
import com.sksamuel.elastic4s.json.{XContentBuilder, XContentFactory}
import com.sksamuel.elastic4s.searches.queries.SimpleStringQueryDefinition

object SimpleStringBodyFn {
  def apply(s: SimpleStringQueryDefinition): XContentBuilder = {
    val builder = XContentFactory.jsonBuilder().startObject("simple_query_string")
    s.operator.map(_.toString).foreach(builder.field("default_operator", _))
    s.analyzer.map(_.toString).foreach(builder.field("analyzer", _))
    s.analyzeWildcard.map(_.toString).foreach(builder.field("analyze_wildcard", _))
    s.lenient.map(_.toString).foreach(builder.field("lenient", _))
    s.minimumShouldMatch.map(_.toString).foreach(builder.field("minimum_should_match", _))
    s.quote_field_suffix.foreach(builder.field("quote_field_suffix", _))
    if (s.fields.nonEmpty) {
      val fields = s.fields.map {
        case (name, 0.0D) => name
        case (name, boost) => s"$name^$boost"
      }.toArray
      builder.array("fields", fields)
    }
    if (s.flags.nonEmpty) {
      val flags = s.flags.map(EnumConversions.simpleQueryStringFlag).mkString("|")
      builder.field("flags", flags)
    }
    builder.field("query", s.query).endObject().endObject()
  }
}
