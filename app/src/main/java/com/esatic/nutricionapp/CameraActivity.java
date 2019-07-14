package com.esatic.nutricionapp;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;

import com.camerakit.CameraKitView;
import com.esatic.nutricionapp.models.Ingrediente;
import com.esatic.nutricionapp.others.CsvController;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.ml.vision.FirebaseVision;
import com.google.firebase.ml.vision.common.FirebaseVisionImage;
import com.google.firebase.ml.vision.text.FirebaseVisionText;
import com.google.firebase.ml.vision.text.FirebaseVisionTextRecognizer;



import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import dmax.dialog.SpotsDialog;

import android.util.Log;
import android.view.View;


import java.util.ArrayList;
import java.util.List;

public class CameraActivity extends AppCompatActivity {

    CameraKitView cameraView;
    CsvController csvController;
    public AlertDialog waitingDialog;
    public Activity activity;
    ArrayList<Ingrediente> ingredientesEncontrados = new ArrayList<>();
    ArrayList<Ingrediente> ingredientes = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        activity = this;
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera);
        csvController = new CsvController(this);
        cameraView = findViewById(R.id.camera_view);
        waitingDialog = new SpotsDialog.Builder().setContext(this).setMessage("Espere por favor").setCancelable(false).build() ;



    }

    @Override
    protected void onStart() {
        super.onStart();
        cameraView.onStart();
    }

    @Override
    protected void onStop() {
        cameraView.onStop();
        super.onStop();
    }

    @Override
    protected void onResume (){
        super.onResume();
        cameraView.onResume();
    }

    @Override
    protected void onPause (){
        cameraView.onPause();
        super.onPause();

    }

    public void clickCapturar(View view){
        //cameraView.start();
        //cameraView.captureImage();

        cameraView.captureImage(new CameraKitView.ImageCallback() {
            @Override
            public void onImage(CameraKitView cameraKitView, final byte[] capturedImage) {
                waitingDialog.show();
                Bitmap bitmap =BitmapFactory.decodeByteArray(capturedImage, 0, capturedImage.length);
                bitmap= Bitmap.createScaledBitmap(bitmap,cameraView.getWidth(),cameraView.getHeight(),false);
                cameraView.onStop();

                runTextRecognition(bitmap);

            }
        });




    }

    private void runTextRecognition(Bitmap mSelectedImage) {

        FirebaseVisionImage image = FirebaseVisionImage.fromBitmap(mSelectedImage);


        FirebaseVisionTextRecognizer detector = FirebaseVision.getInstance().getOnDeviceTextRecognizer();


        detector.processImage(image).addOnSuccessListener(
                new OnSuccessListener<FirebaseVisionText>() {
                    @Override
                    public void onSuccess(FirebaseVisionText firebaseVisionText) {

                       String result = processTextRecognitionResult(firebaseVisionText);

                        waitingDialog.hide();


                        Intent i = new Intent(activity,resultActivity.class);
                        i.putExtra("result",result);
                        i.putParcelableArrayListExtra("ingredientsFounded",ingredientesEncontrados);

                        activity.startActivity(i);

                    }
                }
        ).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

                e.printStackTrace();
            }
        });
        // Replace with code from the codelab to run text recognition.
    }

    private String processTextRecognitionResult(FirebaseVisionText texts) {
        String text ="";
        ingredientes = csvController.getIngredientes();
        List<FirebaseVisionText.TextBlock> blocks = texts.getTextBlocks();
        if (blocks.size()==0){

            return "No encontramos texto";
        }

        for (int i = 0; i < blocks.size();i++){
            List<FirebaseVisionText.Line> lines = blocks.get(i).getLines();
            for (int j = 0; j < lines.size();j++){
                List<FirebaseVisionText.Element> elements = lines.get(j).getElements();
                Log.e("otro element: ",lines.get(j).getText());
                text += lines.get(j).getText().toLowerCase() + "\n";


                for (int k = 0; k < elements.size();k++){
                    String word = elements.get(k).getText().toLowerCase();
                    Log.e("palabra",word);
                     ingredientesEncontrados = new ArrayList<>();


                         for (Ingrediente ingrediente : ingredientes) {

                             Log.e("index  ",ingrediente.getNombre());
                             Log.e("index word ",word);
                             Log.e("index index ",ingrediente.getNombre().indexOf(word) +"");


                             Boolean found = ingrediente.getNombre().indexOf(word) != -1;
                             if (found) {
                                 ingredientesEncontrados.add(ingrediente);

                             }
                         }



                }
            }

        }
        return text;
        // Replace with code from the codelab to process the text recognition result.
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        cameraView.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

}
