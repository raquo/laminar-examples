package com.raquo.laminarexamples.components

import com.raquo.laminar.attrs.cls
import com.raquo.laminar.child
import com.raquo.laminar.emitter.EventBus
import com.raquo.laminar.events.onClick
import com.raquo.laminar.implicits._
import com.raquo.laminar.nodes.ReactiveNode
import com.raquo.laminar.tags.{button, div, span}
import com.raquo.xstream.{MemoryStream, XStream}
import org.scalajs.dom

class Counter private (
  val $count: MemoryStream[Int],
  val node: ReactiveNode
)

object Counter {
  def apply(label: String): Counter = {
    val incClickBus = new EventBus[dom.MouseEvent]
    val decClickBus = new EventBus[dom.MouseEvent]

    val $count = XStream
      .merge(incClickBus.$.mapTo(1), decClickBus.$.mapTo(-1))
      .fold((a: Int, b: Int) => a + b, seed = 0)
      .debugWithLabel("$count")

    val node = div(
      cls := "Counter",
      button(onClick --> decClickBus, "–"),
      child <-- $count.map(count => span(s" :: $count ($label) :: ")),
      button(onClick --> incClickBus, "+")
    )

    new Counter($count, node)
  }
}
