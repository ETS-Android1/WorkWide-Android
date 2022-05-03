package com.example.workwide;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

public class activity_profileIndex extends AppCompatActivity {

    private SharedPreferences sesion;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_index);

        ImageView user_pic, user_banner, profile_icon, request_icon, work_icon, logout_icon, config_icon;
        TextView user_name, work, profile_text, request_text, work_text, logout_text, config_text;

        user_pic = findViewById(R.id.user_pic);
        user_banner = findViewById(R.id.user_banner);
        profile_icon = findViewById(R.id.profile_icon);
        request_icon = findViewById(R.id.request_icon);
        work_icon = findViewById(R.id.work_icon);
        logout_icon = findViewById(R.id.logout_icon);
        config_icon = findViewById(R.id.config_icon);

        user_name = findViewById(R.id.user_name);
        work = findViewById(R.id.work);
        profile_text = findViewById(R.id.profile_text);
        request_text = findViewById(R.id.request_text);
        work_text = findViewById(R.id.work_text);
        logout_text = findViewById(R.id.logout_text);
        config_text = findViewById(R.id.config_text);

        //Datos de la sesión
        sesion = this.getSharedPreferences("SESION", MODE_PRIVATE);
        int id = sesion.getInt("id", 0);

        int tipo = sesion.getInt("tipo", 0);
        String urlPerfil = "http://192.168.0.11:8080/WorkWide/perfilAndroid?id=" + id;
        String urlPortada = "http://192.168.0.11:8080/WorkWide/portadaAndroid?id=" + id;
        if(id != 0){
            user_name.setText(sesion.getString("nombre", "") + " " + sesion.getString("apellido", ""));
            work.setText(sesion.getString("trabajo", "Empleador"));
        }
        else{
            Intent intent = new Intent(this, LoginActivity.class);
            startActivity(intent);
        }

        //Carga de fotos
        Picasso.get().load(urlPerfil).error(R.drawable.user).into(user_pic);
        Picasso.get().load(urlPortada).error(R.drawable.user_banner).into(user_banner);



        //Cerrar sesión
        logout_icon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cerrarSesion();
            }
        });

        logout_text.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cerrarSesion();
            }
        });

        //Ver perfil
        profile_icon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), activity_profile.class);
                startActivity(intent);
            }
        });

        profile_text.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), activity_profile.class);
                startActivity(intent);
            }
        });

        //Configurar perfil
        config_icon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(tipo == 1){
                    Intent intent = new Intent(getApplicationContext(), edit_user.class);
                    startActivity(intent);
                }
                else{
                    Intent intent = new Intent(getApplicationContext(), edit_trab.class);
                    startActivity(intent);
                }
            }
        });

        config_text.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(tipo == 1){
                    Intent intent = new Intent(getApplicationContext(), edit_user.class);
                    startActivity(intent);
                }
                else{
                    Intent intent = new Intent(getApplicationContext(), edit_trab.class);
                    startActivity(intent);
                }
            }
        });
    }

    private void cerrarSesion(){
        SharedPreferences.Editor editor = sesion.edit();
        editor.clear();
        editor.apply();

        Toast.makeText(getApplicationContext(), "Sesión cerrada", Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(getApplicationContext(), IndexActivity.class);
        startActivity(intent);

        finish();
    }
}