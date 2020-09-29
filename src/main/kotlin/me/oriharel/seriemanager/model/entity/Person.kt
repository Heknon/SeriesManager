package me.oriharel.seriemanager.model.entity

abstract class Person(override val id: Int,
                      open val creditId: String,
                      override val name: String,
                      open val gender: Int,
                      open val profile: String) : Entity