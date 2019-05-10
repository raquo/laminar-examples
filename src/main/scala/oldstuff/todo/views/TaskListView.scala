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

    val newTaskInput = TextInput(autoFocus := true)

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

    val updateBus = taskBackend.requestBus.writer
      .contramapWriter[TaskModel](UpdateRequest(_))(owner = node)
    val deleteBus = taskBackend.requestBus.writer
      .contramapWriter[TaskModel](DeleteRequest(_))(owner = node)

    node.subscribeBus($addTaskRequest, taskBackend.requestBus.writer)

    val tasksVar = Var[List[TaskModel]](Nil)

    taskBackend.$response.foreach {
        case CreateResponse(_, newTask) =>
          tasksVar.update(_ :+ newTask)
        case UpdateResponse(_, updatedTask) =>
          tasksVar.update{ tasks =>
            val ix = tasks.indexWhere(_.id == updatedTask.id)
            if (ix >= 0) tasks.updated(ix, updatedTask)
            else tasks
          }
        case DeleteResponse(_, deletedTask) =>
          tasksVar.update(_.filterNot(_.id == deletedTask.id))
        case _ => ()
    }(owner = node)

    newTaskInput <-- focus <-- $addTaskRequest.mapTo(true) // @TODO also do this on validation failure

    newTaskInput <-- value <-- $addTaskRequest.mapTo("")

    node <-- children <-- tasksVar.signal.split(_.id) {
      (taskId, _, $task) => TaskView(taskId, $task, updateBus, deleteBus).node
    }

    // All done!

    new TaskListView(node)
  }
}
