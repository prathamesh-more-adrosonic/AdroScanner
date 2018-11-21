package com.adrosonic.adroscanner.databinding;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.Bindable;
import androidx.databinding.DataBindingComponent;
import androidx.databinding.DataBindingUtil;
import androidx.databinding.ViewDataBinding;
import com.adrosonic.adroscanner.entity.UserEntity;

public abstract class ActivityResultBinding extends ViewDataBinding {
  @NonNull
  public final ImageView imageViewResult;

  @NonNull
  public final EditText resultAddress;

  @NonNull
  public final EditText resultCompany;

  @NonNull
  public final EditText resultEmail;

  @NonNull
  public final EditText resultName;

  @NonNull
  public final EditText resultPhone;

  @NonNull
  public final EditText resultPhoneAlt;

  @NonNull
  public final EditText resultTitle;

  @NonNull
  public final EditText resultWebsite;

  @NonNull
  public final TextView textView1;

  @NonNull
  public final TextView textView2;

  @NonNull
  public final TextView textView3;

  @NonNull
  public final TextView textView4;

  @NonNull
  public final TextView textView5;

  @NonNull
  public final TextView textView6;

  @NonNull
  public final TextView textView7;

  @Bindable
  protected UserEntity mUser;

  protected ActivityResultBinding(DataBindingComponent _bindingComponent, View _root,
      int _localFieldCount, ImageView imageViewResult, EditText resultAddress,
      EditText resultCompany, EditText resultEmail, EditText resultName, EditText resultPhone,
      EditText resultPhoneAlt, EditText resultTitle, EditText resultWebsite, TextView textView1,
      TextView textView2, TextView textView3, TextView textView4, TextView textView5,
      TextView textView6, TextView textView7) {
    super(_bindingComponent, _root, _localFieldCount);
    this.imageViewResult = imageViewResult;
    this.resultAddress = resultAddress;
    this.resultCompany = resultCompany;
    this.resultEmail = resultEmail;
    this.resultName = resultName;
    this.resultPhone = resultPhone;
    this.resultPhoneAlt = resultPhoneAlt;
    this.resultTitle = resultTitle;
    this.resultWebsite = resultWebsite;
    this.textView1 = textView1;
    this.textView2 = textView2;
    this.textView3 = textView3;
    this.textView4 = textView4;
    this.textView5 = textView5;
    this.textView6 = textView6;
    this.textView7 = textView7;
  }

  public abstract void setUser(@Nullable UserEntity user);

  @Nullable
  public UserEntity getUser() {
    return mUser;
  }

  @NonNull
  public static ActivityResultBinding inflate(@NonNull LayoutInflater inflater,
      @Nullable ViewGroup root, boolean attachToRoot) {
    return inflate(inflater, root, attachToRoot, DataBindingUtil.getDefaultComponent());
  }

  @NonNull
  public static ActivityResultBinding inflate(@NonNull LayoutInflater inflater,
      @Nullable ViewGroup root, boolean attachToRoot, @Nullable DataBindingComponent component) {
    return DataBindingUtil.<ActivityResultBinding>inflate(inflater, com.adrosonic.adroscanner.R.layout.activity_result, root, attachToRoot, component);
  }

  @NonNull
  public static ActivityResultBinding inflate(@NonNull LayoutInflater inflater) {
    return inflate(inflater, DataBindingUtil.getDefaultComponent());
  }

  @NonNull
  public static ActivityResultBinding inflate(@NonNull LayoutInflater inflater,
      @Nullable DataBindingComponent component) {
    return DataBindingUtil.<ActivityResultBinding>inflate(inflater, com.adrosonic.adroscanner.R.layout.activity_result, null, false, component);
  }

  public static ActivityResultBinding bind(@NonNull View view) {
    return bind(view, DataBindingUtil.getDefaultComponent());
  }

  public static ActivityResultBinding bind(@NonNull View view,
      @Nullable DataBindingComponent component) {
    return (ActivityResultBinding)bind(component, view, com.adrosonic.adroscanner.R.layout.activity_result);
  }
}
