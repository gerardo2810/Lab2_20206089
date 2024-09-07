package com.example.lab1_20206089;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.Random;

public class GameActivity extends AppCompatActivity {

    private String[] words = {"FIBRA", "REDES", "ANTENA", "PROPA", "CLOUD", "TELECO"};
    private String selectedWord;
    private char[] guessedWord;
    private int incorrectGuesses;
    private long startTime;
    private ArrayList<String> gameResults = new ArrayList<>();
    private String playerName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        ImageView leftIcon = findViewById(R.id.left_icon);
        ImageView rightIcon = findViewById(R.id.right_icon);
        TextView title = findViewById(R.id.toolbar_title);

        leftIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addGameResult("Canceló");
                Intent intent = new Intent(GameActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        });

        rightIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(GameActivity.this, StatisticsActivity.class);
                intent.putStringArrayListExtra("gameResults", gameResults);
                startActivity(intent);
            }
        });

        playerName = getIntent().getStringExtra("PLAYER_NAME");

        GridLayout gridLetters = findViewById(R.id.gridLetters);
        for (int i = 0; i < gridLetters.getChildCount(); i++) {
            Button letterButton = (Button) gridLetters.getChildAt(i);
            letterButton.setOnClickListener(v -> onLetterClicked(letterButton));
        }

        Button btnNewGame = findViewById(R.id.btnNewGame);
        btnNewGame.setOnClickListener(v -> {
            addGameResult("Canceló");
            startNewGame();
        });

        startNewGame();
    }

    private void startNewGame() {
        incorrectGuesses = 0;

        resetHangmanImage();

        Random random = new Random();
        selectedWord = words[random.nextInt(words.length)];
        guessedWord = new char[selectedWord.length()];
        for (int i = 0; i < guessedWord.length; i++) {
            guessedWord[i] = '_';
        }

        updateWordDisplay();

        GridLayout gridLetters = findViewById(R.id.gridLetters);
        for (int i = 0; i < gridLetters.getChildCount(); i++) {
            Button letterButton = (Button) gridLetters.getChildAt(i);
            letterButton.setEnabled(true);
        }

        startTime = System.currentTimeMillis();
    }

    private void resetHangmanImage() {
        findViewById(R.id.ivHangmanHead).setVisibility(View.INVISIBLE);
        findViewById(R.id.ivHangmanTorso).setVisibility(View.INVISIBLE);
        findViewById(R.id.ivHangmanArmRight).setVisibility(View.INVISIBLE);
        findViewById(R.id.ivHangmanArmLeft).setVisibility(View.INVISIBLE);
        findViewById(R.id.ivHangmanLegLeft).setVisibility(View.INVISIBLE);
        findViewById(R.id.ivHangmanLegRight).setVisibility(View.INVISIBLE);
    }

    private void updateWordDisplay() {
        GridLayout gridLines = findViewById(R.id.gridLines);
        gridLines.removeAllViews();

        for (int i = 0; i < guessedWord.length; i++) {
            TextView letterView = new TextView(this);
            letterView.setText(String.valueOf(guessedWord[i]));
            letterView.setTextSize(36);
            letterView.setGravity(View.TEXT_ALIGNMENT_CENTER);

            GridLayout.LayoutParams params = new GridLayout.LayoutParams();
            params.setMargins(8, 8, 8, 8);
            letterView.setLayoutParams(params);

            gridLines.addView(letterView);
        }
    }

    private void onLetterClicked(Button letterButton) {
        char letter = letterButton.getText().toString().charAt(0);
        boolean correctGuess = false;

        letterButton.setEnabled(false);

        for (int i = 0; i < selectedWord.length(); i++) {
            if (selectedWord.charAt(i) == letter) {
                guessedWord[i] = letter;
                correctGuess = true;
            }
        }

        if (correctGuess) {
            updateWordDisplay();
            checkWinCondition();
        } else {
            incorrectGuesses++;
            updateHangmanImage();
            checkLoseCondition();
        }
    }

    private void updateHangmanImage() {
        switch (incorrectGuesses) {
            case 1:
                findViewById(R.id.ivHangmanHead).setVisibility(View.VISIBLE);
                break;
            case 2:
                findViewById(R.id.ivHangmanTorso).setVisibility(View.VISIBLE);
                break;
            case 3:
                findViewById(R.id.ivHangmanArmRight).setVisibility(View.VISIBLE);
                break;
            case 4:
                findViewById(R.id.ivHangmanArmLeft).setVisibility(View.VISIBLE);
                break;
            case 5:
                findViewById(R.id.ivHangmanLegLeft).setVisibility(View.VISIBLE);
                break;
            case 6:
                findViewById(R.id.ivHangmanLegRight).setVisibility(View.VISIBLE);
                break;
        }
    }

    private void checkWinCondition() {
        boolean hasWon = true;
        for (char c : guessedWord) {
            if (c == '_') {
                hasWon = false;
                break;
            }
        }

        if (hasWon) {
            long timeElapsed = (System.currentTimeMillis() - startTime) / 1000;
            addGameResult("Terminó en " + timeElapsed + "s");
            showEndMessage("¡Ganaste! Tiempo: " + timeElapsed + " segundos");
        }
    }

    private void checkLoseCondition() {
        if (incorrectGuesses >= 6) {
            addGameResult("Perdió");
            showEndMessage("¡Perdiste! La palabra era: " + selectedWord);
        }
    }

    private void addGameResult(String result) {
        gameResults.add("Juego " + (gameResults.size() + 1) + ": " + result);
    }

    private void showEndMessage(String message) {
        TextView endMessage = findViewById(R.id.endMessage);
        endMessage.setText(message);
        endMessage.setVisibility(View.VISIBLE);

        GridLayout gridLetters = findViewById(R.id.gridLetters);
        for (int i = 0; i < gridLetters.getChildCount(); i++) {
            Button letterButton = (Button) gridLetters.getChildAt(i);
            letterButton.setEnabled(false);
        }
    }

    private void goToStatistics() {
        Intent intent = new Intent(GameActivity.this, StatisticsActivity.class);
        intent.putStringArrayListExtra("gameResults", gameResults);
        intent.putExtra("PLAYER_NAME", playerName);
        startActivity(intent);
    }
}
