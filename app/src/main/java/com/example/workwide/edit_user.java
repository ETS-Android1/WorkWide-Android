package com.example.workwide;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.squareup.picasso.Picasso;

import org.json.JSONObject;

import java.io.File;

import cz.msebera.android.httpclient.Header;

public class edit_user extends AppCompatActivity {

    EditText nombre, apellido, telefono, contra, contraOld;
    File perfil, portada;
    Button enviar, perfilC, portadaC;
    SharedPreferences sesion;
    RequestParams params;
    AsyncHttpClient client;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_user);

        nombre = findViewById(R.id.nombre_edit);
        apellido = findViewById(R.id.apellido_edit);
        telefono = findViewById(R.id.telefono_edit);
        contra = findViewById(R.id.contra_edit);
        contraOld = findViewById(R.id.contra_old);

        enviar = findViewById(R.id.cambiar);
        enviar.setEnabled(false);

        perfilC = (Button) findViewById(R.id.perfil_edit);
        portadaC = (Button) findViewById(R.id.portada_edit);
        perfilC.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intento = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                intento.setType("image/");
                startActivityForResult(intento.createChooser(intento, "Seleccione una aplicación"), 1);
            }
        });

        portadaC.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intento = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                intento.setType("image/");
                startActivityForResult(intento.createChooser(intento, "Seleccione una aplicación"), 1);
            }
        });

        params = new RequestParams();
        sesion = this.getSharedPreferences("SESION", MODE_PRIVATE);
        int id = sesion.getInt("id", 0);
        params.put("id", id);
        int tipo = sesion.getInt("tipo", 0);
        params.put("tipo", tipo);


        client = new AsyncHttpClient();
        client.post("http://192.168.0.11:8080/WorkWide/contadoresAndroid", params, new JsonHttpResponseHandler(){
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);
                try{
                    String[] nombreData = response.getString("nombre").split(" ");
                    nombre.setText(nombreData[0]);
                    apellido.setText(nombreData[1]);
                    telefono.setText(response.getString("telefono"));
                    enviar.setEnabled(true);
                }
                catch (Exception e){
                    e.printStackTrace();
                    Intent intent = new Intent(getApplicationContext(), activity_profileIndex.class);
                    startActivity(intent);

                    finish();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                super.onFailure(statusCode, headers, responseString, throwable);
                Intent intent = new Intent(getApplicationContext(), activity_profileIndex.class);
                startActivity(intent);

                finish();
            }
        });
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == RESULT_OK){
            Uri miPath = data.getData();
            if(requestCode == 1){
                perfil = new File(getPath(miPath));
                System.out.println(miPath.getPath() + ": " + perfil.getAbsolutePath());
                perfilC.setText("Seleccionada");
            }
            else{
                if(requestCode == 2){
                    portada = new File(getPath(miPath));
                    System.out.println(miPath.getPath() + ": " + portada.getAbsolutePath());
                    portadaC.setText("Seleccionada");
                }
            }
        }
    }

    public String getPath(Uri uri) {
        String[] projection = { MediaStore.Images.Media.DATA };
        Cursor cursor = getContentResolver().query(uri, projection, null, null, null);
        if (cursor == null) return null;
        int column_index =             cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        cursor.moveToFirst();
        String s=cursor.getString(column_index);
        cursor.close();
        System.out.println(s);
        return s;
    }
}