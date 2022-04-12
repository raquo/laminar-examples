package webcomponents.material

import com.raquo.domtypes.generic.codecs.StringAsIsCodec
import com.raquo.laminar.api.L._
import com.raquo.laminar.nodes.ReactiveHtmlElement
import org.scalajs.dom

import scala.scalajs.js
import scala.scalajs.js.annotation.JSImport

object Tab {

  @js.native
  trait RawElement extends js.Object {
    def activate(): Unit
    def deactivate(): Unit
  }

  @js.native
  @JSImport("@material/mwc-tab", JSImport.Default)
  object RawImport extends js.Object
  RawImport // object-s are lazy so you need to actually use them in your code

  type Ref = dom.html.Element with RawElement
  type El = ReactiveHtmlElement[Ref]
  type ModFunction = Tab.type => Mod[El]

  private val tag = customHtmlTag[Ref]("mwc-tab")

  object slots {
    def icon(el: HtmlElement): HtmlElement = el.amend(slot := "icon")
  }

  val label: Prop[String] = customProp("label", StringAsIsCodec)

  def apply(mods: ModFunction*): El = {
    tag(mods.map(_(Tab)): _*)
  }
}
