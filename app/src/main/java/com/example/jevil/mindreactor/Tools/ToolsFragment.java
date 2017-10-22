package com.example.jevil.mindreactor.Tools;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.example.jevil.mindreactor.R;

public class ToolsFragment extends Fragment {

    EditText etStart, etEnd;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_tools, container, false);

        etStart = (EditText) v.findViewById(R.id.et_tools_start);
        etEnd = (EditText) v.findViewById(R.id.et_tools_end);

        return v;
    }
}
