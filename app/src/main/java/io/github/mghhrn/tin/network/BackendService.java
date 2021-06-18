package io.github.mghhrn.tin.network;

import io.github.mghhrn.tin.dto.LoginDto;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface BackendService {

    @POST("/api/v1/user/login")
    Call<Void> login(@Body LoginDto loginDto);
}
