package com.zpj.statemanager;

import android.content.Context;
import android.view.View;

public interface IViewHolder {

    int getLayoutId();

    View getView();

    View onCreateView(Context context);

    void onViewCreated(View view);

    void onDestroyView();

}
