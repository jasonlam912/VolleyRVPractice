package com.jasonstudio.cookbook2.model

data class Ingredient (
    val id: Int,
    val aisle: String,
    val image: String,
    val consistency: String,
    val name: String,
    val nameClean: String,
    val original: String,
    val originalName: String,
    val amount: Double,
    val unit: String,
    val meta: List<String>,
    val measures: Measures
)

data class Measures (
    val us: Metric,
    val metric: Metric
)

data class Metric (
    val amount: Double,
    val unitShort: String,
    val unitLong: String
)
