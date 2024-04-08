package com.example.clase4;

import androidx.appcompat.app.AppCompatActivity;
import androidx.work.OneTimeWorkRequest;
import androidx.work.WorkManager;
import androidx.work.WorkRequest;

import android.os.Bundle;
import android.widget.Button;

import com.example.clase4.workers.ContadorWorker;

public class MainActivity2 extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

        Button button  = findViewById(R.id.buttonWorkMang);
        button.setOnClickListener(view -> {

            WorkRequest workRequest = new OneTimeWorkRequest.Builder(ContadorWorker.class).build();

            WorkManager
                    .getInstance(MainActivity2.this.getApplicationContext())
                    .enqueue(workRequest);
        });
    }
}