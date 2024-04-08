package com.example.clase4;

import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;

import com.example.clase4.databinding.ActivityMainBinding;
import com.example.clase4.services.TypicodeService;

import java.util.concurrent.ExecutorService;

// Usando ExecutorService

public class MainActivity1 extends AppCompatActivity {

    private ActivityMainBinding binding;
    TypicodeService typicodeService;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // ExecutorService
        ApplicationThreads application = (ApplicationThreads) getApplication();
        ExecutorService executorService = application.executorService;

        binding.button.setOnClickListener(view -> {

            executorService.execute(new Runnable() {
                @Override
                public void run() {
                    for (int i = 1; i <= 10; i++) {
                        //contadorViewModel.getContador().postValue(i); // o1
                        binding.contadorTextView.setText(String.valueOf(i));
                        Log.d("msg-test", "i: " + i);
                        try {
                            Thread.sleep(1000);
                        } catch (InterruptedException e) {
                            throw new RuntimeException(e);
                        }
                    }
                }
             });

        });

    }


}