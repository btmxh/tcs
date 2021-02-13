package com.dah.tcs

import java.awt.Color
import java.io.File
import java.nio.file.Path
import java.nio.file.Paths
import java.time.LocalTime
import kotlin.math.roundToLong

data class Viewer(val name: String, val color: Color, val badges: List<String>)
data class Message(val viewer: Viewer, val content: String, val time: LocalTime)

var currentStreamChat = arrayListOf<Message>()

infix fun Viewer.says(content: String) = Pair(this, content)
infix fun Pair<Viewer, String>.at(time: LocalTime) = Message(first, second, time).also(currentStreamChat::add)

fun writeChat(file: String) {
    File(file).bufferedWriter().use {
        currentStreamChat.sortBy { it.time }
        for(m in currentStreamChat) {
            m.time.apply { it.appendLine("$hour:$minute:${second + nano / 1E9}") }
            it.appendLine(m.viewer.name)
            m.viewer.color.apply { it.appendLine("$red,$green,$blue,$alpha") }
            it.appendLine(m.viewer.badges.joinToString(" ").ifEmpty { "\$null" })
            it.appendLine(m.content)
        }
    }
}

infix fun Int.h(m: Int) = LocalTime.of(this, m)
infix fun LocalTime.m(s: Double) = this.plusNanos((s * 1e9).roundToLong())
infix fun LocalTime.m(s: Int) = this.plusNanos(s.toLong() * 1_000_000_000)
infix fun Int.m(s: Double) = 0 h this m s
infix fun Int.m(s: Int) = 0 h this m s
