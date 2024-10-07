package com.cashlord.earn;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.cashlord.earn.csm.fragment.ForgotFragment;
import com.cashlord.earn.csm.fragment.FragmentWebview;
import com.cashlord.earn.csm.fragment.ScratchFragment;
import com.cashlord.earn.csm.fragment.SignupFragment;
import com.cashlord.earn.helper.Helper;

public class FragmentLoadingActivity extends AppCompatActivity {

    private Fragment fm = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fragment_loading);

        String fragmentType = getIntent().getStringExtra(Helper.FRAGMENT_TYPE);
        if (fragmentType != null) {
            switch (fragmentType) {
                case Helper.FRAGMENT_SIGNUP:
                    fm = new SignupFragment();
                    break;
                case Helper.FRAGMENT_FORGOT_PASSWORD:
                    fm = new ForgotFragment();
                    break;
                case Helper.FRAGMENT_CHANGE_PASSWORD:
                    fm = new ForgotFragment();
                    break;
                case Helper.FRAGMENT_LOAD_WEB_VIEW:
                    String url = getIntent().getStringExtra("url");
                    fm = new FragmentWebview(url != null ? url : "");
                    break;
                case Helper.FRAGMENT_SCRATCH:
                    fm = new ScratchFragment();
                    break;
            }
        }

        if (fm != null) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.container, fm)
                    .commit();
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}
