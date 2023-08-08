package com.shoesock.personalassistant1.functions.progress_bar;

import android.view.View;
import android.widget.ProgressBar;

public class ProgressBarUtils {
    private ProgressBar progressBar;

    public ProgressBarUtils(ProgressBar progressBar) {
        this.progressBar = progressBar;
    }

    public void showProgressBar() {
        if (progressBar != null) {
            progressBar.setVisibility(View.VISIBLE);
        }
    }

    public void hideProgressBar() {
        if (progressBar != null) {
            progressBar.setVisibility(View.GONE);
        }
    }

} // close the ProgressBarUtils class
