package com.raquo.laminarexamples.todomvc.views

import com.raquo.laminar.events.onClick
import com.raquo.laminar.implicits._
import com.raquo.laminar.nodes.ReactiveElement
import com.raquo.laminar.tags._
import com.raquo.laminar.attrs.autoFocus
import com.raquo.laminar.emitter.EventBus
import com.raquo.laminar.events._
import com.raquo.laminar.{children, focus, props}
import com.raquo.laminarexamples.todomvc.backend.RestBackend.{CreateRequest, CreateResponse, DeleteRequest, DeleteResponse, Request, UpdateRequest, UpdateResponse}
import com.raquo.laminarexamples.todomvc.backend.TaskBackend
import com.raquo.laminarexamples.todomvc.components.TextInput
import com.raquo.laminarexamples.todomvc.models.TaskModel
import com.raquo.xstream.XStream
import org.scalajs.dom
import org.scalajs.dom.ext.KeyCode
import org.scalajs.dom.raw.{KeyboardEvent, MouseEvent}

class TaskListView(taskBackend: TaskBackend) {

  private val inputKeyUpBus = new EventBus[KeyboardEvent]
  private val inputValueBus = new EventBus[String]
  private val addTaskClickBus = new EventBus[MouseEvent]

  private val updateModelBus = taskBackend.requestBus.map[TaskModel](UpdateRequest(_))
  private val deleteModelBus = taskBackend.requestBus.map[TaskModel](DeleteRequest(_))

  private val $addTaskRequest = XStream.merge(
    inputKeyUpBus.$.filter(ev => ev.keyCode == KeyCode.Enter),
    addTaskClickBus.$
  )
    .sample(inputValueBus.$)
    .filter(taskName => taskName != "")
    .map(taskName => CreateRequest(TaskModel(text = taskName)))

  taskBackend.requestBus.addSource($addTaskRequest) // @TODO this needs to be removed when destroying this component? or maybe implemented addSource differently?

  // @TODO Note: this can also be implemented with ChildrenCommandReceiver, even a bit easier.
  // @TODO Note: we could also simplify implementation if we simply stored prevTaskViews in a variable
  private val $taskViewsDiff: XStream[(Vector[TaskView], Vector[TaskView])] = XStream
    .merge(
      taskBackend.$createResponse,
      taskBackend.$deleteResponse
    )
    .fold(
      accumulate = (taskViewsDiff, response) => {
        val prevTaskViews = taskViewsDiff._2
        val nextTaskViews = response match {
          case CreateResponse(_, newTask) =>
            val newTaskView = TaskView(
              taskId = newTask.id,
              taskBackend.$updateResponse.filter(_.model.id == newTask.id).map(_.model).startWith(newTask),
              updateModelBus = updateModelBus,
              deleteModelBus = deleteModelBus
            )
            prevTaskViews :+ newTaskView
          case DeleteResponse(_, deletedTask) =>
            prevTaskViews.filterNot(_.taskId == deletedTask.id)
        }
        (prevTaskViews, nextTaskViews)
      },
      seed = (Vector(), Vector())
    )

  private val textInput: ReactiveElement[dom.html.Input] = TextInput(
    onKeyUp --> inputKeyUpBus,

    onInput --> (() => textInput.ref.value, inputValueBus), // @TODO .target is not specialized to dom.Input – LAME. Can we make a LaminarEvent[targetType] trait?
    props.value <-- $addTaskRequest.mapTo("").delay(0), // @TODO delay is a hack to get around a race condition, we should do this differently I think

    autoFocus := true,
    focus <-- $addTaskRequest.mapTo(true)
  )

  val node: ReactiveElement[dom.html.Div] = div(
    h1("TaskList"),
    textInput,
    button("Add task", onClick --> addTaskClickBus),
    br(),
    children <-- $taskViewsDiff.map(_._2.map(_.node))
  )
}
