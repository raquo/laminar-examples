package webcomponents

import com.raquo.domtypes.generic.codecs.StringAsIsCodec
import com.raquo.laminar.api.L._
import com.raquo.laminar.api.L
import com.raquo.laminar.builders.HtmlTag
import com.raquo.laminar.keys.{ReactiveHtmlAttr, ReactiveProp, ReactiveStyle}
import com.raquo.laminar.nodes.ReactiveHtmlElement
import org.scalajs.dom

import scala.scalajs.js
import scala.scalajs.js.annotation.JSImport

object MwcButton {

  @js.native
  trait RawMwcButton extends js.Object {
    def doThing(): Unit // Note: This is not actually implemented in mwc-button, just an example
  }

  @js.native
  @JSImport("@material/mwc-button", JSImport.Default)
  object RawMwcButton extends js.Object

  // object-s are lazy so you need to actually use them in your code
  private val _ = RawMwcButton

  type Ref = dom.html.Element with RawMwcButton

  private val tag = new HtmlTag[Ref]("mwc-button", void = false)

  val id: ReactiveProp[String, String] = idAttr

  val label = new ReactiveHtmlAttr[String]("label", StringAsIsCodec)

  val icon = new ReactiveHtmlAttr[String]("icon", StringAsIsCodec)

  val onMouseOver = new EventProp[dom.MouseEvent]("mouseover")

  object slots {
    def icon(el: HtmlElement): HtmlElement = el.amend(slot := "icon")
  }

  object styles {
    import com.raquo.domtypes.generic.keys.Style // Laminar aliases ReactiveStyle as Style, but we want the original underlying type here

    val mdcThemePrimary = new ReactiveStyle(new Style("--mdc-theme-primary", "--mdc-theme-primary"))
  }

  def apply(mods: MwcButton.type => Mod[ReactiveHtmlElement[Ref]]*): HtmlElement = {
    tag(mods.map(_(MwcButton)): _*)
  }

}
