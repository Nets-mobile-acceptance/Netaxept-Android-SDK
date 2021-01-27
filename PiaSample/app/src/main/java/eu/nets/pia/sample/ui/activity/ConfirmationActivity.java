package eu.nets.pia.sample.ui.activity;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;

import butterknife.BindView;
import butterknife.ButterKnife;
import eu.nets.pia.sample.R;
import eu.nets.pia.sample.ui.widget.CustomToolbar;

/**
 * MIT License
 * <p>
 * Copyright (c) 2020 Nets Denmark A/S
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

    public static final String RESULT = "RESULT";
    public static final String TITLE = "TITLE";
    public static final String MESSAGE = "MESSAGE";

    @BindView(R.id.custom_toolbar)
    CustomToolbar mToolbar;
    @BindView(R.id.status_icon)
    ImageView mStatusIcon;
    @BindView(R.id.status_message)
    TextView mStatusMessage;
    @BindView(R.id.root_view)
    ConstraintLayout mRootView;

    public enum Result {
        SUCCESS, CANCELLATION, FAILURE
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confirmation);
        ButterKnife.bind(this);

        Result result = (Result) getIntent().getSerializableExtra("RESULT");
        String title = getIntent().getStringExtra("TITLE");
        String message = getIntent().getStringExtra("MESSAGE");
        switch (result) {
            // TODO: Set the correct background colors
            case SUCCESS:
                mStatusIcon.setBackgroundResource(R.drawable.outline_check_circle_outline_white_48);
                mRootView.setBackgroundColor(
                        ContextCompat.getColor(this, R.color.custom_light_blue_color)
                );
                break;
            case CANCELLATION:
                mStatusIcon.setBackgroundResource(R.drawable.outline_error_outline_white_48);
                mRootView.setBackgroundColor(
                        ContextCompat.getColor(this, R.color.custom_orange_color)
                );
                //TODO Remove this after message from SDK is defined
                message = getString(R.string.process_cancelled);
                break;
            case FAILURE:
                mStatusIcon.setBackgroundResource(R.drawable.ic_retry);
                mRootView.setBackgroundColor(
                        ContextCompat.getColor(this, R.color.custom_red_color)
                );
                //TODO Remove this after message from SDK is defined
                message = getString(R.string.process_error);
                break;
        }
        mToolbar.setTitle(title);
        mStatusMessage.setText(message);

        //dismiss this activity after 2 seconds
        new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
            @Override
            public void run() {
                onBackPressed();
            }
        }, (result == Result.FAILURE) ? 6000 : 2000);
        //for error case dismiss after 6 seconds and for success dismiss after 2 seconds
    }

    @Override
    public void onBackPressed() {
        finish();
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_left);
    }
}
