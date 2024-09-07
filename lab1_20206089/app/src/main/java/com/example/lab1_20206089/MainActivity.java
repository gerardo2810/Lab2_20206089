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

        // Inicialmente, el botón "Jugar" está invisible y deshabilitado
        btnPlay.setEnabled(false);
        btnPlay.setVisibility(View.INVISIBLE);

        // Listener para el campo de texto
        etName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // No es necesario implementar
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // Mostrar y habilitar el botón si hay texto en el campo
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
            // Obtener el texto ingresado en el campo de nombre
            String playerName = etName.getText().toString();

            // Redirigir a la actividad de juego (GameActivity) y pasar el nombre del jugador
            Intent intent = new Intent(MainActivity.this, GameActivity.class);
            intent.putExtra("PLAYER_NAME", playerName); // Enviar el nombre del jugador
            startActivity(intent);
        });
    }
}
