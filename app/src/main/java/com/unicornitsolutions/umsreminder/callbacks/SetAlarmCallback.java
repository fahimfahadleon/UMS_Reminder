package com.unicornitsolutions.umsreminder.callbacks;

public interface SetAlarmCallback {
    void onSuccessful(String time);
    void onFailed(String resone);
    void onPassed(String s);
}
