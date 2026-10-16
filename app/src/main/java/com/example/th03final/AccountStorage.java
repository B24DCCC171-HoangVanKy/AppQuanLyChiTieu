package com.example.th03final;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Base64;

import java.security.GeneralSecurityException;
import java.security.MessageDigest;
import java.security.SecureRandom;

import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;

public final class AccountStorage {
    private AccountStorage() {
    }

    private static SharedPreferences preferences(Context context) {
        return context.getSharedPreferences("accounts", Context.MODE_PRIVATE);
    }

    public static boolean exists(Context context, String username) {
        return preferences(context).contains("password_" + username);
    }

    public static boolean register(Context context, String username, String password) {
        if (exists(context, username)) {
            return false;
        }
        byte[] salt = new byte[16];
        new SecureRandom().nextBytes(salt);
        try {
            String encodedSalt = Base64.encodeToString(salt, Base64.NO_WRAP);
            String encodedPassword = Base64.encodeToString(hash(password, salt), Base64.NO_WRAP);
            return preferences(context).edit()
                    .putString("salt_" + username, encodedSalt)
                    .putString("password_" + username, encodedPassword)
                    .commit();
        } catch (GeneralSecurityException e) {
            return false;
        }
    }

    public static boolean login(Context context, String username, String password) {
        SharedPreferences data = preferences(context);
        String storedPassword = data.getString("password_" + username, "");
        String storedSalt = data.getString("salt_" + username, "");
        if (storedPassword.isEmpty() || storedSalt.isEmpty()) {
            return false;
        }
        try {
            byte[] salt = Base64.decode(storedSalt, Base64.NO_WRAP);
            byte[] expected = Base64.decode(storedPassword, Base64.NO_WRAP);
            return MessageDigest.isEqual(expected, hash(password, salt));
        } catch (GeneralSecurityException | IllegalArgumentException e) {
            return false;
        }
    }

    public static void setCurrentUsername(Context context, String username) {
        preferences(context).edit().putString("current_username", username).apply();
    }

    public static String getCurrentUsername(Context context) {
        return preferences(context).getString("current_username", "");
    }

    public static void logout(Context context) {
        preferences(context).edit().remove("current_username").apply();
    }

    private static byte[] hash(String password, byte[] salt) throws GeneralSecurityException {
        PBEKeySpec key = new PBEKeySpec(password.toCharArray(), salt, 60000, 256);
        try {
            return SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1").generateSecret(key).getEncoded();
        } finally {
            key.clearPassword();
        }
    }
}
