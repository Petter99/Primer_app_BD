package com.example.app;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    EditText txtcarnet, txtnombre, txtapellido, txtedad, txtdirec;
    Button guardar, buscar, actualizar, eliminar;
    AdminSql admin;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        guardar = (Button) findViewById(R.id.btn_guardar);
        buscar = (Button) findViewById(R.id.btn_buscar);
        actualizar = (Button) findViewById(R.id.btn_actualizar);
        eliminar = (Button) findViewById(R.id.btn_eliminar);

        txtcarnet = (EditText) findViewById(R.id.txt_carnet);
        txtnombre = (EditText) findViewById(R.id.txt_nombre);
        txtapellido = (EditText) findViewById(R.id.txt_apellido);
        txtedad = (EditText) findViewById(R.id.txt_edad);
        txtdirec = (EditText) findViewById(R.id.txt_direc);

        admin = new AdminSql(this, "Colegio", null, 1);

        guardar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Guardar();
            }
        });
        buscar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Buscar();
            }
        });


    }

    //Metodo para limpiar cajas de texto
    public void limpiartext(){
        txtcarnet.setText("");
        txtnombre.setText("");
        txtapellido.setText("");
        txtedad.setText("");
        txtdirec.setText("");
    }

    //Metodo para guardar datos en la BD
    public void Guardar(){
        AdminSql admin = new AdminSql(this, "Colegio", null, 1);

        SQLiteDatabase BD= admin.getWritableDatabase();
        try {


            String carnet = txtcarnet.getText().toString();
            String nombre = txtnombre.getText().toString().toLowerCase();
            String apellido = txtapellido.getText().toString().toLowerCase();
            String edad = txtedad.getText().toString();
            String direccion = txtdirec.getText().toString();

            //Ingresando datos en la bd (Insercion)
            ContentValues registro = new ContentValues();

            registro.put("carnet", carnet);
            registro.put("nombre", nombre);
            registro.put("apellido", apellido);
            registro.put("edad", edad);
            registro.put("direccion",direccion);
            BD.insert("estudiantes",null,registro);
            //cerrando conexion base de datos


            limpiartext();//limpiando cajas de texto

            Toast.makeText(this,"Datos guardados",Toast.LENGTH_SHORT).show();//Mensaje toast
        }catch (Exception ex){
            ex.toString();
        }finally {
            BD.close();
        }
    }


    //Metodo para buscar datos en la BD
    public void Buscar(){
        AdminSql admin = new AdminSql(this, "Colegio", null, 1);

        SQLiteDatabase BD= admin.getWritableDatabase();
        try {


            int carnet = Integer.valueOf(txtcarnet.getText().toString());


            //Buscando datos en la bd (Consulta)

            Cursor fila = BD.rawQuery("SELECT * FROM estudiantes WHERE carnet="+carnet,null); //Consulta

            if(fila.moveToFirst()){
                //Colocando datos en las cajas de texto
                txtnombre.setText(fila.getString(1));
                txtapellido.setText(fila.getString(2));
                txtedad.setText(fila.getString(3));
                txtdirec.setText(fila.getString(4));
            }else{
                Toast.makeText(this,"No existe el registro buscado", Toast.LENGTH_LONG).show();

            }


        }catch (Exception ex){
            ex.toString();
        }finally {
            BD.close();
        }

    }

    //Metodo para actualizar datos en la BD
    public void Actualizar(View view)
    {
            String carnettxt = txtcarnet.getText().toString();
            String nombretxt = txtnombre.getText().toString().toLowerCase();
            String apellidotxt = txtapellido.getText().toString().toLowerCase();
            String edadtxt = txtedad.getText().toString();
            String direcciontxt = txtdirec.getText().toString();

            if(admin.Actualizar(carnettxt,nombretxt,apellidotxt,edadtxt,direcciontxt)) {
                Toast.makeText(this, "Se actualizo el Registro", Toast.LENGTH_SHORT).show();//Mensaje toast

                limpiartext();
            }else{
                Toast.makeText(this, "No actualizo el Registro", Toast.LENGTH_SHORT).show();//Mensaje toast
                limpiartext();
            }

    }

    public void eliminar(View view){
        String carnet = txtcarnet.getText().toString();

        if(admin.delete(carnet)){
            limpiartext();
            Toast.makeText(this,"Registro eliminado", Toast.LENGTH_LONG).show();
        }else{
            Toast.makeText(this,"No se elimino el registro", Toast.LENGTH_LONG).show();
        }
    }

}