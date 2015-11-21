package com.aronbordin.reciclaae.adapter;

import android.os.Parcel;
import android.os.Parcelable;

public class Ponto implements Parcelable {
    private int id;
    private String nome;
    private double latitude;
    private double longitude;

    public Ponto(int id, double latitude, double longitude, String nome) {
        this.id = id;
        this.latitude = latitude;
        this.longitude = longitude;
        this.nome = nome;
    }

    private Ponto(Parcel parcel){
        id = parcel.readInt();
        nome = parcel.readString();
        latitude = parcel.readDouble();
        longitude = parcel.readDouble();
    }
    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(id);
        parcel.writeString(nome);
        parcel.writeDouble(latitude);
        parcel.writeDouble(longitude);
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public static final Parcelable.Creator<Ponto> CREATOR = new Parcelable.Creator<Ponto>() {
        @Override
        public Ponto createFromParcel(Parcel parcel) {
            return new Ponto(parcel);
        }

        @Override
        public Ponto[] newArray(int size) {
            return new Ponto[size];
        }
    };
}
