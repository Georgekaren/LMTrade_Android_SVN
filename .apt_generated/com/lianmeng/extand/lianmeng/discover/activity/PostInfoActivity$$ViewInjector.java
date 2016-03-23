// Generated code from Butter Knife. Do not modify!
package com.lianmeng.extand.lianmeng.discover.activity;

import android.view.View;
import butterknife.ButterKnife.Finder;

public class PostInfoActivity$$ViewInjector {
  public static void inject(Finder finder, final com.lianmeng.extand.lianmeng.discover.activity.PostInfoActivity target, Object source) {
    View view;
    view = finder.findById(source, 2131231490);
    if (view == null) {
      throw new IllegalStateException("Required view with id '2131231490' for field 'mTopBar' was not found. If this view is optional add '@Optional' annotation.");
    }
    target.mTopBar = (com.lianmeng.extand.lianmeng.discover.widget.TopBarView) view;
    view = finder.findById(source, 2131231497);
    if (view == null) {
      throw new IllegalStateException("Required view with id '2131231497' for field 'mListView' was not found. If this view is optional add '@Optional' annotation.");
    }
    target.mListView = (com.lianmeng.extand.lianmeng.discover.refresh_list.RefreshListView) view;
    view = finder.findById(source, 2131231494);
    if (view == null) {
      throw new IllegalStateException("Required view with id '2131231494' for field 'mEditInput' was not found. If this view is optional add '@Optional' annotation.");
    }
    target.mEditInput = (android.widget.EditText) view;
    view = finder.findById(source, 2131231492);
    if (view == null) {
      throw new IllegalStateException("Required view with id '2131231492' for field 'mPostTagTv' was not found. If this view is optional add '@Optional' annotation.");
    }
    target.mPostTagTv = (android.widget.TextView) view;
    view = finder.findById(source, 2131231491);
    if (view == null) {
      throw new IllegalStateException("Required view with id '2131231491' for method 'onFromClicked' was not found. If this view is optional add '@Optional' annotation.");
    }
    view.setOnClickListener(
      new android.view.View.OnClickListener() {
        @Override public void onClick(
          android.view.View p0
        ) {
          target.onFromClicked(p0);
        }
      });
    view = finder.findById(source, 2131231495);
    if (view == null) {
      throw new IllegalStateException("Required view with id '2131231495' for method 'onPostSendClicked' was not found. If this view is optional add '@Optional' annotation.");
    }
    view.setOnClickListener(
      new android.view.View.OnClickListener() {
        @Override public void onClick(
          android.view.View p0
        ) {
          target.onPostSendClicked(p0);
        }
      });
  }

  public static void reset(com.lianmeng.extand.lianmeng.discover.activity.PostInfoActivity target) {
    target.mTopBar = null;
    target.mListView = null;
    target.mEditInput = null;
    target.mPostTagTv = null;
  }
}
