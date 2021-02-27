package oldstuff.todo.views

import com.raquo.laminar.api.L._
import oldstuff.todo.components.{TextInput, Toggle}
import oldstuff.todo.models.TaskModel
import org.scalajs.dom
import org.scalajs.dom.ext.KeyCode

class TaskView(
  val taskId: Int,
  val node: Div
)

object TaskView {

  def apply(
    taskId: Int,
    initialTask: TaskModel,
    $task: Signal[TaskModel],
    updateObserver: Observer[TaskModel],
    deleteObserver: Observer[TaskModel]
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

    val $textNode = $isEditing.combineWith($task).mapN { (isEditing, task) =>
      if (isEditing) {
        TextInput(
          onMountFocus,
          value := task.text,
          inContext(thisNode => onKeyUp.collect { // this is clunky
            case ev if ev.keyCode == KeyCode.Enter => thisNode.ref.value
          } --> textInputBus),
        )
      } else {
        span(
          child.text <-- $task.map(_.text),
          onClick --> labelClickBus
        )
      }
    }

    val deleteButton = button("x")

    // Bind

    val $isCompletedInput = toggle.$checkedInput //.debugWithLabel("$isCompleteInput")

    val updatedTaskVar = Var(initial = initialTask)

    val $updatedWithIsCompleted = $isCompletedInput
      .map(newIsCompleted => updatedTaskVar.now().copy(isCompleted = newIsCompleted))

    val $updatedWithText = textInputBus.events
      .map(newText => updatedTaskVar.now().copy(text = newText))

    val $deleteTask = deleteButton
      .events(onClick)
      .sample($task)

    val node = div(
      toggle.node,
      child <-- $textNode,
      deleteButton,

      EventStream.merge(
        $updatedWithIsCompleted,
        $updatedWithText,
        $task.changes
      ) --> updatedTaskVar.writer,

      updatedTaskVar.signal.changes --> updateObserver,

      $deleteTask --> deleteObserver
    )

    new TaskView(taskId, node)
  }
}
