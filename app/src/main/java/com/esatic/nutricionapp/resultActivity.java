package com.esatic.nutricionapp;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;

import com.esatic.nutricionapp.models.Ingrediente;

import java.io.InputStream;
import java.util.ArrayList;

public class resultActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);
        String textIngredientes = "";
        String result = getIntent().getStringExtra("result");

        ArrayList<Ingrediente> ingredientesEncontrados = this.getIntent().getParcelableArrayListExtra("ingredientsFounded");

        for (Ingrediente ingrediente : ingredientesEncontrados) {
            textIngredientes += ingrediente.getNombre() + "\n";


        }


        TextView textViewResult = findViewById(R.id.resultText);
        TextView textViewIngredientesEncontrados = findViewById(R.id.foundedIngredients);
        textViewResult.setText(result);
        textViewIngredientesEncontrados.setText(textIngredientes);



    }
}
