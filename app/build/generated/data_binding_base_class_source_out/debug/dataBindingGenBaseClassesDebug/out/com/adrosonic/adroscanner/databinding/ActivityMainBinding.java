package com.adrosonic.adroscanner.databinding;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingComponent;
import androidx.databinding.DataBindingUtil;
import androidx.databinding.ViewDataBinding;

public abstract class ActivityMainBinding extends ViewDataBinding {
  @NonNull
  public final Button btnLogin;

  @NonNull
  public final Button btnSignup;

  @NonNull
  public final EditText loginEmail;

  @NonNull
  public final ProgressBar loginProgressBar;

  @NonNull
  public final EditText loginPwd;

  @NonNull
  public final TextView loginTextView;

  protected ActivityMainBinding(DataBindingComponent _bindingComponent, View _root,
      int _localFieldCount, Button btnLogin, Button btnSignup, EditText loginEmail,
      ProgressBar loginProgressBar, EditText loginPwd, TextView loginTextView) {
    super(_bindingComponent, _root, _localFieldCount);
    this.btnLogin = btnLogin;
    this.btnSignup = btnSignup;
    this.loginEmail = loginEmail;
    this.loginProgressBar = loginProgressBar;
    this.loginPwd = loginPwd;
    this.loginTextView = loginTextView;
  }

  @NonNull
  public static ActivityMainBinding inflate(@NonNull LayoutInflater inflater,
      @Nullable ViewGroup root, boolean attachToRoot) {
    return inflate(inflater, root, attachToRoot, DataBindingUtil.getDefaultComponent());
  }

  @NonNull
  public static ActivityMainBinding inflate(@NonNull LayoutInflater inflater,
      @Nullable ViewGroup root, boolean attachToRoot, @Nullable DataBindingComponent component) {
    return DataBindingUtil.<ActivityMainBinding>inflate(inflater, com.adrosonic.adroscanner.R.layout.activity_main, root, attachToRoot, component);
  }

  @NonNull
  public static ActivityMainBinding inflate(@NonNull LayoutInflater inflater) {
    return inflate(inflater, DataBindingUtil.getDefaultComponent());
  }

  @NonNull
  public static ActivityMainBinding inflate(@NonNull LayoutInflater inflater,
      @Nullable DataBindingComponent component) {
    return DataBindingUtil.<ActivityMainBinding>inflate(inflater, com.adrosonic.adroscanner.R.layout.activity_main, null, false, component);
  }

  public static ActivityMainBinding bind(@NonNull View view) {
    return bind(view, DataBindingUtil.getDefaultComponent());
  }

  public static ActivityMainBinding bind(@NonNull View view,
      @Nullable DataBindingComponent component) {
    return (ActivityMainBinding)bind(component, view, com.adrosonic.adroscanner.R.layout.activity_main);
  }
}
