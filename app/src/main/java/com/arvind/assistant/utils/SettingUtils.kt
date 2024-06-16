package com.arvind.assistant.utils

fun Set<String>.toIntList() = this.toList().map { it.toInt() }