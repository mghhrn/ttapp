package io.github.mghhrn.tin;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.widget.TextView;

public class TherapySessionActivity extends AppCompatActivity {

    private TextView minuteTextView;
    private TextView secondTextView;

    private long therapySessionId;
    private double selectedFrequency;
    private int duration;
    private int filteredRange;
    private String selectedBalance;
    private Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_therapy_session);

        therapySessionId = getIntent().getExtras().getLong("therapySessionId");
        selectedFrequency = getIntent().getExtras().getDouble("selectedFrequency");
        duration = getIntent().getExtras().getInt("duration");
        filteredRange = getIntent().getExtras().getInt("filteredRange");
        selectedBalance = getIntent().getExtras().getString("selectedBalance");

        minuteTextView = findViewById(R.id.minute);
        secondTextView = findViewById(R.id.second);
        context = this;

        new CountDownTimer(duration * 60 * 1000, 1000) {

            @Override
            public void onTick(long millisUntilFinished) {
                long totalRemainingSeconds = millisUntilFinished / 1000;
                Long remainingMinutes = totalRemainingSeconds / 60;
                Long remainingSeconds = totalRemainingSeconds % 60;
                minuteTextView.setText(remainingMinutes.toString());
                secondTextView.setText(remainingSeconds.toString());
            }

            @Override
            public void onFinish() {
                Intent intent = new Intent(context, AssesmentActivity.class);
                intent.putExtra("therapySessionId", therapySessionId);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
            }
        }.start();
    }
}