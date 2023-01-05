package webcomponents.material

import com.raquo.laminar.api.L._
import com.raquo.laminar.codecs._
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

  private val tag = htmlTag[Ref]("mwc-linear-progress")

  val indeterminate: HtmlProp[Boolean] = htmlProp("indeterminate", BooleanAsIsCodec)

  val reverse: HtmlProp[Boolean] = htmlProp("reverse", BooleanAsIsCodec)

  val closed: HtmlProp[Boolean] = htmlProp("closed", BooleanAsIsCodec)

  val progress: HtmlProp[Double] = htmlProp("progress", DoubleAsIsCodec)

  val buffer: HtmlProp[Double] = htmlProp("buffer", DoubleAsIsCodec)

  object styles {
    val mdcThemePrimary: StyleProp[String] = styleProp("--mdc-theme-primary")
  }

  def apply(mods: ModFunction*): El = {
    tag(mods.map(_(LinearProgressBar)): _*)
  }
}
