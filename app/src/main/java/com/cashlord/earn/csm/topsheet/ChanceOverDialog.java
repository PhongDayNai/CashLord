package com.cashlord.earn.csm.topsheet;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.fragment.app.DialogFragment;
import com.cashlord.earn.R;

public class ChanceOverDialog extends DialogFragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.coins__dialog, container, false);

        LinearLayout coiLayout = rootView.findViewById(R.id.coi);
        coiLayout.setVisibility(View.INVISIBLE);

        TextView txt1 = rootView.findViewById(R.id.txt_1);
        txt1.setText("Congratulation !");

        TextView txt2 = rootView.findViewById(R.id.txt_2);
        txt2.setText("You got a extra spin for watching video");

        LinearLayout okLayout = rootView.findViewById(R.id.ok);
        okLayout.setOnClickListener(v -> dismiss());

        return rootView;
    }
}