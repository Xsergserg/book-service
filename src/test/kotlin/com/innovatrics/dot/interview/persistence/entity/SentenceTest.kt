package com.innovatrics.dot.interview.persistence.entity

import io.kotest.matchers.shouldBe
import io.mockk.mockk
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.MethodSource
import java.util.stream.Stream

class SentenceTest {
    @ParameterizedTest
    @MethodSource("sentencesProvider")
    fun `calculateWordCount should return correct word count`(sentence: Pair<String, Int>) {
        val sentenceInstance = Sentence.create(mockk(), sentence.first)
        sentenceInstance.wordCount shouldBe sentence.second
    }

    companion object {
        @JvmStatic
        fun sentencesProvider(): Stream<Pair<String, Int>> =
            Stream.of(
                Pair(
                    "I swear by my life, and my love of it, " +
                        "that I will never live for the sake of another man, " +
                        "nor ask another man to live for mine.",
                    29,
                ),
                Pair("The question isn’t who is going to let me; it’s who is going to stop me.", 16),
                Pair("We can’t be a   , part of a world that destroys individualism.", 11),
                Pair("In a world where  , no one is willing to think, one must embrace the unknown.", 15),
                Pair("I have no,obligation to tolerate the,intolerable.", 8),
                Pair("It is not, the man who has little, that is poor, but the man who desires more.", 17),
                Pair(
                    "A creative man is motivated by, the desire to achieve, " +
                        "not by the desire to beat others.",
                    17,
                ),
                Pair("Do not let your, weaknesses become a, reason for your failure.", 11),
                Pair("The most, potent weapon in the hands of the oppressor is the, mind of the oppressed.", 16),
                Pair("Freedom is, not an abstract idea   ; it is a, necessity for human progress", 13),
            )
    }
}
