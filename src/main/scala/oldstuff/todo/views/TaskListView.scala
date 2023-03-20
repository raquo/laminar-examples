package oldstuff.todo.views

import com.raquo.laminar.api.L.{*, given}
import oldstuff.todo.backend.RestBackend.*
import oldstuff.todo.backend.TaskBackend
import oldstuff.todo.components.TextInput
import oldstuff.todo.models.TaskModel
import org.scalajs.dom

class TaskListView private (
    val node: Div
)

object TaskListView {

  def apply(taskBackend: TaskBackend): TaskListView = {

    // Create DOM Nodes

    val newTaskInput = TextInput(onMountFocus)

    val addTaskButton = button("Add task")

    val node: Div = div(
      h1("TaskList"),
      newTaskInput,
      addTaskButton,
      br()
    )

    // Setup Data flow

    val enterPresses =
      newTaskInput.events(onKeyUp).filter(_.keyCode == dom.KeyCode.Enter)
    val addTaskClicks = addTaskButton.events(onClick)
    val addTaskRequests = EventStream
      .merge(enterPresses, addTaskClicks)
      .mapTo(newTaskInput.ref.value)
      .filter(taskName => taskName != "")
      .map(taskName => CreateRequest(TaskModel(text = taskName)))

    val tasksVar = Var[List[TaskModel]](Nil)

    // @Note we had to change from Bus-es to Observers to maintain this pattern.

    val updateObserver = taskBackend.requestBus.writer
      .contramap[TaskModel](UpdateRequest(_))

    val deleteObserver = taskBackend.requestBus.writer
      .contramap[TaskModel](DeleteRequest(_))

    node.amend(

      addTaskRequests --> taskBackend.requestBus.writer,

      taskBackend.responses --> Observer[Response[TaskModel]]({
        case CreateResponse(_, newTask) =>
          tasksVar.update(_ :+ newTask)
        case UpdateResponse(_, updatedTask) =>
          tasksVar.update { tasks =>
            val ix = tasks.indexWhere(_.id == updatedTask.id)
            if (ix >= 0) tasks.updated(ix, updatedTask)
            else tasks
          }
        case DeleteResponse(_, deletedTask) =>
          tasksVar.update(_.filterNot(_.id == deletedTask.id))
        case _ => ()
      }),

      focus <-- addTaskRequests.mapTo(true), // @TODO also do this on validation failure

      value <-- addTaskRequests.mapTo(""),

      children <-- tasksVar.signal.split(_.id) {
        (taskId, initialTask, $task) => TaskView(taskId, initialTask, $task, updateObserver, deleteObserver).node
      }
    )

    // All done!

    new TaskListView(node)
  }
}
