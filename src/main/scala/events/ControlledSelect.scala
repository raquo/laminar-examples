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
    val optionsSignal = Signal.fromValue(List( // @Note if this is EventStream, it's different
      option(value("a"), "A"),
      option(value("b"), "B"),
      option(value("c"), "C"),
    ))
    div(
      h3("EventBus."),
      select(
        onChange.mapToValue --> log,
        typ("checkbox"),
        value <-- selectValue.signal.combineWithFn(optionsSignal)((v, _) => v),
        children <-- optionsSignal,
        mods
      ),
      renderLogger(selectValue.signal, log.events, selectValue.writer)
    )
  }

}
