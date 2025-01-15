package com.webserva.wings.android.geschekt

import android.util.Log
import kotlin.math.exp
import kotlin.random.Random

fun evaluateBestAction(
    currentChips: Int,
    currentCard: Int,
    remainingCards: List<Int>,
    jokerCards: List<Int>,
    players: List<Player>,
    currentPlayerIndex: Int,
    level: Int,
    first: Int,
    last: Int,
    jokers: Int,
    remainingTurns: Int
): PlayerAction {
    val currentPlayer = players[currentPlayerIndex]
    val visibleRemainingCards = remainingCards + jokerCards
    val adjacentCards = listOfNotNull(
        (currentCard - 1).takeIf { currentCard - 1 >= first },
        (currentCard + 1).takeIf { currentCard + 1 <= last }
    )

    val remSize = remainingCards.size.toDouble()
    val deckSize = remSize + jokers

    // 基本の期待値計算
    var expectedValue = when (adjacentCards.count { it in visibleRemainingCards }) {
        0 -> 0.0
        1 -> {
            val singleHitProbability = remSize / deckSize
            currentCard * 0.7 * singleHitProbability
        }
        2 -> {
            val firstHitProbability = jokers.toDouble() / deckSize
            val secondHitProbability = jokers.toDouble() / (deckSize - 1)
            currentCard * 0.9 * (1.0 - (firstHitProbability * secondHitProbability))
        }
        else -> 0.0
    }
    Log.d("evaluateBestAction", "cur: $currentCard, initial expectedValue: $expectedValue")

    // レベル4の条件: 対戦相手が現在のカードの2つ隣のカードを所持している場合、期待値を0.85倍に
    if (level >= 4) {
        val opponentHasTwoStepsAwayCard = players
            .filterIndexed { index, _ -> index != currentPlayerIndex }
            .any { it.cards.contains(currentCard - 2) || it.cards.contains(currentCard + 2) }

        if (opponentHasTwoStepsAwayCard) {
            expectedValue *= 0.85
            Log.d("evaluateBestAction", "Adjusted expected value due to opponent having two-step card: $expectedValue")
        }
    }

    // レベル5の重み付けによる処理 (残りターン数に依存した期待値の増減)
    if (level >= 5) {
        val decayFactor = 0.05  // 減衰速度を設定する値
        val turnWeight = exp(-decayFactor * (24 - remainingTurns))
        expectedValue *= turnWeight
        Log.d("evaluateBestAction", "Turn-weighted expected value: $expectedValue")
    }

    // 相手のスコアを分析する関数
    fun calculateScore(player: Player): Int {
        var preVal = 0
        var score = 0
        for (card in player.cards) {
            if (card - preVal != 1) {
                score += card
            }
            preVal = card
        }
        return score - player.chips
    }

    // 現在のスコアと、カードを取得した後の予測スコアを計算
    val currentScore = calculateScore(currentPlayer)
    val newCards = currentPlayer.cards.toMutableList()
    newCards.add(currentCard)
    val predictedScoreAfterCollect = calculateScore(currentPlayer.copy(cards = newCards))

    // 行動フラグ
    var colFlag = false

    // Level 1の条件
    if (currentCard - currentChips == 1) colFlag = true

    // レベル2以上の条件: 期待値に基づいて判断
    if (level >= 2 && !colFlag && expectedValue >= currentCard - currentChips) colFlag = true

    // レベル3以上の条件: 隣のカードを持っている場合
    if (level >= 3 && currentPlayer.cards.any { it in adjacentCards }) {
        colFlag = true



        // レベル3以降の条件: 隣接カードが他プレイヤーにない場合のエクスプロイト
        val hasNextOrPreviousCard = currentPlayer.cards.contains(currentCard + 1) || currentPlayer.cards.contains(currentCard - 1)
        val nobodyHasAdjacentCard = players
            .filterIndexed { index, _ -> index != currentPlayerIndex }
            .none { it.cards.contains(currentCard - 1) || it.cards.contains(currentCard + 1) }
        val nobodyHasZeroChips = players.none { it.chips == 0 }
        Log.d("evaluateBestAction", "hasNextOrPreviousCard: $hasNextOrPreviousCard , nobodyHasAdjacentCard: $nobodyHasAdjacentCard, nobodyHasZeroChips: $nobodyHasZeroChips")


        if (hasNextOrPreviousCard && nobodyHasAdjacentCard && nobodyHasZeroChips) {
            Log.d("evaluateBestAction", "Exploit condition met with next/previous card")
            val exploitValue = currentCard / 2.0
            if (exploitValue <= currentCard - currentChips) {
                colFlag = false
            }
        }
        // レベル4の条件: 次の一周で期待値が増加する場合
        if (level >= 4) {
            val futureExpectedValue = expectedValue + players.size * (expectedValue / deckSize)
            if (futureExpectedValue > currentCard - currentChips) {
                Log.d("evaluateBestAction", "Future Expected Value: $futureExpectedValue")
                colFlag = true
            }
        }
    }

    return if (colFlag) {
        PlayerAction.COLLECT
    } else {
        PlayerAction.STACK
    }
}
