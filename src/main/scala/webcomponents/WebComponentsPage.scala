package webcomponents

import com.raquo.laminar.api.L._
import org.scalajs.dom

object WebComponentsPage {

  def apply(): Div = {
    val actionVar = Var("Do the thing")

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
          _.slots.icon(span("<>")),
          //_ => onMountCallback(ctx => ctx.thisNode.ref.doThing()) // doThing is not implemented, just for reference
        )
      )
    )
  }
}
