package com.zero.ci.widget.nicespinner;

import android.text.Spannable;

public interface SpinnerTextFormatter {
    Spannable format(String text);

    Spannable format(Object item);
}
