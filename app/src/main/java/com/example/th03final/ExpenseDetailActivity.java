package com.example.th03final;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;

public class ExpenseDetailActivity extends BaseActivity {

    private TextView tvDetailName;
    private TextView tvDetailAmount;
    private TextView tvDetailCategory;
    private TextView tvDetailDate;
    private TextView tvDetailNote;
    private Button btnBack;
    private Button btnDelete;

    private Expense currentExpense;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_expense_detail);

        tvDetailName = findViewById(R.id.tvDetailName);
        tvDetailAmount = findViewById(R.id.tvDetailAmount);
        tvDetailCategory = findViewById(R.id.tvDetailCategory);
        tvDetailDate = findViewById(R.id.tvDetailDate);
        tvDetailNote = findViewById(R.id.tvDetailNote);
        btnBack = findViewById(R.id.btnBack);
        btnDelete = findViewById(R.id.btnDelete);

        if (getIntent() != null && getIntent().hasExtra("expense")) {
            currentExpense = (Expense) getIntent().getSerializableExtra("expense");
        }

        if (currentExpense == null) {
            Toast.makeText(this, "Không thể tải thông tin khoản chi", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        displayExpenseDetail();

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setResult(RESULT_CANCELED);
                finish();
            }
        });

        btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AlertDialog.Builder(ExpenseDetailActivity.this)
                        .setTitle("Xóa khoản chi?")
                        .setMessage("Bạn muốn xóa khoản \"" + currentExpense.getName() + "\"?")
                        .setNegativeButton("Hủy", null)
                        .setPositiveButton("Xóa", (dialog, which) -> deleteExpense())
                        .show();
            }
        });
    }

    private void displayExpenseDetail() {
        tvDetailName.setText(currentExpense.getName());
        tvDetailAmount.setText(ExpenseAdapter.formatCurrency(currentExpense.getAmount()));
        tvDetailCategory.setText(currentExpense.getCategory());
        tvDetailDate.setText(currentExpense.getDate());

        String note = currentExpense.getNote();
        if (note == null || note.trim().isEmpty()) {
            tvDetailNote.setText(R.string.no_note);
        } else {
            tvDetailNote.setText(note);
        }
    }

    private void deleteExpense() {
        Intent resultIntent = new Intent();
        resultIntent.putExtra("deleted_expense_id", currentExpense.getId());
        setResult(RESULT_OK, resultIntent);
        Toast.makeText(this, "Đã xóa khoản chi: " + currentExpense.getName(), Toast.LENGTH_SHORT).show();
        finish();
    }
}
