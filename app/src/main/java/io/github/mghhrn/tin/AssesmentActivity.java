package io.github.mghhrn.tin;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.RadioGroup;
import android.widget.Toast;

import io.github.mghhrn.tin.database.AppDatabaseSingleton;
import io.github.mghhrn.tin.entity.TherapySession;

public class AssesmentActivity extends AppCompatActivity {

    private RadioGroup scoreRadioGroup;
    private Button finalSubmitButton;
    private long therapySessionId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_assesment);

        therapySessionId = getIntent().getExtras().getLong("therapySessionId");

        scoreRadioGroup = findViewById(R.id.score_radio_group);
        finalSubmitButton = findViewById(R.id.final_submit_button);

        finalSubmitButton.setOnClickListener(v -> this.onSubmitClicked());
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
        Intent intent = new Intent(this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }
}