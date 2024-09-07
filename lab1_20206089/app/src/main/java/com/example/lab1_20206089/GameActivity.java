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
    private ArrayList<String> gameResults = new ArrayList<>(); // Para almacenar los resultados
    private String playerName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        ImageView leftIcon = findViewById(R.id.left_icon);  // Flecha de retroceso
        ImageView rightIcon = findViewById(R.id.right_icon);  // Icono de estadísticas
        TextView title = findViewById(R.id.toolbar_title);

        // Acción al presionar la flecha (volver a MainActivity)
        leftIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Guardar resultado de juego si fue cancelado
                addGameResult("Canceló");
                Intent intent = new Intent(GameActivity.this, MainActivity.class);
                startActivity(intent);
                finish(); // Cerrar la actividad actual
            }
        });

        // Acción al presionar el icono de estadísticas (ir a StatisticsActivity)
        rightIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(GameActivity.this, StatisticsActivity.class);
                intent.putStringArrayListExtra("gameResults", gameResults);
                startActivity(intent);
            }
        });

        // Obtener el nombre del jugador desde el Intent
        playerName = getIntent().getStringExtra("PLAYER_NAME");

        // Configurar botones de letras
        GridLayout gridLetters = findViewById(R.id.gridLetters);
        for (int i = 0; i < gridLetters.getChildCount(); i++) {
            Button letterButton = (Button) gridLetters.getChildAt(i);
            letterButton.setOnClickListener(v -> onLetterClicked(letterButton));
        }

        // Configurar botón de nuevo juego
        Button btnNewGame = findViewById(R.id.btnNewGame);
        btnNewGame.setOnClickListener(v -> {
            addGameResult("Canceló");
            startNewGame();
        });

        // Iniciar el primer juego
        startNewGame();
    }

    private void startNewGame() {
        // Reiniciar el estado del juego
        incorrectGuesses = 0;

        // Ocultar todas las partes del cuerpo del muñeco
        resetHangmanImage();

        // Seleccionar una palabra aleatoria
        Random random = new Random();
        selectedWord = words[random.nextInt(words.length)];
        guessedWord = new char[selectedWord.length()];
        for (int i = 0; i < guessedWord.length; i++) {
            guessedWord[i] = '_';
        }

        // Actualizar la vista de las letras
        updateWordDisplay();

        // Habilitar todos los botones de letras
        GridLayout gridLetters = findViewById(R.id.gridLetters);
        for (int i = 0; i < gridLetters.getChildCount(); i++) {
            Button letterButton = (Button) gridLetters.getChildAt(i);
            letterButton.setEnabled(true);
        }

        // Iniciar cronómetro
        startTime = System.currentTimeMillis();
    }

    // Método para ocultar todas las partes del cuerpo del muñeco
    private void resetHangmanImage() {
        findViewById(R.id.ivHangmanHead).setVisibility(View.INVISIBLE);  // Ocultar cabeza
        findViewById(R.id.ivHangmanTorso).setVisibility(View.INVISIBLE);  // Ocultar torso
        findViewById(R.id.ivHangmanArmRight).setVisibility(View.INVISIBLE);  // Ocultar brazo derecho
        findViewById(R.id.ivHangmanArmLeft).setVisibility(View.INVISIBLE);  // Ocultar brazo izquierdo
        findViewById(R.id.ivHangmanLegLeft).setVisibility(View.INVISIBLE);  // Ocultar pierna izquierda
        findViewById(R.id.ivHangmanLegRight).setVisibility(View.INVISIBLE);  // Ocultar pierna derecha
    }

    private void updateWordDisplay() {
        GridLayout gridLines = findViewById(R.id.gridLines);
        gridLines.removeAllViews();  // Limpiar vistas previas, si existen

        for (int i = 0; i < guessedWord.length; i++) {
            TextView letterView = new TextView(this);
            letterView.setText(String.valueOf(guessedWord[i]));
            letterView.setTextSize(36);  // Ajusta el tamaño de letra
            letterView.setGravity(View.TEXT_ALIGNMENT_CENTER);

            GridLayout.LayoutParams params = new GridLayout.LayoutParams();
            params.setMargins(8, 8, 8, 8);  // Margen entre letras
            letterView.setLayoutParams(params);

            gridLines.addView(letterView);
        }
    }

    private void onLetterClicked(Button letterButton) {
        char letter = letterButton.getText().toString().charAt(0);
        boolean correctGuess = false;

        letterButton.setEnabled(false);  // Deshabilitar el botón seleccionado

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
                findViewById(R.id.ivHangmanHead).setVisibility(View.VISIBLE); // Mostrar cabeza
                break;
            case 2:
                findViewById(R.id.ivHangmanTorso).setVisibility(View.VISIBLE); // Mostrar torso
                break;
            case 3:
                findViewById(R.id.ivHangmanArmRight).setVisibility(View.VISIBLE); // Mostrar brazo derecho
                break;
            case 4:
                findViewById(R.id.ivHangmanArmLeft).setVisibility(View.VISIBLE); // Mostrar brazo izquierdo
                break;
            case 5:
                findViewById(R.id.ivHangmanLegLeft).setVisibility(View.VISIBLE); // Mostrar pierna izquierda
                break;
            case 6:
                findViewById(R.id.ivHangmanLegRight).setVisibility(View.VISIBLE); // Mostrar pierna derecha
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
