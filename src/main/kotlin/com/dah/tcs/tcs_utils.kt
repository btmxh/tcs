package com.dah.tcs

import com.kdotjpg.OpenSimplexNoise
import java.awt.Color
import java.time.LocalTime
import kotlin.math.max
import kotlin.math.min
import kotlin.math.pow
import kotlin.random.Random

var fps = 30

infix fun LocalTime.f(frame: Int) = this.plusNanos((1e9 / fps).toLong())
infix fun Double.f(frame: Int) = 0 m this f frame
infix fun Int.f(frame: Int) = 0 m this f frame

val defaultColors = listOf(
    0xFF69B4,
    0x359BFF,
    0xAA64EA,
    0xFF581A
).map { Color(it) }

//Taken from Xlice's twitch stream on 13:40 UTC+7
val usernamePool = Viewer::class.java.getResource("/usernames.txt").readText().lines()
    .map {
        if(it.substring(1).all { !it.isUpperCase() }) {
            it[0].toLowerCase() + it.substring(1)
        } else it
    }

fun generateViewerPool(count: Int, badges: List<String>, badgeChance: Double, badgeLimit: Int): List<Viewer> {
    val usrPoolCopy = ArrayList(usernamePool)
    val pool = arrayListOf<Viewer>()
    repeat(count) {
        val viewerName = usrPoolCopy.randomAndRemove()
        val badgesCopy = ArrayList(badges)
        val viewerBadges = arrayListOf<String>()
        for(ignore in 0 until badgeLimit) {
            if(Math.random() < badgeChance) {
                viewerBadges.add(badgesCopy.randomAndRemove())
            }
        }
        pool.add(Viewer(viewerName, defaultColors.random(), viewerBadges))
    }

    return pool
}

fun generateSpam(strength: Double, from: LocalTime, to: LocalTime, spamPeak: LocalTime, viewerPool: List<Viewer>, spamGen: (Viewer, LocalTime) -> String) {
    val delta = (1e9 / fps).toLong()

    var now = from
    var i = 0

    while(now < to) {
        var timeFactor =
            if(now < spamPeak) {
                (now.toNanoOfDay() - from.toNanoOfDay()).toDouble() / (spamPeak.toNanoOfDay() - from.toNanoOfDay()).toDouble()
            } else {
                (to.toNanoOfDay() - now.toNanoOfDay()).toDouble() / (to.toNanoOfDay() - spamPeak.toNanoOfDay()).toDouble()
            }

        timeFactor = 1 - (1 - timeFactor).pow(2.0)

        val spams = min((strength / fps / Random.nextDouble() * timeFactor).toInt(), 10)

        repeat(spams) {
            viewerPool.random().let { it says spamGen(it, now) at now }
        }
        now = now.plusNanos(delta)
    }
}

class ChanceScope<T> {
    var prev = 0.0
    val cases = arrayListOf<Pair<Double, () -> T>>()
    lateinit var elseCase: () -> T

    fun case(chance: Double, supplier: () -> T) {
        cases.add((prev + chance) to supplier)
        prev += chance
    }

    fun elseCase(supplier: () -> T) {
        elseCase = supplier
    }

    fun getValue(): T {
        cases.sortBy { it.first }
        val rng = Math.random()
        for(case in cases) {
            if(case.first > rng)    return case.second()
        }
        return elseCase()

    }
}

fun <T> random(block: ChanceScope<T>.() -> Unit): T {
    val scope = ChanceScope<T>()
    block(scope)
    return scope.getValue()
}

//uints only
fun rdm(from: Int, until: Int = -1): Int {
    return if(until < 0) from else Random.nextInt(from, until)
}

fun <T> T.ifChance(chance: Double, transform: (T) -> T): T {
    return if(Math.random() < chance) transform(this) else this
}

fun String.spam(from: Int, until: Int = -1): String {
    return ("$this ").repeat(rdm(from, until))
}

fun String.spamTrail(from: Int, until: Int = -1): String {
    return this + last().toString().repeat(rdm(from - 1, until - 1))
}

fun <T> MutableList<T>.randomAndRemove(alt: T? = null): T {
    if(isEmpty())   return alt!!
    return removeAt(Random.nextInt(size))
}
