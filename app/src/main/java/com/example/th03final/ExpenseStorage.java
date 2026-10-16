package com.example.th03final;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;


public final class ExpenseStorage {
    private static final String KEY_EXPENSES = "expenses";

    private ExpenseStorage() {
    }

    private static SharedPreferences preferences(Context context) {
        String username = AccountStorage.getCurrentUsername(context);
        return context.getSharedPreferences("expenses_" + username, Context.MODE_PRIVATE);
    }

    public static ArrayList<Expense> load(Context context) {
        SharedPreferences preferences = preferences(context);
        ArrayList<Expense> expenses = new ArrayList<>();
        if (!preferences.contains(KEY_EXPENSES)) {
            String date = new SimpleDateFormat("dd/MM/yyyy", Locale.US).format(new Date());
            expenses.add(new Expense("Ăn sáng", 30000, "Ăn uống", date, ""));
            expenses.add(new Expense("Đổ xăng", 50000, "Đi lại", date, ""));
            expenses.add(new Expense("Mua sách", 120000, "Học tập", date, ""));
            save(context, expenses);
            return expenses;
        }
        try {
            JSONArray data = new JSONArray(preferences.getString(KEY_EXPENSES, "[]"));
            for (int i = 0; i < data.length(); i++) {
                JSONObject item = data.getJSONObject(i);
                expenses.add(new Expense(item.getString("id"), item.getString("name"),
                        item.getLong("amount"), item.getString("category"),
                        item.getString("date"), item.optString("note", "")));
            }
        } catch (JSONException e) {
            Log.e("ExpenseStorage", "Không đọc được dữ liệu đã lưu", e);
        }
        return expenses;
    }

    public static void save(Context context, ArrayList<Expense> expenses) {
        JSONArray data = new JSONArray();
        try {
            for (Expense expense : expenses) {
                JSONObject item = new JSONObject();
                item.put("id", expense.getId());
                item.put("name", expense.getName());
                item.put("amount", expense.getAmount());
                item.put("category", expense.getCategory());
                item.put("date", expense.getDate());
                item.put("note", expense.getNote());
                data.put(item);
            }
            preferences(context)
                    .edit().putString(KEY_EXPENSES, data.toString()).apply();
        } catch (JSONException e) {
            Log.e("ExpenseStorage", "Không lưu được danh sách", e);
        }
    }
}
