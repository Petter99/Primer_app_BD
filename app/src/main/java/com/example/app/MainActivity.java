package com.example.app;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteConstraintException;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

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
        SQLiteDatabase BD= admin.getWritableDatabase();

        guardar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {Guardar();}});
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
        if(Validar_Guardar()) {
            AdminSql admin = new AdminSql(this, "Colegio", null, 1);
            SQLiteDatabase BD= admin.getWritableDatabase();

            try {
                int carnet = Integer.parseInt(txtcarnet.getText().toString());
                //Buscando datos en la bd (Consulta)
                Cursor fila = BD.rawQuery("SELECT * FROM estudiantes WHERE carnet = " + carnet, null); //Consulta
                if (fila.moveToFirst()) {
                    Toast.makeText(this, "Este carnet existe", Toast.LENGTH_SHORT).show();//Mensaje toast

                }else{
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
                    registro.put("direccion", direccion);
                    BD.insert("estudiantes", null, registro);
                    limpiartext();//limpiando cajas de texto
                    Toast.makeText(this, String.valueOf("se guardo el dato"), Toast.LENGTH_SHORT).show();//Mensaje toast
                }

            } catch (SQLiteConstraintException sqlex) {
                System.out.println(sqlex.toString());
                Toast.makeText(this, "ya existe un estudiante con ese carnet", Toast.LENGTH_SHORT).show();
            }
            catch (Exception ex) {
                ex.toString();
                Toast.makeText(this, "No se guardo el dato", Toast.LENGTH_SHORT).show();
            }finally {
                BD.close();
            }
        }
    }


    //Metodo para buscar datos en la BD
    public boolean Buscar(){

        boolean retornar = false;
        if(Validar_CRUD()) {
            AdminSql admin = new AdminSql(this, "Colegio", null, 1);
            SQLiteDatabase BD = admin.getWritableDatabase();
            try{
                int carnet = Integer.parseInt(txtcarnet.getText().toString());
                //Buscando datos en la bd (Consulta)
                Cursor fila = BD.rawQuery("SELECT * FROM estudiantes WHERE carnet=" + carnet, null); //Consulta
                if (fila.moveToFirst()) {
                    //Colocando datos en las cajas de texto
                    limpiartext();
                    txtcarnet.setText(String.valueOf(carnet));
                    txtnombre.setText(fila.getString(1));
                    txtapellido.setText(fila.getString(2));
                    txtedad.setText(fila.getString(3));
                    txtdirec.setText(fila.getString(4));
                    retornar = true;
                } else {
                    Toast.makeText(this, "No existe el registro buscado", Toast.LENGTH_LONG).show();
                    limpiartext();
                    retornar = false;
                }
            } catch (Exception ex) {
                ex.toString();
            } finally {
                BD.close();
            }
        }
        return retornar;
    }

    //Metodo para actualizar datos en la BD
    public void Actualizar(View view)
    {
        if(Validar_Guardar()) {
            String carnettxt = txtcarnet.getText().toString();
            String nombretxt = txtnombre.getText().toString().toLowerCase();
            String apellidotxt = txtapellido.getText().toString().toLowerCase();
            String edadtxt = txtedad.getText().toString();
            String direcciontxt = txtdirec.getText().toString();

            if (admin.Actualizar(carnettxt, nombretxt, apellidotxt, edadtxt, direcciontxt)) {
                Toast.makeText(this, "Se actualizo el Registro", Toast.LENGTH_SHORT).show();//Mensaje toast

                limpiartext();
            } else {
                Toast.makeText(this, "No actualizo el Registro", Toast.LENGTH_SHORT).show();//Mensaje toast
                limpiartext();
            }
        }

    }

    public void eliminar(View view){
        if(Validar_CRUD()) {
            String carnet = txtcarnet.getText().toString();

            if (admin.delete(carnet)) {
                limpiartext();
                Toast.makeText(this, "Registro eliminado", Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(this, "No se elimino el registro", Toast.LENGTH_LONG).show();
            }
        }
    }

    private boolean Validar_Guardar()
    {
        boolean retornar = true; //varible que retornara si los datos se ingresaron correctamente

        //asignar los datos que esten dentro de los EditText
        EditText[] cajas = {txtcarnet,txtnombre,txtapellido,txtedad,txtdirec};
        String[] datos = {txtcarnet.getText().toString(),txtnombre.getText().toString(),
                            txtapellido.getText().toString(),txtedad.getText().toString(),
                            txtdirec.getText().toString()};

        for(int i = 0 ; i <cajas.length;i++) {
            if (datos[i].isEmpty()) {
                cajas[i].setError("Es necesario que lleno el campo");
                retornar = false;
            }
        }
        return retornar;
    }
    private boolean Validar_CRUD()
    {
        boolean retornar = true;
        String dato = txtcarnet.getText().toString();
        if(dato.isEmpty())
        {
            txtcarnet.setError("INGRESE CARNET");
            retornar = false;
        }
        return retornar;
    }
}