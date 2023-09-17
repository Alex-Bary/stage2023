package com.example.speedtestfaitmaison;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.view.View;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button btn1 = (Button) findViewById(R.id.uploadButton);
        btn1.setOnClickListener(this);
    }

    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.uploadButton:
                // Création d’une intention
                Intent playUploadIntent = new Intent(this, uploadActivity.class);
                startActivity(playUploadIntent);
                break;
            case R.id.downloadButton:
                // Création d’une intention
                Intent playDownloadIntent = new Intent(this, downloadActivity.class);
                startActivity(playDownloadIntent);
                break;
        }
    }
}