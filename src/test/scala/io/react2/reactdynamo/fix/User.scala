package io.react2.reactdynamo.fix

import org.specs2.mutable.Specification

trait UserFix {
  this: Specification =>
    
  case class User(name: String, age: Int)

  object User {

    private val fixtures = Map(
      "sample1" -> User("Diego", 25))

    def gimme(alias: String): User = fixtures(alias)

    def toList = fixtures.valuesIterator.toList

  }
}

