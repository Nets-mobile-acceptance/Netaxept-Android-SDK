package eu.nets.pia.sample.ui.activity;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import eu.nets.pia.sample.R;
import eu.nets.pia.sample.data.PiaSampleSharedPreferences;
import eu.nets.pia.sample.network.MerchantRestClient;
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

public class MerchantBESettingsActivity extends AppCompatActivity {

    @BindView(R.id.toolbar)
    CustomToolbar mToolbar;
    @BindView(R.id.test_env_et)
    EditText mTestEnvEdit;
    @BindView(R.id.prod_env_et)
    EditText mProdEnvEdit;
    @BindView(R.id.test_merchant_id_et)
    EditText mTestIdEdit;
    @BindView(R.id.prod_merchant_id_et)
    EditText mProdIdEdit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_merchant_be_settings);
        ButterKnife.bind(this);

        setupToolbar();
        showPreviousConfiguration();
    }

    private void setupToolbar() {
        TextView saveAction = new TextView(this);
        saveAction.setText(getString(R.string.action_save));
        saveAction.setTextSize(18f);
        saveAction.setPadding(10, 10, 10, 10);
        saveAction.setTextColor(ContextCompat.getColor(this, R.color.custom_light_blue_color));
        saveAction.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (validateFields()) {
                    showConfirmationPopup();
                }
            }
        });
        mToolbar.setTitle(getString(R.string.merchant_be_settings_title));
        mToolbar.setupRightView(saveAction);
    }

    private void showPreviousConfiguration() {
        mTestEnvEdit.setText(PiaSampleSharedPreferences.getMerchantEnvTest());
        mProdEnvEdit.setText(PiaSampleSharedPreferences.getMerchantEnvProd());
        mTestIdEdit.setText(PiaSampleSharedPreferences.getMerchantIdTest());
        mProdIdEdit.setText(PiaSampleSharedPreferences.getMerchantIdProd());
    }

    private boolean validateFields() {
        if (mProdEnvEdit.getText().toString().isEmpty() && mProdIdEdit.getText().toString().isEmpty() &&
                mTestEnvEdit.getText().toString().isEmpty() && mTestIdEdit.getText().toString().isEmpty()) {
            // all fields are empty
            showAlert("Error", "Please enter credentials for production and/or test environments");
            return false;
        } else if (!mProdEnvEdit.getText().toString().isEmpty() && mProdIdEdit.getText().toString().isEmpty() ||
                mProdEnvEdit.getText().toString().isEmpty() && !mProdIdEdit.getText().toString().isEmpty()) {
            showAlert("Error", "Production environment settings: missing parameter");
            return false;
        } else if (!mTestEnvEdit.getText().toString().isEmpty() && mTestIdEdit.getText().toString().isEmpty() ||
                mTestEnvEdit.getText().toString().isEmpty() && !mTestIdEdit.getText().toString().isEmpty()) {
            showAlert("Error", "Test environment settings: missing parameter");
            return false;
        } else if (!mProdEnvEdit.getText().toString().isEmpty() && !Patterns.WEB_URL.matcher(mProdEnvEdit.getText().toString()).matches()) {
            showAlert("Error", "Invalid prod URL.");
            return false;
        } else if (!mTestEnvEdit.getText().toString().isEmpty() && !Patterns.WEB_URL.matcher(mTestEnvEdit.getText().toString()).matches()) {
            showAlert("Error", "Invalid test URL.");
            return false;
        } else {
            return true;
        }
    }

    private void showConfirmationPopup() {
        try {
            AlertDialog.Builder builder = new AlertDialog.Builder(this, R.style.AlertDialogTheme);

            builder.setTitle(R.string.new_configuration);
            builder.setCancelable(false);

            LinearLayout rootView = (LinearLayout) LayoutInflater.from(this).inflate(R.layout.config_confirmation_layout, (ViewGroup) null);
            ((TextView) rootView.findViewById(R.id.field_test_env)).setText(mTestEnvEdit.getText().toString());
            ((TextView) rootView.findViewById(R.id.field_prod_env)).setText(mProdEnvEdit.getText().toString());
            ((TextView) rootView.findViewById(R.id.field_test_id)).setText(mTestIdEdit.getText().toString());
            ((TextView) rootView.findViewById(R.id.field_prod_id)).setText(mProdIdEdit.getText().toString());

            builder.setView(rootView);

            builder.setPositiveButton(
                    R.string.action_confirm,
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            saveConfiguration();
                            dialog.cancel();
                        }
                    });
            builder.setNegativeButton(R.string.pia_action_cancel, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.cancel();
                }
            });

            builder.create().show();
        } catch (Exception e) {
            //in case activity is not attached to window -- catch exception here and do nothing
        }
    }

    private void saveConfiguration() {
        PiaSampleSharedPreferences.setMerchantEnvTest(mTestEnvEdit.getText().toString());
        PiaSampleSharedPreferences.setMerchantEnvProd(mProdEnvEdit.getText().toString());
        PiaSampleSharedPreferences.setMerchantIdTest(mTestIdEdit.getText().toString());
        PiaSampleSharedPreferences.setMerchantIdProd(mProdIdEdit.getText().toString());

        MerchantRestClient.notifyBackendConfigurationChanged();
        onBackPressed();
    }

    private void showAlert(String title, String message) {
        try {
            AlertDialog.Builder builder = new AlertDialog.Builder(this, R.style.AlertDialogTheme);

            if (title != null) {
                builder.setTitle(title);
            }
            builder.setMessage(message);
            builder.setCancelable(false);

            builder.setPositiveButton(
                    R.string.action_ok,
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            dialog.cancel();
                        }
                    });

            builder.create().show();
        } catch (Exception e) {
            //in case activity is not attached to window -- catch exception here and do nothing
        }
    }
}
