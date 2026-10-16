package com.example.th03final;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.core.content.ContextCompat;

public class LoginActivity extends BaseActivity {

    private EditText etUsername;
    private EditText etPassword;
    private TextView tvError;
    private Button btnLogin;
    private ActivityResultLauncher<Intent> registerLauncher;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        etUsername = findViewById(R.id.etUsername);
        etPassword = findViewById(R.id.etPassword);
        tvError = findViewById(R.id.tvError);
        btnLogin = findViewById(R.id.btnLogin);
        registerLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(), result -> {
                    if (result.getResultCode() == RESULT_OK && result.getData() != null) {
                        etUsername.setText(result.getData().getStringExtra("registered_username"));
                        etPassword.setText("");
                        tvError.setText(R.string.register_success);
                        tvError.setTextColor(ContextCompat.getColor(this, R.color.primary_color));
                        tvError.setVisibility(View.VISIBLE);
                    }
                });
        findViewById(R.id.btnOpenRegister).setOnClickListener(v ->
                registerLauncher.launch(new Intent(this, RegisterActivity.class)));

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                handleLogin();
            }
        });
    }

    private void handleLogin() {
        String username = etUsername.getText().toString().trim();
        String password = etPassword.getText().toString();
        tvError.setTextColor(ContextCompat.getColor(this, R.color.red_delete));

        if (username.isEmpty() || password.isEmpty()) {
            tvError.setText(R.string.login_empty_error);
            tvError.setVisibility(View.VISIBLE);
            return;
        }

        if (AccountStorage.login(this, username, password)) {
            AccountStorage.setCurrentUsername(this, username);
            tvError.setVisibility(View.GONE);
            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
            startActivity(intent);
            finish();
        } else {
            tvError.setText(R.string.login_wrong_error);
            tvError.setVisibility(View.VISIBLE);
        }
    }
}
