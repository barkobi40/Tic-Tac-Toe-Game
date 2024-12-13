package com.barkobi.myapplicationtictactoe

import android.media.MediaPlayer
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.barkobi.myapplicationtictactoe.ui.theme.MyApplicationTictactoeTheme
import com.barkobi.myapplicationtictactoe.R

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MyApplicationTictactoeTheme {
                TicTacToeGame()
            }
        }
    }
}

@Composable
fun TicTacToeGame() {
    val backgroundColor = Color(0xFFE9EDC9)
    val textColor = Color(0xFF003049)

    val quicksandFont = FontFamily(Font(R.font.delaghotic))

    var board by remember { mutableStateOf(Array(3) { Array(3) { "" } }) }
    var currentPlayer by remember { mutableStateOf("X") }
    var winner by remember { mutableStateOf<String?>(null) }
    val context = LocalContext.current

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(backgroundColor)
            .padding(16.dp)
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 16.dp)
                .align(Alignment.TopCenter)
        ) {
            Text(
                text = "Tic Tac Toe",
                fontSize = 36.sp,
                fontWeight = FontWeight.Bold,
                fontFamily = quicksandFont,
                color = textColor,
                modifier = Modifier
                    .align(Alignment.TopCenter)
                    .padding(top = 32.dp)
            )
        }

        Column(
            modifier = Modifier
                .align(Alignment.Center)
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Box(
                modifier = Modifier
                    .border(4.dp, textColor, RoundedCornerShape(8.dp))
                    .padding(8.dp)
            ) {
                Column {
                    for (row in 0..2) {
                        Row {
                            for (col in 0..2) {
                                Box(
                                    modifier = Modifier
                                        .size(100.dp)
                                        .background(Color.LightGray, RoundedCornerShape(8.dp))
                                        .padding(4.dp)
                                        .clickable {
                                            if (board[row][col].isEmpty() && winner == null) {
                                                board[row][col] = currentPlayer
                                                winner = checkWinner(board)
                                                if (winner != null && winner != "Draw") {
                                                    // Play sound when someone wins
                                                    MediaPlayer.create(context, R.raw.win_sound).apply {
                                                        setOnCompletionListener { release() }
                                                        start()
                                                    }
                                                }
                                                if (winner == null) {
                                                    currentPlayer = if (currentPlayer == "X") "O" else "X"
                                                }
                                            }
                                        },
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text(
                                        text = board[row][col],
                                        fontSize = 24.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = textColor
                                    )
                                }
                            }
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            if (winner != null) {
                Text(
                    text = when (winner) {
                        "Draw" -> "It's a Draw!"
                        else -> "Player $winner Wins!"
                    },
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold,
                    color = textColor
                )
            } else {
                Text(
                    text = "Player $currentPlayer's Turn",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Medium,
                    color = textColor
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            Button(
                onClick = {
                    board = Array(3) { Array(3) { "" } }
                    currentPlayer = "X"
                    winner = null
                },
                modifier = Modifier
                    .width(200.dp)
                    .height(60.dp)
            ) {
                Text(
                    text = "Play Again",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = textColor
                )
            }
        }
    }
}

fun checkWinner(board: Array<Array<String>>): String? {
    for (i in 0..2) {
        if (board[i][0] == board[i][1] && board[i][1] == board[i][2] && board[i][0].isNotEmpty())
            return board[i][0]
        if (board[0][i] == board[1][i] && board[1][i] == board[2][i] && board[0][i].isNotEmpty())
            return board[0][i]
    }
    if (board[0][0] == board[1][1] && board[1][1] == board[2][2] && board[0][0].isNotEmpty())
        return board[0][0]
    if (board[0][2] == board[1][1] && board[1][1] == board[2][0] && board[0][2].isNotEmpty())
        return board[0][2]

    if (board.all { row -> row.all { it.isNotEmpty() } }) return "Draw"

    return null
}
