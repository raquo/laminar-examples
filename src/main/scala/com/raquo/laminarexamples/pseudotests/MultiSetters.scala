package com.raquo.laminarexamples.pseudotests

import com.raquo.laminar.bundle._
import com.raquo.laminar.nodes.ReactiveElement
import com.raquo.laminarexamples.components.Toggle
import org.scalajs.dom

object MultiSetters {

  def apply(): ReactiveElement[dom.Element] = {

    val toggle = Toggle("Big")
    val toggle2 = Toggle("/about")

    val $fontSize = toggle.$checked.map(checked => if (checked) "45px" else "30px")
    val $href = toggle2.$checked.map(checked => if (checked) "http://yolo.com/ABOUT" else "http://yolo.com/")

    div(
      className := "yolo",
      h1("MultiStyleProp"),
      toggle.node,
      toggle2.node,
      a(
        href <-- $href,
        fontSize <-- $fontSize,
        span("HELLO")
      )
    )
  }
}

