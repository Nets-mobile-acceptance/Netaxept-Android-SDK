// Generated code from Butter Knife. Do not modify!
package eu.nets.pia.sample.ui.activity;

import android.view.View;
import android.widget.EditText;
import androidx.annotation.CallSuper;
import androidx.annotation.UiThread;
import butterknife.Unbinder;
import butterknife.internal.DebouncingOnClickListener;
import butterknife.internal.Utils;
import eu.nets.pia.sample.R;
import eu.nets.pia.sample.ui.widget.CustomToolbar;
import java.lang.IllegalStateException;
import java.lang.Override;

public class UICustomizationActivity_ViewBinding implements Unbinder {
  private UICustomizationActivity target;

  private View view7f08012b;

  private View view7f08009d;

  private View view7f08012c;

  private View view7f08009e;

  private View view7f08012d;

  private View view7f08009f;

  private View view7f080122;

  private View view7f080094;

  private View view7f080124;

  private View view7f080096;

  private View view7f080123;

  private View view7f080095;

  private View view7f08012a;

  private View view7f08009c;

  private View view7f080137;

  private View view7f0800a7;

  private View view7f080133;

  private View view7f0800a4;

  private View view7f080132;

  private View view7f0800a3;

  private View view7f080135;

  private View view7f0800a6;

  private View view7f080134;

  private View view7f0800a5;

  private View view7f080130;

  private View view7f0800a1;

  private View view7f08012f;

  private View view7f0800a0;

  private View view7f080136;

  private View view7f080141;

  private View view7f0800ac;

  private View view7f08008e;

  private View view7f080125;

  private View view7f080097;

  private View view7f080129;

  private View view7f08009b;

  private View view7f080128;

  private View view7f08009a;

  private View view7f080126;

  private View view7f080098;

  private View view7f080127;

  private View view7f080099;

  private View view7f080131;

  private View view7f0800a2;

  @UiThread
  public UICustomizationActivity_ViewBinding(UICustomizationActivity target) {
    this(target, target.getWindow().getDecorView());
  }

