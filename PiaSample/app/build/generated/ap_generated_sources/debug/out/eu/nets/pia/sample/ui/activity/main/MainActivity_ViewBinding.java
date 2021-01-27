// Generated code from Butter Knife. Do not modify!
package eu.nets.pia.sample.ui.activity.main;

import android.view.View;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;
import androidx.annotation.CallSuper;
import androidx.annotation.UiThread;
import androidx.appcompat.widget.Toolbar;
import butterknife.Unbinder;
import butterknife.internal.Utils;
import eu.nets.pia.sample.R;
import java.lang.IllegalStateException;
import java.lang.Override;

public class MainActivity_ViewBinding implements Unbinder {
  private MainActivity target;

  @UiThread
  public MainActivity_ViewBinding(MainActivity target) {
    this(target, target.getWindow().getDecorView());
  }

  @UiThread
  public MainActivity_ViewBinding(MainActivity target, View source) {
    this.target = target;

    target.mProgressBar = Utils.findRequiredViewAsType(source, R.id.spinner_holder, "field 'mProgressBar'", RelativeLayout.class);
    target.mToolbar = Utils.findRequiredViewAsType(source, R.id.toolbar_view, "field 'mToolbar'", Toolbar.class);
    target.mFrame = Utils.findRequiredViewAsType(source, R.id.frame_layout, "field 'mFrame'", FrameLayout.class);
  }

  @Override
  @CallSuper
  public void unbind() {
    MainActivity target = this.target;
    if (target == null) throw new IllegalStateException("Bindings already cleared.");
    this.target = null;

    target.mProgressBar = null;
    target.mToolbar = null;
    target.mFrame = null;
  }
}
