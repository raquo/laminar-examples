import ExampleRouter._
import ajax.AjaxTester
import com.raquo.laminar.api.L._
import com.raquo.waypoint._
import events.{ControlledChecked, ControlledValue}
import oldstuff.intro.DuckMaster
import oldstuff.pseudotests.SvgContainer
import org.scalajs.dom
import todomvc.TodoMvcApp
import webcomponents.WebComponents

object App {

  def main(args: Array[String]): Unit = {

    // This div, its id and contents are defined in index-fastopt.html and index-fullopt.html files
    lazy val container = dom.document.getElementById("app-container")

    lazy val appElement = {
      div(
        child.maybe <-- ExampleRouter.router.$currentPage.map {
          case HomePage => None
          case _ => Some(h3(a(navigateTo(HomePage), "Back to home")))
        },
        child <-- $selectedApp.$view
      )
    }

    // Wait until the DOM is loaded, otherwise app-container element might not exist
    renderOnDomContentLoaded(container, appElement)
  }

  private val $selectedApp = SplitRender(ExampleRouter.router.$currentPage)
    .collectStatic(HomePage)(renderHomePage())
    .collectStatic(TodoMvcPage)(TodoMvcApp())
    .collectStatic(AjaxTesterPage)(AjaxTester())
    .collectStatic(WebComponentsPage)(WebComponents())
    .collectStatic(SvgContainerPage)(SvgContainer())
    .collectStatic(DuckCounterPage)(DuckMaster())
    .collectStatic(ControlledValueTesterPage)(ControlledValue())
    .collectStatic(ControlledCheckedTesterPage)(ControlledChecked())

  private def renderHomePage(): HtmlElement = {
    div(
      h1("Laminar Examples"),
      ul(
        fontSize := "120%",
        lineHeight := "2em",
        listStyleType.none,
        paddingLeft := "0px",
        linkPages.map { page =>
          li(a(navigateTo(page), page.title))
        }
      )
    )
  }

  val linkPages: List[Page] = List(
    DuckCounterPage,
    TodoMvcPage,
    AjaxTesterPage,
    WebComponentsPage,
    SvgContainerPage
  )
}
