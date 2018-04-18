package com.raquo.laminarexamples

import com.raquo.airstream.eventbus.EventBus
import com.raquo.laminar.api.L._
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

      val container = document.getElementById("app-container")
      container.textContent = ""

      val requestBus = new EventBus[Request[TaskModel]]
      val backend = new TaskBackend(requestBus)

      render(container, TaskListView(backend).node)
    })
  }}
