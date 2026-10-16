package com.example.th03final;

import java.math.BigInteger;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.List;
import java.util.Locale;

public final class ExpenseUtils {
    private ExpenseUtils() {
    }

    public static long parseAmount(String input) {
        String value = input.trim();
        if (!value.matches("[0-9]+")) {
            throw new NumberFormatException("Số tiền chỉ được chứa chữ số");
        }
        long amount = Long.parseLong(value);
        if (amount <= 0) {
            throw new NumberFormatException("Số tiền phải lớn hơn 0");
        }
        return amount;
    }
    public static BigInteger total(List<Expense> expenses) {
        BigInteger total = BigInteger.ZERO;
        for (Expense expense : expenses) {
            total = total.add(BigInteger.valueOf(expense.getAmount()));
        }
        return total;
    }

    public static String formatCurrency(Number amount) {
        DecimalFormatSymbols symbols = new DecimalFormatSymbols(Locale.US);
        symbols.setGroupingSeparator('.');
        return new DecimalFormat("#,##0 'đ'", symbols).format(amount);
    }
}
