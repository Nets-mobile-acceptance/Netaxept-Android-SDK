package eu.nets.pia.sample.ui.activity;

import android.content.res.Configuration;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.text.SpannableString;
import android.view.Gravity;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SwitchCompat;
import androidx.core.content.ContextCompat;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import eu.nets.pia.PiaInterfaceConfiguration;
import eu.nets.pia.PiaSDK;
import eu.nets.pia.sample.R;
import eu.nets.pia.sample.data.PiaSampleSharedPreferences;
import eu.nets.pia.sample.ui.widget.CustomToolbar;
import eu.nets.pia.ui.themes.PiaSDKTheme;

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

public class UICustomizationActivity extends AppCompatActivity {
    /**
     * This Activity is for testing purpose, to understand how SDK UI customization works.
     */

    @BindView(R.id.toolbar)
    CustomToolbar mToolbar;

    @BindView(R.id.nets_save_card_text)
    EditText mSaveCardText;

    private PiaSDKTheme customTheme;
    private PiaSDKTheme netsTheme;


    private boolean mUseSampleFont;
    private boolean mUseSampleImageForLogo;
    private boolean mTurnSaveCardSwitchOn;

    private final Integer mActionButtonLeftMargin = 50;
    private final Integer mActionButtonRightMargin = 50;
    private final Integer mActionButtonBottomMargin = 50;
    private final Integer mEditFieldRoundedCorner = 50;
    //end section

    //section cardio customization
    private boolean useCardIoTextFont;
    private boolean useCardIoButtonTextFont;

