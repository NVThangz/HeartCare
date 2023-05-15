package com.example.heartcare.fragment;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.heartcare.R;
import com.example.heartcare.activity.ChangePassword;
import com.example.heartcare.activity.SignIn;
import com.example.heartcare.backend.Backend;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ProfileFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ProfileFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private View rootView;
    private EditText editTextFullName;
    private EditText editTextSex;
    private EditText editTextDateOfBirth;
    private EditText editTextPhoneNumber;
    private EditText editTextNationalId;
    private EditText editTextAddress;
    private LinearLayout btnAbout;
    private LinearLayout btnChangePassword;
    private LinearLayout btnLogOut;
    private TextView btnSaveModified;

    public ProfileFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ProfileFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ProfileFragment newInstance(String param1, String param2) {
        ProfileFragment fragment = new ProfileFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    private void map() {
        editTextFullName = rootView.findViewById(R.id.full_name_profile);
        editTextSex = rootView.findViewById(R.id.sex_profile);
        editTextDateOfBirth = rootView.findViewById(R.id.date_of_birth_profile);
        editTextPhoneNumber = rootView.findViewById(R.id.phone_number_profile);
        editTextNationalId = rootView.findViewById(R.id.national_id_profile);
        editTextAddress = rootView.findViewById(R.id.address_profile);
        btnAbout = rootView.findViewById(R.id.btn_about);
        btnChangePassword = rootView.findViewById(R.id.btn_change_password);
        btnLogOut = rootView.findViewById(R.id.btn_log_out);
        btnSaveModified = rootView.findViewById(R.id.btn_save_modified);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        rootView = inflater.inflate(R.layout.fragment_profile, container, false);
        map();

        /*
            Ghép thông tin người dùng backend
         */
        editTextFullName.setText("Bùi Minh Hoạt");
        editTextSex.setText("Nữ :v");
        editTextDateOfBirth.setText("0/0/0");
        editTextPhoneNumber.setText("09xxx");
        editTextNationalId.setText("123");
        editTextAddress.setText("home");


        clickBtnSaveModified();
        clickBtnAbout();
        clickBtnChangePassword();
        clickBtnLogOut();
        return rootView;
    }

    private void clickBtnSaveModified() {
        btnSaveModified.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                /*
                    Lấy thông tin người dùng về backend
                 */
                String fullName = String.valueOf(editTextFullName.getText());
                String sex = String.valueOf(editTextSex.getText());
                String dateOfBirth = String.valueOf(editTextDateOfBirth.getText());
                String phoneNumber = String.valueOf(editTextPhoneNumber.getText());
                String nationalId = String.valueOf(editTextNationalId.getText());
                String address = String.valueOf(editTextAddress.getText());


                Toast.makeText(getActivity().getApplicationContext(), getResources().getString(R.string.save_modified_successfully), Toast.LENGTH_SHORT).show();

            }
        });
    }

    private void clickBtnAbout() {
        btnAbout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            }
        });
    }

    private void clickBtnChangePassword() {
        btnChangePassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), ChangePassword.class);
                startActivity(intent);
            }
        });

    }

    private void clickBtnLogOut() {

        btnLogOut.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                /*
                 * Cài đặt phần đăng xuất ở đây
                 * */
                SharedPreferences sharedPreferences = getContext().getSharedPreferences("HeartCare", Context.MODE_PRIVATE);
                Backend.logout(sharedPreferences);
                Intent intent = new Intent(getActivity(), SignIn.class);
                startActivity(intent);
                getActivity().finish();
            }
        });
    }
}