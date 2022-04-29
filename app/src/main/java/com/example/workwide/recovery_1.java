package com.example.workwide;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.workwide.modelo.Validacion;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONException;
import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;

public class recovery_1 extends AppCompatActivity implements View.OnClickListener {

    private EditText email;
    private Button enviar;
    private String emailCad;
    private Validacion validar;
    private SharedPreferences sesion;
    private AsyncHttpClient cliente;
    private RequestParams parametros;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recovery1);

        email = findViewById(R.id.email_recovery);
        enviar = findViewById(R.id.enviarEmail);

        enviar.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        emailCad = email.getText().toString();
        enviar.setEnabled(false);
        validar = new Validacion();

        if(validar.validarCorreo(emailCad)){
            //Ponemos el correo en los parametros
            parametros = new RequestParams();
            parametros.put("correo", emailCad);

            //Conectamos al web service
            cliente = new AsyncHttpClient();
            cliente.setTimeout(30000);
            cliente.post("http://192.168.0.11:8080/WorkWide/enviarEmailAndroid", parametros, new JsonHttpResponseHandler(){
                @Override
                public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                    super.onSuccess(statusCode, headers, response);
                    try {
                        String estado = response.getString("bandera");
                        if(estado.equals("undefined")){
                            Toast.makeText(getApplicationContext(), "El correo no existe", Toast.LENGTH_SHORT).show();
                            enviar.setEnabled(true);
                        }
                        else{
                            if(estado.equals("true")){
                                //Guardamos el id en la sesion
                                sesion = getApplicationContext().getSharedPreferences("SESION", MODE_PRIVATE);
                                SharedPreferences.Editor editor = sesion.edit();
                                editor.putString("correo", emailCad);
                                editor.putInt("id_prov", response.getInt("id"));
                                editor.apply();

                                //Mostramos mensaje de éxito
                                Toast.makeText(getApplicationContext(), "Revisa tu bandeja de entrada", Toast.LENGTH_SHORT).show();

                                //Cambiamos de activity
                                Intent intent = new Intent(getApplicationContext(), recovery_2.class);
                                startActivity(intent);

                                finish();

                                enviar.setEnabled(true);
                            }
                            else{
                                if(estado.equals("false")){
                                    enviar.setEnabled(true);
                                    Toast.makeText(getApplicationContext(), "Sucedió un error al enviar el correo...", Toast.LENGTH_SHORT).show();
                                }
                            }
                        }
                    }
                    catch (Exception e) {
                        e.printStackTrace();
                        enviar.setEnabled(true);
                    }
                }
            });
        }
        else{
            Toast.makeText(getApplicationContext(), "Inserte un correo válido", Toast.LENGTH_SHORT).show();
            enviar.setEnabled(true);
        }


    }
}