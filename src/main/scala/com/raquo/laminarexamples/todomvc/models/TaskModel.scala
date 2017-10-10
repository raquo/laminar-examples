package com.raquo.laminarexamples.todomvc.models

import scala.util.Random

case class TaskModel(
  id: Int = Random.nextInt(),
  text: String,
  isCompleted: Boolean = false
)
