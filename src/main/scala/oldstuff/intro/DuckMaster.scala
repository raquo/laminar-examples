package oldstuff.intro

import com.raquo.laminar.api.L._
import oldstuff.components.Counter

object DuckMaster {

  def apply(): Div = {
    val counter = Counter("Number of ducks")

    val summarySignal = counter.countSignal.map { count =>
      if (count <= 0) { "Out of ducks today" }
      else if (count < 5) { "Duck colony stable"}
      else { "DUCKMAGEDDON IMMINENT!!!" }
    }

    div(
      h1("DuckMaster 9000 Dashboard"),
      counter.node,
      br(),
      div(child.text <-- summarySignal)
    )
  }
}
