package com.example.th03final;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

public class RegisterActivity extends BaseActivity {
    private EditText etUsername;
    private EditText etPassword;
    private EditText etConfirmPassword;
    private TextView tvRegisterError;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        etUsername = findViewById(R.id.etRegisterUsername);
        etPassword = findViewById(R.id.etRegisterPassword);
        etConfirmPassword = findViewById(R.id.etConfirmPassword);
        tvRegisterError = findViewById(R.id.tvRegisterError);

        findViewById(R.id.btnRegister).setOnClickListener(v -> register());
        findViewById(R.id.btnBackToLogin).setOnClickListener(v -> finish());
    }

    private void register() {
        String username = etUsername.getText().toString().trim();
        String password = etPassword.getText().toString();
        String confirmation = etConfirmPassword.getText().toString();

        etUsername.setError(null);
        etPassword.setError(null);
        etConfirmPassword.setError(null);
        tvRegisterError.setVisibility(View.GONE);

        if (!username.matches("[a-zA-Z0-9_]{3,30}")) {
            etUsername.setError(getString(R.string.username_rule));
            etUsername.requestFocus();
            return;
        }
        if (password.trim().isEmpty() || password.length() < 6) {
            etPassword.setError(getString(R.string.password_rule));
            etPassword.requestFocus();
            return;
        }
        if (!password.equals(confirmation)) {
            etConfirmPassword.setError(getString(R.string.password_mismatch));
            etConfirmPassword.requestFocus();
            return;
        }
        if (AccountStorage.exists(this, username)) {
            etUsername.setError(getString(R.string.username_exists));
            etUsername.requestFocus();
            return;
        }
        if (!AccountStorage.register(this, username, password)) {
            tvRegisterError.setText(R.string.register_failed);
            tvRegisterError.setVisibility(View.VISIBLE);
            return;
        }

        Intent result = new Intent();
        result.putExtra("registered_username", username);
        setResult(RESULT_OK, result);
        finish();
    }
}
