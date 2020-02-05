package br.dexter.idbiomobile.SQlite;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

public class BD
{
    private SQLiteDatabase bd;

    public BD(Context context)
    {
        BDcore auxBd = new BDcore(context);
        bd = auxBd.getWritableDatabase();
    }

    public void inserir(Data usuario)
    {
        ContentValues valores = new ContentValues();
        valores.put("local", usuario.getLocal());
        valores.put("localidade", usuario.getLocalidade());
        valores.put("latitude", usuario.getLatitude());
        valores.put("longitude", usuario.getLongitude());
        valores.put("city", usuario.getCity());
        valores.put("state", usuario.getState());
        valores.put("data", usuario.getData());
        valores.put("resultado", usuario.getResultado());
        bd.insert("usuario", null, valores);
    }

    public List<Data> buscar()
    {
        List<Data> list = new ArrayList<>();
        String[] colunas = new String[]{"_id", "local", "localidade", "latitude", "longitude", "city", "state", "data", "resultado"};

        @SuppressLint("Recycle")
        Cursor cursor = bd.query("usuario", colunas, null, null, null, null, "local ASC");

        if(cursor.getCount() > 0)
        {
            cursor.moveToFirst();

            do
            {
                Data u = new Data();
                u.setId(cursor.getLong(0));
                u.setLocal(cursor.getString(1));
                u.setLocalidade(cursor.getString(2));
                u.setLatitude(cursor.getFloat(3));
                u.setLongitude(cursor.getFloat(4));
                u.setCity(cursor.getString(5));
                u.setState(cursor.getString(6));
                u.setData(cursor.getString(7));
                u.setResultado(cursor.getString(8));
                list.add(u);
            }while(cursor.moveToNext());
        }
        return (list);
    }
}
