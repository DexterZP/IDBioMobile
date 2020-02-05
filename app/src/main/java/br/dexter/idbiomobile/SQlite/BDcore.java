package br.dexter.idbiomobile.SQlite;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class BDcore extends SQLiteOpenHelper
{
    private static final String NOME_BD = "IndiceBiologiaID";
    private static final int VERSAO_BD = 1;

    BDcore(Context ctx)
    {
        super(ctx, NOME_BD, null, VERSAO_BD);
    }

    @Override
    public void onCreate(SQLiteDatabase db)
    {
        db.execSQL("create table usuario(_id integer primary key autoincrement, local text not null, localidade text not null, latitude real not null, longitude real not null, city text not null, state text not null, data text not null, resultado text not null);");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)
    {
        db.execSQL("drop table usuario;");
        onCreate(db);
    }
}

