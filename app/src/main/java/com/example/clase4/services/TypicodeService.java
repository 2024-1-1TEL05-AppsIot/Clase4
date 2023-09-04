package com.example.clase4.services;

import com.example.clase4.dto.Comment;
import com.example.clase4.dto.Profile;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface TypicodeService {

    @GET("/typicode/demo/profile")
    Call<Profile> getProfile();

    @GET("/typicode/demo/comments")
    Call<List<Comment>> getComments();

    // de la forma /prueba?nombre=pedro&apellido=perez
    @GET("/prueba")
    Call<Profile> getProfileWithData(@Query("nombre") String nombre,
                                     @Query("apellido") String apellido);
}
