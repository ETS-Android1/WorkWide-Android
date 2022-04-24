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

import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;

public class delete_profile extends AppCompatActivity {

    EditText pass, confirmPass;
    Button eliminar;
    AsyncHttpClient client;
    RequestParams params;
    SharedPreferences sesion;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_delete_profile);

        pass = findViewById(R.id.pass);
        confirmPass = findViewById(R.id.confirmPass);

        eliminar = findViewById(R.id.eliminarBtn);

        sesion = this.getSharedPreferences("SESION", MODE_PRIVATE);
        int id = sesion.getInt("id", 0);
        if (id == 0){
            Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
            startActivity(intent);
        }
        eliminar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String passCad = pass.getText().toString();
                String confirmPassCad = pass.getText().toString();

                if(passCad.length() < 4 || passCad.length() > 20){
                    Toast.makeText(getApplicationContext(), "Compruebe la lonmgitud de la contraseña" , Toast.LENGTH_SHORT).show();
                }
                else{
                    if(!passCad.equals(confirmPassCad)){
                        Toast.makeText(getApplicationContext(), "Las contraseñas no coinciden", Toast.LENGTH_SHORT).show();
                    }
                    else{
                        client = new AsyncHttpClient();
                        params = new RequestParams();

                        params.put("id", id);
                        params.put("contra", passCad);
                        client.post("http://192.168.0.11:8080/WorkWide/eliminarAndroid", params, new JsonHttpResponseHandler(){
                            @Override
                            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                                super.onSuccess(statusCode, headers, response);
                                try{
                                    String resp = response.getString("estado");
                                    if(resp.equals("yes")){
                                        SharedPreferences.Editor editor = sesion.edit();
                                        editor.clear();
                                        editor.apply();

                                        Toast.makeText(getApplicationContext(), "Cuenta eliminada", Toast.LENGTH_SHORT).show();
                                        Intent intent = new Intent(getApplicationContext(), IndexActivity.class);
                                        startActivity(intent);
                                    }
                                    else{
                                        Toast.makeText(getApplicationContext(), "La contraseña es incorrecta", Toast.LENGTH_SHORT).show();
                                    }
                                }
                                catch (Exception e){
                                    e.printStackTrace();
                                }
                            }
                        });
                    }
                }
            }
        });
    }
}