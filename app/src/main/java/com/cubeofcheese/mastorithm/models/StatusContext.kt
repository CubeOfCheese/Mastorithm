package com.cubeofcheese.mastorithm.models

import com.cubeofcheese.mastorithm.TestData

data class StatusContext(
    val ancestors: ArrayList<TestData>,
    val descendants: ArrayList<TestData>
)