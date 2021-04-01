package oldstuff.pseudotests

import com.raquo.laminar.api.L._
import com.raquo.laminar.api.L.{svg => s}

object SvgContainer {

  def apply(): HtmlElement = {
    div(
      "SVG!!!",
      s.svg(
        // Note: you generally don't need to specify xmlns attribute when building SVGs with scripts (as opposed to downloading)
        s.xmlns := "http://www.w3.org/2000/svg", // @TODO SvgNamespaces.svg constant when it's available
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
