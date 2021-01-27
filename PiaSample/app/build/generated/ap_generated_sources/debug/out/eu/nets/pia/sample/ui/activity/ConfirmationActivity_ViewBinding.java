// Generated code from Butter Knife. Do not modify!
package eu.nets.pia.sample.ui.activity;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.CallSuper;
import androidx.annotation.UiThread;
import androidx.constraintlayout.widget.ConstraintLayout;
import butterknife.Unbinder;
import butterknife.internal.Utils;
import eu.nets.pia.sample.R;
import eu.nets.pia.sample.ui.widget.CustomToolbar;
import java.lang.IllegalStateException;
import java.lang.Override;

public class ConfirmationActivity_ViewBinding implements Unbinder {
  private ConfirmationActivity target;

  @UiThread
  public ConfirmationActivity_ViewBinding(ConfirmationActivity target) {
    this(target, target.getWindow().getDecorView());
  }

  @UiThread
  public ConfirmationActivity_ViewBinding(ConfirmationActivity target, View source) {
    this.target = target;

    target.mToolbar = Utils.findRequiredViewAsType(source, R.id.custom_toolbar, "field 'mToolbar'", CustomToolbar.class);
    target.mStatusIcon = Utils.findRequiredViewAsType(source, R.id.status_icon, "field 'mStatusIcon'", ImageView.class);
    target.mStatusMessage = Utils.findRequiredViewAsType(source, R.id.status_message, "field 'mStatusMessage'", TextView.class);
    target.mRootView = Utils.findRequiredViewAsType(source, R.id.root_view, "field 'mRootView'", ConstraintLayout.class);
  }

  @Override
  @CallSuper
  public void unbind() {
    ConfirmationActivity target = this.target;
    if (target == null) throw new IllegalStateException("Bindings already cleared.");
    this.target = null;

    target.mToolbar = null;
    target.mStatusIcon = null;
    target.mStatusMessage = null;
    target.mRootView = null;
  }
}
