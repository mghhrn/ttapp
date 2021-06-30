package io.github.mghhrn.tin.util;

import android.content.Context;
import android.content.SharedPreferences;

import io.github.mghhrn.tin.dto.TokenDto;

public class SharedPreferencesUtil {
    private static final String SHARED_PREF = "appSharedPrefs";
    private static final String ACCESS_TOKEN_KEY = "ACCESS_TOKEN";
    private static final String REFRESH_TOKEN_KEY = "REFRESH_TOKEN";
    private static final String PROFILE_COMPLETED = "PROFILE_COMPLETED";
    private static final String TOKEN_EXPIRES_IN_MINUTE = "TOKEN_EXPIRES_IN_MINUTE";
    private static final String USER_ID = "USER_ID";

    public static void saveTokenData(Context context, TokenDto tokenDto) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(SHARED_PREF, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(ACCESS_TOKEN_KEY, tokenDto.getAccessToken());
        editor.putString(REFRESH_TOKEN_KEY, tokenDto.getRefreshToken());
        editor.putLong(TOKEN_EXPIRES_IN_MINUTE, tokenDto.getExpiresInMinutes());
        editor.putLong(USER_ID, tokenDto.getUserId());
        editor.apply();
    }

    public static void setProfileCompleted(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(SHARED_PREF, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean(PROFILE_COMPLETED, true);
        editor.apply();
    }

    public static String loadAccessToken(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(SHARED_PREF, Context.MODE_PRIVATE);
        return sharedPreferences.getString(ACCESS_TOKEN_KEY, null);
    }

    public static String loadRefreshToken(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(SHARED_PREF, Context.MODE_PRIVATE);
        return sharedPreferences.getString(REFRESH_TOKEN_KEY, null);
    }

    public static boolean isProfileCompleted(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(SHARED_PREF, Context.MODE_PRIVATE);
        return sharedPreferences.getBoolean(PROFILE_COMPLETED, false);
    }
}
