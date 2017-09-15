package com.lhd.mvp.setpin;

import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.lhd.applock.R;
import com.lhd.module.Config;
import com.lhd.mvp.lockpin.LockPinPresenter;
import com.lhd.mvp.lockpin.LockPinPresenterImpl;
import com.lhd.mvp.logapp.MyLog;
import com.lhd.mvp.main.MainActivity;

/**
 * Created by D on 8/9/2017.
 */

public class SetPinFragment extends Fragment implements SetPinView, View.OnClickListener {
    private WindowManager windowManager;
    private View view;
    WindowManager.LayoutParams params;
    private LockPinPresenter lockPinPresenter;
    private SetPinPresenter setPinPresenter;
    private TextView txtPin;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return initView(inflater.inflate(R.layout.set_pin_layout, null));
    }
//    private Keyboard mKeyboard;
//    private CustomKeyboardView mKeyboardView;

    @Override
    public View initView(View view) {
//        mKeyboard = new Keyboard(getContext(), R.xml.keyboard);
//        mKeyboardView = (CustomKeyboardView) view.findViewById(R.id.keyboard_view);
//        mKeyboardView.setKeyboard(mKeyboard);
//        mKeyboardView.setOnKeyboardActionListener(new BasicOnKeyboardActionListener(getActivity()));
        lockPinPresenter = new LockPinPresenterImpl(getContext());
        setPinPresenter = new SetPinPresenterImpl(getContext());
        txtPin = (TextView) view.findViewById(R.id.set_pin_txt_input_code);
        txtPin.requestFocus();
//        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
        txtPin.setText("");
//        InputMethodManager imm = (InputMethodManager) getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
//
////        imm.showSoftInput(txtPin, InputMethodManager.SHOW_IMPLICIT);
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.CUPCAKE) {
//            imm.hideSoftInputFromWindow(txtPin.getWindowToken(), 0);
//        }
        Button btn1 = (Button) view.findViewById(R.id.btn_set_pin_1);
        Button btn2 = (Button) view.findViewById(R.id.btn_set_pin_2);
        Button btn3 = (Button) view.findViewById(R.id.btn_set_pin_3);
        Button btn4 = (Button) view.findViewById(R.id.btn_set_pin_4);
        Button btn5 = (Button) view.findViewById(R.id.btn_set_pin_5);
        Button btn6 = (Button) view.findViewById(R.id.btn_set_pin_6);
        Button btn7 = (Button) view.findViewById(R.id.btn_set_pin_7);
        Button btn8 = (Button) view.findViewById(R.id.btn_set_pin_8);
        Button btn9 = (Button) view.findViewById(R.id.btn_set_pin_9);
        Button btn0 = (Button) view.findViewById(R.id.btn_set_pin_0);
        ImageView btnBack = (ImageView) view.findViewById(R.id.btn_set_pin_back);
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (txtPin.getText().toString().length() > 0)
                    txtPin.setText(txtPin.getText().toString().substring(0, txtPin.getText().toString().length() - 1));
            }
        });
        btn1.setOnClickListener(this);
        btn2.setOnClickListener(this);
        btn3.setOnClickListener(this);
        btn4.setOnClickListener(this);
        btn5.setOnClickListener(this);
        btn6.setOnClickListener(this);
        btn7.setOnClickListener(this);
        btn8.setOnClickListener(this);
        btn9.setOnClickListener(this);
        btn0.setOnClickListener(this);
//        txtPin.setOnTouchListener(new View.OnTouchListener() {
//
//            @Override
//            public boolean onTouch(View v, MotionEvent event) {
//                // Dobbiamo intercettare l'evento onTouch in modo da aprire la
//                // nostra tastiera e prevenire che venga aperta quella di
//                // Android
////                showKeyboardWithAnimation();
////                getWindow().setSoftInputMode(
////                        WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
//                return true;
//            }
//        });
        password1 = "";
        password2 = "";
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            txtPin.setShowSoftInputOnFocus(true);
        }
        txtPin.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                String pin = charSequence.toString();
                if (pin.length() == 4 && password1.equals("")) {
                    txtPin.setText("");
                    txtPin.setHint(getResources().getString(R.string.set_pin_confirm_pin_code1_2));
                    password1 = pin;
                } else if (pin.length() == 4 && !password1.isEmpty()) {
                    password2 = pin;
                    if (password1.equals(password2)) {
                        byte[] bytesEncoded = Base64.encode(password2.getBytes(), 101);
                        txtPin.setText("");
                        MyLog.putStringValueByName(getContext(), Config.LOG_APP, Config.PIN_CODE, new String(bytesEncoded));
//                        InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
//                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.CUPCAKE) {
//                            imm.hideSoftInputFromWindow(txtPin.getWindowToken(), 0);
//                        }
                        try {
                            MyLog.putBooleanValueByName(getActivity(), Config.LOG_APP, Config.IS_FIRST_SET_LOCK, true);
                            ((MainActivity) getActivity()).startListAppFragment();
                        } catch (ClassCastException e) {
                            getActivity().finish();
                        }
                    } else {
                        password1 = "";
                        password2 = "";
                        txtPin.setHint(getResources().getString(R.string.set_pin_confirm_pin_code1_1));
                        Toast.makeText(getContext(), getResources().getString(R.string.set_pin_fail_pin_code), Toast.LENGTH_SHORT).show();

                    }
                }
                //  setPinPresenter.checkPassCode();
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        return view;
    }

    String pin = "";
    String password1 = "";
    String password2 = "";

    @Override
    public String getPinInput() {
        return null;
    }

    @Override
    public void showError(String s) {

    }

    @Override
    public void pass() {

    }

    @Override
    public void onClick(View view) {
        Button btPin = (Button) view;
        txtPin.setText(txtPin.getText().toString() + btPin.getText());
    }

}
