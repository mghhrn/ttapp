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

    @SerializedName("hasCompletedProfile")
    private Boolean hasCompletedProfile;

    public String getAccessToken() {
        return accessToken;
    }

    public String getRefreshToken() {
        return refreshToken;
    }

    public Long getExpiresInMinutes() {
        return expiresInMinutes;
    }

    public Long getUserId() {
        return userId;
    }

    public Boolean getHasCompletedProfile() {
        return hasCompletedProfile;
    }
}
