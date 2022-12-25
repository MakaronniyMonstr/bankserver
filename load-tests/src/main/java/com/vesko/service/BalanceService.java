package com.vesko.service;

import com.vesko.model.Balance;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface BalanceService {
    @GET("/balance/{id}")
    Call<Balance> getBalanceById(@Path("id") Integer id);

    @POST("/balance/{id}")
    Call<Void> updateBalanceById(@Path("id") Integer id,
                                 @Query("amount") Integer amount);
}
