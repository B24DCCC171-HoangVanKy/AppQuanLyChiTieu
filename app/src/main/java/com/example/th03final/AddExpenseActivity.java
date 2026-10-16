package com.example.th03final;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;


import java.util.Calendar;
import java.util.Locale;

public class AddExpenseActivity extends BaseActivity {

    private EditText etExpenseName;
    private EditText etExpenseAmount;
    private Spinner spCategory;
    private DatePicker dpDate;
    private EditText etExpenseNote;
    private Button btnSaveExpense;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_expense);

        etExpenseName = findViewById(R.id.etExpenseName);
        etExpenseAmount = findViewById(R.id.etExpenseAmount);
        spCategory = findViewById(R.id.spCategory);
        dpDate = findViewById(R.id.dpDate);
        etExpenseNote = findViewById(R.id.etExpenseNote);
        btnSaveExpense = findViewById(R.id.btnSaveExpense);
        findViewById(R.id.btnCancel).setOnClickListener(v -> finish());

        Calendar calendar = Calendar.getInstance();
        dpDate.init(
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH),
                null
        );

        btnSaveExpense.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveExpense();
            }
        });
    }

    private void saveExpense() {
        etExpenseName.setError(null);
        etExpenseAmount.setError(null);
        String name = etExpenseName.getText().toString().trim();
        String amountStr = etExpenseAmount.getText().toString().trim();
        String category = spCategory.getSelectedItem().toString();
        String note = etExpenseNote.getText().toString().trim();

        if (name.isEmpty()) {
            etExpenseName.setError("Tên khoản chi không được để trống");
            etExpenseName.requestFocus();
            return;
        }

        if (amountStr.isEmpty()) {
            etExpenseAmount.setError("Số tiền không được để trống");
            etExpenseAmount.requestFocus();
            return;
        }

        long amount;
        try {
            amount = ExpenseUtils.parseAmount(amountStr);
        } catch (NumberFormatException e) {
            etExpenseAmount.setError("Nhập số nguyên lớn hơn 0 và không vượt quá giới hạn cho phép");
            etExpenseAmount.requestFocus();
            return;
        }

        int day = dpDate.getDayOfMonth();
        int month = dpDate.getMonth() + 1;
        int year = dpDate.getYear();
        String dateFormatted = String.format(Locale.getDefault(), "%02d/%02d/%04d", day, month, year);

        Expense newExpense = new Expense(name, amount, category, dateFormatted, note);

        Intent resultIntent = new Intent();
        resultIntent.putExtra("new_expense", newExpense);
        setResult(RESULT_OK, resultIntent);
        Toast.makeText(this, "Đã thêm khoản chi", Toast.LENGTH_SHORT).show();
        finish();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putInt("date_year", dpDate.getYear());
        outState.putInt("date_month", dpDate.getMonth());
        outState.putInt("date_day", dpDate.getDayOfMonth());
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        dpDate.updateDate(savedInstanceState.getInt("date_year", dpDate.getYear()),
                savedInstanceState.getInt("date_month", dpDate.getMonth()),
                savedInstanceState.getInt("date_day", dpDate.getDayOfMonth()));
    }
}
