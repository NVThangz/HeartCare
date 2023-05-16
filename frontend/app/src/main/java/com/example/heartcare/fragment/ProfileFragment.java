package com.example.heartcare.fragment;

import static android.app.Activity.RESULT_OK;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.apollographql.apollo3.api.Optional;
import com.example.heartcare.QueryProfileQuery;
import com.example.heartcare.R;
import com.example.heartcare.activity.ChangePassword;
import com.example.heartcare.activity.SignIn;
import com.example.heartcare.backend.Backend;
import com.example.heartcare.utilities.DateFormat;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.theartofdev.edmodo.cropper.CropImage;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import de.hdodenhof.circleimageview.CircleImageView;

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
    private TextInputEditText editTextFullName;
    private TextInputEditText editTextSex;
    private TextInputEditText editTextDateOfBirth;
    private TextInputEditText editTextPhoneNumber;
    private TextInputEditText editTextNationalId;
    private TextInputEditText editTextAddress;
    private LinearLayout btnAbout;
    private LinearLayout btnChangePassword;
    private LinearLayout btnLogOut;
    private TextView btnSaveModified;
    private CircleImageView avatar_profile;

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

    /*2 hàm chuyển đổi ngày tháng*/
    public static String convertISODateToShortDate(String isoDate) {
        SimpleDateFormat isoDateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
        SimpleDateFormat shortDateFormat = new SimpleDateFormat("dd/MM/yyyy");

        try {
            System.out.println("cos chayj ham nay");
            Date date = isoDateFormat.parse(isoDate);
            return shortDateFormat.format(date);
        } catch (ParseException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static String convertShortDateToISODate(String shortDate) {
        SimpleDateFormat shortDateFormat = new SimpleDateFormat("dd/MM/yyyy");
        SimpleDateFormat isoDateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");

        try {
            Date date = shortDateFormat.parse(shortDate);
            return isoDateFormat.format(date);
        } catch (ParseException e) {
            e.printStackTrace();
            return null;
        }
    }
    /**/

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
        avatar_profile = rootView.findViewById(R.id.avatar_profile);
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

//        editTextFullName.setText("Bùi Minh Hoạt");
//        editTextSex.setText("Nam");
//        editTextDateOfBirth.setText("06/09/2003");
//        editTextPhoneNumber.setText("094540xxxx");
//        editTextNationalId.setText("025203xxxxxx");
//        editTextAddress.setText("144 Xuân Thủy, Cầu Giấy, Hà Nội");

        QueryProfileQuery.Data Profile = null;
        try {
            Profile = Backend.queryProfile();
            if (Profile == null) throw new Exception();
            Object obj = Profile.profile.dob;
            if (obj != null) {
                String dob = convertISODateToShortDate(obj.toString());
                editTextDateOfBirth.setText(dob);
            }
            editTextFullName.setText(Profile.profile.name);
            editTextSex.setText(Profile.profile.sex);

            editTextPhoneNumber.setText(Profile.profile.phone);
            editTextNationalId.setText(Profile.profile.nationalID);
            editTextAddress.setText(Profile.profile.address);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

//
//        // cai dob nay dang bi null , kiem tra lai ham convert


        // Lấy chuỗi từ SharedPreferences
        SharedPreferences sharedPreferences = requireContext().getSharedPreferences("HeartCare", Context.MODE_PRIVATE);
        if (sharedPreferences.contains("image_data")) {
            String imageDataString = sharedPreferences.getString("image_data", null);

            // Giải mã chuỗi thành mảng byte
            byte[] imageData = Base64.decode(imageDataString, Base64.DEFAULT);

            // Khôi phục Bitmap từ mảng byte
            Bitmap bitmap = BitmapFactory.decodeByteArray(imageData, 0, imageData.length);

            avatar_profile.setImageBitmap(Bitmap.createScaledBitmap(bitmap, 400, 400, false));
        }

        clickAvatarProfile();
        clickBtnSaveModified();
        clickBtnAbout();
        clickBtnChangePassword();
        setFocusChangeListener();
        clickBtnLogOut();
        return rootView;
    }

    private void setFocusChangeListener() {
        editTextFullName.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    hideKeyboard(v);
                }
            }
        });
        editTextSex.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    hideKeyboard(v);
                }
            }
        });
        editTextDateOfBirth.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    hideKeyboard(v);
                }
            }
        });
        editTextPhoneNumber.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    hideKeyboard(v);
                }
            }
        });
        editTextNationalId.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    hideKeyboard(v);
                }
            }
        });
        editTextAddress.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    hideKeyboard(v);
                }
            }
        });
    }

    public void hideKeyboard(View view) {
        InputMethodManager inputMethodManager = (InputMethodManager) getActivity().getSystemService(Activity.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    private void clickBtnSaveModified() {
        btnSaveModified.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                /*
                    Lấy thông tin người dùng về backend
                    Ghép backend
                 */

                String fullName = String.valueOf(editTextFullName.getText()).trim();
                String sex = String.valueOf(editTextSex.getText()).trim();
                String dateOfBirth = String.valueOf(editTextDateOfBirth.getText()).trim();
                String phoneNumber = String.valueOf(editTextPhoneNumber.getText()).trim();
                String nationalId = String.valueOf(editTextNationalId.getText()).trim();
                String address = String.valueOf(editTextAddress.getText()).trim();
                /*
                Cua Son
                */
                Optional<String> newfullname1,newsex1,newdate_of_birth1,newphone_number1,newaddress1,newnational_id1;
                if (TextUtils.isEmpty(fullName)) {
                    newfullname1 = Optional.absent();
                } else {
                    newfullname1 = Optional.present(fullName);
                }
                if (TextUtils.isEmpty(sex)) {
                    newsex1=Optional.absent();
                } else {
                    newsex1=Optional.present(sex);
                }
                if (TextUtils.isEmpty(dateOfBirth)) {
                    newdate_of_birth1 = Optional.absent();
                } else {
                    newdate_of_birth1 = Optional.present(convertShortDateToISODate(dateOfBirth));
                }
                if (TextUtils.isEmpty(address)) {
                    newaddress1 = Optional.absent();
                } else {
                    newaddress1 = Optional.present(address);
                }
                if (TextUtils.isEmpty(nationalId)) {
                    newnational_id1 = Optional.absent();
                } else {
                    newnational_id1 = Optional.present(nationalId);
                }
                if (TextUtils.isEmpty(phoneNumber)) {
                    newphone_number1 = Optional.absent();
                } else {
                    newphone_number1 = Optional.present(phoneNumber);
                }

                try {
                    Backend.updateProfile(newfullname1,newsex1,newdate_of_birth1,newphone_number1,newaddress1,newnational_id1);
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }



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

    private void clickAvatarProfile() {
        avatar_profile.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onClick(View view) {
                boolean pick = true;
                if (pick) {
                    if(!checkCameraPermission()) {
                        requestCameraPermission();
                    } else {
                        PickImage();
                    }
                } else {
                    if(!checkStoragePermission()) {
                        requestStoragePermission();
                    } else {
                        PickImage();
                    }
                }
            }
        });
    }

    private void PickImage() {
//        CropImage.activity().start(getActivity());
        CropImage.activity().start(getContext(), this);
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    private void requestStoragePermission() {
        requestPermissions(new String[]{android.Manifest.permission.WRITE_EXTERNAL_STORAGE}, 100);
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    private void requestCameraPermission() {
        requestPermissions(new String[]{android.Manifest.permission.CAMERA, android.Manifest.permission.WRITE_EXTERNAL_STORAGE}, 100);
    }

    private boolean checkStoragePermission() {
        boolean res2 = ContextCompat.checkSelfPermission(getActivity(), android.Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED;
        return res2;
    }

    private boolean checkCameraPermission() {
        boolean res1 = ContextCompat.checkSelfPermission(getActivity(), android.Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED;
        boolean res2 = ContextCompat.checkSelfPermission(getActivity(), android.Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED;
        return res1 && res2;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        System.out.println(requestCode);
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK) {
                Uri resultUri = result.getUri();
                try {
                    InputStream inputStream = getActivity().getContentResolver().openInputStream(resultUri);
                    Bitmap photo = BitmapFactory.decodeStream(inputStream);
                    avatar_profile.setImageBitmap(Bitmap.createScaledBitmap(photo, 400, 400, false));

                    ByteArrayOutputStream stream = new ByteArrayOutputStream();
                    photo.compress(Bitmap.CompressFormat.PNG, 100, stream);
                    byte[] byteArray = stream.toByteArray();

                    // Lưu mảng byte vào SharedPreferences
                    SharedPreferences sharedPreferences = requireContext().getSharedPreferences("HeartCare", Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putString("image_data", Base64.encodeToString(byteArray, Base64.DEFAULT));
                    editor.apply();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Exception error = result.getError();
            }
        }
    }
}