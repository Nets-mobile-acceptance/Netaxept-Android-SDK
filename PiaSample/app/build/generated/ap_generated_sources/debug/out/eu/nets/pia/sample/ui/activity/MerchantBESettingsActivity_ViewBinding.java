// Generated code from Butter Knife. Do not modify!
package eu.nets.pia.sample.ui.activity;

import android.view.View;
import android.widget.EditText;
import androidx.annotation.CallSuper;
import androidx.annotation.UiThread;
import butterknife.Unbinder;
import butterknife.internal.Utils;
import eu.nets.pia.sample.R;
import eu.nets.pia.sample.ui.widget.CustomToolbar;
import java.lang.IllegalStateException;
import java.lang.Override;

public class MerchantBESettingsActivity_ViewBinding implements Unbinder {
  private MerchantBESettingsActivity target;

  @UiThread
  public MerchantBESettingsActivity_ViewBinding(MerchantBESettingsActivity target) {
    this(target, target.getWindow().getDecorView());
  }

  @UiThread
  public MerchantBESettingsActivity_ViewBinding(MerchantBESettingsActivity target, View source) {
    this.target = target;

    target.mToolbar = Utils.findRequiredViewAsType(source, R.id.toolbar, "field 'mToolbar'", CustomToolbar.class);
    target.mTestEnvEdit = Utils.findRequiredViewAsType(source, R.id.test_env_et, "field 'mTestEnvEdit'", EditText.class);
    target.mProdEnvEdit = Utils.findRequiredViewAsType(source, R.id.prod_env_et, "field 'mProdEnvEdit'", EditText.class);
    target.mTestIdEdit = Utils.findRequiredViewAsType(source, R.id.test_merchant_id_et, "field 'mTestIdEdit'", EditText.class);
    target.mProdIdEdit = Utils.findRequiredViewAsType(source, R.id.prod_merchant_id_et, "field 'mProdIdEdit'", EditText.class);
  }

  @Override
  @CallSuper
  public void unbind() {
    MerchantBESettingsActivity target = this.target;
    if (target == null) throw new IllegalStateException("Bindings already cleared.");
    this.target = null;

    target.mToolbar = null;
    target.mTestEnvEdit = null;
    target.mProdEnvEdit = null;
    target.mTestIdEdit = null;
    target.mProdIdEdit = null;
  }
}
