package com.raquo.laminarexamples

import com.raquo.laminar.api.L._
import com.raquo.laminarexamples.intro.DuckMaster
import com.raquo.laminarexamples.todomvc.backend.RestBackend.Request
import com.raquo.laminarexamples.todomvc.backend.TaskBackend
import com.raquo.laminarexamples.todomvc.models.TaskModel
import com.raquo.laminarexamples.todomvc.views.TaskListView
import org.scalajs.dom
import org.scalajs.dom.document
import org.scalajs.dom.raw.Event

import scala.scalajs.js

object App extends js.JSApp {

  def main(): Unit = {
    document.addEventListener("DOMContentLoaded", (e: Event) => {
      dom.console.log("=== DOMContentLoaded ===")

      val container = document.getElementById("app-container") // This div, its id and contents are defined in index-fastopt.html/index-fullopt.html files
      container.textContent = ""

      render(container, todoApp())
//      render(container, DuckMaster.app())
    })
  }


  def todoApp(): HtmlElement = {
    val requestBus = new EventBus[Request[TaskModel]]
    val backend = new TaskBackend(requestBus)

    TaskListView(backend).node
  }
}
