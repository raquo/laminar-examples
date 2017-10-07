package com.raquo.laminarexamples.todomvc.components

import com.raquo.dombuilder.generic.modifiers.ModifierSeq
import com.raquo.domtypes.generic.Modifier
import com.raquo.laminar.attrs.typ
import com.raquo.laminar.implicits._
import com.raquo.laminar.nodes.ReactiveElement
import com.raquo.laminar.tags.input
import org.scalajs.dom

object TextInput {

  def apply(modifiers: Modifier[ReactiveElement[dom.html.Input]]*): ReactiveElement[dom.html.Input] = {
    input(
      typ := "text",
      new ModifierSeq(modifiers) // @TODO we shouldn't need to import dombuilder to use this, and we should have a value class for this
    )
  }
}
