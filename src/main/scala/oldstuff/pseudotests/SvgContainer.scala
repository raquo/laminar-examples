package oldstuff.pseudotests

import com.raquo.laminar.api.L._
import com.raquo.laminar.api.L.{svg => s}

object SvgContainer {

  def apply(): HtmlElement = {
    div(
      h2("Offset circle"),
      simpleSvg(),
      h2("Foreign object (HTML)"),
      foreignObjectSvg()
    )
  }

  def simpleSvg(): SvgElement = {
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
  }

  def foreignObjectSvg(): SvgElement = {
    s.svg(
      s.height := "200",
      s.width := "400",
      s.polygon(s.points("5,5 195,10 185,175 10,195"), s.fill("yellow")),
      s.foreignObject(
        s.x("20"),
        s.y("20"),
        s.width("160"),
        s.height("160"),
        div(
          //xmlns("http://www.w3.org/1999/xhtml"),
          "Hello!"
        )
      )
    )
  }
}
