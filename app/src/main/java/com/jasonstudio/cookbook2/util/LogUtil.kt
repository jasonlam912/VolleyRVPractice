package com.jasonstudio.cookbook2.util

import android.util.Log

class LogUtil {
    companion object {
        fun <T> log(vararg p0: T) {
            val lineNum = Thread.currentThread().stackTrace[3].lineNumber
            val methodName = Thread.currentThread().stackTrace[3].methodName
            val className = Thread.currentThread().stackTrace[3].className
            val fileName = Thread.currentThread().stackTrace[3].fileName
            val p0Str = p0.map { " $it" }.reduce { acc, str -> "$acc$str" }
            Log.d("", "$fileName $className $methodName:$lineNum $p0Str")
        }
    }
}