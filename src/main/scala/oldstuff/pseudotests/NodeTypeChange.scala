package oldstuff.pseudotests

import com.raquo.laminar.api.L.{*, given}
import oldstuff.components.Toggle2
import org.scalajs.dom

object NodeTypeChange {

  def boldOrItalic(useBold: Observable[Boolean], isBigFont: Observable[Boolean]): Observable[Element] = {
    val fontSizeStream = makeFontSizeStream(isBigFont)
    useBold.map { useB =>
      dom.console.warn("useB: " + useB)
      if (useB) {
        b(
          "B",
          fontSize <-- fontSizeStream
        )
      } else {
        i(
          fontSize <-- fontSizeStream,
          "I"
        )
      }
    }
  }

  def makeFontSizeStream(isBig: Observable[Boolean]): Observable[String] = {
    isBig.map(ok => if (ok) {
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
        child <-- boldOrItalic(useBold = toggle.checkedInputStream, isBigFont = toggle2.checkedInputStream.toSignal(false))
        //        div(
        //          color <-- myColor(toggle.$checked),
        //          b("COLOR")
        //        )
        //        counter.vNode
      )
    )
  }
}
