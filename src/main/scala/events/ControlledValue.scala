package events

import com.raquo.laminar.api.L.{*, given}

object ControlledValue {

  private val sep = " – "

  def apply(): HtmlElement = {
    div(
      h1("Controlled value prop"),
      hr(),
      div("Sample texts: 123 abc 123abc abc123"),
      hr(),
      renderUncontrolled(),
      hr(),
      renderLegacy(),
      hr(),
      renderWithEventBus(),
      hr(),
      renderWithVar(),
      hr(),
      renderWithVarFilter(),
      hr(),
      renderWithSignalAndAppendingObserver(),
      hr(),
      renderWithSignalAndAppendingObserverFilter(),
      hr(),
      renderWithSignalAndFilteringObserver(),
      hr(),
      br(),
      h1("Type=email inputs"),
      hr(),
      renderEmailWithEventBus(),
      hr(),
      renderEmailWithVar(),
      hr(),
      br(),
      h1("Inputs with defaultValue"),
      renderWithEventBus(
        defaultValue("123")
      ),
      hr(),
      renderWithVar(
        defaultValue("123")
      ),
      hr(),
      br(),
      h1("Delayed source updates"),
      renderWithTransactionDelaySignal(),
      hr(),
      renderWithAsyncDelaySignal(),
      hr(),
      h1("Select"),
      renderSelectWithVar(),
      br(),
    )
  }

  private def renderLogger(source: Observable[String], log: Observable[String], observer: Observer[String]): List[Modifier[HtmlElement]] = List(
    sep,
    span(child.text <-- source),
    sep,
    span(child.text <-- log),
    sep,
    button(onClick.mapTo("999") --> observer, "Set from var")
  )

  private def renderUncontrolled(mods: Mod[Input]*): HtmlElement = {
    val bus = new EventBus[String]
    val log = Var("")
    div(
      h3("UNCONTROLLED input"),
      input(
        onInput.mapToValue --> log,
        mods
      ),
      renderLogger(bus.events, log.signal, bus.writer)
    )
  }

  private def renderLegacy(mods: Mod[Input]*): HtmlElement = {
    val bus = new EventBus[String]
    val log = Var("")
    div(
      h3("LEGACY Controlled input"),
      input(
        value <-- bus.events,
        onInput.mapToValue --> bus.writer,
        onInput.mapToValue --> log,
        mods
      ),
      renderLogger(bus.events, log.signal, bus.writer)
    )
  }

  private def renderWithEventBus(mods: Mod[Input]*): HtmlElement = {
    val bus = new EventBus[String]
    val log = Var("")
    div(
      h3("EventBus. Digits only (map)"),
      input(
        controlled(
          value <-- bus.events,
          onInput.mapToValue.map(_.filter(Character.isDigit)) --> bus.writer
        ),
        onInput.mapToValue --> log,
        mods
      ),
      renderLogger(bus.events, log.signal, bus.writer)
    )
  }

  private def renderWithVar(mods: Mod[Input]*): HtmlElement = {
    val v = Var("")
    val log = Var("")
    div(
      h3("Var. Digits only (map)"),
      input(
        onInput.mapToValue --> log,
        controlled(
          value <-- v.signal,
          onInput.mapToValue.map(_.filter(Character.isDigit)) --> v
        ),
        mods
      ),
      renderLogger(v.signal, log.signal, v.writer)
    )
  }

  private def renderWithVarFilter(): HtmlElement = {
    val v = Var("")
    val log = Var("")
    div(
      h3("Var. Digits only (filter)"),
      input(
        onInput.mapToValue --> log,
        controlled(
          value <-- v.signal,
          onInput.mapToValue.filter(_.forall(Character.isDigit)) --> v
        )
      ),
      renderLogger(v.signal, log.signal, v.writer)
    )
  }

