package com.raquo.laminarexamples.todomvc.views

import com.raquo.laminar.bundle._
import com.raquo.laminar.emitter.EventBus.WriteBus
import com.raquo.laminar.nodes.ReactiveElement
import com.raquo.laminarexamples.todomvc.backend.RestBackend.{CreateRequest, CreateResponse, DeleteRequest, DeleteResponse, UpdateRequest}
import com.raquo.laminarexamples.todomvc.backend.TaskBackend
import com.raquo.laminarexamples.todomvc.components.TextInput
import com.raquo.laminarexamples.todomvc.models.TaskModel
import com.raquo.xstream.XStream
import org.scalajs.dom
import org.scalajs.dom.ext.KeyCode

class TaskListView private(
  val node: ReactiveElement[dom.html.Div]
)

object TaskListView {

  def apply(taskBackend: TaskBackend): TaskListView = {

    // Create DOM Nodes

    val newTaskInput = TextInput(autoFocus := true)

    val addTaskButton = button("Add task")

    val node: ReactiveElement[dom.html.Div] = div(
      h1("TaskList"),
      newTaskInput,
      addTaskButton,
      br()
    )

    // Setup Data flow

    val $enterPress = newTaskInput.$event(onKeyUp).filter(_.keyCode == KeyCode.Enter)
    val $addTaskClick = addTaskButton.$event(onClick)
    val $addTaskRequest = XStream.merge($enterPress, $addTaskClick)
      .map(_ => newTaskInput.ref.value)
      .filter(taskName => taskName != "")
      .map(taskName => CreateRequest(TaskModel(text = taskName)))

    val updateBus = taskBackend.requestBus.map[TaskModel](UpdateRequest(_))
    val deleteBus = taskBackend.requestBus.map[TaskModel](DeleteRequest(_))
    taskBackend.requestBus.addSource($addTaskRequest) // @TODO this needs to be removed when destroying/unmounting this component? or maybe implemented addSource differently?

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
  ): XStream[(Vector[TaskView], Vector[TaskView])] = {

    // @TODO Note: this can also be implemented with ChildrenCommandReceiver, even a bit easier.
    // @TODO Note: we could also simplify implementation if we simply stored prevTaskViews in a variable
    XStream
      .merge(
        taskBackend.$createResponse,
        taskBackend.$deleteResponse
      )
      .fold(
        accumulate = (taskViewsDiff: (Vector[TaskView], Vector[TaskView]), response) => {
          val prevTaskViews = taskViewsDiff._2
          val nextTaskViews = response match {
            case CreateResponse(_, newTask) =>
              val newTaskView = TaskView(
                taskId = newTask.id,
                $task = taskBackend.$updateResponse.filter(_.model.id == newTask.id).map(_.model).startWith(newTask),
                updateBus = updateBus,
                deleteBus = deleteBus
              )
              prevTaskViews :+ newTaskView
            case DeleteResponse(_, deletedTask) =>
              prevTaskViews.filterNot(_.taskId == deletedTask.id)
          }
          (prevTaskViews, nextTaskViews)
        },
        seed = (Vector(), Vector())
      )
  }
}
