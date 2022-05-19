package com.example.formulario;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.RecyclerView;

import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;


public class ReservasAdapter extends RecyclerView.Adapter<ReservasAdapter.MyViewHolder> {
    private ArrayList<Reserva> reservas;
    private Context context;

    private class LongRunningGetIO extends AsyncTask<Void, Void, String> {
        Reserva reserva;
        public LongRunningGetIO(Reserva reserva){this.reserva = reserva;}

        protected String doInBackground(Void... params){
            String text = null;
            HttpURLConnection urlConnection = null;
            JSONObject object = new JSONObject();
            try{
                String url = "https://androiddesplegar.herokuapp.com/movies/" +reserva.getDNI();
                URL urlToRequest = new URL(url);
                urlConnection = (HttpURLConnection) urlToRequest.openConnection();
                urlConnection.setRequestMethod("DELETE");
                urlConnection.setRequestProperty("Content-Type", "application/json");
                urlConnection.setRequestProperty("Accept", "application/json");
                urlConnection.setDoOutput(true);
                urlConnection.setDoInput(true);

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


    public static class MyViewHolder extends RecyclerView.ViewHolder {
        TextView number;
        TextView name;
        Button show, editar, eliminar;

        public MyViewHolder(View v) {
            super(v);
            number = (TextView) v.findViewById(R.id.number);
            show = (Button) v.findViewById(R.id.show);
            editar = (Button) v.findViewById(R.id.botonEditar);
            eliminar = (Button) v.findViewById(R.id.botonEliminar);
        }
    }

    public ReservasAdapter(ArrayList<Reserva> myDataset) {
        reservas = myDataset;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item, parent, false);
        MyViewHolder vh = new MyViewHolder(v);
        context = parent.getContext();
        return vh;
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {
        holder.number.setText(String.valueOf(reservas.get(position).getNumreserva()));

        holder.show.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CharSequence text = "You've clicked on " +
                        reservas.get(position).getName();
                int duration = Toast.LENGTH_LONG;
                Toast toast = Toast.makeText(context, text, duration);
                toast.show();

                Intent intent = new Intent(v.getContext(), mostrarreserva.class);

                intent.putExtra("objetoReserva", reservas.get(position));
                v.getContext().startActivity(intent);
            }
        });

        holder.eliminar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), reservasDisplay.class);
                ReservasAdapter.LongRunningGetIO myInvokeTask = new ReservasAdapter.LongRunningGetIO(reservas.get(position));
                myInvokeTask.execute();
                GlobalInfo.reservas.remove(position);


                v.getContext().startActivity(intent);
            }
        });

        holder.editar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), editar.class);
                intent.putExtra("indice", position);

                v.getContext().startActivity(intent);
            }
        });
    }


    @Override
    public int getItemCount() {
        return reservas.size();
    }

}
