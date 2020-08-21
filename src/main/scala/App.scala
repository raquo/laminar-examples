import com.raquo.laminar.api.L._
import oldstuff.intro.DuckMaster
import oldstuff.pseudotests.SvgContainer
import todomvc.TodoMvcApp
import org.scalajs.dom.document
import webcomponents.WebComponentsPage

object App {

  def main(args: Array[String]): Unit = {
    // Wait until the DOM is loaded, otherwise app-container element might not exist
    documentEvents.onDomContentLoaded.foreach { _ =>

      val container = document.getElementById("app-container") // This div, its id and contents are defined in index-fastopt.html/index-fullopt.html files
      container.textContent = ""

      val maybeChosenApp = Var[Option[Example]](None)

      val appElement = div(
        children <-- maybeChosenApp.signal.map {
          case Some(example) =>
            example.init() :: Nil
          case None =>
            List(
              h1("Choose example:"),
              ul(
                fontSize := "130%",
                lineHeight := "2em",
                examples.map(ex => li(a(href := "#", ex.caption, onClick.mapTo(Some(ex)) --> maybeChosenApp.writer)))
              ),
              "Get back to this menu by reloading the page."
            )
        }
      )

      render(container, appElement)

    }(unsafeWindowOwner)
  }

  sealed abstract class Example(val caption: String, val init: () => HtmlElement)

  case object TodoMVCExample extends Example("TodoMVC", TodoMvcApp.render)
  case object WebComponentsExample extends Example("Web Components", WebComponentsPage.apply)
  case object SvgContainerExample extends Example("SVG Container", SvgContainer.apply)
  case object DuckCounterExample extends Example("Duck Counter", DuckMaster.app)

  val examples: List[Example] = TodoMVCExample :: WebComponentsExample :: SvgContainerExample :: Nil
}
