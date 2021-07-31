package com.rifqiandra.dailydzikrorganizer.page.dzikr_page;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;

import com.rifqiandra.dailydzikrorganizer.R;
import com.rifqiandra.dailydzikrorganizer.page.dzikr_page.module.custom.AddDzikrActivity;
import com.rifqiandra.dailydzikrorganizer.page.dzikr_page.module.dzikr_default.DzikrDefaultActivity;


public class DzikrFragment extends Fragment {

    View root;
    LinearLayout btn_new_dzikr, btn_list_dzikr;


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        root = inflater.inflate(R.layout.fragment_dzikr, container, false);

        btn_new_dzikr = (LinearLayout) root.findViewById(R.id.btn_new_dzikr);
        btn_list_dzikr = (LinearLayout) root.findViewById(R.id.btn_list_dzikr);
        btn_new_dzikr.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), AddDzikrActivity.class);
                startActivity(intent);
            }
        });
        btn_list_dzikr.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), DzikrDefaultActivity.class);
                startActivity(intent);
            }
        });
        return root;
    }
}