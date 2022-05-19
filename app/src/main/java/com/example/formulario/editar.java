package com.example.formulario;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;

import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;

public class editar extends AppCompatActivity {
    private EditText nombre, personas, phone, comentario;
    private Toolbar toolbar;
    private Intent intent;

    private class LongRunningGetIO extends AsyncTask<Void, Void, String> {
        Reserva reserva;
        public LongRunningGetIO(Reserva reserva){this.reserva = reserva;}

        protected String doInBackground(Void... params){
            String text = null;
            HttpURLConnection urlConnection = null;
            JSONObject object = new JSONObject();
            try{
                object.put("nombre", reserva.getName());
                object.put("personas", reserva.getPersonas());
                object.put("telefono", reserva.getTelefono());
                object.put("comentario", reserva.getComentario());

                String url = "https://androiddesplegar.herokuapp.com/movies/" +reserva.getDNI();
                URL urlToRequest = new URL(url);
                urlConnection = (HttpURLConnection) urlToRequest.openConnection();
                urlConnection.setRequestMethod("PUT");
                urlConnection.setRequestProperty("Content-Type", "application/json");
                urlConnection.setRequestProperty("Accept", "application/json");
                urlConnection.setDoOutput(true);
                urlConnection.setDoInput(true);

                OutputStreamWriter wr = new OutputStreamWriter(urlConnection.getOutputStream());
                wr.write(object.toString());
                wr.close();

                InputStream in = new BufferedInputStream(urlConnection.getInputStream());
                text = in.toString();
            } catch (Exception e) {
                return e.toString();
            }
            finally{
                if(urlConnection != null){
                    urlConnection.disconnect();
                }
            }
            return text;
        }
        @Override
        protected void onPostExecute(String results) { }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){

            case R.id.Reservas:
                intent = new Intent(editar.this, reservasDisplay.class);
                startActivity(intent);
                return true;

            case R.id.Localizacion:
                intent = new Intent(editar.this, Localizar.class);
                startActivity(intent);
                return true;

            case R.id.Salas:
                intent = new Intent(editar.this, salas.class);
                startActivity(intent);
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editar);

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        final ActionBar ab = getSupportActionBar();
        ab.setDisplayShowHomeEnabled(false);
        ab.setDisplayHomeAsUpEnabled(true);
        ab.setDisplayShowCustomEnabled(true);
        ab.setDisplayShowTitleEnabled(true);

        int index = getIntent().getIntExtra("indice", 0);

        nombre = (EditText)findViewById(R.id.nombre);
        personas = (EditText)findViewById(R.id.personas);
        phone = (EditText)findViewById(R.id.phone);
        comentario = (EditText)findViewById(R.id.comentario);

        nombre.setText(GlobalInfo.reservas.get(index).getName());
        personas.setText(String.valueOf(GlobalInfo.reservas.get(index).getPersonas()));
        phone.setText(String.valueOf(GlobalInfo.reservas.get(index).getTelefono()));
        comentario.setText(GlobalInfo.reservas.get(index).getComentario());

        AppCompatButton button = (AppCompatButton) findViewById(R.id.botonVolver);

        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                GlobalInfo.reservas.get(index).setName(nombre.getText().toString());
                GlobalInfo.reservas.get(index).setPersonas(Integer.parseInt(personas.getText().toString()));
                GlobalInfo.reservas.get(index).setTelefono(Integer.parseInt(phone.getText().toString()));
                GlobalInfo.reservas.get(index).setComentario(comentario.getText().toString());

                editar.LongRunningGetIO myInvokeTask = new editar.LongRunningGetIO(GlobalInfo.reservas.get(index));
                myInvokeTask.execute();

                Intent intent = new Intent(editar.this, reservasDisplay.class);
                startActivity(intent);
            }
        });
    }
}