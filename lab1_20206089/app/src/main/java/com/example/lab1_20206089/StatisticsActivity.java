package com.example.lab1_20206089;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ScrollView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

public class StatisticsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_statistics);

        // Obtener los datos del Intent (nombre del jugador y resultados del juego)
        String playerName = getIntent().getStringExtra("PLAYER_NAME");
        ArrayList<String> gameResults = getIntent().getStringArrayListExtra("gameResults");

        // Mostrar el nombre del jugador
        TextView playerNameTextView = findViewById(R.id.playerNameText);
        playerNameTextView.setText("Jugador: " + playerName);

        // Mostrar los resultados del juego
        TextView statisticsTextView = findViewById(R.id.statisticsTextView);
        for (int i = 0; i < gameResults.size(); i++) {
            statisticsTextView.append(gameResults.get(i) + "\n");
        }

        // Configurar el botón "Nuevo Juego" para iniciar otra partida
        Button btnNewGame = findViewById(R.id.btnNewGame);
        btnNewGame.setOnClickListener(v -> {
            // Redirigir a GameActivity para un nuevo juego
            Intent intent = new Intent(StatisticsActivity.this, GameActivity.class);
            intent.putExtra("PLAYER_NAME", playerName); // Pasar el nombre del jugador
            intent.putStringArrayListExtra("gameResults", gameResults); // Mantener las estadísticas
            startActivity(intent);
            finish(); // Cerrar la actividad actual
        });
    }
}
