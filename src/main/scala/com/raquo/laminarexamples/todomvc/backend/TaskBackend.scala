package com.raquo.laminarexamples.todomvc.backend

import com.raquo.laminar.emitter.EventBus
import com.raquo.laminarexamples.todomvc.backend.RestBackend.Request
import com.raquo.laminarexamples.todomvc.models.TaskModel
import com.raquo.xstream.XStream

class TaskBackend(val requestBus: EventBus[Request[TaskModel]]) extends RestBackend[TaskModel] {

  override val $request: XStream[Request[TaskModel]] = requestBus.$

  override val $response: XStream[RestBackend.Response[TaskModel]] = {
    ???
  }
}
