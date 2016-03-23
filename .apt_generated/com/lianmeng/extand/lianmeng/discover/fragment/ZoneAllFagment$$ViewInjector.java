// Generated code from Butter Knife. Do not modify!
package com.lianmeng.extand.lianmeng.discover.fragment;

import android.view.View;
import butterknife.ButterKnife.Finder;

public class ZoneAllFagment$$ViewInjector {
  public static void inject(Finder finder, final com.lianmeng.extand.lianmeng.discover.fragment.ZoneAllFagment target, Object source) {
    View view;
    view = finder.findById(source, 2131231489);
    if (view == null) {
      throw new IllegalStateException("Required view with id '2131231489' for field 'mListView' was not found. If this view is optional add '@Optional' annotation.");
    }
    target.mListView = (com.lianmeng.extand.lianmeng.discover.refresh_list.RefreshListView) view;
  }

  public static void reset(com.lianmeng.extand.lianmeng.discover.fragment.ZoneAllFagment target) {
    target.mListView = null;
  }
}
