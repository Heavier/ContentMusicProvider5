package com.example.javi.contentmusicprovider.interprete;

import android.content.ContentValues;
import android.database.Cursor;

import com.example.javi.contentmusicprovider.base_de_datos.Contrato;

/**
 * Created by javi on 01/01/2016.
 */
public class Interprete {

    private long id;
    private String nombre;
    private long idInterprete;

    public Interprete() {
    }

    public Interprete(long id, String nombre, long idInterprete) {
        this.id = id;
        this.nombre = nombre;
        this.idInterprete = idInterprete;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public long getIdInterprete() {
        return idInterprete;
    }

    public void setIdInterprete(long idInterprete) {
        this.idInterprete = idInterprete;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Interprete that = (Interprete) o;

        if (id != that.id) return false;
        return idInterprete == that.idInterprete;

    }

    @Override
    public int hashCode() {
        int result = (int) (id ^ (id >>> 32));
        result = 31 * result + (int) (idInterprete ^ (idInterprete >>> 32));
        return result;
    }

    @Override
    public String toString() {
        return "Interprete{" +
                "id=" + id +
                ", nombre='" + nombre + '\'' +
                ", idInterprete=" + idInterprete +
                '}';
    }

    public ContentValues getContentValues(){
        ContentValues cv = new ContentValues();
        //cv.put(Contrato.TablaInterprete._ID,this.id);
        cv.put(Contrato.TablaInterprete.NOMBRE,this.nombre);
        cv.put(Contrato.TablaInterprete.IDINTERPRETE, this.idInterprete);
        return cv;
    }

    public void set(Cursor c){
        this.id = c.getLong(c.getColumnIndex(Contrato.TablaInterprete._ID));
        this.nombre = c.getString(c.getColumnIndex(Contrato.TablaInterprete.NOMBRE));
        this.idInterprete = c.getLong(c.getColumnIndex(Contrato.TablaInterprete.IDINTERPRETE));
    }
}
