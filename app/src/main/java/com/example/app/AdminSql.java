package com.example.app;


import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class AdminSql extends SQLiteOpenHelper {

    public AdminSql(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase bd) {
        bd.execSQL("create table estudiantes(carnet int primary key,nombre text," +
                " apellido text, edad int, direccion text)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
    public boolean delete(String carnet)
    {
        boolean verificador = false;
        SQLiteDatabase BD= this.getWritableDatabase();
        try {
            //BD.execSQL("DELETE FROM estudiantes WHERE id='"+carnet);
            BD.delete("estudiantes","carnet ="+carnet,null);
            verificador = true;
        }catch (Exception e){
            e.toString();
            verificador = false;
            System.out.println(e.toString());
        }finally {
            BD.close();
        }
        return verificador;
    }
    public boolean Actualizar(String carnettxt, String nombretxt,String apellidotxt, String edadtxt, String direcciontxt)
    {
        boolean verificador = false;
        SQLiteDatabase BD= this.getWritableDatabase();
        try {
            ContentValues registro = new ContentValues();

            registro.put("nombre", nombretxt);
            registro.put("apellido", apellidotxt);
            registro.put("edad", edadtxt);
            registro.put("direccion",direcciontxt);

            //Actualizando los datos de la bd (Consulta)

            BD.update("estudiantes",registro,"carnet =" +carnettxt,null);

            verificador = true;
        }catch (Exception e){
            e.toString();
            verificador = false;
            System.out.println(e.toString());
        }finally {
            BD.close();
        }
        return verificador;
    }

}
