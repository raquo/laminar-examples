import ajax.AjaxTester
import com.raquo.laminar.api.L._
import oldstuff.intro.DuckMaster
import oldstuff.pseudotests.SvgContainer
import todomvc.TodoMvcApp
import org.scalajs.dom.document
import webcomponents.WebComponentsPage

object App {

  def main(args: Array[String]): Unit = {

    // This div, its id and contents are defined in index-fastopt.html and index-fullopt.html files
    lazy val container = document.getElementById("app-container")

    lazy val appElement = {
      val maybeChosenApp = Var[Option[Example]](None)

      div(
        children <-- maybeChosenApp.signal.map {
          case Some(example) =>
            example.render() :: Nil
          case None =>
            List(
              h1("Choose example:"),
              ul(
                fontSize := "130%",
                lineHeight := "2em",
                examples.map { ex =>
                  li(
                    a(
                      href := "#",
                      ex.caption,
                      onClick.preventDefault.mapTo(Some(ex)) --> maybeChosenApp.writer
                    )
                  )
                }
              ),
              "Get back to this menu by reloading the page."
            )
        }
      )
    }

    // Wait until the DOM is loaded, otherwise app-container element might not exist
    renderOnDomContentLoaded(container, appElement)
  }

  sealed abstract class Example(val caption: String, val render: () => HtmlElement)

  case object TodoMVCExample extends Example("TodoMVC", () => TodoMvcApp())
  case object AjaxExample extends Example("Ajax", () => AjaxTester())
  case object WebComponentsExample extends Example("Web Components", () => WebComponentsPage())
  case object SvgContainerExample extends Example("SVG Container", () => SvgContainer())
  case object DuckCounterExample extends Example("Duck Counter", () => DuckMaster())
  // case object ControlledValueTester extends Example("Controlled Value Tester", () => ControlledValue())
  // case object ControlledCheckedTester extends Example("Controlled Checked Tester", () => ControlledChecked())

  val examples: List[Example] = List(
    DuckCounterExample,
    TodoMVCExample,
    AjaxExample,
    WebComponentsExample,
    SvgContainerExample,
    // ControlledValueTester,
    // ControlledCheckedTester
  )
}
