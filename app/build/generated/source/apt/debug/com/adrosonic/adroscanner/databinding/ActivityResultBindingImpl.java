package com.adrosonic.adroscanner.databinding;
import com.adrosonic.adroscanner.R;
import com.adrosonic.adroscanner.BR;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import android.view.View;
@SuppressWarnings("unchecked")
public class ActivityResultBindingImpl extends ActivityResultBinding  {

    @Nullable
    private static final androidx.databinding.ViewDataBinding.IncludedLayouts sIncludes;
    @Nullable
    private static final android.util.SparseIntArray sViewsWithIds;
    static {
        sIncludes = null;
        sViewsWithIds = new android.util.SparseIntArray();
        sViewsWithIds.put(R.id.imageViewResult, 9);
        sViewsWithIds.put(R.id.textView1, 10);
        sViewsWithIds.put(R.id.textView2, 11);
        sViewsWithIds.put(R.id.textView3, 12);
        sViewsWithIds.put(R.id.textView4, 13);
        sViewsWithIds.put(R.id.textView5, 14);
        sViewsWithIds.put(R.id.textView6, 15);
        sViewsWithIds.put(R.id.textView7, 16);
    }
    // views
    @NonNull
    private final android.widget.LinearLayout mboundView0;
    // variables
    // values
    // listeners
    // Inverse Binding Event Handlers

    public ActivityResultBindingImpl(@Nullable androidx.databinding.DataBindingComponent bindingComponent, @NonNull View root) {
        this(bindingComponent, root, mapBindings(bindingComponent, root, 17, sIncludes, sViewsWithIds));
    }
    private ActivityResultBindingImpl(androidx.databinding.DataBindingComponent bindingComponent, View root, Object[] bindings) {
        super(bindingComponent, root, 0
            , (android.widget.ImageView) bindings[9]
            , (android.widget.EditText) bindings[7]
            , (android.widget.EditText) bindings[3]
            , (android.widget.EditText) bindings[6]
            , (android.widget.EditText) bindings[1]
            , (android.widget.EditText) bindings[4]
            , (android.widget.EditText) bindings[5]
            , (android.widget.EditText) bindings[2]
            , (android.widget.EditText) bindings[8]
            , (android.widget.TextView) bindings[10]
            , (android.widget.TextView) bindings[11]
            , (android.widget.TextView) bindings[12]
            , (android.widget.TextView) bindings[13]
            , (android.widget.TextView) bindings[14]
            , (android.widget.TextView) bindings[15]
            , (android.widget.TextView) bindings[16]
            );
        this.mboundView0 = (android.widget.LinearLayout) bindings[0];
        this.mboundView0.setTag(null);
        this.resultAddress.setTag(null);
        this.resultCompany.setTag(null);
        this.resultEmail.setTag(null);
        this.resultName.setTag(null);
        this.resultPhone.setTag(null);
        this.resultPhoneAlt.setTag(null);
        this.resultTitle.setTag(null);
        this.resultWebsite.setTag(null);
        setRootTag(root);
        // listeners
        invalidateAll();
    }

    @Override
    public void invalidateAll() {
        synchronized(this) {
                mDirtyFlags = 0x2L;
        }
        requestRebind();
    }

    @Override
    public boolean hasPendingBindings() {
        synchronized(this) {
            if (mDirtyFlags != 0) {
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean setVariable(int variableId, @Nullable Object variable)  {
        boolean variableSet = true;
        if (BR.user == variableId) {
            setUser((com.adrosonic.adroscanner.entity.UserEntity) variable);
        }
        else {
            variableSet = false;
        }
            return variableSet;
    }

    public void setUser(@Nullable com.adrosonic.adroscanner.entity.UserEntity User) {
        this.mUser = User;
        synchronized(this) {
            mDirtyFlags |= 0x1L;
        }
        notifyPropertyChanged(BR.user);
        super.requestRebind();
    }

    @Override
    protected boolean onFieldChange(int localFieldId, Object object, int fieldId) {
        switch (localFieldId) {
        }
        return false;
    }

    @Override
    protected void executeBindings() {
        long dirtyFlags = 0;
        synchronized(this) {
            dirtyFlags = mDirtyFlags;
            mDirtyFlags = 0;
        }
        java.lang.String userCompany = null;
        java.lang.String userName = null;
        java.lang.String userJobTitle = null;
        java.lang.String userPhoneNumberAlt = null;
        java.lang.String userEmail = null;
        java.lang.String userPhoneNumber = null;
        java.lang.String userWebsite = null;
        com.adrosonic.adroscanner.entity.UserEntity user = mUser;
        java.lang.String userAddress = null;

        if ((dirtyFlags & 0x3L) != 0) {



                if (user != null) {
                    // read user.company
                    userCompany = user.getCompany();
                    // read user.name
                    userName = user.getName();
                    // read user.jobTitle
                    userJobTitle = user.getJobTitle();
                    // read user.phoneNumberAlt
                    userPhoneNumberAlt = user.getPhoneNumberAlt();
                    // read user.email
                    userEmail = user.getEmail();
                    // read user.phoneNumber
                    userPhoneNumber = user.getPhoneNumber();
                    // read user.website
                    userWebsite = user.getWebsite();
                    // read user.address
                    userAddress = user.getAddress();
                }
        }
        // batch finished
        if ((dirtyFlags & 0x3L) != 0) {
            // api target 1

            androidx.databinding.adapters.TextViewBindingAdapter.setText(this.resultAddress, userAddress);
            androidx.databinding.adapters.TextViewBindingAdapter.setText(this.resultCompany, userCompany);
            androidx.databinding.adapters.TextViewBindingAdapter.setText(this.resultEmail, userEmail);
            androidx.databinding.adapters.TextViewBindingAdapter.setText(this.resultName, userName);
            androidx.databinding.adapters.TextViewBindingAdapter.setText(this.resultPhone, userPhoneNumber);
            androidx.databinding.adapters.TextViewBindingAdapter.setText(this.resultPhoneAlt, userPhoneNumberAlt);
            androidx.databinding.adapters.TextViewBindingAdapter.setText(this.resultTitle, userJobTitle);
            androidx.databinding.adapters.TextViewBindingAdapter.setText(this.resultWebsite, userWebsite);
        }
    }
    // Listener Stub Implementations
    // callback impls
    // dirty flag
    private  long mDirtyFlags = 0xffffffffffffffffL;
    /* flag mapping
        flag 0 (0x1L): user
        flag 1 (0x2L): null
    flag mapping end*/
    //end
}