package com.example.appandroidconfirebase;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.firebase.auth.FirebaseAuth;

public class HomeActivity extends AppCompatActivity {

    private Button btnObtner, btnCambiar, btnLogout;
    private TextView tvEstadoLed;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_home);

        btnObtner = findViewById(R.id.btnObtener);
        btnCambiar = findViewById(R.id.btnCambiar);
        btnLogout = findViewById(R.id.btnLogout);

        mAuth = FirebaseAuth.getInstance();

        btnLogout.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                logoutUser();
            }
        });

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    private void logoutUser(){
        // 1. Se llama al método de cierre de sesión de Firebase
        mAuth.signOut();
        // 2. Mostrar un mensaje al usuario
        Toast.makeText(this, "Sesión Cerrada :)", Toast.LENGTH_LONG).show();
        // 3. Ir al Activity del Login
        Intent intent = new Intent(this, LoginActivity.class);
        // 3.1 agregamos unas FLAGS para limpiar la pila de actividades y que el usuario no pueda volver
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        // 3.2 Se llama a la actividad
        startActivity(intent);
        // 3.3 Se cierra la actividad actual
        finish();
    }
}