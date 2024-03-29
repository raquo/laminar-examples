package oldstuff.todo.components

import com.raquo.laminar.api.L.{*, given}


import scala.util.Random

class Toggle private(
  val node: Span,
  val checkbox: Input,
  val label: HtmlElement
)

object Toggle {

  /** @param checkedInputStream  Stream of user's input, containing desired checked state */
  class BoundToggle private[Toggle](
    val node: Span,
    val checkedInputStream: EventStream[Boolean]
  )

  def apply(): Toggle = {
    // Note: This method will only be evaluated once per component instance (unlike say React's render method)
    val inputId = "toggle" + Random.nextInt(99)

    val checkbox = input(
      idAttr := inputId,
      className := "red",
      typ := "checkbox"
    )
    val labelNode = label(forId := inputId)

    val node = span(
      className := "Toggle",
      checkbox,
      labelNode
    )

    new Toggle(node, checkbox, labelNode)
  }

  def apply(
    checkedSignal: Signal[Boolean],
    captionSignal: Signal[String]
  ): BoundToggle = {
    val toggle = Toggle()
    toggle.checkbox.amend(checked <-- checkedSignal)
    toggle.label.amend(child.text <-- captionSignal)
    // We set preventDefault=true so that the checkbox only updates when a new value is received from $checked
    // Note that we need to use onClick rather than unChange because onChange fires AFTER the checkbox has been checked.
    // onClick event for checkboxes is more or less equivalent to onInput event for text inputs. #frontendLife
    val checkedInputStream = toggle.checkbox.events(onClick.preventDefault).mapTo(toggle.checkbox.ref.checked)
    new BoundToggle(toggle.node, checkedInputStream)
  }
}
