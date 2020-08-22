package webcomponents

import com.raquo.laminar.api.L._
import org.scalajs.dom
import webcomponents.material._

import scala.scalajs.js

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
          href := "https://github.com/material-components/material-components-web-components/tree/master/packages/button",
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
        p(
          "The progress bar below is a ",
          a(
            href := "https://github.com/material-components/material-components-web-components/tree/master/packages/linear-progress",
            "@material/mwc-linear-progress"
          ),
          " web component"
        ),
        div(
          LinearProgressBar(
            _.progress <-- progressVar.signal,
          )
        ),
        p(
          "The slider below is a ",
          a(
            href := "https://github.com/material-components/material-components-web-components/tree/master/packages/slider",
            "@material/mwc-slider"
          ),
          " web component"
        ),
        div(
          Slider(
            _.pin := true,
            _.min := 0,
            _.max := 20,
             _ => onMountCallback(ctx => {
               js.timers.setTimeout(1) {
                 // This component initializes its mdcFoundation asynchronously,
                 // so we need a short delay before accessing .layout() on it.
                 // To clarify, thisNode.ref already exists on mount, but the web component's
                 // implementation of layout() depends on thisNode.ref.mdcFoundation, which is
                 // populated asynchronously for some reason so it's not available on mount.
                 dom.console.log(ctx.thisNode.ref.layout())
               }
             }),
            slider => inContext { thisNode => slider.onInput.mapTo(thisNode.ref.value / 20) --> progressVar }
          )
        )
      )
    )
  }
}
