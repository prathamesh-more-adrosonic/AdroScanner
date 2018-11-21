package com.adrosonic.adroscanner.databinding;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingComponent;
import androidx.databinding.DataBindingUtil;
import androidx.databinding.ViewDataBinding;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

public abstract class ActivityLandingBinding extends ViewDataBinding {
  @NonNull
  public final FloatingActionButton floatingActionButton;

  protected ActivityLandingBinding(DataBindingComponent _bindingComponent, View _root,
      int _localFieldCount, FloatingActionButton floatingActionButton) {
    super(_bindingComponent, _root, _localFieldCount);
    this.floatingActionButton = floatingActionButton;
  }

  @NonNull
  public static ActivityLandingBinding inflate(@NonNull LayoutInflater inflater,
      @Nullable ViewGroup root, boolean attachToRoot) {
    return inflate(inflater, root, attachToRoot, DataBindingUtil.getDefaultComponent());
  }

  @NonNull
  public static ActivityLandingBinding inflate(@NonNull LayoutInflater inflater,
      @Nullable ViewGroup root, boolean attachToRoot, @Nullable DataBindingComponent component) {
    return DataBindingUtil.<ActivityLandingBinding>inflate(inflater, com.adrosonic.adroscanner.R.layout.activity_landing, root, attachToRoot, component);
  }

  @NonNull
  public static ActivityLandingBinding inflate(@NonNull LayoutInflater inflater) {
    return inflate(inflater, DataBindingUtil.getDefaultComponent());
  }

  @NonNull
  public static ActivityLandingBinding inflate(@NonNull LayoutInflater inflater,
      @Nullable DataBindingComponent component) {
    return DataBindingUtil.<ActivityLandingBinding>inflate(inflater, com.adrosonic.adroscanner.R.layout.activity_landing, null, false, component);
  }

  public static ActivityLandingBinding bind(@NonNull View view) {
    return bind(view, DataBindingUtil.getDefaultComponent());
  }

  public static ActivityLandingBinding bind(@NonNull View view,
      @Nullable DataBindingComponent component) {
    return (ActivityLandingBinding)bind(component, view, com.adrosonic.adroscanner.R.layout.activity_landing);
  }
}
