package io.github.mghhrn.tin;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import io.github.mghhrn.tin.util.SharedPreferencesUtil;

import static io.github.mghhrn.tin.util.SharedPreferencesUtil.isProfileCompleted;

public class MainActivity extends AppCompatActivity {

    private Button letsGoButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        isProfileCompleted(this);
        String accessToken = SharedPreferencesUtil.loadAccessToken(this);
        if (accessToken == null) {
            Intent intent = new Intent(this, LoginActivity.class);
            startActivity(intent);
        } else if (!isProfileCompleted(this)) {
            Intent intent = new Intent(this, ProfileCompletionActivity.class);
            startActivity(intent);
        }
        setContentView(R.layout.activity_main);
        letsGoButton = findViewById(R.id.lets_go_button);
        letsGoButton.setOnClickListener(v -> this.onLetsGoButtonPressed());
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    private void onLetsGoButtonPressed() {
        Intent intent = new Intent(this, FrequencySelectionActivity.class);
        startActivity(intent);
        finish();
    }
}