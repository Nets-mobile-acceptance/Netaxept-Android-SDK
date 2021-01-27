// Generated code from Butter Knife. Do not modify!
package eu.nets.pia.sample.ui.adapter;

import android.view.View;
import android.widget.LinearLayout;
import androidx.annotation.CallSuper;
import androidx.annotation.UiThread;
import butterknife.Unbinder;
import butterknife.internal.Utils;
import eu.nets.pia.sample.R;
import java.lang.IllegalStateException;
import java.lang.Override;

public class PaymentMethodsAdapter$NewCardMethodViewHolder_ViewBinding implements Unbinder {
  private PaymentMethodsAdapter.NewCardMethodViewHolder target;

  @UiThread
  public PaymentMethodsAdapter$NewCardMethodViewHolder_ViewBinding(
      PaymentMethodsAdapter.NewCardMethodViewHolder target, View source) {
    this.target = target;

    target.mSupportedSchemesLayout = Utils.findRequiredViewAsType(source, R.id.supported_schemes_layout, "field 'mSupportedSchemesLayout'", LinearLayout.class);
  }

  @Override
  @CallSuper
  public void unbind() {
    PaymentMethodsAdapter.NewCardMethodViewHolder target = this.target;
    if (target == null) throw new IllegalStateException("Bindings already cleared.");
    this.target = null;

    target.mSupportedSchemesLayout = null;
  }
}
