package com.raquo.laminarexamples.todomvc.views

import com.raquo.laminar.api.L._
import com.raquo.laminarexamples.todomvc.backend.RestBackend.{CreateRequest, CreateResponse, DeleteRequest, DeleteResponse, UpdateRequest}
import com.raquo.laminarexamples.todomvc.backend.TaskBackend
import com.raquo.laminarexamples.todomvc.components.TextInput
import com.raquo.laminarexamples.todomvc.models.TaskModel
import org.scalajs.dom.ext.KeyCode

class TaskListView private(
  val node: Div
)

object TaskListView {

  def apply(taskBackend: TaskBackend): TaskListView = {

    // Create DOM Nodes

    val newTaskInput = TextInput(autoFocus := true)

    val addTaskButton = button("Add task")

    val node: Div = div(
      h1("TaskList"),
      newTaskInput,
      addTaskButton,
      br()
    )

    // Setup Data flow

    val $enterPress = newTaskInput.events(onKeyUp).filter(_.keyCode == KeyCode.Enter)
    val $addTaskClick = addTaskButton.events(onClick)
    val $addTaskRequest = EventStream.merge($enterPress, $addTaskClick)
      .map(_ => newTaskInput.ref.value)
      .filter(taskName => taskName != "")
      .map(taskName => CreateRequest(TaskModel(text = taskName)))

    val updateBus = taskBackend.requestBus.writer.mapWriter[TaskModel](UpdateRequest(_))(owner = node)
    val deleteBus = taskBackend.requestBus.writer.mapWriter[TaskModel](DeleteRequest(_))(owner = node)

    node.subscribeBus($addTaskRequest, taskBackend.requestBus.writer)

    val $taskViewsDiff = taskViewsStream(taskBackend, updateBus, deleteBus)

    newTaskInput <-- focus <-- $addTaskRequest.mapTo(true) // @TODO also do this on validation failure

    newTaskInput <-- value <-- $addTaskRequest.mapTo("")

    node <-- children <-- $taskViewsDiff.map(_._2.map(_.node))

    // All done!

    new TaskListView(node)
  }

  private def taskViewsStream(
    taskBackend: TaskBackend,
    updateBus: WriteBus[TaskModel],
    deleteBus: WriteBus[TaskModel]
  ): EventStream[(Vector[TaskView], Vector[TaskView])] = {

    // @TODO Note: this can also be implemented with ChildrenCommandReceiver, even a bit easier.
    // @TODO Note: we could also simplify implementation if we simply stored prevTaskViews in a variable
    EventStream
      .merge(
        taskBackend.$createResponse,
        taskBackend.$deleteResponse
      )
      .fold(
        initial = (Vector[TaskView](), Vector[TaskView]())
      )(
        (taskViewsDiff, response) => {
          val prevTaskViews = taskViewsDiff._2
          val nextTaskViews = response match {
            case CreateResponse(_, newTask) =>
              val newTaskView = TaskView(
                taskId = newTask.id,
                $task = taskBackend.$updateResponse.filter(_.model.id == newTask.id).map(_.model).toSignal(newTask),
                updateBus = updateBus,
                deleteBus = deleteBus
              )
              prevTaskViews :+ newTaskView
            case DeleteResponse(_, deletedTask) =>
              prevTaskViews.filterNot(_.taskId == deletedTask.id)
          }
          (prevTaskViews, nextTaskViews)
        }
      )
      .changes
  }
}
