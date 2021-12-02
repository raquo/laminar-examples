import ExampleRouter.{*, given}
import benchmarks.ChildrenBenchmark
import com.raquo.laminar.api.L.{*, given}
import com.raquo.laminar.api.L
import com.raquo.waypoint.*
import events.{ControlledChecked, ControlledValue}
import oldstuff.intro.DuckMaster
import oldstuff.pseudotests.SvgContainer
import org.scalajs.dom
import todomvc.TodoMvcApp
import web.{AjaxTester, FetchTester}
import webcomponents.WebComponents

object App {

  def main(args: Array[String]): Unit = {

    // This div, its id and contents are defined in index-fastopt.html and index-fullopt.html files
    lazy val container = dom.document.getElementById("app-container")

    lazy val appElement = {
      div(
        child.maybe <-- ExampleRouter.router.currentPageSignal.map {
          case HomePage => None
          case _ => Some(h3(a(navigateTo(HomePage), "Back to home")))
        },
        child <-- $selectedApp.signal
      )
    }

    // Wait until the DOM is loaded, otherwise app-container element might not exist
    renderOnDomContentLoaded(container, appElement)
  }

  private val $selectedApp = SplitRender(ExampleRouter.router.currentPageSignal)
    .collectStatic(HomePage)(renderHomePage())
    .collectStatic(TodoMvcPage)(TodoMvcApp())
    .collectStatic(AjaxTesterPage)(AjaxTester())
    .collectStatic(FetchTesterPage)(FetchTester())
    .collectStatic(WebComponentsPage)(WebComponents())
    .collectStatic(SvgContainerPage)(SvgContainer())
    .collectStatic(DuckCounterPage)(DuckMaster())
    .collectStatic(ControlledValueTesterPage)(ControlledValue())
    .collectStatic(ControlledCheckedTesterPage)(ControlledChecked())
    .collectStatic(ChildrenBenchmarkPage)(ChildrenBenchmark())

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
    FetchTesterPage,
    WebComponentsPage,
    SvgContainerPage
  )
}
