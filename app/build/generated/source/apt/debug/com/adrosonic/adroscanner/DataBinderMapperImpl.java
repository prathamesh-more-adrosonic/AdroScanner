package com.adrosonic.adroscanner;

import android.util.SparseArray;
import android.util.SparseIntArray;
import android.view.View;
import androidx.databinding.DataBinderMapper;
import androidx.databinding.DataBindingComponent;
import androidx.databinding.ViewDataBinding;
import com.adrosonic.adroscanner.databinding.ActivityLandingBindingImpl;
import com.adrosonic.adroscanner.databinding.ActivityMainBindingImpl;
import com.adrosonic.adroscanner.databinding.ActivityRegisterBindingImpl;
import com.adrosonic.adroscanner.databinding.ActivityResultBindingImpl;
import java.lang.IllegalArgumentException;
import java.lang.Integer;
import java.lang.Object;
import java.lang.Override;
import java.lang.RuntimeException;
import java.lang.String;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class DataBinderMapperImpl extends DataBinderMapper {
  private static final int LAYOUT_ACTIVITYLANDING = 1;

  private static final int LAYOUT_ACTIVITYMAIN = 2;

  private static final int LAYOUT_ACTIVITYREGISTER = 3;

  private static final int LAYOUT_ACTIVITYRESULT = 4;

  private static final SparseIntArray INTERNAL_LAYOUT_ID_LOOKUP = new SparseIntArray(4);

  static {
    INTERNAL_LAYOUT_ID_LOOKUP.put(com.adrosonic.adroscanner.R.layout.activity_landing, LAYOUT_ACTIVITYLANDING);
    INTERNAL_LAYOUT_ID_LOOKUP.put(com.adrosonic.adroscanner.R.layout.activity_main, LAYOUT_ACTIVITYMAIN);
    INTERNAL_LAYOUT_ID_LOOKUP.put(com.adrosonic.adroscanner.R.layout.activity_register, LAYOUT_ACTIVITYREGISTER);
    INTERNAL_LAYOUT_ID_LOOKUP.put(com.adrosonic.adroscanner.R.layout.activity_result, LAYOUT_ACTIVITYRESULT);
  }

  @Override
  public ViewDataBinding getDataBinder(DataBindingComponent component, View view, int layoutId) {
    int localizedLayoutId = INTERNAL_LAYOUT_ID_LOOKUP.get(layoutId);
    if(localizedLayoutId > 0) {
      final Object tag = view.getTag();
      if(tag == null) {
        throw new RuntimeException("view must have a tag");
      }
      switch(localizedLayoutId) {
        case  LAYOUT_ACTIVITYLANDING: {
          if ("layout/activity_landing_0".equals(tag)) {
            return new ActivityLandingBindingImpl(component, view);
          }
          throw new IllegalArgumentException("The tag for activity_landing is invalid. Received: " + tag);
        }
        case  LAYOUT_ACTIVITYMAIN: {
          if ("layout/activity_main_0".equals(tag)) {
            return new ActivityMainBindingImpl(component, view);
          }
          throw new IllegalArgumentException("The tag for activity_main is invalid. Received: " + tag);
        }
        case  LAYOUT_ACTIVITYREGISTER: {
          if ("layout/activity_register_0".equals(tag)) {
            return new ActivityRegisterBindingImpl(component, view);
          }
          throw new IllegalArgumentException("The tag for activity_register is invalid. Received: " + tag);
        }
        case  LAYOUT_ACTIVITYRESULT: {
          if ("layout/activity_result_0".equals(tag)) {
            return new ActivityResultBindingImpl(component, view);
          }
          throw new IllegalArgumentException("The tag for activity_result is invalid. Received: " + tag);
        }
      }
    }
    return null;
  }

  @Override
  public ViewDataBinding getDataBinder(DataBindingComponent component, View[] views, int layoutId) {
    if(views == null || views.length == 0) {
      return null;
    }
    int localizedLayoutId = INTERNAL_LAYOUT_ID_LOOKUP.get(layoutId);
    if(localizedLayoutId > 0) {
      final Object tag = views[0].getTag();
      if(tag == null) {
        throw new RuntimeException("view must have a tag");
      }
      switch(localizedLayoutId) {
      }
    }
    return null;
  }

  @Override
  public int getLayoutId(String tag) {
    if (tag == null) {
      return 0;
    }
    Integer tmpVal = InnerLayoutIdLookup.sKeys.get(tag);
    return tmpVal == null ? 0 : tmpVal;
  }

  @Override
  public String convertBrIdToString(int localId) {
    String tmpVal = InnerBrLookup.sKeys.get(localId);
    return tmpVal;
  }

  @Override
  public List<DataBinderMapper> collectDependencies() {
    ArrayList<DataBinderMapper> result = new ArrayList<DataBinderMapper>(1);
    result.add(new androidx.databinding.library.baseAdapters.DataBinderMapperImpl());
    return result;
  }

  private static class InnerBrLookup {
    static final SparseArray<String> sKeys = new SparseArray<String>(3);

    static {
      sKeys.put(0, "_all");
      sKeys.put(1, "user");
    }
  }

  private static class InnerLayoutIdLookup {
    static final HashMap<String, Integer> sKeys = new HashMap<String, Integer>(4);

    static {
      sKeys.put("layout/activity_landing_0", com.adrosonic.adroscanner.R.layout.activity_landing);
      sKeys.put("layout/activity_main_0", com.adrosonic.adroscanner.R.layout.activity_main);
      sKeys.put("layout/activity_register_0", com.adrosonic.adroscanner.R.layout.activity_register);
      sKeys.put("layout/activity_result_0", com.adrosonic.adroscanner.R.layout.activity_result);
    }
  }
}
