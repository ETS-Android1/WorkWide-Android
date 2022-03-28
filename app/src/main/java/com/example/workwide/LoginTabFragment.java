package com.example.workwide;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.workwide.modelo.Trabajador;
import com.loopj.android.http.*;
import com.example.workwide.modelo.Validacion;

import org.json.JSONException;
import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;

public class LoginTabFragment extends Fragment implements View.OnClickListener {

    private EditText email, pass;
    private TextView text;
    private Button button;
    private String correo, contrasena;
    private SharedPreferences sesion;
    private AsyncHttpClient cliente;
    private RequestParams parametros;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        ViewGroup root = (ViewGroup) inflater.inflate(R.layout.login_tab_fragment, container, false);

        email = root.findViewById(R.id.email);
        pass = root.findViewById(R.id.pass);
        text = root.findViewById(R.id.text);
        button = root.findViewById(R.id.login);

        button.setOnClickListener(this);

        email.setTranslationX(800);
        pass.setTranslationX(800);
        text.setTranslationX(800);
        button.setTranslationX(800);

        email.setAlpha(0);
        pass.setAlpha(0);
        text.setAlpha(0);
        button.setAlpha(0);

        email.animate().translationX(0).alpha(1).setDuration(800).setStartDelay(300).start();
        pass.animate().translationX(0).alpha(1).setDuration(800).setStartDelay(500).start();
        text.animate().translationX(0).alpha(1).setDuration(800).setStartDelay(500).start();
        button.animate().translationX(0).alpha(1).setDuration(800).setStartDelay(700).start();

        return root;
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        if(id == button.getId()){
            Validacion validar = new Validacion();
            if(email.getText().toString().equals("") || email.getText().toString() == null){
                Toast.makeText(getContext(), "El correo no puede estar vacío", Toast.LENGTH_SHORT).show();
            }
            else{
                if(validar.validarCorreo(email.getText().toString())){
                    correo = email.getText().toString();
                    if(pass.getText().toString().equals("") || pass.getText().toString().length() < 4){
                        Toast.makeText(getContext(), "Escribe una contraseña válida", Toast.LENGTH_SHORT).show();
                    }
                    else{
                        contrasena = pass.getText().toString();
                        parametros = new RequestParams();
                        Trabajador user = new Trabajador();
                        //Colocamos los parametros a enviar
                        parametros.put("correo", correo);
                        parametros.put("contra", contrasena);

                        //Inicializamos el cliente
                        cliente = new AsyncHttpClient();

                        //Mandamos la inforamción con post al web service establecido
                        cliente.post("http://192.168.0.11:8080/WorkWide/iniciarAndroid", parametros, new JsonHttpResponseHandler(){
                            @Override
                            //Método que se ejecuta si se realizó con éxito
                            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                                //Obtenemos la información del objeto JSON retornado
                                try {
                                    if(response.getInt("id") != 0){
                                        //Guardamos la sesión del usuario
                                        sesion = getContext().getSharedPreferences("SESION", Context.MODE_PRIVATE);
                                        SharedPreferences.Editor editor = sesion.edit();
                                        editor.putString("nombre", response.getString("nombre"));
                                        editor.putString("apellido",response.getString("apellido"));
                                        editor.putString("correo", correo);
                                        editor.putInt("tipo", response.getInt("tipo"));
                                        editor.putInt("id", response.getInt("id"));
                                        //Comrpobamos el tipo de usuario
                                        if(response.getInt("tipo") == 2) {
                                            if (response.getString("region").equals("")) {
                                                editor.putString("region", "NA");
                                                editor.putString("descripcion", "NA");
                                                editor.putString("trabajo", "NA");
                                                Toast.makeText(getContext(), "Completa el registro", Toast.LENGTH_SHORT).show();
                                                Intent intent = new Intent(getContext(), activity_compReg.class);
                                                startActivity(intent);
                                            } else {
                                                editor.putString("region", response.getString("region"));
                                                editor.putString("descripcion", response.getString("descripcion"));
                                                editor.putString("trabajo", response.getString("trabajo"));
                                            }
                                        }
                                        editor.apply();
                                        Toast.makeText(getContext(), "SESIÓN CREADA, HOLA " + response.getString("nombre"), Toast.LENGTH_SHORT).show();
                                    }
                                    else{
                                        user.setIdUsu(0);
                                        user.setNombre("NA");
                                        Toast.makeText(getContext(), "LAS CREDENCIALES NO COINCIDEN", Toast.LENGTH_SHORT).show();
                                    }
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                        });
                    }
                }
                else{
                    Toast.makeText(getContext(), "Escribe un correo electrónico válido", Toast.LENGTH_SHORT).show();
                }
            }
        }
    }

    //Método para iniciar sesión
    public String iniciarSesion(String email, String contra){
        //String que nos dirá el resultado de nuestro inicio de sesión
        String response = "";



        return response;
    }
}
