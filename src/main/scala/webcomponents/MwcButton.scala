package webcomponents

import com.raquo.domtypes.generic.codecs.StringAsIsCodec
import com.raquo.laminar.api.L._
import com.raquo.laminar.builders.HtmlTag
import com.raquo.laminar.keys.ReactiveHtmlAttr
import org.scalajs.dom

import scala.scalajs.js
import scala.scalajs.js.annotation.{JSExport, JSExportTopLevel, JSImport}

object MwcButton {

  @js.native
  @JSImport("@material/mwc-button", JSImport.Default)
  object RawMwcButton extends js.Object

  // object-s are lazy so you need to actually use them in your code
  private val _ = RawMwcButton

  private val tag = new HtmlTag[dom.html.Element]("mwc-button", void = false)

  val label = new ReactiveHtmlAttr[String]("label", StringAsIsCodec)

  val icon = new ReactiveHtmlAttr[String]("icon", StringAsIsCodec)

  def apply(mods: MwcButton.type => Mod[HtmlElement]*): HtmlElement = {
    tag(mods.map(_(MwcButton)): _*)
  }

}
