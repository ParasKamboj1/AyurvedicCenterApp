package com.example.projectsynopisis;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.speech.RecognizerIntent;
import android.speech.SpeechRecognizer;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;


public class MainActivity extends AppCompatActivity {
    private static final int SPEECH_REQUEST_CODE = 1;
    FirebaseAuth auth;
    TextView usernames,language;
    Spinner spinnerDropDown;
    SpeechRecognizer speechRecognizer;
    EditText editTextSearch;
    ImageView btnSpeech;
    ImageView googlemap,mainimage;
    Button button1,button2,button3;
    int currentImageIndex = 0;
    Handler handler;
    Runnable imageUpdater;
    int[]imageResources = {R.drawable.im1,R.drawable.secondimage,R.drawable.thirdimage};
    String languageCode = "en";
    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        editTextSearch = findViewById(R.id.writehere);
        btnSpeech = findViewById(R.id.imageView7);
        language = findViewById(R.id.textView397);
        usernames = findViewById(R.id.username);
        spinnerDropDown = findViewById(R.id.spinner);

        ArrayAdapter<CharSequence>adapter = ArrayAdapter.createFromResource(this,R.array.dropdown_items, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerDropDown.setAdapter(adapter);
        spinnerDropDown.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long id) {
                String selectedItem = getResources().getStringArray(R.array.dropdown_items)[position];
                language.setText(selectedItem);
                if(selectedItem.equals("Hindi")){
                    languageCode = "hi";
                }
                else if(selectedItem.equals("Punjabi")){
                    languageCode = "pa";
                }
                else if(selectedItem.equals("Marathi")){
                    languageCode = "mr";
                }
                else if(selectedItem.equals("Tamil")){
                    languageCode = "ta";
                }
                else{
                    languageCode = "en";
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        auth = FirebaseAuth.getInstance();


        speechRecognizer = SpeechRecognizer.createSpeechRecognizer(this);

        btnSpeech.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startSpeechRecognition();
            }
        });

        Intent intent = getIntent();
        String username = intent.getStringExtra("USERNAME");
        usernames.setText(username);

        handler = new Handler();
        imageUpdater = new Runnable() {
            @Override
            public void run() {
                updateImage();
                handler.postDelayed(this,2000);
            }
        };
        handler.postDelayed(imageUpdater,2000);


        googlemap = findViewById(R.id.googlemap);
        button1 = findViewById(R.id.button1);
        button2 = findViewById(R.id.button2);
        button3 = findViewById(R.id.button3);
        mainimage = findViewById(R.id.mainimage);

        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mainimage.setImageResource(R.drawable.im1);
            }
        });

        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mainimage.setImageResource(R.drawable.secondimage);
            }
        });

        button3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mainimage.setImageResource(R.drawable.thirdimage);
            }
        });

//        if(auth.getCurrentUser()==null){
//            Intent i = new Intent(MainActivity.this,login.class);
//            startActivity(i);
//            finish();
//        }

        googlemap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // By using google Map API
//                Intent intent2 = new Intent(MainActivity.this, mapshown.class);
//                startActivity(intent2);



                // Using google map url
                Uri location = Uri.parse("geo:0,0?q=Nearest+Ayurvedic+hospitals");
                Intent intent = new Intent(Intent.ACTION_VIEW,location);
                intent.setPackage("com.google.android.apps.maps");
                startActivity(intent);
            }
        });
    }
    void updateImage(){
        currentImageIndex = (currentImageIndex + 1) % imageResources.length;
        mainimage.setImageResource(imageResources[currentImageIndex]);

    }

    void startSpeechRecognition(){
        if (isSpeechRecognitionAvailable()) {

            Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);

            // If you want to use only single language that's english.
//            intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);

//             If you want to use different languages.
            intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE,languageCode);


            startActivityForResult(intent, SPEECH_REQUEST_CODE);
        } else {
            Toast.makeText(this, "Speech recognition is not available on this device.", Toast.LENGTH_SHORT).show();
        }
    }
    boolean isSpeechRecognitionAvailable() {
        return SpeechRecognizer.isRecognitionAvailable(this);
    }

    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == SPEECH_REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            ArrayList<String> results = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
            if (results != null && !results.isEmpty()) {
                String spokenText = results.get(0);
                editTextSearch.setText(spokenText);
                Toast.makeText(this, "You said: " + spokenText, Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (speechRecognizer != null) {
            speechRecognizer.destroy();
        }
    }

}