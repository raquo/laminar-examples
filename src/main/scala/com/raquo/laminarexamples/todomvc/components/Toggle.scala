package com.raquo.laminarexamples.todomvc.components

import com.raquo.laminar.implicits._
import com.raquo.laminar.nodes.ReactiveElement
import com.raquo.laminar.props.textContent
import com.raquo.laminar.tags._
import com.raquo.laminar.attrs._
import com.raquo.laminar.emitter.EventBus
import com.raquo.laminar.events.onClick
import com.raquo.xstream.XStream
import org.scalajs.dom
import org.scalajs.dom.raw.MouseEvent

import scala.util.Random

class Toggle private (
  val $checkedRequest: XStream[Boolean],
  val node: ReactiveElement[dom.html.Span]
)

object Toggle {

  // @TODO how do we make this a controlled component?
  def apply(
    $checked: XStream[Boolean],
    $caption: XStream[String]
  ): Toggle = {
    val clickBus = new EventBus[MouseEvent]

    // This will only be evaluated once
    val inputId = "toggle" + Random.nextInt(99)

    val checkbox = input(
      id := inputId,
      cls := "red",
      typ := "checkbox",
      checked <-- $checked,
      onClick --> clickBus
    )

    val $checkedRequest = clickBus.$.map(_ => checkbox.ref.checked) // @TODO this will change once we have

    // @TODO this implicit conversion is ugly, <-- should pick up on it
    // @TODO or... maybe we should rather document textContent? Wait... textContent actually clears the rest of the content in the element...
    // @TODO I think maybe add a textChild? XStream[String] is too generic to be auto-converted. But then what do we do with maybeChild? Need a different API for that
//    val $caption: XStream[ReactiveChildNode[dom.Node]] = $checked.map(checked => if (checked) "ON" else "off")

    val node = span(
      cls := "Toggle",
      checkbox,
      label(forId := inputId, textContent <-- $caption)
    )

    new Toggle($checkedRequest, node)
  }
}
