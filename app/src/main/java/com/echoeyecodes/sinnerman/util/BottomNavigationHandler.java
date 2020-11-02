package com.echoeyecodes.sinnerman.util;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import com.echoeyecodes.sinnerman.BottomNavigationFragments.HomeFragment;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class BottomNavigationHandler extends ViewModel {
    private final List<Fragment> fragments;
    private final MutableLiveData<Fragment> fragmentLiveData = new MutableLiveData<>();

    public BottomNavigationHandler() {
        fragments = new ArrayList<>();
        Fragment fragment = new HomeFragment();
        addFragment(fragment);
    }

    public void addFragment(Fragment fragment){
        int index = fragments.indexOf(fragment);
        if(index == -1){
            fragments.add(fragment);
        }else{
            Collections.swap(fragments, index, fragments.size() - 1);
        }
        showFragment();
    }

    private void showFragment(){
        Fragment fragment = fragments.get(fragments.size() - 1);
        fragmentLiveData.setValue(fragment);
    }

    public MutableLiveData<Fragment> getFragmentLiveData() {
        return fragmentLiveData;
    }

    public Fragment getCurrentFragment(){
        return fragments.get(fragments.size() - 1);
    }

    public Fragment popBackStack(){
        Fragment fragment = fragments.remove(fragments.size() - 1);
        showFragment();
        return fragment;
    }

    public int getFragmentSize(){
        return fragments.size();
    }
}
