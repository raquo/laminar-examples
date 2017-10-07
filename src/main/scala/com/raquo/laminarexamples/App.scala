package com.raquo.laminarexamples

import com.raquo.laminar.render
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

      render(container, new TaskListView().node)
    })
  }}
