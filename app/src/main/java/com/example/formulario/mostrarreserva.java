package com.example.formulario;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

public class mostrarreserva extends AppCompatActivity {
    private TextView txtNombre, txtPersonas, txtFecha, txtComentario;
    private Toolbar toolbar;
    private Intent intent;

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){

            case R.id.Reservas:
                intent = new Intent(mostrarreserva.this, reservasDisplay.class);
                startActivity(intent);
                return true;

            case R.id.Localizacion:
                intent = new Intent(mostrarreserva.this, Localizar.class);
                startActivity(intent);
                return true;

            case R.id.Salas:
                intent = new Intent(mostrarreserva.this, salas.class);
                startActivity(intent);
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mostrarreserva);

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        final ActionBar ab = getSupportActionBar();
        ab.setDisplayShowHomeEnabled(false);
        ab.setDisplayHomeAsUpEnabled(true);
        ab.setDisplayShowCustomEnabled(true);
        ab.setDisplayShowTitleEnabled(true);

        txtNombre = (TextView)findViewById(R.id.verNombre);
        txtPersonas = (TextView)findViewById(R.id.verPersonas);
        txtFecha = (TextView)findViewById(R.id.verFecha);
        txtComentario = (TextView)findViewById(R.id.verComentario);

        Reserva reserva = (Reserva) getIntent().getSerializableExtra("objetoReserva");

        txtNombre.setText(reserva.getName());
        txtPersonas.setText(String.valueOf(reserva.getPersonas()));
        txtFecha.setText(reserva.getFecha());
        txtComentario.setText(reserva.getComentario());

        AppCompatButton button = (AppCompatButton) findViewById(R.id.botonVolver);

        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(mostrarreserva.this, reservasDisplay.class);
                startActivity(intent);
            }
        });
    }
}