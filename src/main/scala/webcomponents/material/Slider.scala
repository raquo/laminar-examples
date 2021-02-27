package webcomponents.material

import com.raquo.domtypes.generic.codecs._
import com.raquo.laminar.api.L._
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

  private val tag = customHtmlTag[Ref]("mwc-slider")

  val pin: Prop[Boolean] = customProp("pin", BooleanAsIsCodec)

  val markers: Prop[Boolean] = customProp("markers", BooleanAsIsCodec)

  val value: Prop[Double] = customProp("value", DoubleAsIsCodec)

  val min: Prop[Double] = customProp("min", DoubleAsIsCodec)

  val max: Prop[Double] = customProp("max", DoubleAsIsCodec)

  val step: Prop[Double] = customProp("step", DoubleAsIsCodec)


  val onInput: EventProp[dom.Event] = customEventProp("input")

  val onChange: EventProp[dom.Event] = customEventProp("change")


  object styles {
    val mdcThemeSecondary: Style[String] = customStyle("--mdc-theme-secondary")
  }

  def apply(mods: ModFunction*): El = {
    tag(mods.map(_(Slider)): _*)
  }

  //def inContext(makeMod: El => ModFunction): Mod[El] = {
  //  L.inContext { thisNode => makeMod(thisNode)(this) }
  //}
}
