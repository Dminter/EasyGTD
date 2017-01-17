package com.zncm.mxgtd.ft;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.zncm.mxgtd.R;
import com.zncm.mxgtd.data.Constant;
import com.zncm.mxgtd.data.TaskData;
import com.zncm.mxgtd.utils.MySp;
import com.zncm.mxgtd.utils.XUtil;
import com.zncm.mxgtd.view.PinchZoomTextView;

import java.io.Serializable;

/**
 * Created by dminter on 2016/11/7.
 */

public class TextFt extends Fragment {

    PinchZoomTextView textView;
    View view;
    String text;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_text, null);
        textView = (PinchZoomTextView) view.findViewById(R.id.textView);
        textView.setTextSize(MySp.getFontSize());
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


        if (getArguments() != null) {
            Bundle bundle = getArguments();
            text = bundle.getString("text");
            if (XUtil.notEmptyOrNull(text)) {
                textView.setText(text);
            }
        }


    }
}
