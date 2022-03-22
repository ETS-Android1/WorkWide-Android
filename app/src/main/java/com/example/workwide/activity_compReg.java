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
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Gallery;
import android.widget.Spinner;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.RequestParams;

import java.io.File;
import java.io.FileNotFoundException;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;

public class activity_compReg extends AppCompatActivity {

    private Spinner trabajo, region;
    private Button perfil, portada, completar;
    private EditText descripcion;
    private String descripcionCad;
    private List<String> trabajosLista = new ArrayList<>();
    private List<String> regionLista = new ArrayList<>();
    private AsyncHttpClient cliente;
    private RequestParams parametros;
    private SharedPreferences sesion;
    private int trabajoPos = 0;
    private int regionPos = 0;
    private File perfilFile, portadaFile;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comp_reg);
        perfilFile = null;
        portadaFile = null;

        trabajo = findViewById(R.id.trabajo);
        region = findViewById(R.id.region);
        perfil = findViewById(R.id.fotoPerfil);
        portada = findViewById(R.id.fotoPortada);
        completar = findViewById(R.id.completar);
        descripcion = findViewById(R.id.descripcion);

        trabajosLista = listarTrabajos();
        regionLista = listarRegiones();

        ArrayAdapter<String> trabajosAdptr = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_spinner_item, trabajosLista);
        ArrayAdapter<String> regionAdptr = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_spinner_item, regionLista);

        trabajo.setAdapter(trabajosAdptr);
        region.setAdapter(regionAdptr);

        perfil.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intento = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                intento.setType("image/");
                startActivityForResult(intento.createChooser(intento, "Seleccione una aplicación"), 1);
            }
        });

        portada.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intento = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                intento.setType("image/");
                startActivityForResult(intento.createChooser(intento, "Seleccione una aplicación"), 2);
            }
        });

        completar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                descripcionCad = descripcion.getText().toString();
                trabajoPos = trabajo.getSelectedItemPosition();
                regionPos = region.getSelectedItemPosition();

                if(!descripcionCad.equals("")){
                    if(trabajoPos != 0 && regionPos != 0){
                            parametros = new RequestParams();
                            try {
                                parametros.put("perfil", perfilFile);
                                parametros.put("portada", portadaFile);
                                parametros.put("trabajo", trabajoPos);
                                parametros.put("region", regionPos);
                                parametros.put("descripcion", descripcionCad);

                            } catch (FileNotFoundException e) {
                                //Imprimimos los errores
                                Toast.makeText(getApplicationContext(), "No se encontró el archivo", Toast.LENGTH_SHORT).show();
                                e.printStackTrace();
                            }
                    }
                    else{
                        Toast.makeText(getApplicationContext(), "Escoge un trabajo y una región válidos", Toast.LENGTH_SHORT).show();
                    }
                }
                else{
                    Toast.makeText(getApplicationContext(), "La descripción no puede estar vacía", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private List<String> listarTrabajos(){
        List<String> lista = new ArrayList<>();
        lista.add("Trabajos");
        lista.add("Abogado");
        lista.add("Asesor");
        lista.add("Asistente");
        lista.add("Bibliotecario");
        lista.add("Bioquímico");
        lista.add("Camarógrafo");
        lista.add("Campesino");
        lista.add("Carpintero");
        lista.add("Cartógrafo");
        lista.add("Chef");
        lista.add("Chófer");
        lista.add("Científico");
        lista.add("Conserje");
        lista.add("Criminologo");
        lista.add("Cuidador");
        lista.add("Dermatologo");
        lista.add("Dibujante");
        lista.add("Docente");
        lista.add("Doctor");
        lista.add("Economista");
        lista.add("Electricista");
        lista.add("Estilista");
        lista.add("Fabricante");
        lista.add("Farmacéutio");
        lista.add("Guía");
        lista.add("Guarda");
        lista.add("Herborista");
        lista.add("Informático");
        lista.add("Ingeniero agrónomo");
        lista.add("Instructor");
        lista.add("Mecánico");
        lista.add("Médico");
        lista.add("Neumólogo");
        lista.add("Nutriólogo");
        lista.add("Obrero");
        lista.add("Oculista");
        lista.add("Odontólogo");
        lista.add("Ortopedista");
        lista.add("Periodista");
        lista.add("Plomero");
        lista.add("Profesor");
        lista.add("Programador");
        lista.add("Psicólogo");
        lista.add("Químico");
        lista.add("Técnico");
        lista.add("Tesorero");
        lista.add("Veterinario");
        lista.add("Vigilante");
        lista.add("Zapatero");

        return lista;
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == RESULT_OK){
            Uri miPath = data.getData();
            if(requestCode == 1){
                perfilFile = new File(getPath(miPath));
                System.out.println(miPath.getPath() + ": " + perfilFile.getAbsolutePath());
                perfil.setText("Seleccionada");
            }
            else{
                if(requestCode == 2){
                    portadaFile = new File(getPath(miPath));
                    System.out.println(miPath.getPath() + ": " + portadaFile.getAbsolutePath());
                    portada.setText("Seleccionada");
                }
            }
        }
    }

    public String getPath(Uri uri)
    {
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