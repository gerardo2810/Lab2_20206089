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

        String playerName = getIntent().getStringExtra("PLAYER_NAME");
        ArrayList<String> gameResults = getIntent().getStringArrayListExtra("gameResults");

        TextView playerNameTextView = findViewById(R.id.playerNameText);
        playerNameTextView.setText("Jugador: " + playerName);

        TextView statisticsTextView = findViewById(R.id.statisticsTextView);
        for (int i = 0; i < gameResults.size(); i++) {
            statisticsTextView.append(gameResults.get(i) + "\n");
        }

        Button btnNewGame = findViewById(R.id.btnNewGame);
        btnNewGame.setOnClickListener(v -> {
            Intent intent = new Intent(StatisticsActivity.this, GameActivity.class);
            intent.putExtra("PLAYER_NAME", playerName);
            intent.putStringArrayListExtra("gameResults", gameResults);
            startActivity(intent);
            finish();
        });
    }
}
