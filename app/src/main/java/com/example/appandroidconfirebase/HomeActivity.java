package com.example.appandroidconfirebase;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class HomeActivity extends AppCompatActivity {

    private Button btnObtener, btnCambiar, btnLogout;
    private TextView tvEstadoLed;
    private FirebaseAuth mAuth;

    private DatabaseReference mDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_home);

        btnObtener = findViewById(R.id.btnObtener);
        btnCambiar = findViewById(R.id.btnCambiar);
        btnLogout = findViewById(R.id.btnLogout);

        tvEstadoLed = findViewById(R.id.tvEstadoLed);

        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference();

        btnLogout.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                logoutUser();
            }
        });

        btnObtener.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                obtenerDataRTDB("estado_led");
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

    private void obtenerDataRTDB(String clave){
        //mostrarToast("Obteniendo data de RTDB...");
        // obtener data de firebase realtime database
        mDatabase.child(clave).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String mensaje = "";
                if (snapshot.exists()) {
                    Object value = snapshot.getValue();
                    String data = String.valueOf(value);
                    if (Boolean.parseBoolean(data)){
                        mensaje = "ON";
                    }else{
                        mensaje = "OFF";
                    }
                    tvEstadoLed.setText(mensaje);
                } else {
                    mostrarToast("No se encontró el dato para la clave: " + snapshot.getKey());
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                mostrarToast("Error al obtener data de RTDB: " + error.getMessage());
            }
        });

    }

    private void mostrarToast(String mensaje){
        Toast.makeText(this, mensaje, Toast.LENGTH_LONG).show();
    }
}
