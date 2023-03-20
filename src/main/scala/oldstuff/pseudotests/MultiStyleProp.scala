package oldstuff.pseudotests

import com.raquo.laminar.api.L.{*, given}
import oldstuff.components.Toggle2

object MultiStyleProp {

  def apply(): Div = {

    val toggle = Toggle2("Big")
    val toggle2 = Toggle2("Red")

    val fontSizes = toggle.checkedInputStream.toSignal(true).map(checked => if (checked) "45px" else "30px")
    val fontColors = toggle2.checkedInputStream.toSignal(true).map(checked => if (checked) "red" else "lime")

    div(
      className := "yolo",
      h1("MultiStyleProp"),
      toggle.node,
      toggle2.node,
      div(
        color <-- fontColors,
        fontSize <-- fontSizes,
        span("HELLO")
      )
    )
  }
}
