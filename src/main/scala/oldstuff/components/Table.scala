package oldstuff.components

import com.raquo.laminar.api.L.{*, given}

object Table {
  def apply(): HtmlElement = {
    table(
      tr(td(colSpan := 2, "a"), td("b")),
      tr(td("1"), td("2"), td("3"))
    )
  }
}
