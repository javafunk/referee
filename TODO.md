General
=======

* Restructure mechanism factories into validateFor and populateFor rather than being split between factory, engine and
  mechanism
  - It looks like all that should really be needed is a single mechanism and a convention
* Turn coercion engine into a mechanism/convention like anything else
* Naming is currently all over the place
* Consider having the convention return an attribute populator which knows how to perform population for that attribute
* Hints for particular types when one population mechanism is preferred. Maybe annotations?
* Work out how to remove conditional logic into some sort of polymorphism instead

Builder
=======

* Add problem reporting when wither expects different types to what can be constructed from the provided value
* Add recursion support

Bean
====

* Everything

Constructor
===========

* Is this possible? Annotations maybe?

Direct Field
============

* Add problem reporting
* Add iterable support
* Add recursion support