package com.example.workwide;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.workwide.modelo.Validacion;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.RequestParams;
import com.loopj.android.http.SyncHttpClient;

import java.util.ArrayList;
import java.util.List;

public class SignupTabFragment extends Fragment implements View.OnClickListener, AdapterView.OnItemSelectedListener {
    Spinner userType;
    List<String> tipos = new ArrayList<>();
    EditText nombre, apellido, correo, contra, telefono;
    Button registro;
    String nombreCad, apellidoCad, correoCad, contraCad, telefonoCad, tipo;
    private AsyncHttpClient cliente;
    private RequestParams parametros;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        ViewGroup root = (ViewGroup) inflater.inflate(R.layout.signup_tab_fragment, container, false);

        userType = root.findViewById(R.id.userType);
        nombre = root.findViewById(R.id.name);
        apellido = root.findViewById(R.id.lname);
        correo = root.findViewById(R.id.newemail);
        contra = root.findViewById(R.id.newpass);
        telefono = root.findViewById(R.id.phone);

        registro = root.findViewById(R.id.signup);
        registro.setOnClickListener(this);

        //Tipos de usuario para nuestro Spinner
        tipos.add("Usuario");
        tipos.add("Trabajador");

        ArrayAdapter<String> adaptador = new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_item, tipos);
        userType.setAdapter(adaptador);
        userType.setOnItemSelectedListener(this);


        return root;
    }

    @Override
    public void onClick(View view) {
        if(view.getId() == registro.getId()){
            Validacion AUX = new Validacion();
            //Obtenemos los datos de cada campo
            nombreCad = nombre.getText().toString();
            apellidoCad = nombre.getText().toString();
            correoCad = nombre.getText().toString();
            contraCad = nombre.getText().toString();
            telefonoCad = nombre.getText().toString();

            //Validamos la longitud de los campos
            if((nombreCad.length() < 3 || nombreCad.length() > 20) && (apellidoCad.length() < 3 || apellidoCad.length() > 20)
            && (correoCad.length() < 8 || correoCad.length() > 40) && (contraCad.length() < 3 || contraCad.length() > 20)){
                //Comprobamos el formato de los datos
                if(AUX.validarNombre(nombreCad) && AUX.validarNombre(apellidoCad) && AUX.validarCorreo(correoCad)){
                    //Comprobamos si el teléfono está vacío o si cumple el formato
                    if(telefonoCad.length() > 0 || (telefonoCad.length() == 10 && AUX.validarTelefono(telefonoCad))) {
                        parametros = new RequestParams();
                        //Colocamos la información de registro en los parámetros
                        parametros.put("nombre", nombreCad);
                        parametros.put("apellido", apellidoCad);
                        parametros.put("correo", correoCad);
                        parametros.put("contra", contraCad);
                        parametros.put("telefono", telefonoCad);
                        parametros.put("tipo", tipo);

                        //Inicializamos al cliente
                        cliente = new SyncHttpClient();
                    }
                }
            }
            else{
                Toast.makeText(getContext(), "Alguns campos no tienen la longitud válida", Toast.LENGTH_SHORT).show();
            }
        }
    }

    //Métodos para obtener el texto del spinner
    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        tipo = userType.getSelectedItem().toString();
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {
        tipo = "Usuario";
    }
}
