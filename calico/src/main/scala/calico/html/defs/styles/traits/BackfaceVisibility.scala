package calico.html.defs.styles.traits

import calico.html.keys.StyleProp
import calico.html.modifiers.KeySetter.StyleSetter

// #NOTE: GENERATED CODE
//  - This file is generated at compile time from the data in Scala DOM Types
//  - See `project/DomDefsGenerator.scala` for code generation params
//  - Contribute to https://github.com/raquo/scala-dom-types to add missing tags / attrs / props / etc.

trait BackfaceVisibility { this: StyleProp[_] =>

  /** The back face is visible. */
  lazy val visible: StyleSetter = this := "visible"

  /** The back face is not visible. */
  lazy val hidden: StyleSetter = this := "hidden"

}
