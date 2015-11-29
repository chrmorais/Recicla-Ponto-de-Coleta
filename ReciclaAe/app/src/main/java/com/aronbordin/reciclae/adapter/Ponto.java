package com.aronbordin.reciclae.adapter;

import android.os.Parcel;
import android.os.Parcelable;

public class Ponto implements Parcelable {
    private int id;
    private String nome;
    private double latitude;
    private double longitude;
    private String endereco;
    private String telefone;
    private String email;

    public Ponto(int id, double latitude, double longitude, String nome, String endereco, String telefone, String email) {
        this.id = id;
        this.latitude = latitude;
        this.longitude = longitude;
        this.nome = nome;
        this.endereco = endereco;
        this.telefone = telefone;
        this.email = email;
    }

    private Ponto(Parcel parcel){
        id = parcel.readInt();
        nome = parcel.readString();
        latitude = parcel.readDouble();
        longitude = parcel.readDouble();
        endereco = parcel.readString();
        telefone = parcel.readString();
        email = parcel.readString();
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

    public String getEndereco() {
        return endereco;
    }

    public void setEndereco(String endereco) {
        this.endereco = endereco;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTelefone() {
        return telefone;
    }

    public void setTelefone(String telefone) {
        this.telefone = telefone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
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
        parcel.writeString(endereco);
        parcel.writeString(telefone);
        parcel.writeString(email);
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
