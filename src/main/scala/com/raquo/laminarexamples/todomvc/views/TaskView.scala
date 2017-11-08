package com.raquo.laminarexamples.todomvc.views

import com.raquo.laminar.bundle._
import com.raquo.laminar.emitter.EventBus.WriteBus
import com.raquo.laminar.nodes.ReactiveElement
import com.raquo.laminarexamples.todomvc.components.Toggle
import com.raquo.laminarexamples.todomvc.models.TaskModel
import com.raquo.xstream.{MemoryStream, XStream}
import org.scalajs.dom

import scala.util.Random

class TaskView(
  val taskId: Int,
  val node: ReactiveElement[dom.html.Div]
)

object TaskView {

  def apply(
    taskId: Int,
    $task: MemoryStream[TaskModel],
    updateBus: WriteBus[TaskModel],
    deleteBus: WriteBus[TaskModel]
  ): TaskView = {

    // Render

    val toggle = Toggle(
      $checked = $task.map(_.isCompleted).debugWithLabel("$checked"),
      $caption = $task.map(_.text)
    )

    val plusButton = button("+")

    val deleteButton = button("x")

    val node = div(
      toggle.node,
      plusButton,
      deleteButton
    )

    // Bind

    val $isCompletedInput = toggle.$checkedInput.debugWithLabel("$isCompleteInput")
    val $textInput = plusButton.$event(onClick).map(_ => "NEW VALUE... " + Random.nextString(2))

    val $updateTask = XStream
      .combine($isCompletedInput.startWithNone(), $textInput.startWithNone())
      .filter2((maybeNewIsComplete, maybeNewText) => maybeNewIsComplete.isDefined || maybeNewText.isDefined)
      .sampleCombine($task)
      .map3(updatedTask)

    val $deleteTask = deleteButton.$event(onClick)
      .sampleCombine($task)
      .map2((_, task) => task)

    // Output

    updateBus.addSource($updateTask)
    deleteBus.addSource($deleteTask)

    new TaskView(taskId, node)
  }

  def updatedTask(maybeNewIsComplete: Option[Boolean], maybeNewText: Option[String], task: TaskModel) = {
    task.copy(
      isCompleted = maybeNewIsComplete.getOrElse(task.isCompleted),
      text = maybeNewText.getOrElse(task.text)
    )
  }
}
