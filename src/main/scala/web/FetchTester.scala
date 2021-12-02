package web

import com.raquo.laminar.api.L.{*, given}
import com.raquo.laminar.api.features.unitArrows

object FetchTester {

  // Example based on plain JS version: http://plnkr.co/edit/ycQbBr0vr7ceUP2p6PHy?preview

  case class FetchOption(name: String, url: String)

  private val options = List(
    FetchOption("Valid Fetch request", "http://api.zippopotam.us/us/90210"),
    FetchOption("Download 10MB file (gives you time to abort)", "https://cachefly.cachefly.net/10mb.test"),
    FetchOption("Download 100MB file (gives you time to abort)", "https://cachefly.cachefly.net/100mb.test"),
    FetchOption("URL that will fail due to invalid domain", "http://api.zippopotam.uxx/us/90210"),
    FetchOption("URL that will fail due to CORS restriction", "http://unsplash.com/photos/KDYcgCEoFcY/download?force=true")
  )

  def apply(): HtmlElement = {
    val selectedOptionVar = Var(options.head)
    val eventsVar = Var(List.empty[String])
    val (abortStream, abort) = EventStream.withUnitCallback

    div(
      h1("Fetch API Tester"),
      options.map { option =>
        div(
          input(
            idAttr(option.name),
            typ("radio"),
            nameAttr("fetchOption"),
            checked <-- selectedOptionVar.signal.map(_ == option),
            onChange.mapTo(option) --> selectedOptionVar,
          ),
          label(forId(option.name), " " + option.name)
        )
      },
      br(),
      div(
        button(
          "Send",
          inContext { thisNode =>
            val $click = thisNode.events(onClick).sample(selectedOptionVar.signal)
            val $response = $click.flatMap { opt =>
              Fetch.get(url = opt.url, _.abortStream(abortStream))
                .map(resp => if (resp.length >= 1000) resp.substring(0, 1000) else resp)
                .map("Response (first 1000 chars): " + _)
                .recover { case err: Throwable => Some(err.getMessage) }
            }

            List(
              $click.map(opt => List(s"Starting: GET ${opt.url}")) --> eventsVar,
              $response --> eventsVar.updater[String](_ :+ _)
            )
          }
        ),
        " ",
        button(
          "Abort",
          onClick --> abort() // #Note: using advanced Laminar syntax feature – see `unitArrows` import.
          // onClick.mapTo(()) --> abort
        )
      ),
      div(
        h2("Events:"),
        div(children <-- eventsVar.signal.map(_.map(div(_))))
      )
    )
  }
}


