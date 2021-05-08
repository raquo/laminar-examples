package oldstuff.todo.views

import com.raquo.laminar.api.L._
import oldstuff.todo.backend.RestBackend._
import oldstuff.todo.backend.TaskBackend
import oldstuff.todo.components.TextInput
import oldstuff.todo.models.TaskModel
import org.scalajs.dom.ext.KeyCode

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

    val $enterPress =
      newTaskInput.events(onKeyUp).filter(_.keyCode == KeyCode.Enter)
    val $addTaskClick = addTaskButton.events(onClick)
    val $addTaskRequest = EventStream
      .merge($enterPress, $addTaskClick)
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

      $addTaskRequest --> taskBackend.requestBus.writer,

      taskBackend.$response --> Observer[Response[TaskModel]]({
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

      focus <-- $addTaskRequest.mapTo(true), // @TODO also do this on validation failure

      value <-- $addTaskRequest.mapTo(""),

      children <-- tasksVar.signal.split(_.id) {
        (taskId, initialTask, $task) => TaskView(taskId, initialTask, $task, updateObserver, deleteObserver).node
      }
    )

    // All done!

    new TaskListView(node)
  }
}
