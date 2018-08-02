package com.lihao.words.utils;

import android.app.Activity;
import android.view.inputmethod.InputMethodManager;

import static android.content.Context.INPUT_METHOD_SERVICE;

public class SoftInputUtils {

    public static void hideInputForce(Activity activity) {
        if (activity == null || activity.getCurrentFocus() == null)
            return;
        ((InputMethodManager) activity.getSystemService(INPUT_METHOD_SERVICE))
                .hideSoftInputFromWindow(activity.getCurrentFocus()
                        .getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
    }
}
