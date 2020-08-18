package com.guilhermedaros.museunfc.Acitivities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.guilhermedaros.museunfc.R;

public class EscolhaUsuarioActivity extends AppCompatActivity {

    private Button btn_visitante;
    private Button btn_administrador;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_escolha_usuario);
        btn_visitante = findViewById(R.id.btn_visitante);
        btn_administrador= findViewById(R.id.btn_administrador);
        btn_visitante.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences lSharedPreferences = getSharedPreferences("museunfc", MODE_PRIVATE);
                SharedPreferences.Editor lEditor =  lSharedPreferences.edit();
                lEditor.putBoolean("primeiravez", false);
                lEditor.apply();
                Intent intent = new Intent(getBaseContext(), MainActivity.class);
                startActivity(intent);
                overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_left);
                finish();
            }
        });

        btn_administrador.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences lSharedPreferences = getSharedPreferences("museunfc", MODE_PRIVATE);
                SharedPreferences.Editor lEditor =  lSharedPreferences.edit();
                lEditor.putBoolean("primeiravez", false);
                lEditor.apply();
                Intent intent = new Intent(getBaseContext(), LoginActivity.class);
                startActivity(intent);
                overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_left);
                finish();
            }
        });



    }
}
