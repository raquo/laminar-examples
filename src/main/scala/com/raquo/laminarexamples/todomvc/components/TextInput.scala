package com.raquo.laminarexamples.todomvc.components

import com.raquo.laminar.api.L._

object TextInput {

  def apply(modifiers: Mod[Input]*): Input = {
    input(
      typ := "text",
      modifiers
    )
  }
}
