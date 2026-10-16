package com.example.th03final;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

public class ExpenseAdapter extends ArrayAdapter<Expense> {

    public ExpenseAdapter(Context context, List<Expense> expenses) {
        super(context, 0, expenses);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_expense, parent, false);
        }

        Expense expense = getItem(position);

        TextView tvItemName = convertView.findViewById(R.id.tvItemName);
        TextView tvItemAmount = convertView.findViewById(R.id.tvItemAmount);
        TextView tvItemCategory = convertView.findViewById(R.id.tvItemCategory);
        TextView tvItemDate = convertView.findViewById(R.id.tvItemDate);

        if (expense != null) {
            tvItemName.setText(expense.getName());
            tvItemAmount.setText(formatCurrency(expense.getAmount()));
            tvItemCategory.setText(expense.getCategory());
            tvItemDate.setText(expense.getDate());
        }

        return convertView;
    }

    public static String formatCurrency(long amount) {
        return ExpenseUtils.formatCurrency(amount);
    }
}
