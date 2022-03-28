package com.example.workwide.modelo;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Validacion {

    public boolean validarCorreo(String email){
        boolean verificado = false;
        Pattern regexEmail = Pattern.compile("^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@" + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$");
        Matcher comparador = regexEmail.matcher(email);

        if(comparador.find()){
            verificado = true;
        }

        return verificado;
    }

    public boolean validarNombre(String nombre){
        boolean verificado = false;
        Pattern regexName = Pattern.compile("^([A-ZÁÉÍÓÚ]{1}[a-zñáéíóú]+[\\s]*)+$");
        Matcher comparador = regexName.matcher(nombre);

        if(comparador.find()){
            verificado = true;
        }

        return verificado;
    }

    public boolean validarTelefono(String tel){
        boolean verificado = false;
        Pattern regexTel = Pattern.compile("^[0-9]+$");
        Matcher comparador = regexTel.matcher(tel);

        if(comparador.find()){
            verificado = true;
        }

        return verificado;
    }

    public boolean validarDesc(String s){
        boolean verificado = false;
        Pattern regexDesc = Pattern.compile("/(<([^>]+)>)/i");

        Matcher comparador = regexDesc.matcher(s);

        if(comparador.find()){
            verificado = true;
        }

        return verificado;
    }
}
