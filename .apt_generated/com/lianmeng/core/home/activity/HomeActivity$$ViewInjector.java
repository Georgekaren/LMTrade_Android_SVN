// Generated code from Butter Knife. Do not modify!
package com.lianmeng.core.home.activity;

import android.view.View;
import butterknife.ButterKnife.Finder;

public class HomeActivity$$ViewInjector {
  public static void inject(Finder finder, final com.lianmeng.core.home.activity.HomeActivity target, Object source) {
    View view;
    view = finder.findById(source, 2131231549);
    if (view == null) {
      throw new IllegalStateException("Required view with id '2131231549' for field 'lilayout_home_product_pear_su' was not found. If this view is optional add '@Optional' annotation.");
    }
    target.lilayout_home_product_pear_su = (android.widget.ImageView) view;
    view = finder.findById(source, 2131231551);
    if (view == null) {
      throw new IllegalStateException("Required view with id '2131231551' for field 'lilayout_home_product_toothpaste_su' was not found. If this view is optional add '@Optional' annotation.");
    }
    target.lilayout_home_product_toothpaste_su = (android.widget.ImageView) view;
    view = finder.findById(source, 2131231304);
    if (view == null) {
      throw new IllegalStateException("Required view with id '2131231304' for field 'mid_title' was not found. If this view is optional add '@Optional' annotation.");
    }
    target.mid_title = (android.widget.TextView) view;
    view = finder.findById(source, 2131231547);
    if (view == null) {
      throw new IllegalStateException("Required view with id '2131231547' for field 'lilayout_hone_display_big_image_control' was not found. If this view is optional add '@Optional' annotation.");
    }
    target.lilayout_hone_display_big_image_control = (android.widget.LinearLayout) view;
    view = finder.findById(source, 2131231550);
    if (view == null) {
      throw new IllegalStateException("Required view with id '2131231550' for field 'lilayout_home_product_kekou_su' was not found. If this view is optional add '@Optional' annotation.");
    }
    target.lilayout_home_product_kekou_su = (android.widget.ImageView) view;
    view = finder.findById(source, 2131231548);
    if (view == null) {
      throw new IllegalStateException("Required view with id '2131231548' for field 'lilayout_home_product_apple_su' was not found. If this view is optional add '@Optional' annotation.");
    }
    target.lilayout_home_product_apple_su = (android.widget.ImageView) view;
  }

  public static void reset(com.lianmeng.core.home.activity.HomeActivity target) {
    target.lilayout_home_product_pear_su = null;
    target.lilayout_home_product_toothpaste_su = null;
    target.mid_title = null;
    target.lilayout_hone_display_big_image_control = null;
    target.lilayout_home_product_kekou_su = null;
    target.lilayout_home_product_apple_su = null;
  }
}
