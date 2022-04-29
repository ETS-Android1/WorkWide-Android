package com.example.workwide;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONException;
import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;

public class recovery_3 extends AppCompatActivity implements View.OnClickListener {
    private EditText contra, contraVer;
    private Button cambiar;
    private String contraCad;
    private SharedPreferences sesion;
    private RequestParams params;
    private AsyncHttpClient client;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recovery3);

        contra = findViewById(R.id.pass_recovery);
        contraVer = findViewById(R.id.cpass_recovery);

        cambiar = findViewById(R.id.cambiar_pass);

        cambiar.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        cambiar.setEnabled(false);
        if(contra.getText().toString().equals(contraVer.getText().toString())){
            contraCad = contra.getText().toString();
            sesion = this.getSharedPreferences("SESION", MODE_PRIVATE);

            int id = sesion.getInt("id_prov", 0);
            if(id == 0){
                Toast.makeText(this, "Surgió un error al cargar la información, solicite de nuevo un código", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(this, recovery_1.class);
            }
            else{
                params = new RequestParams();
                params.put("id", id);
                params.put("contra", contraCad);

                client = new AsyncHttpClient();
                client.post("http://192.168.0.11:8080/WorkWide/cambiarContraAndroid", params, new JsonHttpResponseHandler(){
                    @Override
                    public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                        super.onSuccess(statusCode, headers, response);

                        try {
                            String estado = response.getString("bandera");
                            if(estado.equals("true")){
                                Toast.makeText(getApplicationContext(), "Contraseña cambiada con éxito", Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                                startActivity(intent);

                                finish();

                                cambiar.setEnabled(true);
                            }
                            else{
                                if(estado.equals("false")){
                                    Toast.makeText(getApplicationContext(), "Sucedió un error al intentar cambiar la contraseña, inténtelo de nuevo", Toast.LENGTH_SHORT).show();
                                    cambiar.setEnabled(true);
                                }
                            }
                        }
                        catch (Exception e) {
                            e.printStackTrace();
                            cambiar.setEnabled(true);
                        }
                    }
                });
            }
        }
        else{
            Toast.makeText(getApplicationContext(), "Las contraseñas no coinciden", Toast.LENGTH_SHORT).show();
        }
    }
}