// Generated code from Butter Knife. Do not modify!
package com.lianmeng.extand.lianmeng.discover.adapter;

import android.view.View;
import butterknife.ButterKnife.Finder;

public class PostAdapter$ViewHolder$$ViewInjector {
  public static void inject(Finder finder, final com.lianmeng.extand.lianmeng.discover.adapter.PostAdapter.ViewHolder target, Object source) {
    View view;
    view = finder.findById(source, 2131231499);
    if (view == null) {
      throw new IllegalStateException("Required view with id '2131231499' for field 'mPostHeadIv' was not found. If this view is optional add '@Optional' annotation.");
    }
    target.mPostHeadIv = (com.lianmeng.extand.lianmeng.discover.common.CircleImageView) view;
    view = finder.findById(source, 2131231752);
    if (view == null) {
      throw new IllegalStateException("Required view with id '2131231752' for field 'mPostChat' was not found. If this view is optional add '@Optional' annotation.");
    }
    target.mPostChat = (android.widget.TextView) view;
    view = finder.findById(source, 2131231502);
    if (view == null) {
      throw new IllegalStateException("Required view with id '2131231502' for field 'mPostTimeTv' was not found. If this view is optional add '@Optional' annotation.");
    }
    target.mPostTimeTv = (android.widget.TextView) view;
    view = finder.findById(source, 2131231500);
    if (view == null) {
      throw new IllegalStateException("Required view with id '2131231500' for field 'mPostNameTv' was not found. If this view is optional add '@Optional' annotation.");
    }
    target.mPostNameTv = (android.widget.TextView) view;
    view = finder.findById(source, 2131231753);
    if (view == null) {
      throw new IllegalStateException("Required view with id '2131231753' for field 'mPostContent' was not found. If this view is optional add '@Optional' annotation.");
    }
    target.mPostContent = (android.widget.TextView) view;
  }

  public static void reset(com.lianmeng.extand.lianmeng.discover.adapter.PostAdapter.ViewHolder target) {
    target.mPostHeadIv = null;
    target.mPostChat = null;
    target.mPostTimeTv = null;
    target.mPostNameTv = null;
    target.mPostContent = null;
  }
}
