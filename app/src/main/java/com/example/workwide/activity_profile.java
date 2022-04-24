package com.example.workwide;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.squareup.picasso.Picasso;

import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;

public class activity_profile extends AppCompatActivity {

    private ImageView perfil, portada;
    private TextView countSoli,countTrab, soli, trab, nombreTitulo, trabajoTitulo;
    private EditText nombre, correo, telefono, estado;
    private Button eliminar;
    private AsyncHttpClient client;
    private RequestParams params;
    private SharedPreferences sesion;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        perfil = findViewById(R.id.perfil);
        portada = findViewById(R.id.portada);

        countSoli = findViewById(R.id.numSoli);
        countTrab = findViewById(R.id.numTrab);
        soli = findViewById(R.id.solicitud);
        trab = findViewById(R.id.traba);

        nombreTitulo = findViewById(R.id.name);
        trabajoTitulo = findViewById(R.id.work);
        nombre = findViewById(R.id.nameProfile);
        correo = findViewById(R.id.emailProfile);
        telefono = findViewById(R.id.phoneProfile);
        estado = findViewById(R.id.locationProfile);

        eliminar = findViewById(R.id.delete_acc);
        eliminar.setEnabled(false);

        sesion = this.getSharedPreferences("SESION", MODE_PRIVATE);
        int id = sesion.getInt("id", 0);
        int tipo = sesion.getInt("tipo", 0);

        String urlPerfil = "http://192.168.0.11:8080/WorkWide/perfilAndroid?id=" + id;
        String urlPortada = "http://192.168.0.11:8080/WorkWide/portadaAndroid?id=" + id;

        params = new RequestParams();
        params.put("tipo", tipo);
        params.put("id", id);

        if(id == 0){
            Intent intent = new Intent(this, LoginActivity.class);
            startActivity(intent);
        }

        client = new AsyncHttpClient();
        client.post("http://192.168.0.11:8080/WorkWide/contadoresAndroid", params, new JsonHttpResponseHandler(){
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);
                try{
                    countSoli.setText(String.valueOf(response.getInt("numSoli")));
                    soli.setText("Solicitudes");
                    countTrab.setText(String.valueOf(response.getInt("numTrab")));
                    trab.setText("Trabajos");
                    nombre.setText(response.getString("nombre"));
                    correo.setText(response.getString("correo"));
                    telefono.setText(response.getString("telefono"));
                    trabajoTitulo.setText(response.getString("trabajo"));
                    nombreTitulo.setText(sesion.getString("nombre", ""));
                    estado.setText(response.getString("region"));
                    Picasso.get().load(urlPerfil).error(R.drawable.user).into(perfil);
                    Picasso.get().load(urlPortada).error(R.drawable.user_banner).into(portada);
                    eliminar.setEnabled(true);
                }
                catch (Exception e){
                    e.printStackTrace();
                    Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                    startActivity(intent);
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                super.onFailure(statusCode, headers, responseString, throwable);
                Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                startActivity(intent);
            }
        });

        eliminar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), delete_profile.class);
                startActivity(intent);
            }
        });
    }
}