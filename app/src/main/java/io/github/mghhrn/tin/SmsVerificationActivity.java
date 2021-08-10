package io.github.mghhrn.tin;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import io.github.mghhrn.tin.dto.SmsVerificationDto;
import io.github.mghhrn.tin.dto.TokenDto;
import io.github.mghhrn.tin.network.BackendClientSingleton;
import io.github.mghhrn.tin.network.BackendService;
import io.github.mghhrn.tin.util.SharedPreferencesUtil;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SmsVerificationActivity extends AppCompatActivity {

    private BackendService backendService;
    private Button verifyButton;
    private EditText smsCodeEditText;
    private String userCellphone;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sms_verification);
        userCellphone = getIntent().getExtras().getString("cellphone");
        init();
    }

    private void init() {
        backendService = BackendClientSingleton.getRetrofitInstance().create(BackendService.class);
        verifyButton = this.findViewById(R.id.submit_profile_button);
        verifyButton.setOnClickListener(v -> this.onVerifyButtonPressed());
        smsCodeEditText = this.findViewById(R.id.age_edit_text);
    }

    private void onVerifyButtonPressed() {
        String verificationCode = smsCodeEditText.getText().toString();
        if (verificationCode == null || verificationCode.length() != 4) {
            Toast.makeText(this, R.string.invalid_phone_number, Toast.LENGTH_SHORT).show();
            return;
        }
        SmsVerificationDto dto = new SmsVerificationDto(userCellphone, verificationCode);
        Call<TokenDto> tokenDtoCall = backendService.verifySms(dto);
        tokenDtoCall.enqueue(new Callback<TokenDto>() {
            private Context context = getContext();
            @Override
            public void onResponse(Call<TokenDto> call, Response<TokenDto> response) {
                //call next Activity
                Toast.makeText(context, "Verification was successful!", Toast.LENGTH_SHORT).show();
                TokenDto tokenDto = response.body();
                SharedPreferencesUtil.saveTokenData(context, tokenDto);
                if (tokenDto.getHasCompletedProfile()) {
                    SharedPreferencesUtil.setProfileCompleted(context);
                    Intent intent = new Intent(context, MainActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                } else {
                    Intent intent = new Intent(context, ProfileCompletionActivity.class);
                    startActivity(intent);
                }
                finish();
            }

            @Override
            public void onFailure(Call<TokenDto> call, Throwable t) {
                Log.e("sms-verification", t.getMessage());
                Toast.makeText(context, "OMG! Something went wrong!", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private Context getContext() {
        return this;
    }
}
