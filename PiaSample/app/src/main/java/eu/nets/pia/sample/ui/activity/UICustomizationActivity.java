
package eu.nets.pia.sample.ui.activity;

import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.GradientDrawable;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.SwitchCompat;
import android.view.Gravity;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import eu.nets.pia.PiaInterfaceConfiguration;
import eu.nets.pia.sample.R;
import eu.nets.pia.sample.ui.widget.CustomToolbar;

/**
 * MIT License
 * <p>
 * Copyright (c) 2018 Nets Denmark A/S
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

    //section general customization
    private Integer mNavBarColor;
    private Integer mNavBarItemColor;
    private Integer mNavBarTitleColor;
    private Integer mBackgroundColor;
    private Integer mButtonTextColor;
    private Integer mLabelTextColor;
    private Integer mTokenCardCvcAreaColor;
    private Integer mTextFieldColor;
    private Integer mTextFieldSuccessColor;
    private Integer mTextFieldErrorColor;
    private Integer mSwitchThumbColor;
    private Integer mSwitchOnTintColor;
    private boolean mUseSampleFont;
    private boolean mUseSampleImageForLogo;
    //end section

    //section cardio customization
    private Integer mCardIoBackground;
    private Integer mCardIoTextColor;
    private Integer mCardIoFrameColor;
    private Integer mCardIoButtonBackground;
    private Integer mCardIoButtonTextColor;
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
        if (configuration.getValidFieldBorderColor() != null) {
            //apply dark theme UI config
            onDarkTextFieldSuccessColor();
        } else {
            //apply nets UI config
            onNetsTextFieldSuccessColor();
        }
        if (configuration.getErrorFieldBorderColor() != null) {
            //apply dark theme UI config
            onDarkTextFieldErrorColor();
        } else {
            //apply nets UI config
            onNetsTextFieldErrorColor();
        }
        if (configuration.getSwitchThumbColor() != null) {
            //apply dark theme UI config
            onDarkSwitchThumbColor();
        } else {
            //apply nets UI config
            onNetsSwitchThumbColor();
        }
        if (configuration.getSwitchOnTrackColor() != null) {
            //apply dark theme UI config
            onDarkSwitchOnTintColor();
        } else {
            //apply nets UI config
            onNetsSwitchOnTintColor();
        }
        if (configuration.getLogoDrawable() != null) {
            ((SwitchCompat) findViewById(R.id.use_sample_image)).setChecked(true);
        }
        if (configuration.getLabelFont() != null) {
            ((SwitchCompat) findViewById(R.id.use_sample_font)).setChecked(true);
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
        //end section
    }

    private void saveSelection() {
        PiaInterfaceConfiguration configuration = PiaInterfaceConfiguration.getInstance();
        if (mNavBarColor != null) {
            configuration.setToolbarBackgroundColor(mNavBarColor);
        }
        if (mNavBarTitleColor != null) {
            configuration.setToolbarTitleColor(mNavBarTitleColor);
        }
        if (mNavBarItemColor != null) {
            configuration.setToolbarActionButtonTextColor(mNavBarItemColor);
        }
        if (mBackgroundColor != null) {
            configuration.setBodyBackgroundColor(mBackgroundColor);
        }
        if (mButtonTextColor != null) {
            configuration.setButtonTextColor(mButtonTextColor);
        }
        if (mLabelTextColor != null) {
            configuration.setLabelTextColor(mLabelTextColor);
        }
        if (mTokenCardCvcAreaColor != null) {
            configuration.setTokenCardCVCLayoutBackgroundColor(mTokenCardCvcAreaColor);
        }
        if (mTextFieldColor != null) {
            configuration.setFieldTextColor(mTextFieldColor);
        }
        if (mTextFieldSuccessColor != null) {
            configuration.setValidFieldBorderColor(mTextFieldSuccessColor);
        }
        if (mTextFieldErrorColor != null) {
            configuration.setErrorFieldBorderColor(mTextFieldErrorColor);
        }
        if (mSwitchThumbColor != null) {
            configuration.setSwitchThumbColor(mSwitchThumbColor);
        }
        if (mSwitchOnTintColor != null) {
            configuration.setSwitchOnTrackColor(mSwitchOnTintColor);
        }

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

        if (mCardIoBackground != null) {
            configuration.setCardIOBackgroundColor(mCardIoBackground);
        }
        if (mCardIoTextColor != null) {
            configuration.setCardIOTextColor(mCardIoTextColor);
        }
        if (mCardIoFrameColor != null) {
            configuration.setCardIOPreviewFrameColor(mCardIoFrameColor);
        }
        if (mCardIoButtonBackground != null) {
            //create gradient drawable for testing purpose
            //it's recommended to pass here the XML drawable for button states: normal, pressed, disabled
            GradientDrawable drawable = new GradientDrawable();
            drawable.setColor(mCardIoButtonBackground);
            drawable.setCornerRadius(8);
            configuration.setCardIOButtonBackgroundSelector(drawable);
        }
        if (mCardIoButtonTextColor != null) {
            configuration.setCardIOButtonTextColor(mCardIoButtonTextColor);
        }

        configuration.setCardIOTextFont(useCardIoTextFont ? Typeface.MONOSPACE : null);
        configuration.setCardIOButtonTextFont(useCardIoButtonTextFont ? Typeface.MONOSPACE : null);

        //end section
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
    }

    @OnClick(R.id.dark_button_text_color)
    public void onDarkButtonTextColor() {
        mButtonTextColor = ((ColorDrawable) findViewById(R.id.dark_button_text_color).getBackground()).getColor();
        ((TextView) findViewById(R.id.button_text_color)).setTextColor(mButtonTextColor);
    }

    @OnClick(R.id.nets_label_text_color)
    public void onNetsLabelTextColor() {
        mLabelTextColor = ((ColorDrawable) findViewById(R.id.nets_label_text_color).getBackground()).getColor();
        ((TextView) findViewById(R.id.label_text_color)).setTextColor(mLabelTextColor);
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
    }

    @OnClick(R.id.dark_text_field_color)
    public void onDarkTextFieldColor() {
        mTextFieldColor = ((ColorDrawable) findViewById(R.id.dark_text_field_color).getBackground()).getColor();
        ((TextView) findViewById(R.id.text_field_color)).setTextColor(mTextFieldColor);
    }

    @OnClick(R.id.nets_text_field_success_color)
    public void onNetsTextFieldSuccessColor() {
        mTextFieldSuccessColor = ((ColorDrawable) findViewById(R.id.nets_text_field_success_color).getBackground()).getColor();
        ((TextView) findViewById(R.id.text_field_success_color)).setTextColor(mTextFieldSuccessColor);
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
    }

    @OnClick(R.id.dark_switch_on_tint_color)
    public void onDarkSwitchOnTintColor() {
        mSwitchOnTintColor = ((ColorDrawable) findViewById(R.id.dark_switch_on_tint_color).getBackground()).getColor();
        ((TextView) findViewById(R.id.switch_on_tint_color)).setTextColor(mSwitchOnTintColor);
    }


    //section card io customization
    @OnClick(R.id.nets_cardio_background_color)
    public void onNetsCardIoBackground() {
        mCardIoBackground = ((ColorDrawable) findViewById(R.id.nets_cardio_background_color).getBackground()).getColor();
        ((TextView) findViewById(R.id.cardio_background_color)).setTextColor(mCardIoBackground);
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
    }

    @OnClick(R.id.dark_cardio_button_text_color)
    public void onDarkCardIoButtonTextColor() {
        mCardIoButtonTextColor = ((ColorDrawable) findViewById(R.id.dark_cardio_button_text_color).getBackground()).getColor();
        ((TextView) findViewById(R.id.cardio_button_text_color)).setTextColor(mCardIoButtonTextColor);
    }
    //end section

}
