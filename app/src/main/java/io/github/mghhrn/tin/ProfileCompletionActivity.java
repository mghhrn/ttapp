package io.github.mghhrn.tin;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.Toast;

import io.github.mghhrn.tin.dto.ProfileDto;
import io.github.mghhrn.tin.network.BackendClientSingleton;
import io.github.mghhrn.tin.network.BackendService;
import io.github.mghhrn.tin.util.SharedPreferencesUtil;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProfileCompletionActivity extends AppCompatActivity {

    private EditText ageEditText;
    private RadioGroup genderRadioGroup;
    private Button submitButton;
    private BackendService backendService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_completion);
        ageEditText = findViewById(R.id.age_edit_text);
        genderRadioGroup = findViewById(R.id.gender_radio_group);
        submitButton = findViewById(R.id.submit_profile_button);
        submitButton.setOnClickListener(v -> this.onSubmitButtonPressed());
        backendService = BackendClientSingleton.getRetrofitInstance().create(BackendService.class);
    }

    private void onSubmitButtonPressed() {
        String ageString = ageEditText.getText().toString();
        if (ageString == null || ageString.equals("")) {
            Toast.makeText(this, R.string.invalid_age, Toast.LENGTH_SHORT).show();
            return;
        }
        Long age = Long.valueOf(ageString);
        // todo: check value of age is valid

        int checkedRadioButtonId = genderRadioGroup.getCheckedRadioButtonId();
        if (checkedRadioButtonId == -1) {
            Toast.makeText(this, R.string.gender_not_selected, Toast.LENGTH_SHORT).show();
            genderRadioGroup.clearCheck();
            return;
        }

        String selectedGender;
        switch (checkedRadioButtonId) {
            case R.id.male:
                selectedGender = "MALE";
                break;
            case R.id.female:
                selectedGender = "FEMALE";
                break;
            case R.id.unknown:
                selectedGender = "UNKNOWN";
                break;
            default:
                Toast.makeText(this, R.string.invalid_gender_radio_button_id, Toast.LENGTH_SHORT).show();
                return;
        }

        ProfileDto profileDto = new ProfileDto(selectedGender, age);
        String authorizationHeader = "Bearer " + SharedPreferencesUtil.loadAccessToken(this);
        Call<Void> call = backendService.completeProfile(authorizationHeader, profileDto);
        call.enqueue(new Callback<Void>() {
            private Context context = getContext();

            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                Toast.makeText(context, "Profile updated successfully!", Toast.LENGTH_SHORT).show();
                SharedPreferencesUtil.setProfileCompleted(context);
                Intent intent = new Intent(context, MainActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Log.e("profile-completion", t.getMessage());
                Toast.makeText(context, "OPS! Something went wrong while updating profile!", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private Context getContext() {
        return this;
    }
}