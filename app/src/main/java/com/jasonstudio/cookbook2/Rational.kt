package com.jasonstudio.cookbook2

class Rational {
    private var num: Int
    private var denom: Int

    constructor(d: Double) {
        var d = d
        val s = d.toString()
        val digitsDec = s.length - 1 - s.indexOf('.')
        var denom = 1
        for (i in 0 until digitsDec) {
            d *= 10.0
            denom *= 10
        }
        val num = Math.round(d).toInt()
        this.num = num
        this.denom = denom
    }

    constructor(num: Int, denom: Int) {
        this.num = num
        this.denom = denom
    }

    override fun toString(): String {
        return "$num/$denom"
    }

    companion object {
        @JvmStatic
        fun main(args: Array<String>) {
            println(Rational(123.456))
        }
    }
}