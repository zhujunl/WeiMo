package com.miaxis.mr230m.view.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.miaxis.mr230m.R;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.databinding.ViewDataBinding;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;


public abstract class BaseBindingFragment<V extends ViewDataBinding> extends Fragment {

    protected V binding;
    private FragmentManager fragmentManager;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, initLayout(), container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        binding.setLifecycleOwner(this);
        fragmentManager=getActivity().getSupportFragmentManager();
        initView(binding, savedInstanceState);
        initData(binding, savedInstanceState);
    }

    protected abstract int initLayout();

    protected abstract void initView(@NonNull V binding, @Nullable Bundle savedInstanceState);

    protected void initData(@NonNull V binding, @Nullable Bundle savedInstanceState) {

    }

    protected void finish() {
        if (fragmentManager.getBackStackEntryCount() > 1) {
            fragmentManager.popBackStack();
            return;
        }
        if(getActivity()!=null) {
            getActivity().finish();
        }
    }


    public void nvTo(Fragment fragment){
        fragmentManager.beginTransaction()
                .replace(R.id.container,fragment)
                .addToBackStack(null)
                .commit();
    }
}
