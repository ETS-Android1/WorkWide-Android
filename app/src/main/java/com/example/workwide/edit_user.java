package com.example.workwide;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.workwide.modelo.Validacion;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.File;
import java.io.IOException;

import cz.msebera.android.httpclient.Header;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class edit_user extends AppCompatActivity implements View.OnClickListener {

    EditText nombre, apellido, telefono, contra, contraOld;
    File perfil, portada;
    Button enviar, perfilC, portadaC;
    SharedPreferences sesion;
    RequestParams params;
    AsyncHttpClient client;
    Validacion validar;

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
        enviar.setOnClickListener(this);

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
                startActivityForResult(intento.createChooser(intento, "Seleccione una aplicación"), 2);
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

    @Override
    public void onClick(View view) {
        if(view.getId() == enviar.getId()){
            enviar.setEnabled(false);

            String nombreCad = nombre.getText().toString();
            String apelldioCad = apellido.getText().toString();
            String telefonoCad = telefono.getText().toString();
            String contraCad = contra.getText().toString();
            String contraOldCad = contraOld.getText().toString();

            validar = new Validacion();

            if((contraCad.length() > 4 && contraCad.length() < 20) && (contraOldCad.length() > 4 && contraOldCad.length() <20)){
                if(validar.validarNombre(nombreCad) && validar.validarNombre(apelldioCad)){
                    if(telefonoCad.length() == 0 || validar.validarTelefono(telefonoCad)){
                        sesion = this.getSharedPreferences("SESION", MODE_PRIVATE);
                        int id = sesion.getInt("id", 0);
                        int tipo = sesion.getInt("tipo", 0);

                        MultipartBody.Builder buildParams = new MultipartBody.Builder().setType(MultipartBody.FORM)
                                .addFormDataPart("nombre", nombreCad)
                                .addFormDataPart("apellido", apelldioCad)
                                .addFormDataPart("telefono", telefonoCad)
                                .addFormDataPart("contranueva", contraCad)
                                .addFormDataPart("contravieja", contraOldCad)
                                .addFormDataPart("id", String.valueOf(id))
                                .addFormDataPart("tipo", String.valueOf(tipo));
                        if(portada != null){
                            buildParams.addFormDataPart("portada", portada.getName(), RequestBody.create(portada, MediaType.parse("image/*")));
                        }

                        if(perfil != null){
                            buildParams.addFormDataPart("perfil", perfil.getName(), RequestBody.create(perfil, MediaType.parse("image/*")));
                        }

                        MultipartBody params = buildParams.build();
                        Request request = new Request.Builder().url("http://192.168.0.11:8080/WorkWide/cambiarAndroid").post(params).build();

                        OkHttpClient cliente = new OkHttpClient();
                        cliente.newCall(request).enqueue(new Callback() {
                            @Override
                            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                                Toast.makeText(getApplicationContext(), "Sucedió un error al conectar con el servidor.", Toast.LENGTH_SHORT).show();
                            }

                            @Override
                            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                                try {
                                    String str = response.body().string();
                                    System.out.println(str);
                                    org.json.simple.JSONObject obj = (org.json.simple.JSONObject) new JSONParser().parse(str);
                                    String state = obj.get("state").toString();
                                    System.out.println(state);
                                    if(state.equals("true")){
                                        new Handler(Looper.getMainLooper()).post(new Runnable() {
                                            @Override
                                            public void run() {
                                                Toast.makeText(getApplicationContext(), "Los cambios se hicieron correctamente", Toast.LENGTH_SHORT).show();
                                                Intent intent = new Intent(getApplicationContext(), activity_profileIndex.class);
                                                startActivity(intent);

                                                finish();
                                            }
                                        });
                                    }
                                    else{
                                        if(state.equals("false")){
                                            new Handler(Looper.getMainLooper()).post(new Runnable() {
                                                @Override
                                                public void run() {
                                                    Toast.makeText(getApplicationContext(), "La contraseña no coincide", Toast.LENGTH_SHORT).show();
                                                }
                                            });
                                        }
                                        else{
                                            if(state.equals("error")){
                                                new Handler(Looper.getMainLooper()).post(new Runnable() {
                                                    @Override
                                                    public void run() {
                                                        Toast.makeText(getApplicationContext(), "Existió un error interno del servidor.", Toast.LENGTH_SHORT).show();
                                                    }
                                                });
                                            }
                                        }
                                    }
                                } catch (ParseException e) {
                                    e.printStackTrace();
                                }
                            }
                        });

                        enviar.setEnabled(true);
                    }
                    else{
                        Toast.makeText(getApplicationContext(), "El télefono es incorrecto", Toast.LENGTH_SHORT).show();
                    }
                }
                else{
                    Toast.makeText(this, "El nombre y/o apellido tienen un formato incorrecto.", Toast.LENGTH_SHORT).show();
                }
            }
        }
        else{
            Toast.makeText(getApplicationContext(), "Ambas contraseñas deben de rellenarse", Toast.LENGTH_SHORT).show();
        }
    }
}