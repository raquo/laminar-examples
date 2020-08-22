package webcomponents

import com.raquo.laminar.api.L._
import org.scalajs.dom
import webcomponents.material.MwcButton

object WebComponentsPage {

  def apply(): Div = {
    val actionVar = Var("Do the thing")
    val iconVar = Var("<>")

    div(
      h1("Web Components"),
      p(
        label("Button label: "),
        input(
          value <-- actionVar.signal,
          inContext { thisNode => onInput.mapTo(thisNode.ref.value) --> actionVar.writer}
        )
      ),
      p(
        label("Button icon: "),
        input(
          value <-- iconVar.signal,
          inContext { thisNode => onInput.mapTo(thisNode.ref.value) --> iconVar.writer}
        )
      ),
      p(
        "The button below is a ",
        a(
          href := "",
          "@material/mwc-button"
        ),
        " web component"
      ),
      p(
        MwcButton(
          _.id := "myButton",
          _.label <-- actionVar.signal,
          //_.icon := "code",
          _.styles.mdcThemePrimary := "#ff0000",
          _ => onClick --> (_ => dom.window.alert("Click")), // standard event
          _.onMouseOver --> (_ => println("MouseOver")), // "custom" event
          _.slots.icon(span(child.text <-- iconVar.signal)),
          //_ => onMountCallback(ctx => ctx.thisNode.ref.doThing()) // doThing is not implemented, just for reference
        )
      )
    )
  }
}
