package com.echoeyecodes.sinnerman;

public abstract class RootBottomFragment extends CustomFragment {
    public String TAG="";
    @Override
    public void onResume() {
        super.onResume();
    }

    public String getTAG() {
        return TAG;
    }
}
