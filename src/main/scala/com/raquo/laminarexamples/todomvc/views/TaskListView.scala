package com.raquo.laminarexamples.todomvc.views

import com.raquo.laminar.collection.CollectionCommand.{Append, Remove}
import com.raquo.laminar.collection.ReactiveCollection
import com.raquo.laminar.events.onClick
import com.raquo.laminar.implicits._
import com.raquo.laminar.nodes.ReactiveElement
import com.raquo.laminar.receivers.MaybeChildReceiver.MaybeChildNode
import com.raquo.laminar.tags._
import com.raquo.laminar.attrs.autoFocus
import com.raquo.laminar.events._
import com.raquo.laminar.{children, focus, maybeChild, props}
import com.raquo.laminarexamples.todomvc.components.TextInput
import com.raquo.laminarexamples.todomvc.models.TaskModel
import com.raquo.xstream.XStream
import org.scalajs.dom
import org.scalajs.dom.ext.KeyCode
import org.scalajs.dom.raw.{KeyboardEvent, MouseEvent}

class TaskListView {

  private val $inputValue = XStream.create[String]()

  private val $addTaskClick = XStream.create[MouseEvent]()

  private val $inputKeyUp = XStream.create[KeyboardEvent]()

  private val $addTaskEvent = XStream.merge(
    $inputKeyUp.filter(ev => ev.keyCode == KeyCode.Enter),
    $addTaskClick
  ).filter(_ => inputValue != "")

  private val textInput = TextInput(
    onKeyUp --> $inputKeyUp,

    // @TODO it would be good to have a reference to the SDB node here, without the need to define a def...
    // @TODO the event itself only has .target which is a dom.Node, not a specialized dom.Input
    // @TODO this _ type needs to go
    onInput --> ((_: KeyboardEvent) => inputValue, $inputValue),

    // @TODO value Prop is missing. Why? Name collision?
    // @TODO this doesn't actually have to be prop.value, can also be attr.value
    props.value <-- $addTaskEvent.mapTo("").delay(0), // @TODO delay is a hack to get around a race condition, we should do this differently I think

    autoFocus := true,
    focus <-- $addTaskEvent.mapTo(true)
  )

  // @TODO need a method that supports () => blah?
  // @TODO Ugh the API is complicated by the 1-to-1 mapping. This definitely needs documentation
  // @TODO Also, consider API warnings when doing insensible things (such as appending an element that is already there)
  // @TODO should we use sampleCombine here?
  private val $command = $addTaskEvent.map { _ =>
    Append(TaskView(TaskModel(inputValue)))
  }

  val taskViewCollection: ReactiveCollection[TaskView, ReactiveElement[dom.html.Div]] = new ReactiveCollection(
    $command,
    (taskView: TaskView) => taskView.$deleteClick.map(_ => Remove(taskView)),
    (taskView: TaskView) => taskView.node
  )

  val node: ReactiveElement[dom.html.Div] = div(
    h1("TaskList"),
    textInput,
    button("Add task", onClick --> $addTaskClick),
    br(),
    children <-- taskViewCollection.$nodeCommand
  )

  // @TODO
  private def inputValue: String = textInput.ref.value
}
