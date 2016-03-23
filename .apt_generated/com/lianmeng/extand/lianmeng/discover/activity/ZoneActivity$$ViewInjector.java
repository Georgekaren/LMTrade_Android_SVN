// Generated code from Butter Knife. Do not modify!
package com.lianmeng.extand.lianmeng.discover.activity;

import android.view.View;
import butterknife.ButterKnife.Finder;

public class ZoneActivity$$ViewInjector {
  public static void inject(Finder finder, final com.lianmeng.extand.lianmeng.discover.activity.ZoneActivity target, Object source) {
    View view;
    view = finder.findById(source, 2131231538);
    if (view == null) {
      throw new IllegalStateException("Required view with id '2131231538' for field 'mLayoutLoading' was not found. If this view is optional add '@Optional' annotation.");
    }
    target.mLayoutLoading = (android.widget.LinearLayout) view;
    view = finder.findById(source, 2131231535);
    if (view == null) {
      throw new IllegalStateException("Required view with id '2131231535' for field 'mRadioGroup' was not found. If this view is optional add '@Optional' annotation.");
    }
    target.mRadioGroup = (android.widget.RadioGroup) view;
    view = finder.findById(source, 2131231539);
    if (view == null) {
      throw new IllegalStateException("Required view with id '2131231539' for field 'mViewPager' was not found. If this view is optional add '@Optional' annotation.");
    }
    target.mViewPager = (android.support.v4.view.ViewPager) view;
    view = finder.findById(source, 2131231534);
    if (view == null) {
      throw new IllegalStateException("Required view with id '2131231534' for method 'onPublishClicked' was not found. If this view is optional add '@Optional' annotation.");
    }
    view.setOnClickListener(
      new android.view.View.OnClickListener() {
        @Override public void onClick(
          android.view.View p0
        ) {
          target.onPublishClicked(p0);
        }
      });
    view = finder.findById(source, 2131231533);
    if (view == null) {
      throw new IllegalStateException("Required view with id '2131231533' for method 'onSearcchClicked' was not found. If this view is optional add '@Optional' annotation.");
    }
    view.setOnClickListener(
      new android.view.View.OnClickListener() {
        @Override public void onClick(
          android.view.View p0
        ) {
          target.onSearcchClicked(p0);
        }
      });
  }

  public static void reset(com.lianmeng.extand.lianmeng.discover.activity.ZoneActivity target) {
    target.mLayoutLoading = null;
    target.mRadioGroup = null;
    target.mViewPager = null;
  }
}
