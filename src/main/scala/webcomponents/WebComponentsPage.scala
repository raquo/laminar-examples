package webcomponents

import com.raquo.laminar.api.L._
import org.scalajs.dom
import webcomponents.material._

object WebComponentsPage {

  def apply(): Div = {
    val actionVar = Var("Do the thing")
    val iconVar = Var("<>")
    val progressVar = Var(0d)

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
        Button(
          _.id := "myButton",
          _.label <-- actionVar.signal,
          //_.icon := "code",
          _.styles.mdcThemePrimary := "#ff0000",
          _ => onClick --> (_ => dom.window.alert("Click")), // standard event
          _.onMouseOver --> (_ => println("MouseOver")), // "custom" event
          _.slots.icon(span(child.text <-- iconVar.signal)),
          //_ => onMountCallback(ctx => ctx.thisNode.ref.doThing()) // doThing is not implemented, just for reference
        )
      ),
      p(
        div(
          LinearProgressBar(
            _.progress <-- progressVar.signal,
          )
        ),
        div(
          Slider(
            _.pin := true,
            _.min := 0,
            _.max := 20,
            // _ => onMountCallback(ctx => ctx.thisNode.ref.layout()), // failing with TypeError: Cannot read property 'layout' of undefined
            _.onInput --> (ev => progressVar.set(ev.target.asInstanceOf[Slider.Ref].value / 20)), // is it possible to avoid the cast here?
          )
        )
      )
    )
  }
}
