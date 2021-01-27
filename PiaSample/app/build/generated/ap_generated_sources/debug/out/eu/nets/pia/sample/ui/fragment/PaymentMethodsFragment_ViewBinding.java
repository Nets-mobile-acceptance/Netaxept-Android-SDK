// Generated code from Butter Knife. Do not modify!
package eu.nets.pia.sample.ui.fragment;

import android.view.View;
import androidx.annotation.CallSuper;
import androidx.annotation.UiThread;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.Unbinder;
import butterknife.internal.Utils;
import eu.nets.pia.sample.R;
import eu.nets.pia.sample.ui.widget.CustomToolbar;
import java.lang.IllegalStateException;
import java.lang.Override;

public class PaymentMethodsFragment_ViewBinding implements Unbinder {
  private PaymentMethodsFragment target;

  @UiThread
  public PaymentMethodsFragment_ViewBinding(PaymentMethodsFragment target, View source) {
    this.target = target;

    target.mToolbar = Utils.findRequiredViewAsType(source, R.id.toolbar, "field 'mToolbar'", CustomToolbar.class);
    target.mRecyclerView = Utils.findRequiredViewAsType(source, R.id.recycler_view, "field 'mRecyclerView'", RecyclerView.class);
  }

  @Override
  @CallSuper
  public void unbind() {
    PaymentMethodsFragment target = this.target;
    if (target == null) throw new IllegalStateException("Bindings already cleared.");
    this.target = null;

    target.mToolbar = null;
    target.mRecyclerView = null;
  }
}
