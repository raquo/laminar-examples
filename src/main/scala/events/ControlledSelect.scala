package events

import com.raquo.airstream.core.Source
import com.raquo.laminar.api.L.{*, given}

object ControlledSelect {

  private val sep = " – "

  def apply(): HtmlElement = {
    val selectValue = Var("c")
    div(
      h1("Controlled <select> value prop"),
      div(
        input(
          value <-- selectValue,
          onInput.mapToValue --> selectValue
        ),
      ),
      hr(),
      hr(),
      renderExternallyControlled(selectValue),
      //hr(),
      //renderWithVar(lockedChecked)
    )
  }

  private def renderLogger(source: Observable[String], log: Observable[String], observer: Observer[String]): List[Modifier[HtmlElement]] = List(
    sep,
    span(child.text <-- source.map(_.toString)),
    sep,
    span(child.text <-- log.map(_.toString)),
    sep,
    button(onClick.mapTo("b") --> observer, "Set from var")
  )

  private def renderExternallyControlled(selectValue: Var[String], mods: Mod[Select]*): HtmlElement = {
    val log = new EventBus[String]
    val $children = Signal.fromValue(List( // @Note if this is EventStream, it's different
      option(value("a"), "A"),
      option(value("b"), "B"),
      option(value("c"), "C"),
    ))
    val dropdown = select(
      onChange.mapToValue --> log,
      typ("checkbox"),
      value <-- selectValue.signal.combineWithFn($children)((v, _) => v),
      children <-- $children,
      mods
    )
    div(
      inContext { thisNode =>
        List[Modifier[HtmlElement]](
          h3("EventBus."),
          button(thisNode.amend(dropdown)),
          renderLogger(selectValue.signal, log.events, selectValue.writer)
        )
      }
    )
  }

}
