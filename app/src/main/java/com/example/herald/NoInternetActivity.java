package com.example.herald;

import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class NoInternetActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.no_connection);

        Button btnRetry = findViewById(R.id.btnRetry);
        btnRetry.setOnClickListener(v -> {
            // On vérifie à nouveau la connexion
            if (NetworkCheck.isNetworkAvailable(NoInternetActivity.this)) {
                finish(); // Ferme cette page et revient à la précédente
            } else {
                Toast.makeText(this, "Toujours pas de réseau...", Toast.LENGTH_SHORT).show();
            }
        });
    }
}