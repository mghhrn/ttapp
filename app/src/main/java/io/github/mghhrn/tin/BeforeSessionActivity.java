package io.github.mghhrn.tin;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.NumberPicker;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.Date;

import io.github.mghhrn.tin.database.AppDatabaseSingleton;
import io.github.mghhrn.tin.entity.TherapySession;

public class BeforeSessionActivity extends AppCompatActivity {

    private NumberPicker numberPicker;
    private RadioGroup balanceRadioGroup;
    private Button startSessionButton;
    private int duration = 1;
    private long therapySessionId;
    private double selectedFrequency;
    public static int FILTERED_RANGE = 2000; // Hz

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_before_session);

        therapySessionId = getIntent().getExtras().getLong("therapySessionId");
        selectedFrequency = getIntent().getExtras().getDouble("selectedFrequency");

        numberPicker = findViewById(R.id.duration_number_picker);
        numberPicker.setMaxValue(20);
        numberPicker.setMinValue(1);
        numberPicker.setValue(duration);
        numberPicker.setOnValueChangedListener(this::onNumberPickerValueChanged);

        balanceRadioGroup = findViewById(R.id.balance_radio_group);
        startSessionButton = findViewById(R.id.start_session_button);

        startSessionButton.setOnClickListener(v -> this.onButtonClicked());
    }

    private void onButtonClicked() {
        int radioButtonId = balanceRadioGroup.getCheckedRadioButtonId();
        if (radioButtonId == -1) {
            Toast.makeText(this, "Balance is not selected!", Toast.LENGTH_SHORT).show();
            balanceRadioGroup.clearCheck();
            return;
        }
        String selectedBalance;
        switch (radioButtonId) {
            case R.id.left:
                selectedBalance = "LEFT";
                break;
            case R.id.left_right:
                selectedBalance = "BOTH";
                break;
            case R.id.right:
                selectedBalance = "RIGHT";
                break;
            default:
                Toast.makeText(this, "Unknown balance value is faced!", Toast.LENGTH_SHORT).show();
                balanceRadioGroup.clearCheck();
                return;
        }

        TherapySession session = AppDatabaseSingleton.getInstance(this).therapySessionDao().findById(therapySessionId);
        session.setAudioBalance(selectedBalance);
        session.setDuration(duration);
        session.setFilteredRange(FILTERED_RANGE);
        session.setStartedAt(new Date());
        AppDatabaseSingleton.getInstance(this).therapySessionDao().update(session);
        
        Intent intent = new Intent(this, TherapySessionActivity.class);
        intent.putExtra("therapySessionId", therapySessionId);
        intent.putExtra("duration", duration);
        intent.putExtra("filteredRange", FILTERED_RANGE);
        intent.putExtra("selectedFrequency", selectedFrequency);
        intent.putExtra("selectedBalance", selectedBalance);
        startActivity(intent);
        finish();
    }

    private void onNumberPickerValueChanged(NumberPicker numberPicker, int i, int i1) {
        this.duration = numberPicker.getValue();
    }
}