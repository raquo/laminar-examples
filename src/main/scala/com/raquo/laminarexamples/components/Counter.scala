package com.raquo.laminarexamples.components

import com.raquo.laminar.api.L._
import com.raquo.airstream.eventbus.EventBus
import com.raquo.airstream.eventstream.EventStream
import com.raquo.airstream.signal.Signal
import org.scalajs.dom

class Counter private (
  val $count: Signal[Int],
  val node: Node
)

object Counter {
  def apply(label: String): Counter = {
    val incClickBus = new EventBus[dom.MouseEvent]
    val decClickBus = new EventBus[dom.MouseEvent]

    val $count = EventStream
      .merge(incClickBus.events.mapTo(1), decClickBus.events.mapTo(-1))
      .fold(0)(_ + _)
      // .debugWithLabel("$count")

    val node = div(
      className := "Counter",
      button(onClick --> decClickBus, "–"),
      child <-- $count.map(count => span(s" :: $count ($label) :: ")),
      button(onClick --> incClickBus, "+")
    )

    new Counter($count, node)
  }
}
