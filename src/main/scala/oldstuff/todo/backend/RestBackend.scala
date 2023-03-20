package oldstuff.todo.backend

import com.raquo.laminar.api.L.{*, given}

trait RestBackend[Model] {

  import RestBackend.*

  val requests: EventStream[Request[Model]]

  val responses: EventStream[Response[Model]]

  lazy val listResponses: EventStream[ListResponse[Model]] = responses.collect {
    case response: ListResponse[Model @unchecked] => response
  }

  lazy val readResponses: EventStream[ReadResponse[Model]] = responses.collect {
    case response: ReadResponse[Model @unchecked] => response
  }

  lazy val createResponses: EventStream[CreateResponse[Model]] = responses.collect {
    case response: CreateResponse[Model @unchecked] => response
  }

  lazy val updateResponses: EventStream[UpdateResponse[Model]] = responses.collect {
    case response: UpdateResponse[Model @unchecked] => response
  }

  lazy val deleteResponses: EventStream[DeleteResponse[Model]] = responses.collect {
    case response: DeleteResponse[Model @unchecked] => response
  }
}

object RestBackend {

  sealed trait Request[Model]

  case class ListRequest[Model]() extends Request[Model]
  case class ReadRequest[Model](id: Int) extends Request[Model]
  case class CreateRequest[Model](model: Model) extends Request[Model]
  case class UpdateRequest[Model](model: Model) extends Request[Model]
  case class DeleteRequest[Model](model: Model) extends Request[Model]

  // @TODO add failure case

  sealed trait Response[Model] {
    val request: Request[Model]
  }

  case class ListResponse[Model](request: Request[Model], tasks: Iterable[Model]) extends Response[Model]

  sealed trait ItemResponse[Model] extends Response[Model] {
    val model: Model
  }

  case class ReadResponse[Model](request: Request[Model], model: Model) extends ItemResponse[Model]
  case class CreateResponse[Model](request: Request[Model], model: Model) extends ItemResponse[Model]
  case class UpdateResponse[Model](request: Request[Model], model: Model) extends ItemResponse[Model]
  case class DeleteResponse[Model](request: Request[Model], model: Model) extends ItemResponse[Model]
}
