package com.dah.tcs

import java.awt.Color
import kotlin.random.Random

fun main() {
    val foo = Viewer("foo", Color.WHITE, listOf("b_prime"))
    val bar = Viewer("bar", Color.RED, listOf())

    foo says "Pog lets go" at (0 f 15)
    bar says "Widega Widega Widega" at (1 f 0)

    val pool = generateViewerPool(30, listOf("b_prime", "b_t1_sub", "b_t2_sub", "b_t3_sub"), 0.1, 3)

    val specialMessages = arrayListOf(
        "HOW DID HE DO THAT?",
        "INSANE Pog"
    )

    generateSpam(20.0, 2 f 0, 8 f 0, 3 f 0, pool) {_, _ ->
        random {
//            case(0.5) { "catJAM ".repeat(Random.nextInt(1, 4)) }
//            elseCase { "pepeJAM ".repeat(Random.nextInt(1, 5)) }
            case(0.2) {
                random {
                    case(0.3) { "Pog".spam(5, 10) }
                    elseCase { "Pog" }
                }
            }

            case(0.4) {
                val msg = listOf("I WAS HERE Pog", "Pog I WAS HERE").random()
                random {
                    case(0.8) { msg }
                    case(0.2) { msg.spam(8, 16) }
                }
            }

            case(0.1) {
                "EZ Clap"
            }

            case(0.1) { "POG".spamTrail(4, 8).ifChance(0.3) { it.toLowerCase() } }
            case(0.1) {
                listOf("HOLY FUCK", "HOLY SHIT", "WTF", "LETS GO").random()
                    .ifChance(0.4) { it.spamTrail(4, 8) }
            }

            case(0.1) {
                listOf("WTF?", "HOLY").random().spamTrail(4, 8)
            }

            elseCase {
                specialMessages.randomAndRemove("HOW??????")
            }
        }
    }

    writeChat("D:\\data\\cpp\\tcr\\res\\script.txt")
}
