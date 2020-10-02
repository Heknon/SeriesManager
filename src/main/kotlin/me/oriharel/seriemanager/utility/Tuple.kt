package me.oriharel.seriemanager.utility

import com.fasterxml.jackson.annotation.JsonCreator

data class Tuple<T1, T2> @JsonCreator constructor(val one: T1, val two: T2)