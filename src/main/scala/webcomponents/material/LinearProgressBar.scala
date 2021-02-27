package webcomponents.material

import com.raquo.domtypes.generic.codecs._
import com.raquo.laminar.api.L._
import com.raquo.laminar.nodes.ReactiveHtmlElement
import org.scalajs.dom

import scala.scalajs.js
import scala.scalajs.js.annotation.JSImport

object LinearProgressBar {

  @js.native
  trait RawElement extends js.Object {
    def open(): Unit
    def close(): Unit
  }

  @js.native
  @JSImport("@material/mwc-linear-progress", JSImport.Default)
  object RawImport extends js.Object
  RawImport // object-s are lazy so you need to actually use them in your code

  type Ref = dom.html.Element with RawElement
  type El = ReactiveHtmlElement[Ref]
  type ModFunction = LinearProgressBar.type => Mod[El]

  private val tag = customHtmlTag[Ref]("mwc-linear-progress")

  val indeterminate: Prop[Boolean] = customProp("indeterminate", BooleanAsIsCodec)

  val reverse: Prop[Boolean] = customProp("reverse", BooleanAsIsCodec)

  val closed: Prop[Boolean] = customProp("closed", BooleanAsIsCodec)

  val progress: Prop[Double] = customProp("progress", DoubleAsIsCodec)

  val buffer: Prop[Double] = customProp("buffer", DoubleAsIsCodec)

  object styles {
    val mdcThemePrimary: Style[String] = customStyle("--mdc-theme-primary")
  }

  def apply(mods: ModFunction*): El = {
    tag(mods.map(_(LinearProgressBar)): _*)
  }
}
