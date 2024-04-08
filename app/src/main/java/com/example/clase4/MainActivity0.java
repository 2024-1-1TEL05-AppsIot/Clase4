package com.example.clase4;

import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;

import com.example.clase4.databinding.ActivityMainBinding;
import com.example.clase4.dto.Comment;
import com.example.clase4.dto.Profile;
import com.example.clase4.services.TypicodeService;

// Correr todo en UI thread.

public class MainActivity0 extends AppCompatActivity {

        private ActivityMainBinding binding;
        TypicodeService typicodeService;

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            binding = ActivityMainBinding.inflate(getLayoutInflater());
            setContentView(binding.getRoot());

            binding.button.setOnClickListener(view -> {
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
            });


        }





}
