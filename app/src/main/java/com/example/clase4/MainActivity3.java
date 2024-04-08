package com.example.clase4;

import android.os.Bundle;
import android.util.Log;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import androidx.work.Data;
import androidx.work.OneTimeWorkRequest;
import androidx.work.WorkInfo;
import androidx.work.WorkManager;
import androidx.work.WorkRequest;

import com.example.clase4.databinding.ActivityMain2Binding;
import com.example.clase4.databinding.ActivityMainBinding;
import com.example.clase4.workers.ContadorWorker3;

import java.util.Random;
import java.util.UUID;

// Usando Work Manager

public class MainActivity3 extends AppCompatActivity {

    private ActivityMain2Binding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMain2Binding.inflate(this.getLayoutInflater());
        setContentView(binding.getRoot());


        UUID uuid = UUID.randomUUID();

        binding.buttonWorkMang.setOnClickListener(view -> {

            Data dataBuilder = new Data.Builder()
                    .putInt("numero", new Random().nextInt(10))
                    .build();

            WorkRequest workRequest =  new OneTimeWorkRequest.Builder(ContadorWorker3.class)
                    .setId(uuid)
                    .setInputData(dataBuilder)
                    .build();

            WorkManager
                    .getInstance(MainActivity3.this.getApplicationContext())
                    .enqueue(workRequest);


            WorkManager.getInstance(binding.getRoot().getContext())
                    .getWorkInfoByIdLiveData(uuid)
                    .observe(MainActivity3.this, workInfo -> {
                        if(workInfo != null){
                            Data progress = workInfo.getProgress();
                            int contador = progress.getInt("contador", 0);
                            Log.d("msg-test", "progress: " + contador);
                            binding.contadorVal.setText(String.valueOf(contador));
                        }else{
                            Log.d("msg-test", "work info == null ");
                        }
                    });

            /*WorkManager.getInstance(binding.getRoot().getContext())
                    .getWorkInfoByIdLiveData(uuid)
                    .observe(MainActivity3.this, workInfo -> {
                if (workInfo != null) {
                    if (workInfo.getState() == WorkInfo.State.RUNNING) {
                        Data progress = workInfo.getProgress();
                        int contador = progress.getInt("contador", 0);
                        Log.d("msg-test", "progress: " + contador);
                        binding.contadorVal.setText(String.valueOf(contador));

                    } else if (workInfo.getState() == WorkInfo.State.SUCCEEDED) {
                        Data outputData = workInfo.getOutputData();
                        String texto = outputData.getString("info");
                        Log.d("msg-test", texto);

                    }
                } else {
                    Log.d("msg-test", "work info == null ");
                }
            });*/


        });
    }
}