package com.discord.bikumin

import java.awt.Color
import kotlin.properties.ReadOnlyProperty

object Env {
    val BOT_TOKEN by string { "" }
    val HOST by string { "0.0.0.0" }
    val PORT by int { 80 }
    val LOG by string { "INFO" }
}

private val stringOrNull: ReadOnlyProperty<Env, String?>
    get() = ReadOnlyProperty { _, property ->
        System.getenv(property.name)
    }

private fun string(default: () -> String): ReadOnlyProperty<Env, String> = ReadOnlyProperty { _, property ->
    System.getenv(property.name) ?: default()
}

private fun int(default: () -> Int): ReadOnlyProperty<Env, Int> = ReadOnlyProperty { _, property ->
    System.getenv(property.name)?.toIntOrNull() ?: default()
}