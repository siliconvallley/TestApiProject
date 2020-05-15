package com.dh.testproject.activity.viewpager

import android.os.Bundle
import androidx.core.text.BidiFormatter

class Card private constructor(val suit: String, val value: String) {

    val cornerLabel: String
        get() = "$suit\n$value"

    /** Use in conjunction with [Card.fromBundle]  */
    fun toBundle(): Bundle {
        val args = Bundle(1)
        args.putStringArray(ARGS_BUNDLE, arrayOf(suit, value))
        return args
    }

    override fun toString(): String {
        val instance = BidiFormatter.getInstance();
        return if (!instance.isRtlContext) {
            instance.unicodeWrap("$value $suit")
        } else {
            instance.unicodeWrap("$suit $value")
        }
    }

    companion object {
        //private val ARGS_BUNDLE = Card::class.java.name + ":Bundle"
        private val ARGS_BUNDLE = "${Card::class.java.simpleName}:Bundle"

        private val SUITS = setOf("♣" /* clubs*/, "♦" /* diamonds*/, "♥" /* hearts*/, "♠" /*spades*/)
        private val VALUES = setOf("2", "3", "4", "5", "6", "7", "8", "9", "10", "J", "Q", "K", "A")

        val DECK = SUITS.flatMap { suit ->
            VALUES.map { value ->
                Card(suit, value)
            }
        }

        fun List<Card>.find(value: String, suit: String): Card? {
            return find { it.value == value && it.suit == suit }
        }

        /** Use in conjunction with [Card.toBundle]  */
        fun fromBundle(bundle: Bundle): Card {
            val spec = bundle.getStringArray(ARGS_BUNDLE)
            return Card(spec!![0], spec[1])
        }
    }
}