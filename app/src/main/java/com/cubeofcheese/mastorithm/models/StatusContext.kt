package com.cubeofcheese.mastorithm.models

data class StatusContext(
    val ancestors: ArrayList<TestData>,
    val descendants: ArrayList<TestData>
)