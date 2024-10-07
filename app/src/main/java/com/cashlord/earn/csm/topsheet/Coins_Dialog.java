package com.cashlord.earn.csm.topsheet;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.fragment.app.DialogFragment;

import com.cashlord.earn.AdsManager;
import com.cashlord.earn.InterstitialAds;
import com.cashlord.earn.OnScratchComplete;
import com.cashlord.earn.R;

public class Coins_Dialog extends DialogFragment {
    View root_view;
    LinearLayout ok;
    TextView txt_2, coinss;
    OnScratchComplete onScratchComplete;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        root_view = inflater.inflate(R.layout.coins__dialog, container, false);
        ok = root_view.findViewById(R.id.ok);
        txt_2 = root_view.findViewById(R.id.txt_2);
        coinss = root_view.findViewById(R.id.coins);
        String from = getArguments().getString("from");
        String coins = getArguments().getString("coins");
        String show_ad = getArguments().getString("show_ad");
        txt_2.setText("You got " + coins + " coins ");
        coinss.setText(coins);


        ok.setOnClickListener(view -> {
            performAction(from);
            if (show_ad.equalsIgnoreCase("true")) {
                InterstitialAds interstitialAds = InterstitialAds.getInstance(getActivity());
                if (interstitialAds.isAdReady()) {
                    if (getActivity() != null) {
                        interstitialAds.showAd();
                    }
                }
            }
        });
        return root_view;
    }

    public void setOnScratchCompleteListener(OnScratchComplete onScratchComplete) {
        this.onScratchComplete = onScratchComplete;
    }

    public void performAction(String from) {
        if (from.equals("GameZone")) {
            getActivity().finish();
            dismiss();
        } else {
            dismiss();
        }
        if (onScratchComplete!=null) {
            onScratchComplete.onComplete();
        }
    }
}