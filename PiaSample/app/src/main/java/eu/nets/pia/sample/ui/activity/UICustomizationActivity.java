package eu.nets.pia.sample.ui.activity;

import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SwitchCompat;
import android.text.SpannableString;
import android.view.Gravity;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import eu.nets.pia.PiaInterfaceConfiguration;
import eu.nets.pia.sample.R;
import eu.nets.pia.sample.data.PiaSampleSharedPreferences;
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

public class UICustomizationActivity extends AppCompatActivity {
    /**
     * This Activity is for testing purpose, to understand how SDK UI customization works.
     */

    @BindView(R.id.toolbar)
    CustomToolbar mToolbar;

    @BindView(R.id.nets_save_card_text)
    EditText mSaveCardText;

    //section general customization
    private Integer mNavBarColor;
    private Integer mNavBarItemColor;
    private Integer mNavBarTitleColor;
    private Integer mBackgroundColor;
    private Integer mButtonTextColor;
    private Integer mButtonBackground;
    private Integer mLabelTextColor;
    private Integer mTokenCardCvcAreaColor;
    private Integer mTextFieldColor;
    private Integer mTextFieldBackgroundColor;
    private Integer mTextFieldSuccessColor;
    private Integer mTextFieldErrorColor;
    private Integer mSwitchThumbColor;
    private Integer mSwitchOnTintColor;
    private Integer mInputTextHintColor;
    private Integer mInputTextBorderColor;
    private boolean mUseSampleFont;
    private boolean mUseSampleImageForLogo;
    private boolean mTurnSaveCardSwitchOn;

    private final Integer mActionButtonLeftMargin = 50;
    private final Integer mActionButtonRightMargin = 50;
    private final Integer mActionButtonBottomMargin = 50;
    private final Integer mEditFieldRoundedCorner = 50;
    //end section

