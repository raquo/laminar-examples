package webcomponents.material

import com.raquo.laminar.api.L._
import com.raquo.laminar.codecs._
import com.raquo.laminar.nodes.ReactiveHtmlElement
import org.scalajs.dom

import scala.scalajs.js
import scala.scalajs.js.annotation.JSImport

object Slider {

  @js.native
  trait RawElement extends js.Object {
    def layout(): Unit
    def value: Double
  }

  @js.native
  @JSImport("@material/mwc-slider", JSImport.Default)
  object RawImport extends js.Object
  RawImport // object-s are lazy so you need to actually use them in your code

  type Ref = dom.html.Element with RawElement
  type El = ReactiveHtmlElement[Ref]
  type ModFunction = Slider.type => Mod[El]

  private val tag = htmlTag[Ref]("mwc-slider")

  val pin: HtmlProp[Boolean] = htmlProp("pin", BooleanAsIsCodec)

  val markers: HtmlProp[Boolean] = htmlProp("markers", BooleanAsIsCodec)

  val value: HtmlProp[Double] = htmlProp("value", DoubleAsIsCodec)

  val min: HtmlProp[Double] = htmlProp("min", DoubleAsIsCodec)

  val max: HtmlProp[Double] = htmlProp("max", DoubleAsIsCodec)

  val step: HtmlProp[Double] = htmlProp("step", DoubleAsIsCodec)


  val onInput: EventProp[dom.Event] = eventProp("input")

  val onChange: EventProp[dom.Event] = eventProp("change")


  object styles {
    val mdcThemeSecondary: StyleProp[String] = styleProp("--mdc-theme-secondary")
  }

  def apply(mods: ModFunction*): El = {
    tag(mods.map(_(Slider)): _*)
  }

  //def inContext(makeMod: El => ModFunction): Mod[El] = {
  //  L.inContext { thisNode => makeMod(thisNode)(this) }
  //}
}
