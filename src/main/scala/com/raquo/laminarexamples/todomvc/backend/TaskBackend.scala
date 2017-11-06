package com.raquo.laminarexamples.todomvc.backend

import com.raquo.laminar.emitter.EventBus
import com.raquo.laminarexamples.todomvc.backend.RestBackend.{CreateRequest, CreateResponse, DeleteRequest, DeleteResponse, ListRequest, ListResponse, ReadRequest, ReadResponse, Request, Response, UpdateRequest, UpdateResponse}
import com.raquo.laminarexamples.todomvc.models.TaskModel
import com.raquo.xstream.{Listener, XStream}
import org.scalajs.dom

class TaskBackend(val requestBus: EventBus[Request[TaskModel]]) extends RestBackend[TaskModel] {

  private var items: Map[Int, TaskModel] = Map()

  override val $request: XStream[Request[TaskModel]] = requestBus.$

  override val $response: XStream[RestBackend.Response[TaskModel]] = $request.map(processRequest)

  // THe logging listener is just to ensure that requests will be processed even if nothing else
  // is listening, because we expect some of those requests to have desired side effects on the backend.
  $response.addListener(Listener(onNext = logRequest))

  def processRequest(request: Request[TaskModel]): Response[TaskModel] = {
    request match {
      case ReadRequest(modelId) =>
        ReadResponse(request, items(modelId))
      case ListRequest() =>
        ListResponse(request, items.values)
      case CreateRequest(model) =>
        items = items + ((model.id, model))
        CreateResponse(request, model)
      case UpdateRequest(model) =>
        items = items.updated(model.id, model)
        UpdateResponse(request, model)
      case DeleteRequest(model) =>
        // @TODO this should just accept modelId
        items = items - model.id
        DeleteResponse(request, model)
    }
  }

  def logRequest(request: Response[TaskModel]): Unit = {
    request match {
      case ReadResponse(_, modelId) =>
        dom.console.log(s"READ $modelId")
      case ListResponse(_, _) =>
        dom.console.log("LIST")
      case CreateResponse(_, model) =>
        dom.console.log(s"CREATE (id=${model.id}, completed=${model.isCompleted}, text=${model.text})")
      case UpdateResponse(_, model) =>
        dom.console.log(s"UPDATE (id=${model.id}, completed=${model.isCompleted}, text=${model.text})")
      case DeleteResponse(_, model) =>
        dom.console.log(s"DELETE (id=${model.id}, completed=${model.isCompleted}, text=${model.text})")
    }
  }
}
