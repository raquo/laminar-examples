package ajax

import com.raquo.airstream.web.AjaxEventStream
import com.raquo.airstream.web.AjaxEventStream.AjaxStreamError
import com.raquo.laminar.api.L._
import org.scalajs.dom

object AjaxTester {

  // Example based on plain JS version: http://plnkr.co/edit/ycQbBr0vr7ceUP2p6PHy?preview

  case class AjaxOption(name: String, url: String)

  private val options = List(
    AjaxOption("Valid Ajax request", "http://api.zippopotam.us/us/90210"),
    AjaxOption("Download 100MB file (gives you time to abort)", "http://cachefly.cachefly.net/100mb.test"),
    AjaxOption("URL that will fail due to invalid domain", "http://api.zippopotam.uxx/us/90210"),
    AjaxOption("URL that will fail due to CORS restriction", "http://unsplash.com/photos/KDYcgCEoFcY/download?force=true")
  )

  def apply(): HtmlElement = {
    val selectedOptionVar = Var(options.head)
    val pendingRequestVar = Var[Option[dom.XMLHttpRequest]](None)
    val eventsVar = Var(List.empty[String])

    div(
      h1("Ajax Tester"),
      options.map { option =>
        div(
          input(
            idAttr(option.name),
            typ("radio"),
            name("ajaxOption"),
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
              AjaxEventStream
                .get(
                  url = opt.url,
                  // These observers are optional, we're just using them for demo
                  requestObserver = pendingRequestVar.someWriter,
                  progressObserver = eventsVar.updater { (evs, p) =>
                    val ev = p._2
                    evs :+ s"Progress: ${ev.loaded} / ${ev.total} (lengthComputable = ${ev.lengthComputable})"
                  },
                  readyStateChangeObserver = eventsVar.updater { (evs, req) =>
                    evs :+ s"Ready state: ${req.readyState}"
                  }
                )
                .map("Response: " + _.responseText)
                .recover { case err: AjaxStreamError => Some(err.getMessage) }
            }

            List(
              $click.map(opt => List(s"Starting: GET ${opt.url}")) --> eventsVar.writer, // @TODO remove .writer
              $response --> eventsVar.updater[String](_ :+ _)
            )
          }
        ),
        " ",
        button(
          "Abort",
          onClick --> (_ => pendingRequestVar.now().foreach(_.abort()))
        )
      ),
      div(
        h2("Events:"),
        div(children <-- eventsVar.signal.map(_.map(div(_))))
      )
    )
  }
}


