// Generated code from Butter Knife. Do not modify!
package eu.nets.pia.sample.ui.activity;

import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import androidx.annotation.CallSuper;
import androidx.annotation.UiThread;
import androidx.appcompat.widget.SwitchCompat;
import butterknife.Unbinder;
import butterknife.internal.DebouncingOnClickListener;
import butterknife.internal.Utils;
import eu.nets.pia.sample.R;
import eu.nets.pia.sample.ui.widget.CustomToolbar;
import java.lang.IllegalStateException;
import java.lang.Override;

public class LoginActivity_ViewBinding implements Unbinder {
  private LoginActivity target;

  private View view7f08007a;

  private View view7f080077;

  private View view7f08003b;

  private View view7f0800af;

  private View view7f080188;

  private View view7f080037;

  private View view7f080169;

  private View view7f08016e;

  @UiThread
  public LoginActivity_ViewBinding(LoginActivity target) {
    this(target, target.getWindow().getDecorView());
  }

  @UiThread
  public LoginActivity_ViewBinding(final LoginActivity target, View source) {
    this.target = target;

    View view;
    target.mNotLoggedInLayout = Utils.findRequiredViewAsType(source, R.id.not_logged_in_layout, "field 'mNotLoggedInLayout'", RelativeLayout.class);
    target.mSignUpEditText = Utils.findRequiredViewAsType(source, R.id.customer_id_et, "field 'mSignUpEditText'", EditText.class);
    target.mMainLayout = Utils.findRequiredViewAsType(source, R.id.main_layout, "field 'mMainLayout'", RelativeLayout.class);
    target.mBackground = Utils.findRequiredViewAsType(source, R.id.imageView, "field 'mBackground'", ImageView.class);
    target.mToolbar = Utils.findRequiredViewAsType(source, R.id.toolbar, "field 'mToolbar'", CustomToolbar.class);
    target.mCustomerIdLabel = Utils.findRequiredViewAsType(source, R.id.customer_id_label, "field 'mCustomerIdLabel'", TextView.class);
    target.mAppVersionLabel = Utils.findRequiredViewAsType(source, R.id.app_version_label, "field 'mAppVersionLabel'", TextView.class);
    target.mUrlSwitch = Utils.findRequiredViewAsType(source, R.id.url_switch, "field 'mUrlSwitch'", SwitchCompat.class);
    target.mSystemAuthSwitch = Utils.findRequiredViewAsType(source, R.id.switch_sistem_auth, "field 'mSystemAuthSwitch'", SwitchCompat.class);
    target.mDisableCardIOSwitch = Utils.findRequiredViewAsType(source, R.id.switch_disable_cardio, "field 'mDisableCardIOSwitch'", SwitchCompat.class);
    target.mSkipConfirmationSwitch = Utils.findRequiredViewAsType(source, R.id.switch_sample_skip_confirmation, "field 'mSkipConfirmationSwitch'", SwitchCompat.class);
    target.mLanguageSpinner = Utils.findRequiredViewAsType(source, R.id.language_dropdown, "field 'mLanguageSpinner'", Spinner.class);
    target.customerPhoneNumber = Utils.findRequiredViewAsType(source, R.id.phone_number_text, "field 'customerPhoneNumber'", TextView.class);
    view = Utils.findRequiredView(source, R.id.change_phone_number, "field 'changePhoneNumber' and method 'onChangePhoneNumber'");
    target.changePhoneNumber = Utils.castView(view, R.id.change_phone_number, "field 'changePhoneNumber'", TextView.class);
    view7f08007a = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.onChangePhoneNumber();
      }
    });
    target.mProgressBar = Utils.findRequiredViewAsType(source, R.id.spinner_holder, "field 'mProgressBar'", RelativeLayout.class);
    view = Utils.findRequiredView(source, R.id.change_customer_id, "method 'onChangeCustomerId'");
    view7f080077 = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.onChangeCustomerId();
      }
    });
    view = Utils.findRequiredView(source, R.id.action_customize_ui, "method 'onCustomizeUi'");
    view7f08003b = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.onCustomizeUi();
      }
    });
    view = Utils.findRequiredView(source, R.id.details_app_version, "method 'onShowAppVersion'");
    view7f0800af = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.onShowAppVersion();
      }
    });
    view = Utils.findRequiredView(source, R.id.sign_up_btn, "method 'onSignUp'");
    view7f080188 = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.onSignUp();
      }
    });
    view = Utils.findRequiredView(source, R.id.action_change_merchant_info, "method 'onChangeMerchantInfo'");
    view7f080037 = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.onChangeMerchantInfo();
      }
    });
    view = Utils.findRequiredView(source, R.id.save_card, "method 'onSaveCardBtnClicked'");
    view7f080169 = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.onSaveCardBtnClicked();
      }
    });
    view = Utils.findRequiredView(source, R.id.save_sgroup_card, "method 'onSaveSGroupCardBtnClicked'");
    view7f08016e = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.onSaveSGroupCardBtnClicked();
      }
    });
  }

  @Override
  @CallSuper
  public void unbind() {
    LoginActivity target = this.target;
    if (target == null) throw new IllegalStateException("Bindings already cleared.");
    this.target = null;

    target.mNotLoggedInLayout = null;
    target.mSignUpEditText = null;
    target.mMainLayout = null;
    target.mBackground = null;
    target.mToolbar = null;
    target.mCustomerIdLabel = null;
    target.mAppVersionLabel = null;
    target.mUrlSwitch = null;
    target.mSystemAuthSwitch = null;
    target.mDisableCardIOSwitch = null;
    target.mSkipConfirmationSwitch = null;
    target.mLanguageSpinner = null;
    target.customerPhoneNumber = null;
    target.changePhoneNumber = null;
    target.mProgressBar = null;

    view7f08007a.setOnClickListener(null);
    view7f08007a = null;
    view7f080077.setOnClickListener(null);
    view7f080077 = null;
    view7f08003b.setOnClickListener(null);
    view7f08003b = null;
    view7f0800af.setOnClickListener(null);
    view7f0800af = null;
    view7f080188.setOnClickListener(null);
    view7f080188 = null;
    view7f080037.setOnClickListener(null);
    view7f080037 = null;
    view7f080169.setOnClickListener(null);
    view7f080169 = null;
    view7f08016e.setOnClickListener(null);
    view7f08016e = null;
  }
}
