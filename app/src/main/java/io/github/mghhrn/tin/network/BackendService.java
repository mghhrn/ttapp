package io.github.mghhrn.tin.network;

import io.github.mghhrn.tin.dto.LoginDto;
import io.github.mghhrn.tin.dto.ProfileDto;
import io.github.mghhrn.tin.dto.SmsVerificationDto;
import io.github.mghhrn.tin.dto.TokenDto;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public interface BackendService {

    @POST("/api/v1/user/login")
    Call<Void> login(@Body LoginDto loginDto);

    @POST("/api/v1/user/login/sms-verification")
    Call<TokenDto> verifySms(@Body SmsVerificationDto smsVerificationDto);

    @POST("/api/v1/user/profile")
    Call<Void> completeProfile(@Header("Authorization") String authorization, @Body ProfileDto profileDto);
}
