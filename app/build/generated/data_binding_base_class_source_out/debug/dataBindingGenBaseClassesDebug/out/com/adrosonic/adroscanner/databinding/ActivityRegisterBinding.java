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

public abstract class ActivityRegisterBinding extends ViewDataBinding {
  @NonNull
  public final Button btnFb;

  @NonNull
  public final Button btnGoogle;

  @NonNull
  public final Button btnRegister;

  @NonNull
  public final EditText regEmail;

  @NonNull
  public final EditText regPwd;

  @NonNull
  public final ProgressBar registerProgressBar;

  @NonNull
  public final TextView registerTextView;

  protected ActivityRegisterBinding(DataBindingComponent _bindingComponent, View _root,
      int _localFieldCount, Button btnFb, Button btnGoogle, Button btnRegister, EditText regEmail,
      EditText regPwd, ProgressBar registerProgressBar, TextView registerTextView) {
    super(_bindingComponent, _root, _localFieldCount);
    this.btnFb = btnFb;
    this.btnGoogle = btnGoogle;
    this.btnRegister = btnRegister;
    this.regEmail = regEmail;
    this.regPwd = regPwd;
    this.registerProgressBar = registerProgressBar;
    this.registerTextView = registerTextView;
  }

  @NonNull
  public static ActivityRegisterBinding inflate(@NonNull LayoutInflater inflater,
      @Nullable ViewGroup root, boolean attachToRoot) {
    return inflate(inflater, root, attachToRoot, DataBindingUtil.getDefaultComponent());
  }

  @NonNull
  public static ActivityRegisterBinding inflate(@NonNull LayoutInflater inflater,
      @Nullable ViewGroup root, boolean attachToRoot, @Nullable DataBindingComponent component) {
    return DataBindingUtil.<ActivityRegisterBinding>inflate(inflater, com.adrosonic.adroscanner.R.layout.activity_register, root, attachToRoot, component);
  }

  @NonNull
  public static ActivityRegisterBinding inflate(@NonNull LayoutInflater inflater) {
    return inflate(inflater, DataBindingUtil.getDefaultComponent());
  }

  @NonNull
  public static ActivityRegisterBinding inflate(@NonNull LayoutInflater inflater,
      @Nullable DataBindingComponent component) {
    return DataBindingUtil.<ActivityRegisterBinding>inflate(inflater, com.adrosonic.adroscanner.R.layout.activity_register, null, false, component);
  }

  public static ActivityRegisterBinding bind(@NonNull View view) {
    return bind(view, DataBindingUtil.getDefaultComponent());
  }

  public static ActivityRegisterBinding bind(@NonNull View view,
      @Nullable DataBindingComponent component) {
    return (ActivityRegisterBinding)bind(component, view, com.adrosonic.adroscanner.R.layout.activity_register);
  }
}
