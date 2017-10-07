package com.raquo.laminarexamples.todomvc.views

import com.raquo.laminar.events._
import com.raquo.laminar.implicits._
import com.raquo.laminar.nodes.ReactiveElement
import com.raquo.laminar.tags._
import com.raquo.laminarexamples.todomvc.components.Toggle
import com.raquo.laminarexamples.todomvc.models.TaskModel
import com.raquo.xstream.XStream
import org.scalajs.dom
import org.scalajs.dom.raw.MouseEvent

class TaskView (
  val node: ReactiveElement[dom.html.Div],
  val $completed: XStream[Boolean],
  val $deleteClick: XStream[MouseEvent]
)

object TaskView {

  def apply(task: TaskModel): TaskView = {
    val $deleteClick = XStream.create[MouseEvent]()
    val toggle = Toggle(initialChecked = task.isCompleted, caption = task.text)

    val node = div(
      toggle.node,
      button("x", onClick --> $deleteClick)
    )

    new TaskView(node, $completed = toggle.$checked, $deleteClick)
  }
}
