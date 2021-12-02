package oldstuff.todo

import com.raquo.laminar.api.L.{*, given}
import oldstuff.todo.backend.RestBackend.Request
import oldstuff.todo.backend.TaskBackend
import oldstuff.todo.models.TaskModel
import oldstuff.todo.views.TaskListView

object TodoApp {

  def apply(): HtmlElement = {
    val requestBus = new EventBus[Request[TaskModel]]
    val backend = new TaskBackend(requestBus)

    TaskListView(backend).node
  }
}
