package webcomponents

import com.raquo.laminar.api.L._

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
          _.label <-- actionVar.signal,
          _.icon := "code"
        )
      )
    )
  }
}
