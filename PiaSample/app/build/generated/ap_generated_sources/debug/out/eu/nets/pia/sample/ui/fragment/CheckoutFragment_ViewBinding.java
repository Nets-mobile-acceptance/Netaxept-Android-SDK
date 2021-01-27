// Generated code from Butter Knife. Do not modify!
package eu.nets.pia.sample.ui.fragment;

import android.view.View;
import android.widget.EditText;
import android.widget.Spinner;
import androidx.annotation.CallSuper;
import androidx.annotation.UiThread;
import butterknife.Unbinder;
import butterknife.internal.DebouncingOnClickListener;
import butterknife.internal.Utils;
import eu.nets.pia.sample.R;
import java.lang.IllegalStateException;
import java.lang.Override;

public class CheckoutFragment_ViewBinding implements Unbinder {
  private CheckoutFragment target;

  private View view7f08014a;

  @UiThread
  public CheckoutFragment_ViewBinding(final CheckoutFragment target, View source) {
    this.target = target;

    View view;
    target.mPriceView = Utils.findRequiredViewAsType(source, R.id.price_value_et, "field 'mPriceView'", EditText.class);
    target.mCurrencyDropdown = Utils.findRequiredViewAsType(source, R.id.currency_dropdown, "field 'mCurrencyDropdown'", Spinner.class);
    view = Utils.findRequiredView(source, R.id.payment_btn, "method 'onPayClicked'");
    view7f08014a = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.onPayClicked();
      }
    });
  }

  @Override
  @CallSuper
  public void unbind() {
    CheckoutFragment target = this.target;
    if (target == null) throw new IllegalStateException("Bindings already cleared.");
    this.target = null;

    target.mPriceView = null;
    target.mCurrencyDropdown = null;

    view7f08014a.setOnClickListener(null);
    view7f08014a = null;
  }
}
