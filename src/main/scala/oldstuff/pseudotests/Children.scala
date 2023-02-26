package oldstuff.pseudotests

import com.raquo.laminar.api.L.{*, given}
import oldstuff.components.Toggle2

object Children {
  def apply(): Div = {
    val toggle = Toggle2("Toggle #1")
    val $text = toggle.$checkedInput.map(checked => if (checked) "[X]" else "[O]")
    val $div = $text.map(div(_)).toSignal(div("INIT"))

    div(
      className := "yolo",
      h1("Children"),
      toggle.node,
      child <-- $div
    )
  }
}
