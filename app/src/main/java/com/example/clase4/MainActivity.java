package com.example.clase4;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.example.clase4.databinding.ActivityMainBinding;
import com.example.clase4.dto.Comment;
import com.example.clase4.dto.Post;
import com.example.clase4.dto.Profile;
import com.example.clase4.services.TypicodeService;
import com.example.clase4.viewmodel.ContadorViewModel;

import java.util.List;
import java.util.concurrent.ExecutorService;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.POST;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;
    TypicodeService typicodeService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Usando ExecutorService
        ApplicationThreads application = (ApplicationThreads) getApplication();
        ExecutorService executorService = application.executorService;
        // **********************

        ContadorViewModel contadorViewModel =
                new ViewModelProvider(MainActivity.this).get(ContadorViewModel.class);

        contadorViewModel.getContador().observe(this, contador -> {
            //aquí o2
            binding.contadorTextView.setText(String.valueOf(contador));
       });

        binding.button.setOnClickListener(view -> {

            //es un hilo en background
            executorService.execute(() -> {
                for (int i = 1; i <= 10; i++) {
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

        /******************************* Internet *****************************/

        Toast.makeText(this, "Tiene internet: " + tengoInternet(), Toast.LENGTH_LONG).show();


        // Utilizando Retrofit
        typicodeService = new Retrofit.Builder()
                .baseUrl("https://my-json-server.typicode.com")
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(TypicodeService.class);

        binding.button3.setOnClickListener(view -> fetchProfileFromWs());

    }

    public void fetchProfileFromWs(){
        if(tengoInternet()){
            typicodeService.getProfile().enqueue(new Callback<Profile>() {
                @Override
                public void onResponse(Call<Profile> call, Response<Profile> response) {
                    //aca estoy en el UI Thread
                    if(response.isSuccessful()){
                        Profile profile = response.body();
                        binding.rptaTextView.setText(profile.getName());
                        Log.d("msg-test-ws-profile","name: " + profile.getName());
                        fetchCommentsFromWs();
                        fetchPotsFromWs(2);
                    }
                }

                @Override
                public void onFailure(Call<Profile> call, Throwable t) {
                    t.printStackTrace();
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
                            Log.d("msg-test-ws-comments","id: " + c.getId() + " | body: " + c.getBody());
                        }
                    }
                }

                @Override
                public void onFailure(Call<List<Comment>> call, Throwable t) {
                    t.printStackTrace();
                }
            });

            //typicodeService.getProfileWithData(nombre, apellido)
        }
    }

    public void fetchPotsFromWs(int id){
        if(tengoInternet()){
            typicodeService.existePost(id).enqueue(new Callback<List<Post>>() {
                @Override
                    public void onResponse(Call<List<Post>> call, Response<List<Post>> response) {
                    if(response.isSuccessful()){
                        List<Post> post = response.body();
                        for(Post p : post){
                            Log.d("msg-test-ws-post","id: " + p.getId()
                                    + " | title: " + p.getTitle());
                        }
                    }
                }

                @Override
                public void onFailure(Call<List<Post>> call, Throwable t) {
                    t.printStackTrace();
                }
            });

            //typicodeService.getProfileWithData(nombre, apellido)
        }
    }

    public boolean tengoInternet() {
        ConnectivityManager manager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeNetworkInfo = manager.getActiveNetworkInfo();
        boolean tieneInternet = activeNetworkInfo != null && activeNetworkInfo.isConnected();

        Log.d("msg-test-internet", "Internet: " + tieneInternet);

        return tieneInternet;
    }
}