  private def renderWithSignalAndAppendingObserver(): HtmlElement = {
    val v = Var("")
    val log = Var("")
    div(
      h3("Signal. Appending observer. Digits only (map)"),
      input(
        onInput.mapToValue --> log,
        controlled(
          value <-- v.signal,
          onInput.mapToValue.map(_.filter(Character.isDigit)) --> v.writer.contramap[String](_ + "0").debugLog()
        ),
      ),
      renderLogger(v.signal, log.signal, v.writer)
    )
  }

  private def renderWithSignalAndAppendingObserverFilter(): HtmlElement = {
    val v = Var("")
    val log = Var("")
    div(
      h3("Signal. Appending observer. Digits only (filter)"),
      input(
        onInput.mapToValue --> log,
        controlled(
          value <-- v.signal,
          onInput.mapToValue.filter(_.forall(Character.isDigit)) --> v.writer.contramap[String](_ + "0")
        ),
      ),
      renderLogger(v.signal, log.signal, v.writer)
    )
  }

  private def renderWithSignalAndFilteringObserver(): HtmlElement = {
    val v = Var("")
    val log = Var("")
    div(
      h3("Signal. Filtering observer."),
      input(
        onInput.mapToValue --> log,
        controlled(
          value <-- v.signal,
          onInput.mapToValue --> v.writer.contramap[String](_.filter(Character.isDigit))
        ),
      ),
      renderLogger(v.signal, log.signal, v.writer)
    )
  }

  private def renderWithTransactionDelaySignal(): HtmlElement = {
    val v = Var("")
    val log = Var("")
    val signal = v.signal.flatMap(v => EventStream.fromValue(v.filter(Character.isDigit)))
    div(
      h3("Signal w/ trx delay. Digits only (map in signal)"),
      input(
        onInput.mapToValue --> log,
        controlled(value <-- signal, onInput.mapToValue --> v.writer),
      ),
      renderLogger(signal, log.signal, v.writer)
    )
  }

  private def renderWithAsyncDelaySignal(): HtmlElement = {
    val v = Var("")
    val log = Var("")
    val signal = v.signal.composeChanges(_.delay(1000)).map(_.filter(Character.isDigit)).setDisplayName("signal").debugLog()
    div(
      h3("Signal w/ async delay. Digits only (map in signal)"),
      input(
        controlled(value <-- signal, onInput.mapToValue --> v.writer.setDisplayName("v.writer").debugLog()),
        onInput.mapToValue --> log,
      ),
      renderLogger(signal, log.signal, v.writer)
    )
  }



  // -- Email

  private def renderEmailWithEventBus(): HtmlElement = {
    val bus = new EventBus[String]
    val log = Var("")
    div(
      h3("EventBus"),
      input(
        typ("email"),
        onInput.mapToValue --> log,
        controlled(
          value <-- bus.events,
          onInput.mapToValue --> bus.writer
        )
      ),
      renderLogger(bus.events, log.signal, bus.writer)
    )
  }

  private def renderEmailWithVar(): HtmlElement = {
    val v = Var("")
    val log = Var("")
    div(
      h3("Var"),
      input(
        typ("email"),
        onInput.mapToValue --> log,
        controlled(
          value <-- v.signal,
          onInput.mapToValue --> v.writer
        )
      ),
      renderLogger(v.signal, log.signal, v.writer)
    )
  }



  // Select

  private def renderSelectWithVar(mods: Mod[Select]*): HtmlElement = {
    val options = List(
      "123",
      "456",
      "789",
      "abc",
      "def",
      "ghi"
    )
    val initialValue = options(2)
    val v = Var(initialValue)
    val log = Var("")
    div(
      h3("Select. Var. Digits only (filter)"),
      select(
        onChange.mapToValue --> log,
        controlled(
          value <-- v.signal,
          onChange.mapToValue.filter(_.forall(Character.isDigit)) --> v
        ),
        options.map(optionValue => option(value(optionValue), optionValue)),
        mods
      ),
      renderLogger(v.signal, log.signal, v.writer)
    )
  }
}