    private int uiMode;
    //end section

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_uicustomization);
        ButterKnife.bind(this);

        uiMode = getResources().getConfiguration().uiMode & Configuration.UI_MODE_NIGHT_MASK;
        customTheme = PiaSDK.netsStandardThemeForUIMode(uiMode, this);
        netsTheme = PiaSDK.netsStandardThemeForUIMode(uiMode, this);

        setupToolbar();

        handleGeneralUICustomizationSwitches();
        handleCardIoCustomizationSwitches();

        handlePreviousSelection();
    }

    private void handleGeneralUICustomizationSwitches() {
        ((SwitchCompat) findViewById(R.id.use_sample_font)).setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                mUseSampleFont = isChecked;
                if (isChecked) {
                    ((TextView) findViewById(R.id.sample_font)).setTypeface(Typeface.SERIF, Typeface.BOLD);
                } else {
                    ((TextView) findViewById(R.id.sample_font)).setTypeface(null);
                }
            }
        });

        ((SwitchCompat) findViewById(R.id.use_sample_image)).setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                mUseSampleImageForLogo = isChecked;
                if (isChecked) {
                    ((TextView) findViewById(R.id.sample_image)).setTypeface(Typeface.SANS_SERIF, Typeface.BOLD);
                } else {
                    ((TextView) findViewById(R.id.sample_image)).setTypeface(null);
                }
            }
        });

        ((SwitchCompat) findViewById(R.id.use_disable_save_card_option)).setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                PiaSampleSharedPreferences.setDisableSaveCardOption(isChecked);
                PiaInterfaceConfiguration.getInstance().setDisableSaveCardOption(isChecked);

                if (isChecked) {
                    ((TextView) findViewById(R.id.disable_save_card_option)).setTypeface(Typeface.SANS_SERIF, Typeface.BOLD);
                } else {
                    ((TextView) findViewById(R.id.disable_save_card_option)).setTypeface(null);
                }
            }
        });

        ((SwitchCompat) findViewById(R.id.switch_turn_on_save_card_option)).setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                mTurnSaveCardSwitchOn = isChecked;
                if (isChecked) {
                    ((TextView) findViewById(R.id.turn_on_save_card_option)).setTypeface(Typeface.SANS_SERIF, Typeface.BOLD);
                } else {
                    ((TextView) findViewById(R.id.turn_on_save_card_option)).setTypeface(null);
                }
            }
        });

        ((SwitchCompat) findViewById(R.id.switch_action_button_left_margin)).setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    ((TextView) findViewById(R.id.text_action_button_left_margin)).setTypeface(Typeface.SANS_SERIF, Typeface.BOLD);
                    PiaInterfaceConfiguration.getInstance().setActionButtonLeftMargin(mActionButtonLeftMargin);
                } else {
                    ((TextView) findViewById(R.id.text_action_button_left_margin)).setTypeface(null);
                    PiaInterfaceConfiguration.getInstance().setActionButtonLeftMargin(null);
                }
            }
        });

        ((SwitchCompat) findViewById(R.id.switch_action_button_right_margin)).setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    ((TextView) findViewById(R.id.text_action_button_right_margin)).setTypeface(Typeface.SANS_SERIF, Typeface.BOLD);
                    PiaInterfaceConfiguration.getInstance().setActionButtonRightMargin(mActionButtonRightMargin);
                } else {
                    ((TextView) findViewById(R.id.text_action_button_right_margin)).setTypeface(null);
                    PiaInterfaceConfiguration.getInstance().setActionButtonRightMargin(null);
                }
            }
        });

        ((SwitchCompat) findViewById(R.id.switch_action_button_bottom_margin)).setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    ((TextView) findViewById(R.id.text_action_button_bottom_margin)).setTypeface(Typeface.SANS_SERIF, Typeface.BOLD);
                    PiaInterfaceConfiguration.getInstance().setActionButtonBottomMargin(mActionButtonBottomMargin);
                } else {
                    ((TextView) findViewById(R.id.text_action_button_bottom_margin)).setTypeface(null);
                    PiaInterfaceConfiguration.getInstance().setActionButtonBottomMargin(null);
                }
            }
        });

        ((SwitchCompat) findViewById(R.id.switch_field_rounded_corner)).setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    ((TextView) findViewById(R.id.text_field_rounded_corner)).setTypeface(Typeface.SANS_SERIF, Typeface.BOLD);
                    PiaInterfaceConfiguration.getInstance().setFieldRoundCorner(mEditFieldRoundedCorner);
                } else {
                    ((TextView) findViewById(R.id.text_field_rounded_corner)).setTypeface(null);
                    PiaInterfaceConfiguration.getInstance().setFieldRoundCorner(null);
                }
            }
        });

        ((SwitchCompat) findViewById(R.id.switch_sample_button_rounded_corner)).setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    ((TextView) findViewById(R.id.text_sample_button_rounded_corner)).setTypeface(Typeface.SANS_SERIF, Typeface.BOLD);
                    PiaInterfaceConfiguration.getInstance().setButtonRoundCorner((int) getResources().getDimension(R.dimen.custom_button_radius));
                } else {
                    ((TextView) findViewById(R.id.text_sample_button_rounded_corner)).setTypeface(null);
                    PiaInterfaceConfiguration.getInstance().setButtonRoundCorner(null);
                }
            }
        });

    }

    private void handleCardIoCustomizationSwitches() {
        ((SwitchCompat) findViewById(R.id.use_cardio_text_font)).setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                useCardIoTextFont = isChecked;
                if (isChecked) {
                    ((TextView) findViewById(R.id.cardio_text_font)).setTypeface(Typeface.MONOSPACE);
                } else {
                    ((TextView) findViewById(R.id.cardio_text_font)).setTypeface(null);
                }
            }
        });

        ((SwitchCompat) findViewById(R.id.use_button_cardio_text_font)).setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                useCardIoButtonTextFont = isChecked;
                if (isChecked) {
                    ((TextView) findViewById(R.id.cardio_button_text_font)).setTypeface(Typeface.SANS_SERIF, Typeface.BOLD);
                } else {
                    ((TextView) findViewById(R.id.cardio_button_text_font)).setTypeface(null);
                }
            }
        });
    }

    private void handlePreviousSelection() {
        PiaInterfaceConfiguration configuration = PiaInterfaceConfiguration.getInstance();
        if (configuration.getLogoDrawable() != null) {
            ((SwitchCompat) findViewById(R.id.use_sample_image)).setChecked(true);
        }
        if (configuration.getLabelFont() != null) {
            ((SwitchCompat) findViewById(R.id.use_sample_font)).setChecked(true);
        }
        if (configuration.isDisableSaveCardOption()) {
            ((SwitchCompat) findViewById(R.id.use_disable_save_card_option)).setChecked(true);
        }
        if (configuration.isSaveCardSwitchDefault()) {
            ((SwitchCompat) findViewById(R.id.switch_turn_on_save_card_option)).setChecked(true);
        }
        if (configuration.getCardIOTextFont() != null) {
            ((SwitchCompat) findViewById(R.id.use_cardio_text_font)).setChecked(true);
        }
        if (configuration.getCardIOButtonTextFont() != null) {
            ((SwitchCompat) findViewById(R.id.use_button_cardio_text_font)).setChecked(true);
        }

        if (configuration.getSpannableSaveCardText() != null) {
            mSaveCardText.setText(configuration.getSpannableSaveCardText());
        }
        if (configuration.getActionButtonLeftMargin() != null) {
            ((SwitchCompat) findViewById(R.id.switch_action_button_left_margin)).setChecked(true);
        } else {
            ((SwitchCompat) findViewById(R.id.switch_action_button_left_margin)).setChecked(false);
        }
        if (configuration.getActionButtonRightMargin() != null) {
            ((SwitchCompat) findViewById(R.id.switch_action_button_right_margin)).setChecked(true);
        } else {
            ((SwitchCompat) findViewById(R.id.switch_action_button_right_margin)).setChecked(false);
        }
        if (configuration.getActionButtonBottomMargin() != null) {
            ((SwitchCompat) findViewById(R.id.switch_action_button_bottom_margin)).setChecked(true);
        } else {
            ((SwitchCompat) findViewById(R.id.switch_action_button_bottom_margin)).setChecked(false);
        }

        if (PiaInterfaceConfiguration.getInstance().getFieldRoundCorner() != null) {
            ((SwitchCompat) findViewById(R.id.switch_field_rounded_corner)).setChecked(true);
        } else {
            ((SwitchCompat) findViewById(R.id.switch_field_rounded_corner)).setChecked(false);
        }
        if (configuration.getButtonRoundCorner() != null) {
            ((SwitchCompat) findViewById(R.id.switch_sample_button_rounded_corner)).setChecked(true);
        } else {
            ((SwitchCompat) findViewById(R.id.switch_sample_button_rounded_corner)).setChecked(false);
        }

        //end section
    }

    private void saveSelection() {

        PiaSDK.setThemeForUIMode(customTheme, uiMode);

        PiaInterfaceConfiguration configuration = PiaInterfaceConfiguration.getInstance();
        configuration.setLogoDrawable(mUseSampleImageForLogo ?
                ContextCompat.getDrawable(this, R.drawable.ic_header) :
                null
        );

        configuration.setButtonFont(mUseSampleFont ? Typeface.SERIF : null);
        configuration.setFieldFont(mUseSampleFont ? Typeface.SERIF : null);
        configuration.setLabelFont(mUseSampleFont ? Typeface.SERIF : null);
        configuration.setCardIOTextFont(mUseSampleFont ? Typeface.SERIF : null);
        configuration.setCardIOButtonTextFont(mUseSampleFont ? Typeface.SERIF : null);

        //create gradient drawable for testing purpose
        //it's recommended to pass here the XML drawable for button states: normal, pressed, disabled
        GradientDrawable drawable = new GradientDrawable();
        drawable.setColor(customTheme.getCardIOBackgroundColor());

        drawable.setCornerRadius(8);
        configuration.setCardIOButtonBackgroundSelector(drawable);

        configuration.setCardIOTextFont(useCardIoTextFont ? Typeface.MONOSPACE : null);
        configuration.setCardIOButtonTextFont(useCardIoButtonTextFont ? Typeface.MONOSPACE : null);
        configuration.setSaveCardSwitchDefault(mTurnSaveCardSwitchOn);

        configuration.setSpannableSaveCardText(new SpannableString(mSaveCardText.getText().toString()));
        if (configuration.getButtonRoundCorner() != null) {
            configuration.setButtonRoundCorner((int) getResources().getDimension(R.dimen.custom_button_radius));
        }
    }

    private void setupToolbar() {
        mToolbar.setTitle("UI Customization");

        TextView mSaveAction = new TextView(this);
        mSaveAction.setText(R.string.action_save);
        mSaveAction.setGravity(Gravity.END | Gravity.CENTER_VERTICAL);
        mSaveAction.setTextSize(18f);
        mSaveAction.setPadding(10, 10, 10, 10);
        mSaveAction.setTextColor(ContextCompat.getColor(this, R.color.custom_light_blue_color));
        mSaveAction.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //save configuration
                saveSelection();
                //pop back to settings
                onBackPressed();
            }
        });

        mToolbar.setupRightView(mSaveAction);
    }

    @OnClick(R.id.nets_nav_bar_color)
    public void onNetsNavBar() {
        customTheme.setToolbarColor(netsTheme.getToolbarColor());
        ((TextView) findViewById(R.id.nav_bar_color)).setTextColor(netsTheme.getToolbarColor());
    }

    @OnClick(R.id.dark_nav_bar_color)
    public void onDarkNavBar() {
        customTheme.setToolbarColor(((ColorDrawable) findViewById(R.id.dark_nav_bar_color).getBackground()).getColor());
        ((TextView) findViewById(R.id.nav_bar_color)).setTextColor(((ColorDrawable) findViewById(R.id.dark_nav_bar_color).getBackground()).getColor());
    }

    @OnClick(R.id.nets_nav_bar_item_color)
    public void onNetsNavBarItem() {
        customTheme.setToolbarItemsColor(netsTheme.getToolbarItemsColor());
        ((TextView) findViewById(R.id.nav_bar_item_color)).setTextColor(netsTheme.getToolbarItemsColor());
    }

    @OnClick(R.id.dark_nav_bar_item_color)
    public void onDarkNavBarItem() {
        customTheme.setToolbarItemsColor(((ColorDrawable) findViewById(R.id.dark_nav_bar_item_color).getBackground()).getColor());
        ((TextView) findViewById(R.id.nav_bar_item_color)).setTextColor(((ColorDrawable) findViewById(R.id.dark_nav_bar_item_color).getBackground()).getColor());
    }

    @OnClick(R.id.nets_nav_bar_title_color)
    public void onNetsNavBarTitle() {
        customTheme.setToolbarTitleColor(netsTheme.getToolbarTitleColor());
        ((TextView) findViewById(R.id.nav_bar_title_color)).setTextColor(netsTheme.getToolbarTitleColor());
    }

    @OnClick(R.id.dark_nav_bar_title_color)
    public void onDarkNavBarTitle() {
        customTheme.setToolbarTitleColor(((ColorDrawable) findViewById(R.id.dark_nav_bar_title_color).getBackground()).getColor());
        ((TextView) findViewById(R.id.nav_bar_title_color)).setTextColor(((ColorDrawable) findViewById(R.id.dark_nav_bar_title_color).getBackground()).getColor());
    }

    @OnClick(R.id.nets_background_color)
    public void onNetsBackgroundColor() {
        customTheme.setBackgroundColor(netsTheme.getBackgroundColor());
        ((TextView) findViewById(R.id.background_color)).setTextColor(netsTheme.getBackgroundColor());
    }

    @OnClick(R.id.dark_background_color)
    public void onDarkBackgroundColor() {
        customTheme.setBackgroundColor(((ColorDrawable) findViewById(R.id.dark_background_color).getBackground()).getColor());
        ((TextView) findViewById(R.id.background_color)).setTextColor(((ColorDrawable) findViewById(R.id.dark_background_color).getBackground()).getColor());
    }

    @OnClick(R.id.nets_button_text_color)
    public void onNetsButtonTextColor() {
        customTheme.setButtonTextColor(netsTheme.getButtonTextColor());
        ((TextView) findViewById(R.id.button_text_color)).setTextColor(netsTheme.getButtonTextColor());
    }

    @OnClick(R.id.dark_button_text_color)
    public void onDarkButtonTextColor() {
        customTheme.setButtonTextColor(((ColorDrawable) findViewById(R.id.dark_button_text_color).getBackground()).getColor());
        ((TextView) findViewById(R.id.button_text_color)).setTextColor(((ColorDrawable) findViewById(R.id.dark_button_text_color).getBackground()).getColor());

    }

    @OnClick(R.id.nets_button_background)
    public void onNetsButtonBackground() {
        customTheme.setButtonBackgroundColor(netsTheme.getButtonBackgroundColor());
        ((TextView) findViewById(R.id.button_background_color)).setTextColor(netsTheme.getButtonBackgroundColor());
    }

    @OnClick(R.id.dark_button_background)
    public void onDarkButtonBackground() {
        customTheme.setButtonBackgroundColor(((ColorDrawable) findViewById(R.id.dark_button_background).getBackground()).getColor());
        ((TextView) findViewById(R.id.button_background_color)).setTextColor(((ColorDrawable) findViewById(R.id.dark_button_background).getBackground()).getColor());
    }

    @OnClick(R.id.nets_label_text_color)
    public void onNetsLabelTextColor() {
        customTheme.setLabelTextColor(netsTheme.getLabelTextColor());
        ((TextView) findViewById(R.id.label_text_color)).setTextColor(netsTheme.getLabelTextColor());

    }

    @OnClick(R.id.dark_label_text_color)
    public void onDarkLabelTextColor() {
        customTheme.setLabelTextColor(((ColorDrawable) findViewById(R.id.dark_label_text_color).getBackground()).getColor());
        ((TextView) findViewById(R.id.label_text_color)).setTextColor(((ColorDrawable) findViewById(R.id.dark_label_text_color).getBackground()).getColor());

    }

    @OnClick(R.id.nets_token_card_cvc_area_color)
    public void onNetsTokenCardCvcAreaColor() {
        customTheme.setTokenCardCVCLayoutBackgroundColor(netsTheme.getTokenCardCVCLayoutBackgroundColor());
        ((TextView) findViewById(R.id.token_card_cvc_area_color)).setTextColor(netsTheme.getTokenCardCVCLayoutBackgroundColor());

    }

    @OnClick(R.id.dark_token_card_cvc_area_color)
    public void onDarkTokenCardCvcAreaColor() {
        customTheme.setTokenCardCVCLayoutBackgroundColor(((ColorDrawable) findViewById(R.id.dark_token_card_cvc_area_color).getBackground()).getColor());
        ((TextView) findViewById(R.id.token_card_cvc_area_color)).setTextColor(((ColorDrawable) findViewById(R.id.dark_token_card_cvc_area_color).getBackground()).getColor());

    }

    @OnClick(R.id.nets_text_field_color)
    public void onNetsTextFieldColor() {
        customTheme.setTextFieldTextColor(netsTheme.getTextFieldTextColor());
        ((TextView) findViewById(R.id.text_field_color)).setTextColor(netsTheme.getTextFieldTextColor());

    }

    @OnClick(R.id.dark_text_field_color)
    public void onDarkTextFieldColor() {
        customTheme.setTextFieldTextColor(((ColorDrawable) findViewById(R.id.dark_text_field_color).getBackground()).getColor());
        ((TextView) findViewById(R.id.text_field_color)).setTextColor(((ColorDrawable) findViewById(R.id.dark_text_field_color).getBackground()).getColor());

    }

    @OnClick(R.id.nets_text_field_background_color)
    public void onNetsTextFieldBackgroundColor() {
        customTheme.setTextFieldBackgroundColor(netsTheme.getTextFieldBackgroundColor());
        ((TextView) findViewById(R.id.text_field_background_color)).setTextColor(netsTheme.getTextFieldBackgroundColor());

    }

    @OnClick(R.id.dark_text_field_background_color)
    public void onDarkTextFieldBackgroundColor() {
        customTheme.setTextFieldBackgroundColor(((ColorDrawable) findViewById(R.id.dark_text_field_background_color).getBackground()).getColor());
        ((TextView) findViewById(R.id.text_field_background_color)).setTextColor(((ColorDrawable) findViewById(R.id.dark_text_field_background_color).getBackground()).getColor());

    }

    @OnClick(R.id.nets_text_field_success_color)
    public void onNetsTextFieldSuccessColor() {
        customTheme.setTextFieldSuccessColor(netsTheme.getTextFieldSuccessColor());
        ((TextView) findViewById(R.id.text_field_success_color)).setTextColor(netsTheme.getTextFieldSuccessColor());

    }

    @OnClick(R.id.dark_text_field_success_color)
    public void onDarkTextFieldSuccessColor() {
        customTheme.setTextFieldSuccessColor(((ColorDrawable) findViewById(R.id.dark_text_field_success_color).getBackground()).getColor());
        ((TextView) findViewById(R.id.text_field_success_color)).setTextColor(((ColorDrawable) findViewById(R.id.dark_text_field_success_color).getBackground()).getColor());

    }

    @OnClick(R.id.nets_text_field_error_color)
    public void onNetsTextFieldErrorColor() {
        customTheme.setTextFieldSuccessColor(netsTheme.getTextFieldErrorColor());
        ((TextView) findViewById(R.id.text_field_error_color)).setTextColor(netsTheme.getTextFieldErrorColor());
    }

    @OnClick(R.id.dark_text_field_error_color)
    public void onDarkTextFieldErrorColor() {
        customTheme.setTextFieldErrorColor(((ColorDrawable) findViewById(R.id.dark_text_field_error_color).getBackground()).getColor());
        ((TextView) findViewById(R.id.text_field_error_color)).setTextColor(((ColorDrawable) findViewById(R.id.dark_text_field_error_color).getBackground()).getColor());
    }

    @OnClick(R.id.nets_switch_thumb_color)
    public void onNetsSwitchThumbColor() {
        customTheme.setSwitchThumbColor(netsTheme.getSwitchThumbColor());
        ((TextView) findViewById(R.id.switch_thumb_color)).setTextColor(netsTheme.getSwitchThumbColor());
    }

    @OnClick(R.id.dark_switch_thumb_color)
    public void onDarkSwitchThumbColor() {
        customTheme.setSwitchThumbColor(((ColorDrawable) findViewById(R.id.dark_switch_thumb_color).getBackground()).getColor());
        ((TextView) findViewById(R.id.switch_thumb_color)).setTextColor(((ColorDrawable) findViewById(R.id.dark_switch_thumb_color).getBackground()).getColor());
    }

    @OnClick(R.id.nets_switch_on_tint_color)
    public void onNetsSwitchOnTintColor() {
        customTheme.setSwitchOnTintColor(netsTheme.getSwitchOnTintColor());
        ((TextView) findViewById(R.id.switch_on_tint_color)).setTextColor(netsTheme.getSwitchOnTintColor());

    }

    @OnClick(R.id.dark_switch_on_tint_color)
    public void onDarkSwitchOnTintColor() {
        customTheme.setSwitchOnTintColor(((ColorDrawable) findViewById(R.id.dark_switch_on_tint_color).getBackground()).getColor());
        ((TextView) findViewById(R.id.switch_on_tint_color)).setTextColor(((ColorDrawable) findViewById(R.id.dark_switch_on_tint_color).getBackground()).getColor());

    }

    /*Cardio text input hint*/
    @OnClick(R.id.nets_text_on_hint_color)
    public void onTextInputHintDefaultColor() {
        customTheme.setTextFieldHintColor(netsTheme.getTextFieldHintColor());
        ((TextView) findViewById(R.id.text_on_hint_color)).setTextColor(netsTheme.getTextFieldHintColor());

    }

    @OnClick(R.id.orange_text_on_hint_color)
    public void onTextInputHintCustomColor() {
        customTheme.setTextFieldHintColor(((ColorDrawable) findViewById(R.id.orange_text_on_hint_color).getBackground()).getColor());
        ((TextView) findViewById(R.id.text_on_hint_color)).setTextColor(((ColorDrawable) findViewById(R.id.orange_text_on_hint_color).getBackground()).getColor());

    }
    /*Cardio text input hint*/

    @OnClick(R.id.default_text_border_color)
    public void onTextInputBorderCustomColor() {
        customTheme.setTextFieldBorderColor(netsTheme.getTextFieldBorderColor());
        ((TextView) findViewById(R.id.text_border_color)).setTextColor(netsTheme.getTextFieldBorderColor());

    }

    @OnClick(R.id.customer_text_border_color)
    public void onTextInputBorderDefaultColor() {
        customTheme.setTextFieldBorderColor(((ColorDrawable) findViewById(R.id.customer_text_border_color).getBackground()).getColor());
        ((TextView) findViewById(R.id.text_border_color)).setTextColor(((ColorDrawable) findViewById(R.id.customer_text_border_color).getBackground()).getColor());

    }

    //section card io customization
    @OnClick(R.id.nets_cardio_background_color)
    public void onNetsCardIoBackground() {
        customTheme.setCardIOBackgroundColor(netsTheme.getCardIOBackgroundColor());
        ((TextView) findViewById(R.id.cardio_background_color)).setTextColor(netsTheme.getCardIOBackgroundColor());

    }

    @OnClick(R.id.dark_cardio_background_color)
    public void onDarkCardIoBackground() {
        customTheme.setCardIOBackgroundColor(((ColorDrawable) findViewById(R.id.dark_cardio_background_color).getBackground()).getColor());
        ((TextView) findViewById(R.id.cardio_background_color)).setTextColor(((ColorDrawable) findViewById(R.id.dark_cardio_background_color).getBackground()).getColor());

    }

    @OnClick(R.id.nets_cardio_text_color)
    public void onNetsCardIoTextColor() {
        customTheme.setCardIOTextColor(netsTheme.getCardIOTextColor());
        ((TextView) findViewById(R.id.cardio_text_color)).setTextColor(netsTheme.getCardIOTextColor());

    }

    @OnClick(R.id.dark_cardio_text_color)
    public void onDarkCardIoTextColor() {
        customTheme.setCardIOTextColor(((ColorDrawable) findViewById(R.id.dark_cardio_text_color).getBackground()).getColor());
        ((TextView) findViewById(R.id.cardio_text_color)).setTextColor(((ColorDrawable) findViewById(R.id.dark_cardio_text_color).getBackground()).getColor());

    }

    @OnClick(R.id.nets_cardio_frame_color)
    public void onNetsCardIoFrameColor() {
        customTheme.setCardIOPreviewFrameColor(netsTheme.getCardIOPreviewFrameColor());
        ((TextView) findViewById(R.id.cardio_frame_color)).setTextColor(netsTheme.getCardIOPreviewFrameColor());

    }

    @OnClick(R.id.dark_cardio_frame_color)
    public void onDarkCardIoFrameColor() {
        customTheme.setCardIOPreviewFrameColor(((ColorDrawable) findViewById(R.id.dark_cardio_frame_color).getBackground()).getColor());
        ((TextView) findViewById(R.id.cardio_frame_color)).setTextColor(((ColorDrawable) findViewById(R.id.dark_cardio_frame_color).getBackground()).getColor());
    }

    @OnClick(R.id.nets_cardio_button_background)
    public void onNetsCardIoButtonBackgroundColor() {
        customTheme.setCardIOButtonBackgroundColor(netsTheme.getCardIOButtonBackgroundColor());
        ((TextView) findViewById(R.id.cardio_button_background)).setTextColor(netsTheme.getCardIOButtonBackgroundColor());
    }

    @OnClick(R.id.dark_cardio_button_background)
    public void onDarkCardIoButtonBackgroundColor() {
        customTheme.setCardIOButtonBackgroundColor(((ColorDrawable) findViewById(R.id.dark_cardio_button_background).getBackground()).getColor());
        ((TextView) findViewById(R.id.cardio_button_background)).setTextColor(((ColorDrawable) findViewById(R.id.dark_cardio_button_background).getBackground()).getColor());
    }

    @OnClick(R.id.nets_cardio_button_text_color)
    public void onNetsCardIoButtonTextColor() {
        customTheme.setCardIOButtonTextColor(netsTheme.getCardIOButtonTextColor());
        ((TextView) findViewById(R.id.cardio_button_text_color)).setTextColor(netsTheme.getCardIOButtonTextColor());

    }

    @OnClick(R.id.dark_cardio_button_text_color)
    public void onDarkCardIoButtonTextColor() {
        customTheme.setCardIOButtonTextColor(((ColorDrawable) findViewById(R.id.dark_cardio_button_text_color).getBackground()).getColor());
        ((TextView) findViewById(R.id.cardio_button_text_color)).setTextColor(((ColorDrawable) findViewById(R.id.dark_cardio_button_text_color).getBackground()).getColor());

    }

    @OnClick(R.id.nets_switch_turn_off_color)
    public void onSwitchTurnOffDefaultColor() {
        customTheme.setSwitchOffTintColor(netsTheme.getSwitchOffTintColor());
        ((TextView) findViewById(R.id.text_switch_turn_off_color)).setTextColor(netsTheme.getSwitchOffTintColor());

    }

    @OnClick(R.id.dark_switch_turn_off_color)
    public void onSwitchTurnOffDarkColor() {
        customTheme.setSwitchOffTintColor(((ColorDrawable) findViewById(R.id.dark_switch_turn_off_color).getBackground()).getColor());
        ((TextView) findViewById(R.id.text_switch_turn_off_color)).setTextColor(((ColorDrawable) findViewById(R.id.dark_switch_turn_off_color).getBackground()).getColor());
    }
    //end section

}
