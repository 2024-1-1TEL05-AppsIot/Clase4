package com.example.clase4;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.example.clase4.databinding.ActivityMainBinding;
import com.example.clase4.dto.Comment;
import com.example.clase4.dto.Profile;
import com.example.clase4.services.TypicodeService;

import java.util.List;
import java.util.concurrent.ExecutorService;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;
    TypicodeService typicodeService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        ApplicationThreads application = (ApplicationThreads) getApplication();
        ExecutorService executorService = application.executorService;

        ContadorViewModel contadorViewModel = new ViewModelProvider(MainActivity.this).get(ContadorViewModel.class);

        contadorViewModel.getContador().observe(this, contador -> {
            //aquí o2
            binding.textView.setText(String.valueOf(contador));
        });

        binding.button.setOnClickListener(view -> {

            //es un hilo en background
            executorService.execute(() -> {
                for (int i = 1; i <= 10; i++) {

                    //
                    contadorViewModel.getContador().postValue(i); // o1
                    Log.d("msg-test", "i: " + i);
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                }
            });

        });

        Toast.makeText(this, "Tiene internet: " + tengoInternet(), Toast.LENGTH_SHORT).show();

        typicodeService = new Retrofit.Builder()
                .baseUrl("https://my-json-server.typicode.com")
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(TypicodeService.class);

        binding.button3.setOnClickListener(view -> fetchWebServiceData());

    }

    public void fetchWebServiceData(){
        if(tengoInternet()){
            typicodeService.getProfile().enqueue(new Callback<Profile>() {
                @Override
                public void onResponse(Call<Profile> call, Response<Profile> response) {
                    //aca estoy en el UI Thread
                    if(response.isSuccessful()){
                        Profile profile = response.body();
                        binding.rpta.setText(profile.getName());
                        fetchCommentsFromWs();
                    }
                }

                @Override
                public void onFailure(Call<Profile> call, Throwable t) {

                }
            });
        }
    }

    public void fetchCommentsFromWs(){
        if(tengoInternet()){
            typicodeService.getComments().enqueue(new Callback<List<Comment>>() {
                @Override
                public void onResponse(Call<List<Comment>> call, Response<List<Comment>> response) {
                    if(response.isSuccessful()){
                        List<Comment> comments = response.body();
                        for(Comment c : comments){
                            Log.d("msg-test","id: " + c.getId() + " | body: " + c.getBody());
                        }
                    }
                }

                @Override
                public void onFailure(Call<List<Comment>> call, Throwable t) {

                }
            });

            //typicodeService.getProfileWithData(nombre, apellido)
        }
    }

    public boolean tengoInternet() {
        ConnectivityManager manager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeNetworkInfo = manager.getActiveNetworkInfo();
        boolean tieneInternet = activeNetworkInfo != null && activeNetworkInfo.isConnected();

        Log.d("msg-test", "Internet: " + tieneInternet);

        return tieneInternet;
    }
}