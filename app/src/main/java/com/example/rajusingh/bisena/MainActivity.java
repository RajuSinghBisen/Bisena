package com.example.rajusingh.bisena;

import android.app.Activity;
import android.content.Intent;
import android.speech.RecognizerIntent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.Toast;

import java.util.List;

public class MainActivity extends Activity {
ImageView myImageView;
    private static final int SPEECH_REQUEST_CODE = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        myImageView = (ImageView) findViewById(R.id.imageblink);
        Animation myFadeInAnimation = AnimationUtils.loadAnimation(MainActivity.this, R.anim.blinkimage);
        myImageView.startAnimation(myFadeInAnimation);
        myImageView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                displaySpeechRecognizer();
                return true;
            }
        });
    }
    // Create an intent that can start the Speech Recognizer activity
    private void displaySpeechRecognizer() {
        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
// Start the activity, the intent will be populated with the speech text
        startActivityForResult(intent, SPEECH_REQUEST_CODE);
    }

    // This callback is invoked when the Speech Recognizer returns.
// This is where you process the intent and extract the speech text from the intent.
    @Override
    protected void onActivityResult(int requestCode, int resultCode,
                                    Intent data) {
        if (requestCode == SPEECH_REQUEST_CODE && resultCode == RESULT_OK) {
            List<String> results = data.getStringArrayListExtra(
                    RecognizerIntent.EXTRA_RESULTS);
            String spokenText = results.get(0);
            super.onActivityResult(requestCode, resultCode, data);
            if(spokenText.equals("agent")){
                Intent itt= new Intent(this,MainScreen.class);
               startActivity(itt);
            }else{
                Toast.makeText(this, "Password incorrect", Toast.LENGTH_SHORT).show();
            }
            // Do something with spokenText
        }

    }

}
