package oldstuff.components

import com.raquo.laminar.api.L.{*, given}
import org.scalajs.dom

class Counter private (
  val countSignal: Signal[Int],
  val node: Node
)

object Counter {
  def apply(label: String): Counter = {

    val incClickBus = new EventBus[dom.MouseEvent]
    val decClickBus = new EventBus[dom.MouseEvent]

    val countSignal = EventStream
      .merge(incClickBus.events.mapTo(1), decClickBus.events.mapTo(-1))
      .scanLeft(initial = 0)(_ + _)

    val node = div(
      className := "Counter",
      button(onClick --> decClickBus, "–"),
      child <-- countSignal.map(count => span(s" :: $count ($label) :: ")),
      button(onClick --> incClickBus, "+")
    )

    new Counter(countSignal, node)
  }
}