  @UiThread
  public UICustomizationActivity_ViewBinding(final UICustomizationActivity target, View source) {
    this.target = target;

    View view;
    target.mToolbar = Utils.findRequiredViewAsType(source, R.id.toolbar, "field 'mToolbar'", CustomToolbar.class);
    target.mSaveCardText = Utils.findRequiredViewAsType(source, R.id.nets_save_card_text, "field 'mSaveCardText'", EditText.class);
    view = Utils.findRequiredView(source, R.id.nets_nav_bar_color, "method 'onNetsNavBar'");
    view7f08012b = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.onNetsNavBar();
      }
    });
    view = Utils.findRequiredView(source, R.id.dark_nav_bar_color, "method 'onDarkNavBar'");
    view7f08009d = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.onDarkNavBar();
      }
    });
    view = Utils.findRequiredView(source, R.id.nets_nav_bar_item_color, "method 'onNetsNavBarItem'");
    view7f08012c = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.onNetsNavBarItem();
      }
    });
    view = Utils.findRequiredView(source, R.id.dark_nav_bar_item_color, "method 'onDarkNavBarItem'");
    view7f08009e = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.onDarkNavBarItem();
      }
    });
    view = Utils.findRequiredView(source, R.id.nets_nav_bar_title_color, "method 'onNetsNavBarTitle'");
    view7f08012d = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.onNetsNavBarTitle();
      }
    });
    view = Utils.findRequiredView(source, R.id.dark_nav_bar_title_color, "method 'onDarkNavBarTitle'");
    view7f08009f = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.onDarkNavBarTitle();
      }
    });
    view = Utils.findRequiredView(source, R.id.nets_background_color, "method 'onNetsBackgroundColor'");
    view7f080122 = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.onNetsBackgroundColor();
      }
    });
    view = Utils.findRequiredView(source, R.id.dark_background_color, "method 'onDarkBackgroundColor'");
    view7f080094 = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.onDarkBackgroundColor();
      }
    });
    view = Utils.findRequiredView(source, R.id.nets_button_text_color, "method 'onNetsButtonTextColor'");
    view7f080124 = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.onNetsButtonTextColor();
      }
    });
    view = Utils.findRequiredView(source, R.id.dark_button_text_color, "method 'onDarkButtonTextColor'");
    view7f080096 = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.onDarkButtonTextColor();
      }
    });
    view = Utils.findRequiredView(source, R.id.nets_button_background, "method 'onNetsButtonBackground'");
    view7f080123 = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.onNetsButtonBackground();
      }
    });
    view = Utils.findRequiredView(source, R.id.dark_button_background, "method 'onDarkButtonBackground'");
    view7f080095 = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.onDarkButtonBackground();
      }
    });
    view = Utils.findRequiredView(source, R.id.nets_label_text_color, "method 'onNetsLabelTextColor'");
    view7f08012a = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.onNetsLabelTextColor();
      }
    });
    view = Utils.findRequiredView(source, R.id.dark_label_text_color, "method 'onDarkLabelTextColor'");
    view7f08009c = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.onDarkLabelTextColor();
      }
    });
    view = Utils.findRequiredView(source, R.id.nets_token_card_cvc_area_color, "method 'onNetsTokenCardCvcAreaColor'");
    view7f080137 = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.onNetsTokenCardCvcAreaColor();
      }
    });
    view = Utils.findRequiredView(source, R.id.dark_token_card_cvc_area_color, "method 'onDarkTokenCardCvcAreaColor'");
    view7f0800a7 = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.onDarkTokenCardCvcAreaColor();
      }
    });
    view = Utils.findRequiredView(source, R.id.nets_text_field_color, "method 'onNetsTextFieldColor'");
    view7f080133 = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.onNetsTextFieldColor();
      }
    });
    view = Utils.findRequiredView(source, R.id.dark_text_field_color, "method 'onDarkTextFieldColor'");
    view7f0800a4 = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.onDarkTextFieldColor();
      }
    });
    view = Utils.findRequiredView(source, R.id.nets_text_field_background_color, "method 'onNetsTextFieldBackgroundColor'");
    view7f080132 = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.onNetsTextFieldBackgroundColor();
      }
    });
    view = Utils.findRequiredView(source, R.id.dark_text_field_background_color, "method 'onDarkTextFieldBackgroundColor'");
    view7f0800a3 = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.onDarkTextFieldBackgroundColor();
      }
    });
    view = Utils.findRequiredView(source, R.id.nets_text_field_success_color, "method 'onNetsTextFieldSuccessColor'");
    view7f080135 = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.onNetsTextFieldSuccessColor();
      }
    });
    view = Utils.findRequiredView(source, R.id.dark_text_field_success_color, "method 'onDarkTextFieldSuccessColor'");
    view7f0800a6 = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.onDarkTextFieldSuccessColor();
      }
    });
    view = Utils.findRequiredView(source, R.id.nets_text_field_error_color, "method 'onNetsTextFieldErrorColor'");
    view7f080134 = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.onNetsTextFieldErrorColor();
      }
    });
    view = Utils.findRequiredView(source, R.id.dark_text_field_error_color, "method 'onDarkTextFieldErrorColor'");
    view7f0800a5 = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.onDarkTextFieldErrorColor();
      }
    });
    view = Utils.findRequiredView(source, R.id.nets_switch_thumb_color, "method 'onNetsSwitchThumbColor'");
    view7f080130 = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.onNetsSwitchThumbColor();
      }
    });
    view = Utils.findRequiredView(source, R.id.dark_switch_thumb_color, "method 'onDarkSwitchThumbColor'");
    view7f0800a1 = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.onDarkSwitchThumbColor();
      }
    });
    view = Utils.findRequiredView(source, R.id.nets_switch_on_tint_color, "method 'onNetsSwitchOnTintColor'");
    view7f08012f = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.onNetsSwitchOnTintColor();
      }
    });
    view = Utils.findRequiredView(source, R.id.dark_switch_on_tint_color, "method 'onDarkSwitchOnTintColor'");
    view7f0800a0 = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.onDarkSwitchOnTintColor();
      }
    });
    view = Utils.findRequiredView(source, R.id.nets_text_on_hint_color, "method 'onTextInputHintDefaultColor'");
    view7f080136 = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.onTextInputHintDefaultColor();
      }
    });
    view = Utils.findRequiredView(source, R.id.orange_text_on_hint_color, "method 'onTextInputHintCustomColor'");
    view7f080141 = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.onTextInputHintCustomColor();
      }
    });
    view = Utils.findRequiredView(source, R.id.default_text_border_color, "method 'onTextInputBorderCustomColor'");
    view7f0800ac = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.onTextInputBorderCustomColor();
      }
    });
    view = Utils.findRequiredView(source, R.id.customer_text_border_color, "method 'onTextInputBorderDefaultColor'");
    view7f08008e = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.onTextInputBorderDefaultColor();
      }
    });
    view = Utils.findRequiredView(source, R.id.nets_cardio_background_color, "method 'onNetsCardIoBackground'");
    view7f080125 = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.onNetsCardIoBackground();
      }
    });
    view = Utils.findRequiredView(source, R.id.dark_cardio_background_color, "method 'onDarkCardIoBackground'");
    view7f080097 = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.onDarkCardIoBackground();
      }
    });
    view = Utils.findRequiredView(source, R.id.nets_cardio_text_color, "method 'onNetsCardIoTextColor'");
    view7f080129 = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.onNetsCardIoTextColor();
      }
    });
    view = Utils.findRequiredView(source, R.id.dark_cardio_text_color, "method 'onDarkCardIoTextColor'");
    view7f08009b = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.onDarkCardIoTextColor();
      }
    });
    view = Utils.findRequiredView(source, R.id.nets_cardio_frame_color, "method 'onNetsCardIoFrameColor'");
    view7f080128 = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.onNetsCardIoFrameColor();
      }
    });
    view = Utils.findRequiredView(source, R.id.dark_cardio_frame_color, "method 'onDarkCardIoFrameColor'");
    view7f08009a = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.onDarkCardIoFrameColor();
      }
    });
    view = Utils.findRequiredView(source, R.id.nets_cardio_button_background, "method 'onNetsCardIoButtonBackgroundColor'");
    view7f080126 = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.onNetsCardIoButtonBackgroundColor();
      }
    });
    view = Utils.findRequiredView(source, R.id.dark_cardio_button_background, "method 'onDarkCardIoButtonBackgroundColor'");
    view7f080098 = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.onDarkCardIoButtonBackgroundColor();
      }
    });
    view = Utils.findRequiredView(source, R.id.nets_cardio_button_text_color, "method 'onNetsCardIoButtonTextColor'");
    view7f080127 = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.onNetsCardIoButtonTextColor();
      }
    });
    view = Utils.findRequiredView(source, R.id.dark_cardio_button_text_color, "method 'onDarkCardIoButtonTextColor'");
    view7f080099 = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.onDarkCardIoButtonTextColor();
      }
    });
    view = Utils.findRequiredView(source, R.id.nets_switch_turn_off_color, "method 'onSwitchTurnOffDefaultColor'");
    view7f080131 = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.onSwitchTurnOffDefaultColor();
      }
    });
    view = Utils.findRequiredView(source, R.id.dark_switch_turn_off_color, "method 'onSwitchTurnOffDarkColor'");
    view7f0800a2 = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.onSwitchTurnOffDarkColor();
      }
    });
  }

  @Override
  @CallSuper
  public void unbind() {
    UICustomizationActivity target = this.target;
    if (target == null) throw new IllegalStateException("Bindings already cleared.");
    this.target = null;

    target.mToolbar = null;
    target.mSaveCardText = null;

    view7f08012b.setOnClickListener(null);
    view7f08012b = null;
    view7f08009d.setOnClickListener(null);
    view7f08009d = null;
    view7f08012c.setOnClickListener(null);
    view7f08012c = null;
    view7f08009e.setOnClickListener(null);
    view7f08009e = null;
    view7f08012d.setOnClickListener(null);
    view7f08012d = null;
    view7f08009f.setOnClickListener(null);
    view7f08009f = null;
    view7f080122.setOnClickListener(null);
    view7f080122 = null;
    view7f080094.setOnClickListener(null);
    view7f080094 = null;
    view7f080124.setOnClickListener(null);
    view7f080124 = null;
    view7f080096.setOnClickListener(null);
    view7f080096 = null;
    view7f080123.setOnClickListener(null);
    view7f080123 = null;
    view7f080095.setOnClickListener(null);
    view7f080095 = null;
    view7f08012a.setOnClickListener(null);
    view7f08012a = null;
    view7f08009c.setOnClickListener(null);
    view7f08009c = null;
    view7f080137.setOnClickListener(null);
    view7f080137 = null;
    view7f0800a7.setOnClickListener(null);
    view7f0800a7 = null;
    view7f080133.setOnClickListener(null);
    view7f080133 = null;
    view7f0800a4.setOnClickListener(null);
    view7f0800a4 = null;
    view7f080132.setOnClickListener(null);
    view7f080132 = null;
    view7f0800a3.setOnClickListener(null);
    view7f0800a3 = null;
    view7f080135.setOnClickListener(null);
    view7f080135 = null;
    view7f0800a6.setOnClickListener(null);
    view7f0800a6 = null;
    view7f080134.setOnClickListener(null);
    view7f080134 = null;
    view7f0800a5.setOnClickListener(null);
    view7f0800a5 = null;
    view7f080130.setOnClickListener(null);
    view7f080130 = null;
    view7f0800a1.setOnClickListener(null);
    view7f0800a1 = null;
    view7f08012f.setOnClickListener(null);
    view7f08012f = null;
    view7f0800a0.setOnClickListener(null);
    view7f0800a0 = null;
    view7f080136.setOnClickListener(null);
    view7f080136 = null;
    view7f080141.setOnClickListener(null);
    view7f080141 = null;
    view7f0800ac.setOnClickListener(null);
    view7f0800ac = null;
    view7f08008e.setOnClickListener(null);
    view7f08008e = null;
    view7f080125.setOnClickListener(null);
    view7f080125 = null;
    view7f080097.setOnClickListener(null);
    view7f080097 = null;
    view7f080129.setOnClickListener(null);
    view7f080129 = null;
    view7f08009b.setOnClickListener(null);
    view7f08009b = null;
    view7f080128.setOnClickListener(null);
    view7f080128 = null;
    view7f08009a.setOnClickListener(null);
    view7f08009a = null;
    view7f080126.setOnClickListener(null);
    view7f080126 = null;
    view7f080098.setOnClickListener(null);
    view7f080098 = null;
    view7f080127.setOnClickListener(null);
    view7f080127 = null;
    view7f080099.setOnClickListener(null);
    view7f080099 = null;
    view7f080131.setOnClickListener(null);
    view7f080131 = null;
    view7f0800a2.setOnClickListener(null);
    view7f0800a2 = null;
  }
}
