package com.esatic.nutricionapp.others;

import android.app.Activity;
import android.util.Log;

import com.esatic.nutricionapp.R;
import com.esatic.nutricionapp.models.Ingrediente;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

public class CsvController {

    InputStream inputStream;
    String[] data;
    Activity activity;
    ArrayList<Ingrediente> ingredientes;

    public CsvController(Activity activity){
        this.activity = activity;
        ingredientes = new ArrayList<>();
        insertIngredients();

    }

    public void insertIngredients (){
        inputStream = activity.getResources().openRawResource(R.raw.nutriapp);

        BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));

        try {
            String cvsLine;
            while ((cvsLine=reader.readLine())!= null){
                data = cvsLine.split(",");
                try {
                    Ingrediente ingrediente = new Ingrediente(data[0].toLowerCase(),data[1]);
                    ingredientes.add(ingrediente);
                     Log.e("Data é ",ingrediente.getNombre());



                   // Log.e("Data é ",data[0]);

                }catch (Exception e){
                    Log.e("Problem ",e.toString());
                }
            }

        }catch (IOException ex){
            throw new RuntimeException("Error readin csv file "+ex);
        }
    }

    public ArrayList<Ingrediente> getIngredientes() {
        return ingredientes;
    }
}
