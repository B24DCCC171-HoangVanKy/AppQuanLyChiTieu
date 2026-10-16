package com.example.th03final;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;

import java.util.ArrayList;
import java.text.Normalizer;
import java.util.Locale;

public class MainActivity extends BaseActivity {

    private TextView tvTotalAmount;
    private ListView lvExpenses;
    private Button btnAddExpense;
    private EditText etSearch;

    private ArrayList<Expense> expenseList;
    private final ArrayList<Expense> visibleExpenses = new ArrayList<>();
    private ExpenseAdapter adapter;

    private ActivityResultLauncher<Intent> addExpenseLauncher;
    private ActivityResultLauncher<Intent> detailExpenseLauncher;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (AccountStorage.getCurrentUsername(this).isEmpty()) {
            startActivity(new Intent(this, LoginActivity.class));
            finish();
            return;
        }
        setContentView(R.layout.activity_main);

        tvTotalAmount = findViewById(R.id.tvTotalAmount);
        lvExpenses = findViewById(R.id.lvExpenses);
        btnAddExpense = findViewById(R.id.btnAddExpense);
        etSearch = findViewById(R.id.etSearch);
        TextView greeting = findViewById(R.id.tvSignedIn);
        if (greeting != null) {
            greeting.setText(getString(R.string.signed_in_as, AccountStorage.getCurrentUsername(this)));
        }
        findViewById(R.id.btnLogout).setOnClickListener(v -> {
            AccountStorage.logout(this);
            Intent intent = new Intent(this, LoginActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            finish();
        });

        expenseList = ExpenseStorage.load(this);

        adapter = new ExpenseAdapter(this, visibleExpenses);
        lvExpenses.setAdapter(adapter);
        lvExpenses.setEmptyView(findViewById(R.id.tvEmpty));

        updateTotalAmount();
        etSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence text, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence text, int start, int before, int count) {
                filterExpenses();
            }

            @Override
            public void afterTextChanged(Editable text) {
            }
        });

        addExpenseLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == RESULT_OK && result.getData() != null) {
                        Expense newExpense = (Expense) result.getData().getSerializableExtra("new_expense");
                        if (newExpense != null) {
                            expenseList.add(newExpense);
                            ExpenseStorage.save(this, expenseList);
                            updateTotalAmount();
                        }
                    }
                }
        );

        detailExpenseLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == RESULT_OK && result.getData() != null) {
                        String deletedId = result.getData().getStringExtra("deleted_expense_id");
                        if (deletedId != null) {
                            removeExpenseById(deletedId);
                            ExpenseStorage.save(this, expenseList);
                            updateTotalAmount();
                        }
                    }
                }
        );

        btnAddExpense.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, AddExpenseActivity.class);
                addExpenseLauncher.launch(intent);
            }
        });

        lvExpenses.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Expense selectedExpense = adapter.getItem(position);
                Intent intent = new Intent(MainActivity.this, ExpenseDetailActivity.class);
                intent.putExtra("expense", selectedExpense);
                detailExpenseLauncher.launch(intent);
            }
        });
    }

    private void updateTotalAmount() {
        tvTotalAmount.setText(ExpenseUtils.formatCurrency(ExpenseUtils.total(expenseList)));
        filterExpenses();
    }

    private void filterExpenses() {
        String query = normalize(etSearch.getText().toString().trim());
        visibleExpenses.clear();
        for (Expense expense : expenseList) {
            if (normalize(expense.getName()).contains(query)) {
                visibleExpenses.add(expense);
            }
        }
        adapter.notifyDataSetChanged();
        TextView count = findViewById(R.id.tvExpenseCount);
        if (query.isEmpty()) {
            count.setText(getResources().getQuantityString(R.plurals.expense_count,
                    expenseList.size(), expenseList.size()));
        } else {
            count.setText(getString(R.string.search_count, visibleExpenses.size(), expenseList.size()));
        }
        TextView empty = findViewById(R.id.tvEmpty);
        empty.setText(expenseList.isEmpty() ? R.string.empty_expenses : R.string.no_search_results);
    }

    private String normalize(String text) {
        return Normalizer.normalize(text.toLowerCase(Locale.ROOT), Normalizer.Form.NFD)
                .replaceAll("\\p{M}+", "").replace('đ', 'd');
    }

    private void removeExpenseById(String id) {
        for (int i = 0; i < expenseList.size(); i++) {
            if (expenseList.get(i).getId().equals(id)) {
                expenseList.remove(i);
                break;
            }
        }
    }

}
