package webcomponents.material

import com.raquo.laminar.api.L._
import com.raquo.laminar.nodes.ReactiveHtmlElement
import org.scalajs.dom

import scala.scalajs.js
import scala.scalajs.js.annotation.JSImport

object TabBar {

  @js.native
  trait RawElement extends js.Object {}

  @js.native
  @JSImport("@material/mwc-tab-bar", JSImport.Default)
  object RawImport extends js.Object
  RawImport // object-s are lazy so you need to actually use them in your code

  type Ref = dom.html.Element with RawElement
  type El = ReactiveHtmlElement[Ref]
  type ModFunction = TabBar.type => Mod[El]

  private val tag = customHtmlTag[Ref]("mwc-tab-bar")

  val activated: EventProp[Activated] = customEventProp("MDCTabBar:activated")

  @js.native
  trait Activated extends dom.Event {
    val detail: Index = js.native
  }

  @js.native
  trait Index extends js.Object {
    val index: Double = js.native
  }

  def apply(mods: ModFunction*): El = {
    tag(mods.map(_(TabBar)): _*)
  }
}
