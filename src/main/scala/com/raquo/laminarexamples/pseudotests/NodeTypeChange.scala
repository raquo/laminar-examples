package com.raquo.laminarexamples.pseudotests

import com.raquo.laminar.api.L._
import com.raquo.laminarexamples.components.Toggle2
import org.scalajs.dom

object NodeTypeChange {

  def boldOrItalic($useB: LazyObservable[Boolean], $bigFont: LazyObservable[Boolean]): LazyObservable[Element] = {
    val $fontSize = fontSizeStream($bigFont) // @TODO use remember()?
    $useB.map { useB =>
      dom.console.warn("useB: " + useB)
      if (useB) {
        b(
          "B",
          fontSize <-- $fontSize
        )
      } else {
        i(
          fontSize <-- $fontSize,
          "I"
        )
      }
    }
  }

  def fontSizeStream($big: LazyObservable[Boolean]): LazyObservable[String] = {
    $big.map(ok => if (ok) {
      "45px"
    } else {
      "30px"
    })
  }

  def apply(): Div = {

    val toggle = Toggle2("Bold")
    val toggle2 = Toggle2("Big")
    //    val counter = Counter()
    div(
      "APP",
      div(
        toggle.node,
        toggle2.node,
        child <-- boldOrItalic($useB = toggle.$checkedInput, $bigFont = toggle2.$checkedInput.toSignal(false))
        //        div(
        //          color <-- myColor(toggle.$checked),
        //          b("COLOR")
        //        )
        //        counter.vNode
      )
    )
  }
}
