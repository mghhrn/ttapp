

package io.github.mghhrn.tin;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import io.github.mghhrn.tin.dto.LoginDto;
import io.github.mghhrn.tin.network.BackendClientSingleton;
import io.github.mghhrn.tin.network.BackendService;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

    private Button buttonTwoButton;
    private EditText phoneNumberEditText;
    private BackendService backendService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        init();
    }

    private void init() {
        backendService = BackendClientSingleton.getRetrofitInstance().create(BackendService.class);

        // Configure Button component
        buttonTwoButton = this.findViewById(R.id.login_button);
        buttonTwoButton.setOnClickListener(v -> this.onLoginButtonPressed());

        phoneNumberEditText = this.findViewById(R.id.phone_number_edit_text);
    }

    private void onLoginButtonPressed() {
        String cellphone = phoneNumberEditText.getText().toString();
        if (cellphone == null || cellphone.length() != 11) {
            Toast.makeText(this, R.string.invalid_phone_number, Toast.LENGTH_SHORT).show();
            return;
        }
        LoginDto loginDto = new LoginDto(cellphone);
        Call<Void> call = backendService.login(loginDto);
        call.enqueue(new Callback<Void>() {
            private Context context = getContext();
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                Toast.makeText(context, "Request was successful!", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(context, SmsVerificationActivity.class);
                intent.putExtra("cellphone", cellphone);
                startActivity(intent);
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Log.e("login", t.getMessage());
                Toast.makeText(context, "OMG! Something went wrong!", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private Context getContext() {
        return this;
    }

}