package com.rel3.lixoconsciente.dbhelper;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.rel3.lixoconsciente.model.Endereco;

import java.util.ArrayList;


public class DBHelper extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "LixoConsciente.db";
    public static final String TABLE_NAME = "T_ENDERECO";
    public static final String COLUNA_ID = "id";
    public static final String COLUNA_ENDERECO = "endereco";


    public DBHelper(Context context){
        super(context, DATABASE_NAME, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table "+ TABLE_NAME + "( " + COLUNA_ID +" integer primary key, " + COLUNA_ENDERECO + " text)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }

    public boolean cadastrarCep(Endereco endereco){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUNA_ENDERECO, endereco.getEndereco());
        db.insert(TABLE_NAME, null, values);
        return true;
    }

    public int numberOfRows(){
        SQLiteDatabase db = this.getReadableDatabase();
        int numRows = (int) DatabaseUtils.queryNumEntries(db, TABLE_NAME);
        return numRows;
    }

    public Integer deletarCep(Integer id){
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete(TABLE_NAME, "id = ?", new String[] {Integer.toString(id)});
    }

    public ArrayList<String> listarTudo(){
        ArrayList<String> lista = new ArrayList<>();
        Endereco endereco = null;

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery("select * from " + TABLE_NAME, null);
        c.moveToFirst();

        while (!c.isAfterLast()){
            endereco = new Endereco();
            endereco.setId(c.getInt(c.getColumnIndex(COLUNA_ID)));
            endereco.setEndereco(c.getString(c.getColumnIndex(COLUNA_ENDERECO)));
            lista.add(endereco.getEndereco());
            c.moveToNext();
        }

        return lista;
    }
}
