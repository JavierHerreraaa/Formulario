package com.example.formulario;

import android.os.AsyncTask;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Scanner;

public class GlobalInfo {
    public static ArrayList<Reserva> reservas = new ArrayList<Reserva>();
    public static int numreserva = 1;

    public static class LongRunningGetIO extends AsyncTask<Void, Void, String> {

        @Override
        protected String doInBackground(Void... params){
            String text = null;
            HttpURLConnection urlConnection = null;
            try {
                URL urlToRequest = new URL("https://androiddesplegar.herokuapp.com/movies/");
                urlConnection = (HttpURLConnection) urlToRequest.openConnection();
                InputStream in = new BufferedInputStream(urlConnection.getInputStream());
                text = new Scanner(in).useDelimiter("\\A").next();

            } catch (Exception e) {
                return e.toString();
            } finally {
                if (urlConnection != null)
                    urlConnection.disconnect();
            }
            return text;
        }

        @Override
        protected void onPostExecute(String results) {

            if (results != null) {
                JSONArray respJSON = null;
                try {
                    respJSON = new JSONArray(results);
                    rellenar_array(respJSON);

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }

    }

    public static void rellenar_array(JSONArray respJSON){
        GlobalInfo.reservas.clear();
        JSONObject object = new JSONObject();

        for(int i=0; i <respJSON.length(); i++){
            try {
                object = respJSON.getJSONObject(i);
                Reserva reserva = new Reserva(Integer.valueOf(object.getString("numreserva")), object.getString("DNI"));
                reserva.setName(object.getString("nombre"));
                reserva.setEmail(object.getString("email"));
                reserva.setTelefono(Integer.parseInt(object.getString("telefono")));
                reserva.setPersonas(Integer.parseInt(object.getString("personas")));
                reserva.setComentario(object.getString("comentario"));
                reserva.setFecha(object.getString("fecha"));

                GlobalInfo.reservas.add(reserva);

            }catch (JSONException e) {
                    e.printStackTrace();
                }

        }
    }

    public static void actualizarArray() {

        LongRunningGetIO myInvokeTask = new LongRunningGetIO();
        myInvokeTask.execute();
    }
}

