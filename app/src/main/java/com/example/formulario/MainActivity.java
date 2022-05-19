package com.example.formulario;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.drawerlayout.widget.DrawerLayout;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.widget.Toolbar;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Calendar;
import java.util.Date;

public class MainActivity extends AppCompatActivity {
    private String sFecha;
    private Toolbar toolbar;
    private Intent intent;
    private EditText nombre, dni, fecha, phone, personas, email, comentario;

    private class LongRunningGetIO extends AsyncTask<Void, Void, String> {
        private Reserva reserva;
        public LongRunningGetIO(Reserva reserva){
            this.reserva = reserva;
        }

        protected String doInBackground(Void... params){
            String text = null;
            HttpURLConnection urlConnection = null;
            JSONObject object = new JSONObject();

            try{
                object.put("nombre", reserva.getName() );
                object.put("DNI", reserva.getDNI());
                object.put("email", reserva.getEmail());
                object.put("fecha", reserva.getFecha());
                object.put("personas", reserva.getPersonas());
                object.put("numreserva", reserva.getNumreserva());
                object.put("telefono", reserva.getTelefono());
                object.put("comentario", reserva.getComentario());

                URL urlToRequest = new URL("https://androiddesplegar.herokuapp.com/movies/");
                urlConnection = (HttpURLConnection) urlToRequest.openConnection();
                urlConnection.setRequestMethod("POST");
                urlConnection.setRequestProperty("Content-Type", "application/json");
                urlConnection.setRequestProperty("Accept", "application/json");
                urlConnection.setDoOutput(true);
                urlConnection.setDoInput(true);

                OutputStreamWriter wr = new OutputStreamWriter(urlConnection.getOutputStream());
                wr.write(object.toString());
                wr.close();

                JSONArray respJSON = null;

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
        protected void onPostExecute(String results) {
        }
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
                intent = new Intent(MainActivity.this,  reservasDisplay.class);
                startActivity(intent);
                return true;

            case R.id.Localizacion:
                intent = new Intent(MainActivity.this, Localizar.class);
                startActivity(intent);
                return true;

            case R.id.Salas:
                intent = new Intent(MainActivity.this, salas.class);
                startActivity(intent);
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        GlobalInfo.actualizarArray();

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        final ActionBar ab = getSupportActionBar();
        ab.setDisplayShowHomeEnabled(false);
        ab.setDisplayHomeAsUpEnabled(true);
        ab.setDisplayShowCustomEnabled(true);
        ab.setDisplayShowTitleEnabled(true);

        nombre = (EditText)findViewById(R.id.nombre);
        dni = (EditText)findViewById(R.id.dni);
        email = (EditText)findViewById(R.id.email);
        phone = (EditText)findViewById(R.id.phone);
        fecha = (EditText)findViewById(R.id.fecha);
        personas = (EditText)findViewById(R.id.personas);
        comentario = (EditText)findViewById(R.id.comentario);

        fecha.setOnClickListener(new View.OnClickListener(){
           public void onClick(View v){
               int mYear, mMonth, mDay;
               final Calendar calendar = Calendar.getInstance ();

               mYear = calendar.get ( Calendar.YEAR );
               mMonth = calendar.get ( Calendar.MONTH );
               mDay = calendar.get ( Calendar.DAY_OF_MONTH );

               DatePickerDialog datePickerDialog = new DatePickerDialog ( MainActivity.this, new DatePickerDialog.OnDateSetListener () {
                   @Override
                   public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                       fecha.setText ( dayOfMonth + "/" + (month + 1) + "/" + year );
                        sFecha = dayOfMonth+"/"+(month+1)+"/"+year;
                   }
               }, mYear, mMonth, mDay );

               datePickerDialog.show ();
            }
        });

        AppCompatButton button = (AppCompatButton) findViewById(R.id.botonFormulario);

        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if (comprobar(nombre.getText().toString(), email.getText().toString(), "asd", dni.getText().toString(), phone.getText().toString())) {
                    Reserva reserva = new Reserva(GlobalInfo.numreserva, dni.toString());

                    reserva.setName(nombre.getText().toString());
                    reserva.setEmail(email.getText().toString());
                    reserva.setTelefono(Integer.parseInt(phone.getText().toString()));
                    reserva.setPersonas(Integer.parseInt(personas.getText().toString()));
                    reserva.setComentario(comentario.getText().toString());
                    reserva.setDNI(dni.getText().toString());
                    reserva.setFecha(sFecha);

                    GlobalInfo.numreserva++;

                    Intent intent = new Intent(MainActivity.this, reservasDisplay.class);
                    //intent.putExtra("objetoReserva", reserva);

                    MainActivity.LongRunningGetIO myInvokeTask = new MainActivity.LongRunningGetIO(reserva);
                    myInvokeTask.execute();

                    startActivity(intent);
                }
            }
        });
    }

    public boolean comprobar(String nombre, String email, String fecha, String DNI, String telefono){
        boolean condicion = false;
        Toast toast = Toast.makeText(this, "Exito al registrarte.", Toast.LENGTH_LONG);
        if(nombre.length() < 2 ){
            toast = Toast.makeText(this, "Error en el nombre.", Toast.LENGTH_LONG);
        }
        else if(email.length() < 2){
            toast = Toast.makeText(this, "Error en el email.", Toast.LENGTH_LONG);
        }
        else if(DNI.length()<8){
            toast = Toast.makeText(this, "Error en el DNI.", Toast.LENGTH_LONG);
        }
        else if(telefono.length()<8){
            toast = Toast.makeText(this, "Error en el Teléfono.", Toast.LENGTH_LONG);
        }
        else{
            condicion = true;
        }
        toast.show();
        return condicion;
    }




}

