package eu.nets.pia.sample.ui.activity;

import android.os.Handler;
import android.os.Looper;
import android.support.constraint.ConstraintLayout;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import eu.nets.pia.data.model.PiaResult;
import eu.nets.pia.sample.R;
import eu.nets.pia.sample.ui.widget.CustomToolbar;

/**
 * MIT License
 * <p>
 * Copyright (c) 2019 Nets Denmark A/S
 * <p>
 * Permission is hereby granted, free of charge, to any person obtaining a copy  of this software
 * and associated documentation files (the "Software"), to deal  in the Software without restriction,
 * including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense,
 * and/or sell copies of the Software, and to permit persons to whom the Software is  furnished to do so,
 * subject to the following conditions:
 * The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.
 * <p>
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED,
 * INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE
 * AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM,
 * DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */

public class ConfirmationActivity extends AppCompatActivity {

    /**
     * This Activity is a sample of confirmation page (Success, Error or Cancelled)
     */

    public static final String BUNDLE_ERROR_OBJECT = "bundle_error_object";
    public static final String BUNDLE_PAYMENT_SUCCESS = "bundle_payment_success";
    public static final String BUNDLE_PAYMENT_CANCELED = "bundle_payment_canceled";
    public static final String BUNDLE_SUCCESS_MESSAGE = "bundle_success_message";
    public static final String BUNDLE_SUCCESS_TITLE = "bundle_success_title";

    @BindView(R.id.custom_toolbar)
    CustomToolbar mToolbar;
    @BindView(R.id.status_icon)
    ImageView mStatusIcon;
    @BindView(R.id.status_message)
    TextView mStatusMessage;
    @BindView(R.id.root_view)
    ConstraintLayout mRootView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confirmation);
        ButterKnife.bind(this);

        initView();

        boolean isError = !getIntent().getBooleanExtra(BUNDLE_PAYMENT_SUCCESS, false) &&
                !getIntent().getBooleanExtra(BUNDLE_PAYMENT_CANCELED, false);

        //dismiss this activity after 2 seconds
        new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
            @Override
            public void run() {
                onBackPressed();
            }
        }, isError ? 6000 : 2000);
        //for error case dismiss after 6 seconds and for success dismiss after 2 seconds
    }

    private void initView() {
        PiaResult piaError = getIntent().getParcelableExtra(BUNDLE_ERROR_OBJECT);

        if (getIntent().getBooleanExtra(BUNDLE_PAYMENT_CANCELED, false)) {
            //the user cancelled the transaction
            mStatusIcon.setBackgroundResource(R.drawable.outline_error_outline_white_48);
            mStatusMessage.setText(R.string.process_cancelled);
            mToolbar.setTitle(R.string.toolbar_title_cancelled);
            mRootView.setBackgroundColor(ContextCompat.getColor(this, R.color.custom_orange_color));
        } else if (getIntent().getBooleanExtra(BUNDLE_PAYMENT_SUCCESS, false)) {
            //the transaction has ended successfully
            mStatusIcon.setBackgroundResource(R.drawable.outline_check_circle_outline_white_48);
            mStatusMessage.setText(getIntent().getStringExtra(BUNDLE_SUCCESS_MESSAGE));
            mToolbar.setTitle(getIntent().getStringExtra(BUNDLE_SUCCESS_TITLE));
            mRootView.setBackgroundColor(ContextCompat.getColor(this, R.color.custom_light_blue_color));
        } else {
            //there was an error processing the transaction
            String errorMessage = "";
            if (piaError != null && piaError.getError() != null) {
                errorMessage = String.format(
                        "%1$s%n[%2$s]", piaError.getError().getMessage(this), piaError.getError().getCode().getStatusCode()
                );
            } else {
                errorMessage = getString(R.string.process_error);
            }
            mStatusIcon.setBackgroundResource(R.drawable.outline_highlight_off_white_48);
            mStatusMessage.setText(errorMessage);
            mToolbar.setTitle(R.string.toolbar_title_failed);
            mRootView.setBackgroundColor(ContextCompat.getColor(this, R.color.custom_red_color));
        }
    }

    @Override
    public void onBackPressed() {
        finish();
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_left);
    }
}
