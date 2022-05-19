package com.example.formulario;

import java.io.Serializable;

public class Reserva implements Serializable {
    private String name, comentario, DNI, email, fecha;
    private int telefono, numreserva, personas;

    public Reserva(int numreserva, String DNI){
        this.DNI = DNI;
        this.numreserva = numreserva;
    }

    public String getFecha() {
        return fecha;
    }
    public String getName() {
        return name;
    }
    public String getComentario() {
        return comentario;
    }
    public int getTelefono() {
        return telefono;
    }
    public String getEmail() { return email; }
    public int getNumreserva() { return numreserva; }
    public String getDNI() { return DNI; }
    public int getPersonas() { return personas; }

    public void setName(String name) {
        this.name = name;
    }
    public void setFecha(String name) {
        this.fecha = name;
    }
    public void setComentario(String name) { this.comentario = name; }
    public void setTelefono(int number) {
        this.telefono = number;
    }
    public void setEmail(String name) { this.email = name; }
    public void setPersonas(int number) { this.personas = number; }
    public void setDNI(String name) { this.DNI = name; }
}
