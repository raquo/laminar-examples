package webcomponents.material

import com.raquo.laminar.api.L.{*, given}
import com.raquo.laminar.codecs.*
import com.raquo.laminar.nodes.ReactiveHtmlElement
import org.scalajs.dom

import scala.scalajs.js
import scala.scalajs.js.annotation.JSImport

object Button {

  @js.native
  trait RawElement extends js.Object {
    def doThing(): Unit // Note: This is not actually implemented in mwc-button, just an example
  }

  @js.native
  @JSImport("@material/mwc-button", JSImport.Default)
  object RawImport extends js.Object
  RawImport // object-s are lazy so you need to actually use them in your code

  type Ref = dom.html.Element with RawElement
  type El = ReactiveHtmlElement[Ref]
  type ModFunction = Button.type => Mod[El]

  private val tag = htmlTag[Ref]("mwc-button")

  val id: HtmlProp[String] = idAttr

  val raised: HtmlProp[Boolean] = htmlProp("raised", BooleanAsIsCodec)

  val label: HtmlAttr[String] = htmlAttr("label", StringAsIsCodec)

  val icon: HtmlAttr[String] = htmlAttr("icon", StringAsIsCodec)

  val onMouseOver: EventProp[dom.MouseEvent] = eventProp("mouseover")

  object slots {
    def icon(el: HtmlElement): HtmlElement = el.amend(slot := "icon")
  }

  object styles {
    val mdcThemePrimary: StyleProp[String] = styleProp("--mdc-theme-primary")
  }

  def apply(mods: ModFunction*): El = {
    tag(mods.map(_(Button)): _*)
  }

}
