package com.example.lab1_20206089;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        EditText etName = findViewById(R.id.etName);
        Button btnPlay = findViewById(R.id.btnPlay);

        btnPlay.setEnabled(false);
        btnPlay.setVisibility(View.INVISIBLE);

        etName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (!s.toString().trim().isEmpty()) {
                    btnPlay.setVisibility(View.VISIBLE);
                    btnPlay.setEnabled(true);
                } else {
                    btnPlay.setVisibility(View.INVISIBLE);
                    btnPlay.setEnabled(false);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
                // No es necesario implementar
            }
        });

        btnPlay.setOnClickListener(view -> {
            String playerName = etName.getText().toString();


            Intent intent = new Intent(MainActivity.this, GameActivity.class);
            intent.putExtra("PLAYER_NAME", playerName);
            startActivity(intent);
        });
    }
}
