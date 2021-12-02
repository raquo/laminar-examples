package events

import com.raquo.laminar.api.L.{*, given}

object ControlledChecked {

  private val sep = " – "

  def apply(): Div = {
    val lockedChecked = Var(false)
    div(
      h1("Controlled checked prop"),
      div(
        "Lock to:",
        input(
          typ("checkbox"),
          onClick.mapToChecked --> lockedChecked
        ),
      ),
      hr(),
      hr(),
      renderWithEventBus(lockedChecked),
      hr(),
      renderWithVar(lockedChecked)
    )
  }

  private def renderLogger(source: Observable[Boolean], log: Observable[Boolean], observer: Observer[Boolean]): List[Modifier[HtmlElement]] = List(
    sep,
    span(child.text <-- source.map(_.toString)),
    sep,
    span(child.text <-- log.map(_.toString)),
    sep,
    button(onClick.mapTo(true) --> observer, "Set from var")
  )

  private def renderWithEventBus(lockedTo: Var[Boolean], mods: Mod[Input]*): HtmlElement = {
    val bus = new EventBus[Boolean]
    val log = new EventBus[Boolean]
    val i = input(
      onClick.mapToChecked --> log,
      typ("checkbox"),
      controlled(
        checked <-- bus.events,
        onClick.mapToChecked --> bus.writer.filter[Boolean](_ == lockedTo.now())
      ),
      mods
    )
    div(
      inContext { thisNode =>
        List[Modifier[HtmlElement]](
          h3("EventBus."),
          button(thisNode.amend(i)),
          renderLogger(bus.events, log.events, bus.writer)
        )
      }
    )
  }

  private def renderWithVar(lockedTo: Var[Boolean], mods: Mod[Input]*): HtmlElement = {
    val v = Var(false)
    val log = new EventBus[Boolean]
    val logMod = onClick.mapToChecked.map { checked =>
      println("- LOG observed = " + checked)
      checked
    } --> log.writer //.delay(100).setDisplayName("- LOG recorded = ").debugLog()
    val i = input(
      typ("checkbox"),
      logMod,
      onClick.mapToChecked.map { checked =>
        println("- LOG2 observed = " + checked)
        checked
      } --> Observer.empty,
      controlled(
        checked <-- v.signal,
        onClick.mapToChecked --> v.writer.filter[Boolean](_ == lockedTo.now())
      ),
      mods
    )
    div(
      inContext { thisNode =>
        List[Modifier[HtmlElement]](
          h3("Var."),
          button(thisNode.amend(i)),
          renderLogger(v.signal, log.events, v.writer)
        )
      }
    )
  }
}
