package ar.edu.unnoba.tpfinalppc.Utils;

import android.content.Context;
import android.content.SharedPreferences;

public class Session {

    SharedPreferences prefs;
    SharedPreferences.Editor editor;
    Context context;

    public Session(Context context){
        this.context = context;
        prefs = context.getSharedPreferences("SP", Context.MODE_PRIVATE);
        editor = prefs.edit();
    }

    public void setLoggedin(boolean logggedin){
        editor.putBoolean("loggedInmode",logggedin);
        editor.commit();
    }

    public boolean loggedin(){
        return prefs.getBoolean("loggedInmode", false);
    }

    public void guardarDatos(String e, String p){
        editor.putString("usuario", e);
        editor.putString("contraseña",p);
        editor.commit();
    }

    public String mostrarDatos(){
        String usuario = prefs.getString("usuario","null");
        return usuario;
    }

    public void borrarSP(){
        editor.clear();
        editor.commit();
    }
}
