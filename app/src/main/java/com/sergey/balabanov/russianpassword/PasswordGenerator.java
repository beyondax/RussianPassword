package com.sergey.balabanov.russianpassword;

import android.util.Log;

import java.util.Random;

public class PasswordGenerator {

    private final String mLowerCase = "abcdefghijklmnopqrstuvwxyz";
    private final String mUpperCase = mLowerCase.toUpperCase();
    private final String mDigits = "0123456789";
    private final String mSymbols = "!@#$%^&*_=+-/.?<>)";
    private String mPassword;
    private int mLength;
    private boolean mUseUpperCase;
    private boolean mUseDigits;
    private boolean mUseSymbols;


    public PasswordGenerator(int length, boolean useUpperCase, boolean useDigits, boolean useSymbols) {

        mLength = length;
        mUseUpperCase = useUpperCase;
        mUseDigits = useDigits;
        mUseSymbols = useSymbols;

    }

    public String getPassword() {
        char[] password = new char[mLength];
        Random random = new Random();
        StringBuilder values = new StringBuilder(mLowerCase);
        if (mUseUpperCase) values.append(mUpperCase);
        if (mUseDigits) values.append(mDigits);
        if (mUseSymbols) values.append(mSymbols);
        Log.d("values", values.toString());
        for (int i = 0; i < password.length; i++) {
            password[i] = values.charAt(random.nextInt(values.length()));
        }
        mPassword = String.valueOf(password);
        return mPassword;
    }
}
