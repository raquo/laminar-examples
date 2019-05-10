package oldstuff.pseudotests

import com.raquo.laminar.api.L._
import oldstuff.components.Toggle2

object MultiStyleProp {

  def apply(): Div = {

    val toggle = Toggle2("Big")
    val toggle2 = Toggle2("Red")

    val $fontSize = toggle.$checkedInput.toSignal(true).map(checked => if (checked) "45px" else "30px")
    val $fontColor = toggle2.$checkedInput.toSignal(true).map(checked => if (checked) "red" else "lime")

    div(
      className := "yolo",
      h1("MultiStyleProp"),
      toggle.node,
      toggle2.node,
      div(
        color <-- $fontColor,
        fontSize <-- $fontSize,
        span("HELLO")
      )
    )
  }
}
