package com.jasonstudio.cookbook2.model

data class NutritionResponse (
    val calories: String,
    val carbs: String,
    val fat: String,
    val protein: String,
    val bad: List<Nutrition>,
    val good: List<Nutrition>,
    val nutrients: List<Flavonoid>,
    val properties: List<Flavonoid>,
    val flavonoids: List<Flavonoid>,
    val IngredientNutritions: List<IngredientNutrition>,
    val caloricBreakdown: CaloricBreakdown,
    val weightPerServing: WeightPerServing,
    val expires: Long
)

data class Nutrition (
    val title: String,
    val amount: String,
    val indented: Boolean,
    val percentOfDailyNeeds: Double
)

data class CaloricBreakdown (
    val percentProtein: Double,
    val percentFat: Double,
    val percentCarbs: Double
)

data class Flavonoid (
    val name: String,
    val amount: Double,
    val unit: String,
    val percentOfDailyNeeds: Double? = null
)

data class IngredientNutrition (
    val id: Long,
    val name: String,
    val amount: Double,
    val unit: String,
    val nutrients: List<Flavonoid>
)

data class WeightPerServing (
    val amount: Long,
    val unit: String
)
