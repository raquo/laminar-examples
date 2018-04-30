package com.raquo.laminarexamples.components

import com.raquo.laminar.api.L._

import scala.util.Random

class Toggle2 private (
  val $checkedInput: EventStream[Boolean],
  val node: Div
)

object Toggle2 {

  // @TODO how do we make this a controlled component?
  def apply(caption: String = "TOGGLE MEEEE"): Toggle2 = {
    val checkedBus = new EventBus[Boolean]

    // This will only be evaluated once
    val rand = Random.nextInt(99)

    val checkbox = input(
      id := "toggle" + rand,
      className := "red",
      `type` := "checkbox",
      inContext(thisNode => onClick.preventDefault.map(_ => thisNode.ref.checked) --> checkedBus)
    )

    val $captionNode = checkedBus.events.map(checked => span(if (checked) "ON" else "off"))

    val node = div(
      className := "Toggle",
      checkbox,
      label(forId := "toggle" + rand, caption),
      " — ",
      child <-- $captionNode
    )

    new Toggle2(checkedBus.events, node)
  }
}
