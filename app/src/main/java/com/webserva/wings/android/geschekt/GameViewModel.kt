package com.webserva.wings.android.geschekt
import android.util.Log
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.*
import kotlin.random.Random


class GameViewModel : ViewModel() {

    val currentTurnIndex = mutableStateOf(0)
    val isControlEnabled = mutableStateOf(false)

    val remainingCards = mutableStateListOf<Int>()
    val currentCard = mutableStateOf<Int?>(null)

    val players = mutableStateListOf<MutableState<Player>>()
    val stackedChips = mutableStateOf(0)

    val first = mutableStateOf(3)
    val last = mutableStateOf(35)
    val jokers = mutableStateOf(9)

    val jokerCards = mutableStateListOf<Int>()
    val isGameOver = mutableStateOf(false)

    val level = mutableStateOf(0)

    private val coroutineScope = CoroutineScope(Dispatchers.Main)

    fun initializeGame(
        playerCount: Int,
        level: Int,
        first: Int,
        last: Int,
        jokers: Int
    ) {
        players.clear()
        repeat(playerCount) {
            players.add(mutableStateOf(Player("プレイヤー${it + 1}", chips = 11, cards = mutableStateListOf())))
        }
        this.level.value = level
        this.first.value = first
        this.last.value = last
        this.jokers.value = jokers
        this.isGameOver.value = false

        remainingCards.clear()
        remainingCards.addAll(3..35)

        jokerCards.clear()
        for (i in 0 until jokers) {
            selectRandomCard(true)
        }
        selectRandomCard(false)
        currentTurnIndex.value = 0
        stackedChips.value = 0

        isControlEnabled.value = true
    }

    fun takeTurns() {
        isControlEnabled.value = false

        coroutineScope.launch {
            for (i in 1 until players.size) {
                currentTurnIndex.value = i
                delay(350L)
                cpuTurn()
                yield()
            }
            currentTurnIndex.value = 0
            isControlEnabled.value = true
        }
    }

    private fun cpuTurn() {
        if (isGameOver.value) return

        var continueTurn = true
        while (continueTurn) {
            val currentPlayer = players[currentTurnIndex.value].value

            // 残りカードがない場合はゲーム終了
            if (remainingCards.isEmpty()) {
                isGameOver.value = true
                Log.d("GameViewModel", "Game Over: No remaining cards.")
                return
            }

            // アクションの決定
            val bestAction = if (currentPlayer.chips == 0) {
                PlayerAction.COLLECT
            } else {
                evaluateBestAction(
                    currentChips = stackedChips.value,
                    remainingCards = remainingCards,
                    currentCard = currentCard.value ?: 0,
                    jokerCards = jokerCards,
                    players = players.map { it.value },
                    currentPlayerIndex = currentTurnIndex.value,
                    level = level.value,
                    first = first.value,
                    last = last.value,
                    jokers = jokers.value,
                    remainingTurns = remainingCards.size
                )
            }

            // アクションの実行
            playerAction(bestAction)

            // アクションの結果によってターンの進行を決定
            continueTurn = when (bestAction) {
                PlayerAction.COLLECT -> !isGameOver.value // COLLECT なら同じプレイヤーで続行
                PlayerAction.STACK -> false // STACK なら次のプレイヤーに移行
            }
        }
    }




    fun playerAction(action: PlayerAction) {
        val currentPlayer = players[currentTurnIndex.value].value

        when (action) {
            PlayerAction.STACK -> {
                if (currentPlayer.chips > 0) {
                    stackedChips.value += 1
                    currentPlayer.chips -= 1
                }
            }
            PlayerAction.COLLECT -> {
                currentPlayer.chips += stackedChips.value
                stackedChips.value = 0

                currentCard.value?.let { card ->
                    currentPlayer.cards.add(card)
                    currentPlayer.cards.sort()
                    currentCard.value = null
                }

                // 次のカードを選択、カードが残っていない場合に結果発表
                selectRandomCard(false)
            }
        }
    }


    // 残っているカードからランダムに1枚選択してcurrentCardに設定
    private fun selectRandomCard(state: Boolean) {
        if (remainingCards.isNotEmpty()) {
            val randomIndex = Random.nextInt(remainingCards.size)
            val selectedCard = remainingCards.removeAt(randomIndex)
            if (state) {
                jokerCards.add(selectedCard)
                return
            }
            currentCard.value = selectedCard // 抽選されたカードをリストから削除
        }
    }

    override fun onCleared() {
        super.onCleared()
        coroutineScope.cancel()
    }

    fun calculateScore(player: Player): Int {
        var preVal = 0
        var score = 0
        for (card in player.cards) {
            if (card - preVal != 1) {
                score += card
            }
            preVal = card
        }
        return player.chips - score
    }

    // 全プレイヤーのスコアを取得する関数
    fun getAllScores(): List<Int> {
        return players.map { calculateScore(it.value) }
    }
}

// プレイヤーの行動を定義する列挙型
enum class PlayerAction {
    STACK, COLLECT
}

// プレイヤーのデータクラス
data class Player(
    val name: String,
    var chips: Int,
    val cards: MutableList<Int>
)
