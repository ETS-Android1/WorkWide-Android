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
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.workwide.modelo.Validacion;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import cz.msebera.android.httpclient.Header;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class edit_trab extends AppCompatActivity implements View.OnClickListener {

    private Spinner region;
    private EditText nombre, apellido, telefono, contran, contrav, descripcion;
    private Button portadac, perfilc, cambiar;
    private String nombreCad, apellidoCad, telefonoCad, contraCad, contravCad, descripcionCad, regionCad;
    private int regionPos;
    private File perfil, portada;
    SharedPreferences sesion;
    RequestParams params;
    AsyncHttpClient client;
    Validacion validar = new Validacion();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_trab);

        nombre = findViewById(R.id.nombre_edit);
        apellido = findViewById(R.id.apellido_edit);
        telefono = findViewById(R.id.telefono_edit);
        contran = findViewById(R.id.contra_edit);
        contrav = findViewById(R.id.contra_old);
        descripcion = findViewById(R.id.desc_edit);

        nombre.setEnabled(false);
        apellido.setEnabled(false);
        telefono.setEnabled(false);
        contran.setEnabled(false);
        contrav.setEnabled(false);
        descripcion.setEnabled(false);

        region = findViewById(R.id.estado_edit);
        region.setEnabled(false);
        ArrayAdapter<String> regionAdptr = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_spinner_item, listarRegiones());
        region.setAdapter(regionAdptr);
        region.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                regionPos = region.getSelectedItemPosition();
                regionCad = region.getSelectedItem().toString();

                System.out.println(regionPos + " " + regionCad);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        portadac = findViewById(R.id.portada_edit);
        perfilc = findViewById(R.id.perfil_edit);
        cambiar = findViewById(R.id.cambiar);

        portadac.setEnabled(false);
        perfilc.setEnabled(false);
        cambiar.setEnabled(false);

        cambiar.setOnClickListener(this);

        perfilc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intento = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                intento.setType("image/");
                startActivityForResult(intento.createChooser(intento, "Seleccione una aplicación"), 1);
            }
        });

        portadac.setOnClickListener(new View.OnClickListener() {
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

        client = new AsyncHttpClient();
        client.post("http://192.168.0.11:8080/WorkWide/listarPerfilAndroid", params, new JsonHttpResponseHandler(){
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);
                try{
                    String nombreResp = response.getString("nombre");
                    String apellidoResp= response.getString("apellido");
                    String telefonoResp = response.getString("telefono");
                    String regionResp = response.getString("region");
                    String descResp = response.getString("descripcion");


                    nombre.setText(nombreResp);
                    apellido.setText(apellidoResp);
                    telefono.setText(telefonoResp);
                    region.setSelection(getPos(regionResp));
                    descripcion.setText(descResp.replace("<br>", "\n"));

                    cambiar.setEnabled(true);
                    nombre.setEnabled(true);
                    apellido.setEnabled(true);
                    telefono.setEnabled(true);
                    contran.setEnabled(true);
                    contrav.setEnabled(true);
                    descripcion.setEnabled(true);

                    region.setEnabled(true);

                    portadac.setEnabled(true);
                    perfilc.setEnabled(true);
                    cambiar.setEnabled(true);
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
                perfilc.setText("Seleccionada");
            }
            else{
                if(requestCode == 2){
                    portada = new File(getPath(miPath));
                    System.out.println(miPath.getPath() + ": " + portada.getAbsolutePath());
                    portadac.setText("Seleccionada");
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

    private List<String> listarRegiones(){
        List<String> lista = new ArrayList<>();
        lista.add("Estados");
        lista.add("Aguascalientes");
        lista.add("Baja California");
        lista.add("Baja California Sur");
        lista.add("Campeche");
        lista.add("Chiapas");
        lista.add("Chihuahua");
        lista.add("Ciudad de México");
        lista.add("Coahuila");
        lista.add("Colima");
        lista.add("Durango");
        lista.add("Estado de México");
        lista.add("Guanajuato");
        lista.add("Guerrero");
        lista.add("Hidalgo");
        lista.add("Jalisco");
        lista.add("Michoacán");
        lista.add("Morelos");
        lista.add("Nayarit");
        lista.add("Nuevo León");
        lista.add("Oaxacq");
        lista.add("Puebla");
        lista.add("Querétaro");
        lista.add("Quintana Roo");
        lista.add("San Luis Potosí");
        lista.add("Sinaloa");
        lista.add("Sonora");
        lista.add("Tabasco");
        lista.add("Tamaulipas");
        lista.add("Tlaxcala");
        lista.add("Veracruz");
        lista.add("Yucatán");
        lista.add("Zacatecas");

        return lista;
    }

    private int getPos(String estado){
        List<String> list = listarRegiones();
        int pos=0;
        int size = list.size();
        for(int i=0; i<size; i++){
            if(estado.equals(list.get(i))){
                pos = i;
                break;
            }
        }
        return pos;
    }

    @Override
    public void onClick(View view) {
        if(view.getId() == cambiar.getId()){
            cambiar.setEnabled(false);

            nombreCad = nombre.getText().toString();
            apellidoCad = apellido.getText().toString();
            telefonoCad = telefono.getText().toString();
            contraCad = contran.getText().toString();
            contravCad = contrav.getText().toString();
            descripcionCad = descripcion.getText().toString();
            regionPos = region.getSelectedItemPosition();

            if(validar.validarNombre(nombreCad) && validar.validarNombre(apellidoCad)){
                if(telefono.length() == 0 || validar.validarTelefono(telefonoCad)){
                    if((contran.length() > 4 && contran.length() < 20) && (contrav.length() > 4 && contrav.length() < 20)){
                        if(regionPos != 0){
                            sesion = this.getSharedPreferences("SESION", MODE_PRIVATE);
                            int id = sesion.getInt("id", 0);
                            int tipo = sesion.getInt("tipo", 0);

                            MultipartBody.Builder buildParams = new MultipartBody.Builder().setType(MultipartBody.FORM)
                                    .addFormDataPart("nombren", nombreCad)
                                    .addFormDataPart("apellidon", apellidoCad)
                                    .addFormDataPart("telefonon", telefonoCad)
                                    .addFormDataPart("contranuevan", contraCad)
                                    .addFormDataPart("contraviejan", contravCad)
                                    .addFormDataPart("descripcionn", descripcionCad)
                                    .addFormDataPart("ubicationn", String.valueOf(regionPos))
                                    .addFormDataPart("id", String.valueOf(id))
                                    .addFormDataPart("tipo", String.valueOf(tipo));
                            if(portada != null){
                                buildParams.addFormDataPart("bannern", portada.getName(), RequestBody.create(portada, MediaType.parse("image/*")));
                            }

                            if(perfil != null){
                                buildParams.addFormDataPart("perfiln", perfil.getName(), RequestBody.create(perfil, MediaType.parse("image/*")));
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

                            cambiar.setEnabled(true);
                        }
                        else{
                            Toast.makeText(this, "Escoge un estado válido", Toast.LENGTH_SHORT).show();
                        }
                    }
                    else{
                        Toast.makeText(this, "Escribe las contraseñas correctas", Toast.LENGTH_SHORT).show();
                    }
                }
                else{
                    Toast.makeText(this, "Escribe un teléfono válido", Toast.LENGTH_SHORT).show();
                }
            }
            else{
                Toast.makeText(this, "Escribe un nombre en el formato correcto", Toast.LENGTH_SHORT).show();
            }
        }
    }
}