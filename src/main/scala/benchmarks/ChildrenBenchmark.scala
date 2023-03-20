package benchmarks

import com.raquo.laminar.api.L.{*, given}
import org.scalajs.dom

  object ChildrenBenchmark {

  def apply(): HtmlElement = {
    val itemCountVar = Var(1000)
    val logVar = Var(List[HtmlElement]())
    var ms = dom.window.performance.now()
    div(
      div(
        h2("Number of elements"),
        input(
          controlled(
            value <-- itemCountVar.signal.map(_.toString),
            onInput.mapToValue.map(_.toInt) --> itemCountVar.writer
          )
        ),
      ),
      child <-- itemCountVar.signal.map { itemCount =>
        val filterText = Var("")
        val items = Var(makeRange(itemCount).map(idx => (idx, s"Item ${idx}")))
        val filteredItems = items.signal.combineWith(filterText.signal).map {
          case (its, "") => its
          case (its, x) => its.filter(_._2.contains(x)).toList
        }

        div(
          div(
            h2("Filter"),
            input(
              `type`("text"),
              value <-- filterText,
              onInput.mapToValue --> filterText
            )
          ),
          div(
            maxHeight("300px"),
            overflow("scroll"),
            children <-- filteredItems.signal
              .split(_._1)((idx, initial, fstream) => {
                div(s"I am item: ${initial._2}")
              })
          )
        )
      }
    )
  }

  private def makeRange(itemCount: Int) = {
    val debugLabel = s"makeRange($itemCount)"
    dom.console.time(debugLabel)
    val range = (0 until itemCount)
    dom.console.timeEnd(debugLabel)

    val debugLabel2 = s"rangeToList($itemCount)"
    dom.console.time(debugLabel2)
    val list = range.toList
    dom.console.timeEnd(debugLabel2)

    list
  }
}
