package com.esatic.nutricionapp.models;

import android.os.Parcel;
import android.os.Parcelable;

public class Ingrediente implements Parcelable {
    String nombre;
    String categoria;


    public Ingrediente(String nombre, String categoria) {
        this.nombre = nombre;
        this.categoria = categoria;
    }

    protected Ingrediente(Parcel in) {
        nombre = in.readString();
        categoria = in.readString();
    }

    public static final Creator<Ingrediente> CREATOR = new Creator<Ingrediente>() {
        @Override
        public Ingrediente createFromParcel(Parcel in) {
            return new Ingrediente(in);
        }

        @Override
        public Ingrediente[] newArray(int size) {
            return new Ingrediente[size];
        }
    };

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getCategoria() {
        return categoria;
    }

    public void setCategoria(String categoria) {
        this.categoria = categoria;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(nombre);
        dest.writeString(categoria);
    }
}
