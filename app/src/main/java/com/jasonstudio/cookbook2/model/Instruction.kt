package com.jasonstudio.cookbook2.model

data class Instruction (
    val name: String,
    val steps: List<Step>
)

data class Step (
    val number: Long,
    val step: String,
    val ingredients: List<Ent>,
    val equipment: List<Ent>
)

data class Ent (
    val id: Long,
    val name: String,
    val localizedName: String,
    val image: String
)