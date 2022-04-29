package com.example.workwide;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONException;
import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;

public class recovery_2 extends AppCompatActivity implements View.OnClickListener {

    private EditText c1, c2, c3, c4, c5, c6, c7, c8;
    private Button verificar;
    private TextView recovery;
    private String codigo = "";
    private SharedPreferences sesion;
    private AsyncHttpClient cliente;
    private RequestParams parametros;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recovery2);

        c1 = findViewById(R.id.code1);
        c2 = findViewById(R.id.code2);
        c3 = findViewById(R.id.code3);
        c4 = findViewById(R.id.code4);
        c5 = findViewById(R.id.code5);
        c6 = findViewById(R.id.code6);
        c7 = findViewById(R.id.code7);
        c8 = findViewById(R.id.code8);

        recovery = findViewById(R.id.text_recovery_again);

        recovery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), recovery_1.class);
                startActivity(intent);

                finish();
            }
        });

        c1.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View view, int i, KeyEvent keyEvent) {
                if(c1.getText().toString().length() != 0){
                    c2.requestFocus();
                }
                else{
                    if(c1.getText().toString().length() > 1){
                        c1.setText(c1.getText().toString().substring(0, 1));
                    }
                }
                return false;
            }
        });

        c2.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View view, int i, KeyEvent keyEvent) {
                if(c2.getText().toString().length() != 0){
                    c3.requestFocus();
                }
                else{
                    if(c2.getText().toString().length() > 1){
                        c2.setText(c2.getText().toString().substring(0, 1));
                    }
                }
                return false;
            }
        });

        c3.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View view, int i, KeyEvent keyEvent) {
                if(c3.getText().toString().length() != 0){
                    c4.requestFocus();
                }
                else{
                    if(c3.getText().toString().length() > 1){
                        c3.setText(c3.getText().toString().substring(0, 1));
                    }
                }
                return false;
            }
        });

        c4.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View view, int i, KeyEvent keyEvent) {
                if(c4.getText().toString().length() != 0){
                    c5.requestFocus();
                }
                else{
                    if(c4.getText().toString().length() > 1){
                        c4.setText(c4.getText().toString().substring(0, 1));
                    }
                }

                return false;
            }
        });

        c5.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View view, int i, KeyEvent keyEvent) {
                if(c5.getText().toString().length() != 0){
                    c6.requestFocus();
                }
                else{
                    if(c5.getText().toString().length() > 1){
                        c5.setText(c5.getText().toString().substring(0, 1));
                    }
                }

                return false;
            }
        });

        c6.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View view, int i, KeyEvent keyEvent) {
                if(c6.getText().toString().length() != 0){
                    c7.requestFocus();
                }
                else{
                    if(c6.getText().toString().length() > 1){
                        c6.setText(c6.getText().toString().substring(0, 1));
                    }
                }
                return false;
            }
        });

        c7.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View view, int i, KeyEvent keyEvent) {
                if(c7.getText().toString().length() != 0){
                    c8.requestFocus();
                }
                else{
                    if(c7.getText().toString().length() > 1){
                        c7.setText(c7.getText().toString().substring(0, 1));
                    }
                }
                return false;
            }
        });

        verificar = findViewById(R.id.verificar);
        verificar.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        verificar.setEnabled(false);
        if(c1.getText().toString().equals("") || c2.getText().toString().equals("") || c3.getText().toString().equals("") || c4.getText().toString().equals("") ||
        c5.getText().toString().equals("") || c6.getText().toString().equals("") || c7.getText().toString().equals("") || c8.getText().toString().equals("")){
            Toast.makeText(this, "Rellena el código correctamente", Toast.LENGTH_SHORT).show();
            verificar.setEnabled(true);
        }
        else{
            codigo = c1.getText().toString() + c2.getText().toString() + c3.getText().toString() + c4.getText().toString() + c5.getText().toString() +
                    c6.getText().toString() + c7.getText().toString() + c8.getText().toString();

            sesion = this.getSharedPreferences("SESION", MODE_PRIVATE);
            int id = sesion.getInt("id_prov", 0);
            if(id == 0){
                Toast.makeText(this, "Hubo un error al cargar la información, vuelva a solicitar el código", Toast.LENGTH_SHORT).show();
                verificar.setEnabled(true);
            }
            else{
                parametros = new RequestParams();
                parametros.put("codigo", codigo);
                parametros.put("id", id);

                cliente = new AsyncHttpClient();
                cliente.post("http://192.168.0.11:8080/WorkWide/verificarCodigoAndroid", parametros, new JsonHttpResponseHandler(){
                    @Override
                    public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                        super.onSuccess(statusCode, headers, response);

                        try {
                            String resp = response.getString("bandera");
                            if(resp.equals("true")){
                                Toast.makeText(getApplicationContext(), "Código correcto", Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(getApplicationContext(), recovery_3.class);
                                startActivity(intent);

                                finish();
                            }
                            else{
                                if(resp.equals("false")){
                                    Toast.makeText(getApplicationContext(), "Código expirado, genera uno nuevo", Toast.LENGTH_SHORT).show();
                                }
                                else{
                                    if(resp.equals("undefined")){
                                        Toast.makeText(getApplicationContext(), "Código erróneo, verifique la informació o genere uno nuevo", Toast.LENGTH_SHORT).show();
                                    }
                                    else{
                                        if(resp.equals("error")){
                                            Toast.makeText(getApplicationContext(), "Sucedió un error interno del servidor...", Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                }
                            }
                            verificar.setEnabled(true);
                        }
                        catch (Exception e) {
                            e.printStackTrace();
                            Toast.makeText(getApplicationContext(), "Sucedió un error", Toast.LENGTH_SHORT).show();
                            verificar.setEnabled(true);
                        }
                    }
                });
            }
        }
    }
}