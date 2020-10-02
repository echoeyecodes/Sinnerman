package com.example.myapplication.util;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import com.example.myapplication.BottomNavigationFragments.HomeFragment;

import java.util.ArrayList;
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
            Fragment frag = fragments.get(index);
            fragments.remove(frag);
            fragments.add(frag);
        }
        showFragment();
    }

    private void showFragment(){
        fragmentLiveData.setValue(fragments.get(fragments.size() - 1));
    }

    public MutableLiveData<Fragment> getFragmentLiveData() {
        return fragmentLiveData;
    }

    public Fragment getCurrentFragment(){
        return fragments.get(fragments.size() - 1);
    }

    public void popBackStack(){
        fragments.remove(fragments.size() - 1);
        showFragment();
    }

    public int getFragmentSize(){
        return fragments.size();
    }
}
