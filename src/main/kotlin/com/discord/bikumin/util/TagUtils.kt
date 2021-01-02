package com.discord.bikumin.util

import com.discord.bikumin.Bot
import java.lang.StringBuilder

object TagUtils {

    var alphabet: CharArray = CharArray(26)

    val hiragana = arrayOf(
        "あ", "い", "う", "え", "お",
        "か", "き", "く", "け", "こ",
        "さ", "し", "す", "せ", "そ",
        "た", "ち", "つ", "て", "と",
        "な", "に", "ぬ", "ね", "の",
        "は", "ひ", "ふ", "へ", "ほ",
        "ま", "み", "む", "め", "も",
        "や", "ゆ", "よ",
        "ら", "り", "る", "れ", "ろ",
        "わ", "を", "ん"
    )

    val num = arrayOf(0, 1, 2, 3, 4, 5, 6, 7, 8, 9)

    init {
        var c = 'a'
        for (i in 0..25) {
            alphabet[i] = c++
        }
    }

    fun getAlphabet(i: Int) : String {
        return alphabet[i].toString()
    }

    fun randomAlphabets(length: Int): String{
        val tag = StringBuilder()

        for(i in 0 until length){
            tag.append(getAlphabet(Bot.random.nextInt(alphabet.size)))
        }

        return tag.toString() + " | " + tag.toString().toUpperCase()
    }

    fun randomHiragana(length: Int): String {
        return StringBuilder().apply {

            for (i in 0 until length) {
                append(hiragana[Bot.random.nextInt(hiragana.size)])
            }

        }.toString()
    }

    fun randomNum(length: Int): String{
        val tag = StringBuilder()

        for(i in 0 until length){
            tag.append(num[Bot.random.nextInt(num.size)])
        }

        return tag.toString()
    }

}