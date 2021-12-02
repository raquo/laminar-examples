import com.raquo.laminar.api.L.{*, given}
import com.raquo.waypoint.*
import org.scalajs.dom
import com.github.plokhotnyuk.jsoniter_scala.core.*
import com.github.plokhotnyuk.jsoniter_scala.macros.*

object ExampleRouter {

  sealed abstract class Page(val title: String)

  case object HomePage extends Page("Home")
  case object TodoMvcPage extends Page("Todo MVC")
  case object AjaxTesterPage extends Page("Ajax Tester")
  case object FetchTesterPage extends Page("Fetch Tester")
  case object WebComponentsPage extends Page("Web Components")
  case object SvgContainerPage extends Page("SVG Container")
  case object DuckCounterPage extends Page("Duck Counter")
  case object ControlledValueTesterPage extends Page("Controlled Value Tester")
  case object ControlledCheckedTesterPage extends Page("Controlled Checked Tester")
  case object ChildrenBenchmarkPage extends Page("Children Benchmark")

  // In Scala 3, you can also say `given codec: ...`
  implicit val codec: JsonValueCodec[Page] = JsonCodecMaker.make

  private val routes = List(
    Route.static(HomePage, root / endOfSegments, Router.localFragmentBasePath),
    Route.static(TodoMvcPage, root / "todo-mvc" / endOfSegments, Router.localFragmentBasePath),
    Route.static(AjaxTesterPage, root / "ajax" / endOfSegments, Router.localFragmentBasePath),
    Route.static(FetchTesterPage, root / "fetch" / endOfSegments, Router.localFragmentBasePath),
    Route.static(WebComponentsPage, root / "web-components" / endOfSegments, Router.localFragmentBasePath),
    Route.static(SvgContainerPage, root / "svg" / endOfSegments, Router.localFragmentBasePath),
    Route.static(DuckCounterPage, root / "duck-counter" / endOfSegments, Router.localFragmentBasePath),
    Route.static(ControlledValueTesterPage, root / "controlled-value" / endOfSegments, Router.localFragmentBasePath),
    Route.static(ControlledCheckedTesterPage, root / "controlled-checked" / endOfSegments, Router.localFragmentBasePath),
    Route.static(ChildrenBenchmarkPage, root / "children-benchmark" / endOfSegments, Router.localFragmentBasePath)
  )

  val router = new Router[Page](
    routes = routes,
    getPageTitle = _.title, // displayed in the browser tab next to favicon
    serializePage = page => writeToString(page), // serialize page data for storage in History API log
    deserializePage = pageStr => readFromString(pageStr) // deserialize the above
  )(
    popStateEvents = windowEvents(_.onPopState), // this is how Waypoint avoids an explicit dependency on Laminar
    owner = unsafeWindowOwner // this router will live as long as the window
  )

  // Note: for fragment ('#') URLs this isn't actually needed.
  // See https://github.com/raquo/Waypoint docs for why this modifier is useful in general.
  def navigateTo(page: Page): Binder[HtmlElement] = Binder { el =>

    val isLinkElement = el.ref.isInstanceOf[dom.html.Anchor]

    if (isLinkElement) {
      el.amend(href(router.absoluteUrlForPage(page)))
    }

    // If element is a link and user is holding a modifier while clicking:
    //  - Do nothing, browser will open the URL in new tab / window / etc. depending on the modifier key
    // Otherwise:
    //  - Perform regular pushState transition
    (onClick
      .filter(ev => !(isLinkElement && (ev.ctrlKey || ev.metaKey || ev.shiftKey || ev.altKey)))
      .preventDefault
      --> (_ => router.pushState(page))
      ).bind(el)
  }
}
