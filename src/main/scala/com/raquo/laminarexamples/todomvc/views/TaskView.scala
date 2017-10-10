package com.raquo.laminarexamples.todomvc.views

import com.raquo.laminar.emitter.EventBus
import com.raquo.laminar.emitter.EventBus.WriteBus
import com.raquo.laminar.events._
import com.raquo.laminar.implicits._
import com.raquo.laminar.nodes.ReactiveElement
import com.raquo.laminar.tags._
import com.raquo.laminarexamples.todomvc.backend.RestBackend.Request
import com.raquo.laminarexamples.todomvc.components.Toggle
import com.raquo.laminarexamples.todomvc.models.TaskModel
import com.raquo.xstream.{MemoryStream, XStream}
import org.scalajs.dom
import org.scalajs.dom.raw.MouseEvent

import scala.util.Random

class TaskView(
  val taskId: Int,
  val node: ReactiveElement[dom.html.Div]
)

// @TODO alternative style would be to expose streams, and then add them as sources to the bus in the parent element!!!!!!

object TaskView {

  def apply(
    taskId: Int,
    $task: XStream[TaskModel],
    updateModelBus: WriteBus[TaskModel],
    deleteModelBus: WriteBus[TaskModel]
  ): TaskView = {

    val updateClickBus = new EventBus[MouseEvent]
    val deleteClickBus = new EventBus[MouseEvent]

    val $updateText = updateClickBus.$.map(_ => "NEW VALUE... " + Random.nextString(2))
    val $updateModel = $updateText
      .sampleCombine($task)
      .map2((newText, taskModel) => taskModel.copy(text = newText))

    val $deleteModel = deleteClickBus.$
      .sampleCombine($task)
      .map2((_, taskModel) => taskModel)

    updateModelBus.addSource($updateModel)
    deleteModelBus.addSource($deleteModel)

    val toggle = Toggle(
      $checked = $task.map(_.isCompleted),
      $caption = $task.map(_.text)
    )

    val node = div(
      toggle.node,
      button("+", onClick --> updateClickBus),
      button("x", onClick --> deleteClickBus)
    )

    new TaskView(taskId, node)
  }
}
