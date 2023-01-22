/*
 * Copyright 2022 Arman Bilge
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package calico
package html

import cats.effect.kernel.Async
import cats.effect.kernel.Resource
import cats.effect.syntax.all.*
import cats.syntax.all.*
import org.scalajs.dom
import shapeless3.deriving.K0

final class HtmlTag[F[_], E] private[calico] (name: String, void: Boolean)(using F: Async[F]):

  def apply[M](modifier: M)(using M: Modifier[F, E, M]): Resource[F, E] =
    build.toResource.flatTap(M.modify(modifier, _))

  def withSelf[M](mkModifier: E => M)(using M: Modifier[F, E, M]): Resource[F, E] =
    build.toResource.flatTap(e => M.modify(mkModifier(e), e))

  def apply[M <: Tuple](modifiers: M)(
      using inst: K0.ProductInstances[Modifier[F, E, _], M]): Resource[F, E] =
    inst.foldLeft(modifiers)(build.toResource) {
      [a] => (r: Resource[F, E], m: Modifier[F, E, a], a: a) => r.flatTap(m.modify(a, _))
    }

  def withSelf[M <: Tuple](mkModifiers: E => M)(
      using inst: K0.ProductInstances[Modifier[F, E, _], M]): Resource[F, E] =
    build.toResource.flatTap { e =>
      inst.foldLeft(mkModifiers(e))(Resource.pure(e)) {
        [a] => (r: Resource[F, E], m: Modifier[F, E, a], a: a) => r.flatTap(m.modify(a, _))
      }
    }

  private def build = F.delay(dom.document.createElement(name).asInstanceOf[E])
