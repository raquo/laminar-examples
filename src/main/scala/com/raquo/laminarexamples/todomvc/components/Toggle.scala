package com.raquo.laminarexamples.todomvc.components

import com.raquo.laminar.bundle._
import com.raquo.laminar.nodes.ReactiveElement
import com.raquo.xstream.XStream
import org.scalajs.dom

import scala.util.Random

class Toggle private(
  val node: ReactiveElement[dom.html.Span],
  val checkbox: ReactiveElement[dom.html.Input],
  val label: ReactiveElement[dom.html.Label]
)

object Toggle {

  /** @param $checkedInput  Stream of user's input, containing desired checked state */
  class BoundToggle private[Toggle](
    val node: ReactiveElement[dom.html.Span],
    val $checkedInput: XStream[Boolean]
  )

  def apply(): Toggle = {
    // Note: This method will only be evaluated once per component instance (unlike say React's render method)
    val inputId = "toggle" + Random.nextInt(99)

    val checkbox = input(
      id := inputId,
      className := "red",
      typ := "checkbox"
    )
    val labelNode = label(forId := inputId)

    val node = span(
      className := "Toggle",
      checkbox,
      labelNode
    )

    new Toggle(node, checkbox, labelNode)
  }

  def apply(
    $checked: XStream[Boolean],
    $caption: XStream[String]
  ): BoundToggle = {
    val toggle = Toggle()
    toggle.checkbox <-- checked <-- $checked
    toggle.label <-- child.text <-- $caption
    // We set preventDefault=true so that the checkbox only updates when a new value is received from $checked
    // Note that we need to use onClick rather than unChange because onChange fires AFTER the checkbox has been checked.
    // onClick event for checkboxes is more or less equivalent to onInput event for text inputs. #frontendLife
    val $checkedInput = toggle.checkbox.$event(onClick, preventDefault = true).map(_ => !toggle.checkbox.ref.checked).debugWithLabel("$checkedInput")
    new BoundToggle(toggle.node, $checkedInput)
  }
}
