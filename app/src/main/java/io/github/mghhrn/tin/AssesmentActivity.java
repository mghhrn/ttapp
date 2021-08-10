package io.github.mghhrn.tin;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.Collections;

import io.github.mghhrn.tin.database.AppDatabaseSingleton;
import io.github.mghhrn.tin.dto.TherapySessionDto;
import io.github.mghhrn.tin.entity.TherapySession;
import io.github.mghhrn.tin.network.BackendClientSingleton;
import io.github.mghhrn.tin.network.BackendService;
import io.github.mghhrn.tin.util.SharedPreferencesUtil;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AssesmentActivity extends AppCompatActivity {

    private RadioGroup scoreRadioGroup;
    private Button finalSubmitButton;
    private long therapySessionId;
    private BackendService backendService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_assesment);

        therapySessionId = getIntent().getExtras().getLong("therapySessionId");

        scoreRadioGroup = findViewById(R.id.score_radio_group);
        finalSubmitButton = findViewById(R.id.final_submit_button);

        finalSubmitButton.setOnClickListener(v -> this.onSubmitClicked());
        backendService = BackendClientSingleton.getRetrofitInstance().create(BackendService.class);
    }

    private void onSubmitClicked() {
        int scoreButtonId = scoreRadioGroup.getCheckedRadioButtonId();
        if (scoreButtonId == -1) {
            Toast.makeText(this, "Satisfaction score is not selected!", Toast.LENGTH_SHORT).show();
            scoreRadioGroup.clearCheck();
            return;
        }
        int satisfactionPoint;
        switch (scoreButtonId) {
            case R.id.great:
                satisfactionPoint = 5;
                break;
            case R.id.good:
                satisfactionPoint = 4;
                break;
            case R.id.soso:
                satisfactionPoint = 3;
                break;
            case R.id.bad:
                satisfactionPoint = 2;
                break;
            case R.id.awful:
                satisfactionPoint = 1;
                break;
            default:
                Toast.makeText(this, "Unknown satisfaction score is faced!", Toast.LENGTH_SHORT).show();
                scoreRadioGroup.clearCheck();
                return;
        }

        TherapySession session = AppDatabaseSingleton.getInstance(this).therapySessionDao().findById(therapySessionId);
        session.setSatisfactionPoint(satisfactionPoint);
        AppDatabaseSingleton.getInstance(this).therapySessionDao().update(session);

        TherapySessionDto dto = new TherapySessionDto();
        dto.setAudioBalance(session.getAudioBalance());
        dto.setDuration(session.getDuration());
        dto.setFilteredRange(session.getFilteredRange());
        dto.setSatisfactionPoint(session.getSatisfactionPoint());
        dto.setSelectedFrequency(session.getSelectedFrequency());
        dto.setStartedAt(session.getStartedAt().getTime());
        dto.setVolume(session.getVolume());
        String authorizationHeader = "Bearer " + SharedPreferencesUtil.loadAccessToken(this);
        Call<Void> call = backendService.sendTherapySessions(authorizationHeader, Collections.singletonList(dto));
        finalSubmitButton.setEnabled(false);

        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    TherapySession session = AppDatabaseSingleton.getInstance(getApplicationContext()).therapySessionDao().findById(therapySessionId);
                    session.setSentToServer(true);
                    AppDatabaseSingleton.getInstance(getApplicationContext()).therapySessionDao().update(session);
                    Toast.makeText(getApplicationContext(), "Data has been sent!", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                    finish();
                } else {
                    Toast.makeText(getApplicationContext(), "Response was not successful!", Toast.LENGTH_SHORT).show();
                    finalSubmitButton.setEnabled(true);
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                finalSubmitButton.setEnabled(true);
                Toast.makeText(getApplicationContext(), "Submit again, please!", Toast.LENGTH_SHORT).show();
            }
        });
    }
}