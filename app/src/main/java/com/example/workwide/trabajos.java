package com.example.workwide;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.SharedPreferences;
import android.os.Bundle;

import com.example.workwide.modelo.Trabajo;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONArray;

import java.util.ArrayList;
import java.util.List;

import cz.msebera.android.httpclient.Header;

public class trabajos extends AppCompatActivity {
    ArrayList<Trabajo> lista = new ArrayList<>();
    RecyclerView reciclador;
    SharedPreferences sesion;
    RequestParams params;
    AsyncHttpClient client;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trabajos);

        reciclador = findViewById(R.id.reciclador);

        reciclador.setLayoutManager(new LinearLayoutManager(this));


        sesion = getSharedPreferences("SESION", MODE_PRIVATE);
        int id = sesion.getInt("id", 0);
        int tipo = sesion.getInt("tipo", 0);
        params = new RequestParams();
        params.put("id", id);
        params.put("tipo", tipo);

        client = new AsyncHttpClient();
        client.post("http://192.168.0.11:8080/WorkWide/listaTrabajosAndroid", params, new JsonHttpResponseHandler(){
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONArray response) {
                super.onSuccess(statusCode, headers, response);
                String s = response.toString();
                System.out.println("JSON: " + s);
                try{
                    lista = new Gson().fromJson(s, new TypeToken<List<Trabajo>>(){}.getType());
                    System.out.println("EXITOSO");
                    Adapter adaptador = new Adapter(lista, tipo);
                    reciclador.setAdapter(adaptador);
                    reciclador.setHasFixedSize(true);

                    System.out.println(lista);
                }
                catch (Exception e){
                    System.out.println(s);
                    e.printStackTrace();
                }
            }
        });
    }
}