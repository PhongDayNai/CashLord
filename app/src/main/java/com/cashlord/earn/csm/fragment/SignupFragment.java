package com.cashlord.earn.csm.fragment;

import static com.cashlord.earn.Activity_otp.randomAlphaNumeric;
import static com.cashlord.earn.helper.PrefManager.activity;

import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.fragment.app.Fragment;

import com.android.volley.Request;
import com.cashlord.earn.Activity_otp;
import com.cashlord.earn.R;
import com.cashlord.earn.helper.AppController;
import com.cashlord.earn.helper.Constatnt;
import com.cashlord.earn.helper.Helper;
import com.cashlord.earn.helper.JsonRequest;
import com.google.android.material.textfield.TextInputEditText;

import org.json.JSONException;

import java.util.HashMap;
import java.util.Map;

/**
 * A simple [Fragment] subclass.
 * Use the [SingupFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
public class SignupFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_singup, container, false);
        AppController.initpDialog((AppCompatActivity) requireActivity());
        AppCompatButton signupBtn = view.findViewById(R.id.signupBtn);
        TextInputEditText passwordEditText = view.findViewById(R.id.password_edit_text);
        TextInputEditText confirmPasswordEditText = view.findViewById(R.id.confirm_password_edit_text);
        TextInputEditText emailEditText = view.findViewById(R.id.email_edit_text);
        TextInputEditText nameEditText = view.findViewById(R.id.name_edit_text);
        TextView loginTextTextView = view.findViewById(R.id.login_text);

        loginTextTextView.setOnClickListener(v -> activity.onBackPressed());

        signupBtn.setOnClickListener(v -> {
            String email = Helper.getTextFromTextView(emailEditText);
            String password = Helper.getTextFromTextView(passwordEditText);
            String confirmPassword = Helper.getTextFromTextView(confirmPasswordEditText);
            String name = Helper.getTextFromTextView(nameEditText);

            String emailErrorMessage = Helper.isValidEmail(email);
            String passwordErrorMessage = Helper.isValidPassword(password);
            String confirmPasswordErrorMessage = Helper.isValidPassword(confirmPassword);
            String nameErrorMessage = Helper.isValidName(name);

            if (!nameErrorMessage.equals("ok")) {
                nameEditText.setError(nameErrorMessage);
                nameEditText.requestFocus();
            } else if (!emailErrorMessage.equals("ok")) {
                emailEditText.setError(emailErrorMessage);
                emailEditText.requestFocus();
            } else if (!passwordErrorMessage.equals("ok")) {
                passwordEditText.setError(passwordErrorMessage);
                passwordEditText.requestFocus();
            } else if (!confirmPasswordErrorMessage.equals("ok")) {
                confirmPasswordEditText.setError(confirmPasswordErrorMessage);
                confirmPasswordEditText.requestFocus();
            } else if (!password.equals(confirmPassword)) {
                confirmPasswordEditText.setError("password not matching");
                confirmPasswordEditText.requestFocus();
            } else {
                checkUser(email, password, name);
            }
        });

        return view;
    }

    private void checkUser(String e, String password, String name) {
        AppController.showpDialog();
        if (AppController.isConnected((AppCompatActivity) requireActivity())) {
            JsonRequest jsonReq = new JsonRequest(Request.Method.POST, Constatnt.Base_Url, null,
                    response -> {
                        try {
                            if (response.getString("error").equalsIgnoreCase("false")) {
                                AppController.hidepDialog();
                                Toast.makeText(requireActivity(), "This email is already registered", Toast.LENGTH_LONG).show();
                            } else if (response.getString("error").equalsIgnoreCase("true")) {
                                register("0000000000", e.replace("@gmail.com", ""), name, e, Uri.EMPTY, password);
                            }
                        } catch (JSONException ex) {
                            ex.printStackTrace();
                            Toast.makeText(requireActivity(), "Check User " + ex, Toast.LENGTH_LONG).show();
                            AppController.hidepDialog();
                        }
                    },
                    error -> {
                        Toast.makeText(requireActivity(), "Error in Check User " + error, Toast.LENGTH_LONG).show();
                        AppController.hidepDialog();
                    }) {
                @Override
                protected Map<String, String> getParams() {
                    Map<String, String> params = new HashMap<>();
                    params.put(Constatnt.ACCESS_KEY, Constatnt.ACCESS_Value);
                    params.put("user_check", Constatnt.API);
                    params.put("phone", e);
                    return params;
                }
            };
            AppController.getInstance().addToRequestQueue(jsonReq);
        }
    }

    public void register(String enter_phone, String username, String name, String email, Uri pro, String password) {
        String tempAndroidId;
        tempAndroidId = Settings.Secure.getString(requireActivity().getContentResolver(), Settings.Secure.ANDROID_ID);
        /*if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            tempAndroidId = Settings.Secure.getString(requireActivity().getContentResolver(), Settings.Secure.ANDROID_ID);
            if (tempAndroidId == null) {
                tempAndroidId = " ";
            }
        } else {
            TelephonyManager mTelephony = (TelephonyManager) requireActivity().getSystemService(Context.TELEPHONY_SERVICE);
            tempAndroidId = (mTelephony.getDeviceId() != null) ? mTelephony.getDeviceId() : Settings.Secure.getString(requireActivity().getContentResolver(), Settings.Secure.ANDROID_ID);*/
            if (tempAndroidId == null) {
                tempAndroidId = " ";
            }
        //}

        final String androidId = tempAndroidId;

        if (AppController.isConnected((AppCompatActivity) requireActivity())) {
            AppController.showpDialog();
            JsonRequest jsonReq = new JsonRequest(Request.Method.POST, Constatnt.Base_Url, null,
                    response -> {
                        try {
                            if (response.getString("error").equalsIgnoreCase("true")) {
                                Toast.makeText(requireActivity(), response.getString("message"), Toast.LENGTH_LONG).show();
                            } else if (response.getString("error").equalsIgnoreCase("false")) {
                                signin(email, password);
                            }
                        } catch (JSONException ex) {
                            ex.printStackTrace();
                        }
                        AppController.hidepDialog();
                    },
                    error -> {
                        Toast.makeText(requireActivity(), error.toString(), Toast.LENGTH_LONG).show();
                        AppController.hidepDialog();
                    }) {
                @Override
                protected Map<String, String> getParams() {
                    Map<String, String> params = new HashMap<>();
                    params.put(Constatnt.ACCESS_KEY, Constatnt.ACCESS_Value);
                    params.put("user_signup", Constatnt.API);
                    params.put("phone", enter_phone);
                    params.put("password", password);
                    params.put("username", username);
                    params.put("name", name);
                    params.put("email", email);
                    params.put("refer", randomAlphaNumeric(8));
                    params.put("device", androidId);
                    params.put("image", pro.toString());
                    return params;
                }
            };
            AppController.getInstance().addToRequestQueue(jsonReq);
        }
    }

    public void signin(String enter_phone, String password) {
        if (AppController.isConnected((AppCompatActivity) requireActivity())) {
            AppController.showpDialog();
            JsonRequest jsonReq = new JsonRequest(Request.Method.POST, Constatnt.Base_Url, null,
                    response -> {
                        Log.e("RESPONSE", "signin: " + response.toString());
                        if (AppController.getInstance().authorize(response)) {
                            if (AppController.getInstance().getState().equals(Constatnt.ACCOUNT_STATE_ENABLED)) {
                                try {
                                    if (response.getString("error").equalsIgnoreCase("true")) {
                                        Toast.makeText(requireActivity(), response.getString("message"), Toast.LENGTH_SHORT).show();
                                    } else if (response.getString("error").equalsIgnoreCase("false")) {
                                        Activity_otp.chkref((AppCompatActivity) requireActivity());
                                    }
                                } catch (JSONException ex) {
                                    ex.printStackTrace();
                                }
                            } else {
                                AppController.getInstance().logout((AppCompatActivity) requireActivity());
                                Toast.makeText(requireActivity(), getString(R.string.msg_account_blocked), Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            Toast.makeText(requireActivity(), "Something Gone Wrong", Toast.LENGTH_SHORT).show();
                        }
                        AppController.hidepDialog();
                    },
                    error -> {
                        Toast.makeText(requireActivity(), getString(R.string.error_data_loading), Toast.LENGTH_LONG).show();
                        AppController.hidepDialog();
                    }) {
                @Override
                protected Map<String, String> getParams() {
                    Map<String, String> params = new HashMap<>();
                    params.put(Constatnt.ACCESS_KEY, Constatnt.ACCESS_Value);
                    params.put(Constatnt.USER_LOGIN, Constatnt.API);
                    params.put("phone", enter_phone);
                    params.put("password", password);
                    AppController.getInstance().password = password;
                    return params;
                }
            };
            AppController.getInstance().getRequestQueue().getCache().clear();
            AppController.getInstance().addToRequestQueue(jsonReq);
        }
    }
}
