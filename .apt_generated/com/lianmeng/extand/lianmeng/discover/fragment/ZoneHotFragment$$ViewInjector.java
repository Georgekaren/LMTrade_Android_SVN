// Generated code from Butter Knife. Do not modify!
package com.lianmeng.extand.lianmeng.discover.fragment;

import android.view.View;
import butterknife.ButterKnife.Finder;

public class ZoneHotFragment$$ViewInjector {
  public static void inject(Finder finder, final com.lianmeng.extand.lianmeng.discover.fragment.ZoneHotFragment target, Object source) {
    View view;
    view = finder.findById(source, 2131231527);
    if (view == null) {
      throw new IllegalStateException("Required view with id '2131231527' for field 'mGridView' was not found. If this view is optional add '@Optional' annotation.");
    }
    target.mGridView = (android.widget.GridView) view;
  }

  public static void reset(com.lianmeng.extand.lianmeng.discover.fragment.ZoneHotFragment target) {
    target.mGridView = null;
  }
}
