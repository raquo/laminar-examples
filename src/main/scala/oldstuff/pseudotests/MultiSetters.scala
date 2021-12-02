package oldstuff.pseudotests

import com.raquo.laminar.api.L.{*, given}
import oldstuff.components.Toggle2
import org.scalajs.dom

object MultiSetters {

  def apply(): Div = {

    val toggle = Toggle2("Big")
    val toggle2 = Toggle2("/about")

    val $fontSize = toggle.$checkedInput.map(checked => if (checked) "45px" else "30px")
    val $href = toggle2.$checkedInput.map(checked => if (checked) "http://yolo.com/ABOUT" else "http://yolo.com/")

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

