package io.github.mghhrn.tin.dto;

import com.google.gson.annotations.SerializedName;

public class TokenDto {

    @SerializedName("accessToken")
    private String accessToken;

    @SerializedName("refreshToken")
    private String refreshToken;

    @SerializedName("expiresInMinutes")
    private Long expiresInMinutes;

    @SerializedName("userId")
    private Long userId;
}
