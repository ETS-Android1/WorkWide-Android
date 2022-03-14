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

import com.example.workwide.controlador.WebServices;
import com.example.workwide.modelo.Trabajador;
import com.example.workwide.modelo.Usuario;
import com.loopj.android.http.*;
import com.example.workwide.modelo.Validacion;

import org.json.JSONException;
import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;

public class LoginTabFragment extends Fragment implements View.OnClickListener {

    EditText email, pass;
    TextView text;
    Button button;
    String correo, contrasena;
    SharedPreferences sesion;
    WebServices SERVICES = new WebServices();

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
                Toast.makeText(getContext(), "Escribe un correo válido", Toast.LENGTH_SHORT).show();
            }
            else{
                if(validar.validarCorreo(email.getText().toString())){
                    correo = email.getText().toString();
                    if(pass.getText().toString().equals("") || pass.getText().toString().length() < 4){
                        Toast.makeText(getContext(), "Escribe una contraseña válida", Toast.LENGTH_SHORT).show();
                    }
                    else{
                        contrasena = pass.getText().toString();
                        Trabajador user = SERVICES.iniciarSesion(correo, contrasena);
                        while(user.getNombre().equals("Waiting...")){
                            System.out.println("waiting...");
                        }
                        if(user.getIdUsu() != 0){
                            System.out.println(user.getNombre() + " " + user.getApellido());
                        }
                        else{
                            Toast.makeText(getContext(), "Los datos no coinciden", Toast.LENGTH_SHORT).show();
                        }
                    }
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
