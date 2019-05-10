package oldstuff.todo.models

import scala.util.Random

case class TaskModel(
  id: Int = Random.nextInt(),
  text: String,
  isCompleted: Boolean = false
)
