package com.raquo.laminarexamples.todomvc.components

import com.raquo.laminar.implicits._
import com.raquo.laminar.nodes.ReactiveElement
import com.raquo.laminar.tags._
import com.raquo.laminar.attrs._
import com.raquo.laminar.events.onClick
import com.raquo.xstream.XStream
import org.scalajs.dom

import scala.util.Random

class Toggle private (
  val $checked: XStream[Boolean],
  val node: ReactiveElement[dom.html.Span]
)

object Toggle {

  // @TODO how do we make this a controlled component?
  def apply(initialChecked: Boolean, caption: String): Toggle = {
    val $click = XStream.create[dom.MouseEvent]()

    // This will only be evaluated once
    val inputId = "toggle" + Random.nextInt(99)

    val checkbox = input(
      id := inputId,
      cls := "red",
      typ := "checkbox",
      checked := initialChecked,
      onClick --> $click
    )

    val $checked = $click.map(_ => checkbox.ref.checked)

    // @TODO this implicit conversion is ugly, <-- should pick up on it
    // @TODO or... maybe we should rather document textContent? Wait... textContent actually clears the rest of the content in the element...
    // @TODO I think maybe add a textChild? XStream[String] is too generic to be auto-converted. But then what do we do with maybeChild? Need a different API for that
//    val $caption: XStream[ReactiveChildNode[dom.Node]] = $checked.map(checked => if (checked) "ON" else "off")

    val node = span(
      cls := "Toggle",
      checkbox,
      label(forId := inputId, caption)
    )

    new Toggle($checked, node)
  }
}
