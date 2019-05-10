package oldstuff.pseudotests

import com.raquo.laminar.api.L._
import com.raquo.laminar.api.L.{svg => s}

object SvgContainer {

  def apply(): HtmlElement = {
    div(
      "SVG!!!",
      s.svg(
        s.height := "200",
        s.width := "400",
        s.circle(
          s.cx := "200",
          s.cy := "15",
          s.r := "30",
          s.fill := "red"
        )
      )
    )
  }
}
