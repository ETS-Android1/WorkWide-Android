package com.example.workwide.controlador;

import com.example.workwide.modelo.Trabajador;
import com.example.workwide.modelo.Usuario;
import com.loopj.android.http.*;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;

public class WebServices {

    private AsyncHttpClient cliente;
    private RequestParams parametros;


    public Trabajador iniciarSesion(String correo, String contrasena){
        parametros = new RequestParams();
        Trabajador user = new Trabajador();
        user.setNombre("Waiting...");
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
                        user.setIdUsu(response.getInt("id"));
                        user.setNombre(response.getString("nombre"));
                        user.setApellido(response.getString("apellido"));
                        user.setCorreoUsu(correo);
                        if(response.getInt("tipo") == 2){
                            if(response.getString("region").equals("")){
                                user.setRegionNombre("NA");
                                user.setDescripcion("NA");
                                user.setTrabajoNombre("NA");
                            }
                        }

                    }
                    else{
                        user.setIdUsu(0);
                        user.setNombre("NA");
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });

        return user;
    }

    public void registro(Usuario usu, String tipo){

    }
}
