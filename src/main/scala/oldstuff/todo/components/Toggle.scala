package oldstuff.todo.components

import com.raquo.laminar.api.L._


import scala.util.Random

class Toggle private(
  val node: Span,
  val checkbox: Input,
  val label: HtmlElement
)

object Toggle {

  /** @param $checkedInput  Stream of user's input, containing desired checked state */
  class BoundToggle private[Toggle](
    val node: Span,
    val $checkedInput: EventStream[Boolean]
  )

  def apply(): Toggle = {
    // Note: This method will only be evaluated once per component instance (unlike say React's render method)
    val inputId = "toggle" + Random.nextInt(99)

    val checkbox = input(
      id := inputId,
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
    $checked: Signal[Boolean],
    $caption: Signal[String]
  ): BoundToggle = {
    val toggle = Toggle()
    toggle.checkbox.amend(checked <-- $checked)
    toggle.label.amend(child.text <-- $caption)
    // We set preventDefault=true so that the checkbox only updates when a new value is received from $checked
    // Note that we need to use onClick rather than unChange because onChange fires AFTER the checkbox has been checked.
    // onClick event for checkboxes is more or less equivalent to onInput event for text inputs. #frontendLife
    val $checkedInput = toggle.checkbox.events(onClick, preventDefault = true).mapTo(toggle.checkbox.ref.checked) //.debugWithLabel("$checkedInput")
    new BoundToggle(toggle.node, $checkedInput)
  }
}
