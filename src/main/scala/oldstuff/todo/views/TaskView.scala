package oldstuff.todo.views

import com.raquo.laminar.api.L.{*, given}
import oldstuff.todo.components.{TextInput, Toggle}
import oldstuff.todo.models.TaskModel
import org.scalajs.dom
import org.scalajs.dom.KeyCode

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
      checkedSignal = $task.map(_.isCompleted), //.debugWithLabel("$checked"),
      captionSignal = Val("") // $task.map(_.text)
    )

    val labelClickBus = new EventBus[dom.MouseEvent]
    val textInputBus = new EventBus[String]

    val isEditingSignal = EventStream.merge(
      labelClickBus.events.mapTo(true),
      $task.changes.mapTo(false) // possible alternative wording: Val(false).sampledBy($task.changes)
    ).toSignal(initial = false)

    val textNodeSignal = isEditingSignal.combineWith($task).mapN { (isEditing, task) =>
      if (isEditing) {
        TextInput(
          onMountFocus,
          value := task.text,
          onKeyUp.filter(_.keyCode == KeyCode.Enter).mapToValue --> textInputBus
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

    val isCompletedInputStream = toggle.checkedInputStream //.debugWithLabel("$isCompleteInput")

    val updatedTaskVar = Var(initial = initialTask)

    val updatedWithIsCompletedStream = isCompletedInputStream
      .map(newIsCompleted => updatedTaskVar.now().copy(isCompleted = newIsCompleted))

    val updatedWithTextStream = textInputBus.events
      .map(newText => updatedTaskVar.now().copy(text = newText))

    val deleteTaskStream = deleteButton
      .events(onClick)
      .sample($task)

    val node = div(
      toggle.node,
      child <-- textNodeSignal,
      deleteButton,

      EventStream.merge(
        updatedWithIsCompletedStream,
        updatedWithTextStream,
        $task.changes
      ) --> updatedTaskVar.writer,

      updatedTaskVar.signal.changes --> updateObserver,

      deleteTaskStream --> deleteObserver
    )

    new TaskView(taskId, node)
  }
}
