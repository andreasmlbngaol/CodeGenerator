package com.andreasmlbngaol.codegenerator.model

import java.time.Instant
import java.time.ZoneId
import java.time.format.DateTimeFormatter

fun Long.millisToLocalDateTime(): String {
    val instant = Instant.ofEpochMilli(this)
    val formatter = DateTimeFormatter.ofPattern("dd MMM yyyy, HH:mm:ss")
        .withZone(ZoneId.systemDefault())
    return formatter.format(instant)
}