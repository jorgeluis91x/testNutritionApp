package com.esatic.nutricionapp;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.ml.vision.FirebaseVision;
import com.google.firebase.ml.vision.common.FirebaseVisionImage;
import com.google.firebase.ml.vision.text.FirebaseVisionText;
import com.google.firebase.ml.vision.text.FirebaseVisionTextRecognizer;
import com.wonderkiln.camerakit.CameraKitError;
import com.wonderkiln.camerakit.CameraKitEvent;
import com.wonderkiln.camerakit.CameraKitEventListener;
import com.wonderkiln.camerakit.CameraKitImage;
import com.wonderkiln.camerakit.CameraKitVideo;
import com.wonderkiln.camerakit.CameraView;


import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import dmax.dialog.SpotsDialog;

import android.util.Log;
import android.view.View;

import java.util.List;

public class CameraActivity extends AppCompatActivity {

    CameraView cameraView;
    public AlertDialog waitingDialog;
    public Activity activity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        activity = this;
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera);

        cameraView = findViewById(R.id.camera_view);
        waitingDialog = new SpotsDialog.Builder().setContext(this).setMessage("Espere por favor").setCancelable(false).build() ;

        cameraView.addCameraKitListener(new CameraKitEventListener() {
            @Override
            public void onEvent(CameraKitEvent cameraKitEvent) {

            }

            @Override
            public void onError(CameraKitError cameraKitError) {

            }

            @Override
            public void onImage(CameraKitImage cameraKitImage) {
                waitingDialog.show();
                Bitmap bitmap = cameraKitImage.getBitmap();
                bitmap= Bitmap.createScaledBitmap(bitmap,cameraView.getWidth(),cameraView.getHeight(),false);
                cameraView.stop();

                runTextRecognition(bitmap);




            }

            @Override
            public void onVideo(CameraKitVideo cameraKitVideo) {

            }
        });



    }
    @Override
    protected void onResume (){
        super.onResume();
        cameraView.start();
    }

    @Override
    protected void onPause (){
        super.onPause();
        cameraView.stop();
    }

    public void clickCapturar(View view){
        cameraView.start();
        cameraView.captureImage();


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

                       // Intent i = new Intent(getApplicationContext(),resultActivity.class);
                        Intent i = new Intent(activity,resultActivity.class);
                        i.putExtra("result",result);

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
        List<FirebaseVisionText.TextBlock> blocks = texts.getTextBlocks();
        if (blocks.size()==0){

            return "No encontramos texto";
        }

        for (int i = 0; i < blocks.size();i++){
            List<FirebaseVisionText.Line> lines = blocks.get(i).getLines();
            for (int j = 0; j < lines.size();j++){
                List<FirebaseVisionText.Element> elements = lines.get(j).getElements();
                Log.e("otro element: ",lines.get(j).getText());
                text += lines.get(j).getText() + "\\n";


                for (int k = 0; k < elements.size();k++){
                    Log.e("palabra",elements.get(k).getText());


                }
            }

        }
        return text;
        // Replace with code from the codelab to process the text recognition result.
    }

}
