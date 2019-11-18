package com.sergey.balabanov.russianpassword;

import android.app.Activity;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends Activity {

    private TextView mResultTextView;
    private EditText mSourceTextView;
    private View mCopyButton;
    private ImageView mQuality;
    private TextView mQualityTextView;
    private View mCopyGeneratedPasswordButton;
    private Button mGeneratePasswordButton;
    private TextView mGeneratedPasswordTextView;
    private SeekBar mSetPasswordLengthBar;
    private TextView mAdditionalSymbol;
    private TextView mGeneralQuantityTextView;
    private CompoundButton mUseUppercase;
    private CompoundButton mUseDigits;
    private CompoundButton mUseSymbols;


    private PasswordHelper mHelper;
    private PasswordGenerator mPasswordGenerator;
    private boolean mIsUppercase = false;
    private boolean mIsSymbols = false;
    private boolean mIsDigits = false;
    private int mDefaultLength = 8;
    private int mCurrentSeekBarLevel = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mHelper = new PasswordHelper(
                getResources().getStringArray(R.array.russian),
                getResources().getStringArray(R.array.english)
        );

        mAdditionalSymbol = findViewById(R.id.additional_symbol_amount);
        mResultTextView = findViewById(R.id.result_text);
        mSourceTextView = findViewById(R.id.source_text);
        mCopyButton = findViewById(R.id.button_copy);
        mCopyGeneratedPasswordButton = findViewById(R.id.button_copy_generated);
        mQuality = findViewById(R.id.quality);
        mQualityTextView = findViewById(R.id.quality_text);
        mUseUppercase = findViewById(R.id.check_uppercase);
        mUseDigits = findViewById(R.id.check_digits);
        mUseSymbols = findViewById(R.id.check_symbols);
        mGeneratePasswordButton = findViewById(R.id.button_generate_password);
        mGeneratedPasswordTextView = findViewById(R.id.generated_password_text);
        mUseUppercase = findViewById(R.id.check_uppercase);
        mUseSymbols = findViewById(R.id.check_symbols);
        mUseDigits = findViewById(R.id.check_digits);
        mSetPasswordLengthBar = findViewById(R.id.password_length);
        mGeneralQuantityTextView = findViewById(R.id.general_quantity);
        mCopyButton.setEnabled(false);
        mCopyGeneratedPasswordButton.setEnabled(false);


        mSetPasswordLengthBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {

                String additionalSymbols = getResources().getQuantityString(R.plurals.symbols, i, i);
                mAdditionalSymbol.setText(additionalSymbols);
                mCurrentSeekBarLevel = i + mDefaultLength;
                String additionSymbolsEqual = getResources().getQuantityString(R.plurals.symbols, mCurrentSeekBarLevel, mCurrentSeekBarLevel);
                mGeneralQuantityTextView.setText(additionSymbolsEqual);

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {


            }
        });

        mCopyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ClipboardManager manager = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
                manager.setPrimaryClip(ClipData.newPlainText(
                        MainActivity.this.getString(R.string.clipboard_title),
                        mResultTextView.getText()
                ));
                Toast.makeText(MainActivity.this, R.string.message_copied, Toast.LENGTH_SHORT)
                        .show();
            }
        });


        mCopyGeneratedPasswordButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ClipboardManager manager = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
                manager.setPrimaryClip(ClipData.newPlainText(
                        MainActivity.this.getString(R.string.clipboard_title),
                        mGeneratedPasswordTextView.getText()
                ));
                Toast.makeText(MainActivity.this, R.string.message_copied, Toast.LENGTH_SHORT)
                        .show();
            }
        });

        mUseUppercase.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                mIsUppercase = b;
            }
        });

        mUseSymbols.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                mIsSymbols = b;
            }
        });

        mUseDigits.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                mIsDigits = b;
            }
        });

        mSourceTextView.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                mResultTextView.setText(mHelper.convert(s));
                mCopyButton.setEnabled(s.length() > 0);
                int quality = mHelper.getQuality(s);
                mQuality.setImageLevel(quality * 1000);
                mQualityTextView.setText(getResources().getStringArray(R.array.qualities)[quality]);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        mGeneratePasswordButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(mCurrentSeekBarLevel > 0) {
                    mPasswordGenerator = new PasswordGenerator(mCurrentSeekBarLevel, mIsUppercase, mIsDigits, mIsSymbols);
                } else mPasswordGenerator = new PasswordGenerator(mDefaultLength, mIsUppercase, mIsDigits, mIsSymbols);
                mGeneratedPasswordTextView.setText(mPasswordGenerator.getPassword());
                mCopyGeneratedPasswordButton.setEnabled(true);
            }
        });
    }
}