    //section cardio customization
    private Integer mCardIoBackground;
    private Integer mCardIoTextColor;
    private Integer mCardIoFrameColor;
    private Integer mCardIoButtonBackground;
    private Integer mCardIoButtonTextColor;
    private Integer mSwitchTurnOffColor;
    private boolean useCardIoTextFont;
    private boolean useCardIoButtonTextFont;
    //end section


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_uicustomization);
        ButterKnife.bind(this);

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
        if (configuration.getToolbarBackgroundColor() != null) {
            //apply dark theme UI config
            onDarkNavBar();
        } else {
            //apply nets UI config
            onNetsNavBar();
        }
        if (configuration.getToolbarTitleColor() != null) {
            //apply dark theme UI config
            onDarkNavBarTitle();
        } else {
            //apply nets UI config
            onNetsNavBarTitle();
        }
        if (configuration.getToolbarActionButtonTextColor() != null) {
            //apply dark theme UI config
            onDarkNavBarItem();
        } else {
            //apply nets UI config
            onNetsNavBarItem();
        }
        if (configuration.getBodyBackgroundColor() != null) {
            //apply dark theme UI config
            onDarkBackgroundColor();
        } else {
            //apply nets UI config
            onNetsBackgroundColor();
        }
        if (configuration.getButtonTextColor() != null) {
            //apply dark theme UI config
            onDarkButtonTextColor();
        } else {
            //apply nets UI config
            onNetsButtonTextColor();
        }
        if (configuration.getButtonBackgroundColor() != null) {
            //apply dark theme UI config
            onDarkButtonBackground();
        } else {
            //apply nets UI config
            onNetsButtonBackground();
        }
        if (configuration.getLabelTextColor() != null) {
            //apply dark theme UI config
            onDarkLabelTextColor();
        } else {
            //apply nets UI config
            onNetsLabelTextColor();
        }
        if (configuration.getTokenCardCVCLayoutBackgroundColor() != null) {
            //apply dark theme UI config
            onDarkTokenCardCvcAreaColor();
        } else {
            //apply nets UI config
            onNetsTokenCardCvcAreaColor();
        }
        if (configuration.getFieldTextColor() != null) {
            //apply dark theme UI config
            onDarkTextFieldColor();
        } else {
            //apply nets UI config
            onNetsTextFieldColor();
        }
        if (configuration.getFieldBackgroundColor() != null) {
            //apply dark theme UI config
            onDarkTextFieldBackgroundColor();
        } else {
            //apply nets UI config
            onNetsTextFieldBackgroundColor();
        }
        if (configuration.getValidFieldBorderColor() != null) {
            //apply dark theme UI config
            onDarkTextFieldSuccessColor();
        } else {
            //apply nets UI config
            onNetsTextFieldSuccessColor();
        }
        if (configuration.getErrorFieldBorderColor() != null) {
            //apply nets UI config
            onDarkTextFieldErrorColor();
        } else {
            //apply dark theme UI config
            onNetsTextFieldErrorColor();
        }
        if (configuration.getSwitchThumbColor() != null) {
            onDarkSwitchThumbColor();
        } else {
            onNetsSwitchThumbColor();
        }
        if (configuration.getSwitchOnTrackColor() != null) {
            //apply dark theme UI config
            onDarkSwitchOnTintColor();
        } else {
            //apply nets UI config
            onNetsSwitchOnTintColor();
        }
        /*Cardio text hint*/
        if (configuration.getInputTextHintColor() != null) {
            //apply orange color to hint
            onTextInputHintCustomColor();
        } else {
            //apply default color to hint
            onTextInputHintDefaultColor();
        }
        /*Cardio text hint*/
        if (configuration.getInputTextBorderColor() != null) {
            onTextInputBorderCustomColor();
        } else {
            onTextInputBorderDefaultColor();
        }
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

        //section card io customization selection
        if (configuration.getCardIOBackgroundColor() != null) {
            //apply dark theme UI config
            onDarkCardIoBackground();
        } else {
            //apply nets UI config
            onNetsCardIoBackground();
        }
        if (configuration.getCardIOTextColor() != null) {
            //apply dark theme UI config
            onDarkCardIoTextColor();
        } else {
            //apply nets UI config
            onNetsCardIoTextColor();
        }
        if (configuration.getCardIOPreviewFrameColor() != null) {
            //apply dark theme UI config
            onDarkCardIoFrameColor();
        } else {
            //apply nets UI config
            onNetsCardIoFrameColor();
        }
        if (configuration.getCardIOButtonBackgroundSelector() != null) {
            //apply dark theme UI config
            onDarkCardIoButtonBackgroundColor();
        } else {
            //apply nets UI config
            onNetsCardIoButtonBackgroundColor();
        }
        if (configuration.getCardIOButtonTextColor() != null) {
            //apply dark theme UI config
            onDarkCardIoButtonTextColor();
        } else {
            //apply nets UI config
            onNetsCardIoButtonTextColor();
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

        if (configuration.getSwitchTurnOffColor() != null) {
            onSwitchTurnOffDarkColor();
        } else {
            onSwitchTurnOffDefaultColor();
        }
        //end section
    }

    private void saveSelection() {
        PiaInterfaceConfiguration configuration = PiaInterfaceConfiguration.getInstance();
        configuration.setToolbarBackgroundColor(mNavBarColor);
        configuration.setToolbarTitleColor(mNavBarTitleColor);
        configuration.setToolbarActionButtonTextColor(mNavBarItemColor);
        configuration.setBodyBackgroundColor(mBackgroundColor);
        configuration.setButtonTextColor(mButtonTextColor);
        configuration.setButtonBackgroundColor(mButtonBackground);
        configuration.setLabelTextColor(mLabelTextColor);
        configuration.setTokenCardCVCLayoutBackgroundColor(mTokenCardCvcAreaColor);
        configuration.setFieldTextColor(mTextFieldColor);
        configuration.setFieldBackgroundColor(mTextFieldBackgroundColor);
        configuration.setValidFieldBorderColor(mTextFieldSuccessColor);
        configuration.setErrorFieldBorderColor(mTextFieldErrorColor);
        configuration.setSwitchThumbColor(mSwitchThumbColor);
        configuration.setSwitchOnTrackColor(mSwitchOnTintColor);
        configuration.setLogoDrawable(mUseSampleImageForLogo ?
                ContextCompat.getDrawable(this, R.drawable.ic_header) :
                null
        );

        configuration.setButtonFont(mUseSampleFont ? Typeface.SERIF : null);
        configuration.setFieldFont(mUseSampleFont ? Typeface.SERIF : null);
        configuration.setLabelFont(mUseSampleFont ? Typeface.SERIF : null);
        configuration.setCardIOTextFont(mUseSampleFont ? Typeface.SERIF : null);
        configuration.setCardIOButtonTextFont(mUseSampleFont ? Typeface.SERIF : null);

        //section cardio save configuration
        configuration.setCardIOBackgroundColor(mCardIoBackground);
        configuration.setCardIOTextColor(mCardIoTextColor);
        configuration.setCardIOPreviewFrameColor(mCardIoFrameColor);
        if (mCardIoButtonBackground != null) {
            //create gradient drawable for testing purpose
            //it's recommended to pass here the XML drawable for button states: normal, pressed, disabled
            GradientDrawable drawable = new GradientDrawable();
            drawable.setColor(mCardIoButtonBackground);
            drawable.setCornerRadius(8);
            configuration.setCardIOButtonBackgroundSelector(drawable);
        } else {
            configuration.setCardIOButtonBackgroundSelector(null);
        }
        configuration.setCardIOButtonTextColor(mCardIoButtonTextColor);
        configuration.setCardIOTextFont(useCardIoTextFont ? Typeface.MONOSPACE : null);
        configuration.setCardIOButtonTextFont(useCardIoButtonTextFont ? Typeface.MONOSPACE : null);
        configuration.setSaveCardSwitchDefault(mTurnSaveCardSwitchOn);
        PiaInterfaceConfiguration.getInstance().setInputTextBorderColor(mInputTextBorderColor);
        configuration.setInputTextHintColor(mInputTextHintColor);
        configuration.setSpannableSaveCardText(new SpannableString(mSaveCardText.getText().toString()));
        if (configuration.getButtonRoundCorner() != null) {
            configuration.setButtonRoundCorner((int) getResources().getDimension(R.dimen.custom_button_radius));
        }
        configuration.setSwitchTurnOffColor(mSwitchTurnOffColor);
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
        mNavBarColor = ((ColorDrawable) findViewById(R.id.nets_nav_bar_color).getBackground()).getColor();
        ((TextView) findViewById(R.id.nav_bar_color)).setTextColor(mNavBarColor);
        //for nets theme, save null on PiaInterfaceConfiguration
        mNavBarColor = null;
    }

    @OnClick(R.id.dark_nav_bar_color)
    public void onDarkNavBar() {
        mNavBarColor = ((ColorDrawable) findViewById(R.id.dark_nav_bar_color).getBackground()).getColor();
        ((TextView) findViewById(R.id.nav_bar_color)).setTextColor(mNavBarColor);
    }

    @OnClick(R.id.nets_nav_bar_item_color)
    public void onNetsNavBarItem() {
        mNavBarItemColor = ((ColorDrawable) findViewById(R.id.nets_nav_bar_item_color).getBackground()).getColor();
        ((TextView) findViewById(R.id.nav_bar_item_color)).setTextColor(mNavBarItemColor);
        //for nets theme, save null on PiaInterfaceConfiguration
        mNavBarItemColor = null;
    }

    @OnClick(R.id.dark_nav_bar_item_color)
    public void onDarkNavBarItem() {
        mNavBarItemColor = ((ColorDrawable) findViewById(R.id.dark_nav_bar_item_color).getBackground()).getColor();
        ((TextView) findViewById(R.id.nav_bar_item_color)).setTextColor(mNavBarItemColor);
    }

    @OnClick(R.id.nets_nav_bar_title_color)
    public void onNetsNavBarTitle() {
        mNavBarTitleColor = ((ColorDrawable) findViewById(R.id.nets_nav_bar_title_color).getBackground()).getColor();
        ((TextView) findViewById(R.id.nav_bar_title_color)).setTextColor(mNavBarTitleColor);
        //for nets theme, save null on PiaInterfaceConfiguration
        mNavBarTitleColor = null;
    }

    @OnClick(R.id.dark_nav_bar_title_color)
    public void onDarkNavBarTitle() {
        mNavBarTitleColor = ((ColorDrawable) findViewById(R.id.dark_nav_bar_title_color).getBackground()).getColor();
        ((TextView) findViewById(R.id.nav_bar_title_color)).setTextColor(mNavBarTitleColor);
    }

    @OnClick(R.id.nets_background_color)
    public void onNetsBackgroundColor() {
        mBackgroundColor = ((ColorDrawable) findViewById(R.id.nets_background_color).getBackground()).getColor();
        ((TextView) findViewById(R.id.background_color)).setTextColor(mBackgroundColor);
        //for nets theme, save null on PiaInterfaceConfiguration
        mBackgroundColor = null;
    }

    @OnClick(R.id.dark_background_color)
    public void onDarkBackgroundColor() {
        mBackgroundColor = ((ColorDrawable) findViewById(R.id.dark_background_color).getBackground()).getColor();
        ((TextView) findViewById(R.id.background_color)).setTextColor(mBackgroundColor);
    }

    @OnClick(R.id.nets_button_text_color)
    public void onNetsButtonTextColor() {
        mButtonTextColor = ((ColorDrawable) findViewById(R.id.nets_button_text_color).getBackground()).getColor();
        ((TextView) findViewById(R.id.button_text_color)).setTextColor(mButtonTextColor);
        //for nets theme, save null on PiaInterfaceConfiguration
        mButtonTextColor = null;
    }

    @OnClick(R.id.dark_button_text_color)
    public void onDarkButtonTextColor() {
        mButtonTextColor = ((ColorDrawable) findViewById(R.id.dark_button_text_color).getBackground()).getColor();
        ((TextView) findViewById(R.id.button_text_color)).setTextColor(mButtonTextColor);
    }

    @OnClick(R.id.nets_button_background)
    public void onNetsButtonBackground() {
        mButtonBackground = ((ColorDrawable) findViewById(R.id.nets_button_background).getBackground()).getColor();
        ((TextView) findViewById(R.id.button_background_color)).setTextColor(mButtonBackground);
        //for nets theme, save null on PiaInterfaceConfiguration
        mButtonBackground = null;
    }

    @OnClick(R.id.dark_button_background)
    public void onDarkButtonBackground() {
        mButtonBackground = ((ColorDrawable) findViewById(R.id.dark_button_background).getBackground()).getColor();
        ((TextView) findViewById(R.id.button_background_color)).setTextColor(mButtonBackground);
    }

    @OnClick(R.id.nets_label_text_color)
    public void onNetsLabelTextColor() {
        mLabelTextColor = ((ColorDrawable) findViewById(R.id.nets_label_text_color).getBackground()).getColor();
        ((TextView) findViewById(R.id.label_text_color)).setTextColor(mLabelTextColor);
        //for nets theme, save null on PiaInterfaceConfiguration
        mLabelTextColor = null;
    }

    @OnClick(R.id.dark_label_text_color)
    public void onDarkLabelTextColor() {
        mLabelTextColor = ((ColorDrawable) findViewById(R.id.dark_label_text_color).getBackground()).getColor();
        ((TextView) findViewById(R.id.label_text_color)).setTextColor(mLabelTextColor);
    }

    @OnClick(R.id.nets_token_card_cvc_area_color)
    public void onNetsTokenCardCvcAreaColor() {
        mTokenCardCvcAreaColor = ((ColorDrawable) findViewById(R.id.nets_token_card_cvc_area_color).getBackground()).getColor();
        ((TextView) findViewById(R.id.token_card_cvc_area_color)).setTextColor(mTokenCardCvcAreaColor);
        //for nets theme, save null on PiaInterfaceConfiguration
        mTokenCardCvcAreaColor = null;
    }

    @OnClick(R.id.dark_token_card_cvc_area_color)
    public void onDarkTokenCardCvcAreaColor() {
        mTokenCardCvcAreaColor = ((ColorDrawable) findViewById(R.id.dark_token_card_cvc_area_color).getBackground()).getColor();
        ((TextView) findViewById(R.id.token_card_cvc_area_color)).setTextColor(mTokenCardCvcAreaColor);
    }

    @OnClick(R.id.nets_text_field_color)
    public void onNetsTextFieldColor() {
        mTextFieldColor = ((ColorDrawable) findViewById(R.id.nets_text_field_color).getBackground()).getColor();
        ((TextView) findViewById(R.id.text_field_color)).setTextColor(mTextFieldColor);
        //for nets theme, save null on PiaInterfaceConfiguration
        mTextFieldColor = null;
    }

    @OnClick(R.id.dark_text_field_color)
    public void onDarkTextFieldColor() {
        mTextFieldColor = ((ColorDrawable) findViewById(R.id.dark_text_field_color).getBackground()).getColor();
        ((TextView) findViewById(R.id.text_field_color)).setTextColor(mTextFieldColor);
    }

    @OnClick(R.id.nets_text_field_background_color)
    public void onNetsTextFieldBackgroundColor() {
        mTextFieldBackgroundColor = ((ColorDrawable) findViewById(R.id.nets_text_field_background_color).getBackground()).getColor();
        ((TextView) findViewById(R.id.text_field_background_color)).setTextColor(mTextFieldBackgroundColor);
        //for nets theme, save null on PiaInterfaceConfiguration
        mTextFieldBackgroundColor = null;
    }

    @OnClick(R.id.dark_text_field_background_color)
    public void onDarkTextFieldBackgroundColor() {
        mTextFieldBackgroundColor = ((ColorDrawable) findViewById(R.id.dark_text_field_background_color).getBackground()).getColor();
        ((TextView) findViewById(R.id.text_field_background_color)).setTextColor(mTextFieldBackgroundColor);
    }

    @OnClick(R.id.nets_text_field_success_color)
    public void onNetsTextFieldSuccessColor() {
        mTextFieldSuccessColor = ((ColorDrawable) findViewById(R.id.nets_text_field_success_color).getBackground()).getColor();
        ((TextView) findViewById(R.id.text_field_success_color)).setTextColor(mTextFieldSuccessColor);
        //for nets theme, save null on PiaInterfaceConfiguration
        mTextFieldSuccessColor = null;
    }

    @OnClick(R.id.dark_text_field_success_color)
    public void onDarkTextFieldSuccessColor() {
        mTextFieldSuccessColor = ((ColorDrawable) findViewById(R.id.dark_text_field_success_color).getBackground()).getColor();
        ((TextView) findViewById(R.id.text_field_success_color)).setTextColor(mTextFieldSuccessColor);
    }

    @OnClick(R.id.nets_text_field_error_color)
    public void onNetsTextFieldErrorColor() {
        mTextFieldErrorColor = ((ColorDrawable) findViewById(R.id.nets_text_field_error_color).getBackground()).getColor();
        ((TextView) findViewById(R.id.text_field_error_color)).setTextColor(mTextFieldErrorColor);
        //for nets theme, save null on PiaInterfaceConfiguration
        mTextFieldErrorColor = null;
    }

    @OnClick(R.id.dark_text_field_error_color)
    public void onDarkTextFieldErrorColor() {
        mTextFieldErrorColor = ((ColorDrawable) findViewById(R.id.dark_text_field_error_color).getBackground()).getColor();
        ((TextView) findViewById(R.id.text_field_error_color)).setTextColor(mTextFieldErrorColor);
    }

    @OnClick(R.id.nets_switch_thumb_color)
    public void onNetsSwitchThumbColor() {
        mSwitchThumbColor = ((ColorDrawable) findViewById(R.id.nets_switch_thumb_color).getBackground()).getColor();
        ((TextView) findViewById(R.id.switch_thumb_color)).setTextColor(mSwitchThumbColor);
        mSwitchThumbColor = null;
    }

    @OnClick(R.id.dark_switch_thumb_color)
    public void onDarkSwitchThumbColor() {
        mSwitchThumbColor = ((ColorDrawable) findViewById(R.id.dark_switch_thumb_color).getBackground()).getColor();
        ((TextView) findViewById(R.id.switch_thumb_color)).setTextColor(mSwitchThumbColor);
    }

    @OnClick(R.id.nets_switch_on_tint_color)
    public void onNetsSwitchOnTintColor() {
        mSwitchOnTintColor = ((ColorDrawable) findViewById(R.id.nets_switch_on_tint_color).getBackground()).getColor();
        ((TextView) findViewById(R.id.switch_on_tint_color)).setTextColor(mSwitchOnTintColor);
        mSwitchOnTintColor = null;
    }

    @OnClick(R.id.dark_switch_on_tint_color)
    public void onDarkSwitchOnTintColor() {
        mSwitchOnTintColor = ((ColorDrawable) findViewById(R.id.dark_switch_on_tint_color).getBackground()).getColor();
        ((TextView) findViewById(R.id.switch_on_tint_color)).setTextColor(mSwitchOnTintColor);
    }

    /*Cardio text input hint*/
    @OnClick(R.id.nets_text_on_hint_color)
    public void onTextInputHintDefaultColor() {
        mInputTextHintColor = ((ColorDrawable) findViewById(R.id.nets_text_on_hint_color).getBackground()).getColor();
        ((TextView) findViewById(R.id.text_on_hint_color)).setTextColor(mInputTextHintColor);
        //for nets theme, save null on PiaInterfaceConfiguration
        mInputTextHintColor = null;
    }

    @OnClick(R.id.orange_text_on_hint_color)
    public void onTextInputHintCustomColor() {
        mInputTextHintColor = ((ColorDrawable) findViewById(R.id.orange_text_on_hint_color).getBackground()).getColor();
        ((TextView) findViewById(R.id.text_on_hint_color)).setTextColor(mInputTextHintColor);

    }
    /*Cardio text input hint*/

    @OnClick(R.id.customer_text_border_color)
    public void onTextInputBorderCustomColor() {
        mInputTextBorderColor = ((ColorDrawable) findViewById(R.id.customer_text_border_color).getBackground()).getColor();
        ((TextView) findViewById(R.id.text_border_color)).setTextColor(mInputTextBorderColor);
    }

    @OnClick(R.id.default_text_border_color)
    public void onTextInputBorderDefaultColor() {
        mInputTextBorderColor = ((ColorDrawable) findViewById(R.id.default_text_border_color).getBackground()).getColor();
        ((TextView) findViewById(R.id.text_border_color)).setTextColor(mInputTextBorderColor);
        mInputTextBorderColor = null;
    }

    //section card io customization
    @OnClick(R.id.nets_cardio_background_color)
    public void onNetsCardIoBackground() {
        mCardIoBackground = ((ColorDrawable) findViewById(R.id.nets_cardio_background_color).getBackground()).getColor();
        ((TextView) findViewById(R.id.cardio_background_color)).setTextColor(mCardIoBackground);
        //for nets theme, save null on PiaInterfaceConfiguration
        mCardIoBackground = null;
    }

    @OnClick(R.id.dark_cardio_background_color)
    public void onDarkCardIoBackground() {
        mCardIoBackground = ((ColorDrawable) findViewById(R.id.dark_cardio_background_color).getBackground()).getColor();
        ((TextView) findViewById(R.id.cardio_background_color)).setTextColor(mCardIoBackground);
    }

    @OnClick(R.id.nets_cardio_text_color)
    public void onNetsCardIoTextColor() {
        mCardIoTextColor = ((ColorDrawable) findViewById(R.id.nets_cardio_text_color).getBackground()).getColor();
        ((TextView) findViewById(R.id.cardio_text_color)).setTextColor(mCardIoTextColor);
        //for nets theme, save null on PiaInterfaceConfiguration
        mCardIoTextColor = null;
    }

    @OnClick(R.id.dark_cardio_text_color)
    public void onDarkCardIoTextColor() {
        mCardIoTextColor = ((ColorDrawable) findViewById(R.id.dark_cardio_text_color).getBackground()).getColor();
        ((TextView) findViewById(R.id.cardio_text_color)).setTextColor(mCardIoTextColor);
    }

    @OnClick(R.id.nets_cardio_frame_color)
    public void onNetsCardIoFrameColor() {
        mCardIoFrameColor = ((ColorDrawable) findViewById(R.id.nets_cardio_frame_color).getBackground()).getColor();
        ((TextView) findViewById(R.id.cardio_frame_color)).setTextColor(mCardIoFrameColor);
        //for nets theme, save null on PiaInterfaceConfiguration
        mCardIoFrameColor = null;
    }

    @OnClick(R.id.dark_cardio_frame_color)
    public void onDarkCardIoFrameColor() {
        mCardIoFrameColor = ((ColorDrawable) findViewById(R.id.dark_cardio_frame_color).getBackground()).getColor();
        ((TextView) findViewById(R.id.cardio_frame_color)).setTextColor(mCardIoFrameColor);
    }

    @OnClick(R.id.nets_cardio_button_background)
    public void onNetsCardIoButtonBackgroundColor() {
        mCardIoButtonBackground = ((ColorDrawable) findViewById(R.id.nets_cardio_button_background).getBackground()).getColor();
        ((TextView) findViewById(R.id.cardio_button_background)).setTextColor(mCardIoButtonBackground);
        //for nets theme, save null on PiaInterfaceConfiguration
        mCardIoButtonBackground = null;
    }

    @OnClick(R.id.dark_cardio_button_background)
    public void onDarkCardIoButtonBackgroundColor() {
        mCardIoButtonBackground = ((ColorDrawable) findViewById(R.id.dark_cardio_button_background).getBackground()).getColor();
        ((TextView) findViewById(R.id.cardio_button_background)).setTextColor(mCardIoButtonBackground);
    }

    @OnClick(R.id.nets_cardio_button_text_color)
    public void onNetsCardIoButtonTextColor() {
        mCardIoButtonTextColor = ((ColorDrawable) findViewById(R.id.nets_cardio_button_text_color).getBackground()).getColor();
        ((TextView) findViewById(R.id.cardio_button_text_color)).setTextColor(mCardIoButtonTextColor);
        //for nets theme, save null on PiaInterfaceConfiguration
        mCardIoButtonTextColor = null;
    }

    @OnClick(R.id.dark_cardio_button_text_color)
    public void onDarkCardIoButtonTextColor() {
        mCardIoButtonTextColor = ((ColorDrawable) findViewById(R.id.dark_cardio_button_text_color).getBackground()).getColor();
        ((TextView) findViewById(R.id.cardio_button_text_color)).setTextColor(mCardIoButtonTextColor);
    }

    @OnClick(R.id.nets_switch_turn_off_color)
    public void onSwitchTurnOffDefaultColor() {
        mSwitchTurnOffColor = ((ColorDrawable) findViewById(R.id.nets_switch_turn_off_color).getBackground()).getColor();
        ((TextView) findViewById(R.id.text_switch_turn_off_color)).setTextColor(mSwitchTurnOffColor);
        mSwitchTurnOffColor = null;
    }

    @OnClick(R.id.dark_switch_turn_off_color)
    public void onSwitchTurnOffDarkColor() {
        mSwitchTurnOffColor = ((ColorDrawable) findViewById(R.id.dark_switch_turn_off_color).getBackground()).getColor();
        ((TextView) findViewById(R.id.text_switch_turn_off_color)).setTextColor(mSwitchTurnOffColor);
    }
    //end section

}
