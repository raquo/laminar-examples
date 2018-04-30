package com.raquo.laminarexamples.todomvc.views

import com.raquo.laminar.api.L._
import com.raquo.laminar.lifecycle.NodeDidMount
import com.raquo.laminarexamples.todomvc.components.{TextInput, Toggle}
import com.raquo.laminarexamples.todomvc.models.TaskModel
import org.scalajs.dom
import org.scalajs.dom.ext.KeyCode

class TaskView(
  val taskId: Int,
  val node: Div
)

object TaskView {

  def apply(
    taskId: Int,
    $task: Signal[TaskModel],
    updateBus: WriteBus[TaskModel],
    deleteBus: WriteBus[TaskModel]
  ): TaskView = {

    // Render

    val toggle = Toggle(
      $checked = $task.map(_.isCompleted), //.debugWithLabel("$checked"),
      $caption = Val("") // $task.map(_.text)
    )

    val labelClickBus = new EventBus[dom.MouseEvent]
    val textInputBus = new EventBus[String]

    val $isEditing = EventStream.merge(
      labelClickBus.events.mapTo(true),
      $task.changes.mapTo(false) // possible alternative wording: Val(false).sampledBy($task.changes)
    ).toSignal(initial = false)

    val $textNode = $isEditing.combineWith($task).map2 { (isEditing, task) =>
      if (isEditing) {
        val input = TextInput(
          value := task.text,
          inContext(thisNode => onKeyUp.collect { // this is clunky
            case ev if ev.keyCode == KeyCode.Enter => thisNode.ref.value
          } --> textInputBus)
        )
        input.mountEvents.filter(_ == NodeDidMount).foreach(_ => input.ref.focus())(input)
        input
      } else {
        span(
          child.text <-- $task.map(_.text),
          onClick --> labelClickBus
        )
      }
    }

    val deleteButton = button("x")

    val node = div(
      toggle.node,
      child <-- $textNode,
      deleteButton
    )

    // Bind

    val $isCompletedInput = toggle.$checkedInput //.debugWithLabel("$isCompleteInput")

    // @TODO this is uglier than it needs to be
    val updatedTaskVar = StateVar(initial = $task.toState(owner = node).now())(owner = node)
    val $updatedWithIsCompleted = $isCompletedInput
      .withCurrentValueOf(updatedTaskVar.state)
      .map2((newIsCompleted, updatedTask) => updatedTask.copy(isCompleted = newIsCompleted))
    val $updatedWithText = textInputBus.events
      .withCurrentValueOf(updatedTaskVar.state)
      .map2((newText, updatedTask) => updatedTask.copy(text = newText))

    updatedTaskVar.writer.addSource(
      EventStream.merge(
        $updatedWithIsCompleted,
        $updatedWithText,
        $task.changes
      )
    )(owner = node)

    val $deleteTask = deleteButton
      .events(onClick)
      .sample($task)

    node.subscribeBus(updatedTaskVar.state.changes, updateBus)
    node.subscribeBus($deleteTask, deleteBus)

    new TaskView(taskId, node)
  }
}
