package com.example.heartcare.activity;

import static android.content.ContentValues.TAG;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.example.heartcare.R;
import com.example.heartcare.backend.Backend;
import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Arrays;

public class SignIn extends AppCompatActivity {
    private static final int RC_SIGN_IN = 200;
    private EditText txt_email = null;
    private EditText txt_password = null;
    private ImageButton btn_login_facebook;

    private ImageButton btn_login_google;
    private CallbackManager callbackManager;
    private TextView tv_createNewOne;
    private ImageView btn_back;
    private TextView tv_forgot_password;
    private ConstraintLayout btn_sign_in;
    private SharedPreferences mPref;
    private SharedPreferences.Editor editor;
    private GoogleSignInClient mGoogleSignInClient;

    private void map() {
        tv_forgot_password = findViewById(R.id.tv_forgot_password);
        txt_email = findViewById(R.id.txt_email);
        txt_password = findViewById(R.id.txt_password);
        btn_sign_in = findViewById(R.id.btn_sign_in);
        tv_createNewOne = findViewById(R.id.tv_createNewOne);
        btn_login_google = findViewById(R.id.btn_login_google);
        btn_login_facebook = findViewById(R.id.btn_login_facebook);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);
        map();

        initializationLoginWithSocial();

        setListenerTxtPassword();
        setFocusChangeListener();
        clickBtnLoginFacebook();
        clickBtnLoginGoogle();
        clickTvCreateNewOne();
        clickTvForgotPassword();
        clickBtnSignIn();

        SharedPreferences sharedPreferences = getSharedPreferences("HeartCare", Context.MODE_PRIVATE);
        String refreshToken = sharedPreferences.getString("refresh_token", null);
        if(refreshToken != null) {
            try {
                Backend.refresh(refreshToken, sharedPreferences);
                Toast.makeText(getApplicationContext(), getResources().getString(R.string.logged_in_successfully), Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(SignIn.this, MainActivity.class);
                startActivity(intent);
                finish();
            } catch (Exception e) {
                Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void setListenerTxtPassword() {
        txt_password.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE || actionId == EditorInfo.IME_NULL) {
                    try {
                        boolean successLogin = loginAccount();
                        if (successLogin) {
                            Toast.makeText(getApplicationContext(), "Logged in successfully", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(SignIn.this, MainActivity.class);
                            startActivity(intent);
                            finish();
                        }
                        else {
                            Toast.makeText(getApplicationContext(), "Login failed", Toast.LENGTH_SHORT).show();
                        }
                    } catch (Exception e) {
                        Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                    return true;
                }
                return false;
            }
        });
    }

    public void hideKeyboard(View view) {
        InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    private void setFocusChangeListener() {
        txt_email.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    hideKeyboard(v);
                }
            }
        });

        txt_password.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    hideKeyboard(v);
                }
            }
        });
    }

    void initializationLoginWithSocial() {
        callbackManager = CallbackManager.Factory.create();
        LoginManager.getInstance().registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                System.out.println("onSuccess");
                AccessToken accessToken = loginResult.getAccessToken();
                getUserProfileFacebook(accessToken);
            }

            @Override
            public void onCancel() {
                System.out.println("onCancel");
            }

            @Override
            public void onError(@NonNull FacebookException e) {
                System.out.println("onCancel");
            }
        });

        // Configure sign-in to request the user's ID, email address, and basic
        // profile. ID and basic profile are included in DEFAULT_SIGN_IN.
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();
        // Build a GoogleSignInClient with the options specified by gso.
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);
    }

    private void clickTvForgotPassword() {
        tv_forgot_password.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SignIn.this, ForgotPassword.class);
                startActivity(intent);
            }
        });
    }

    private void clickBtnLoginFacebook() {
        btn_login_facebook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                LoginManager.getInstance().logInWithReadPermissions(SignIn.this, Arrays.asList("public_profile", "email"));
            }
        });
    }

    private void clickBtnLoginGoogle() {
        btn_login_google.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent signInIntent = mGoogleSignInClient.getSignInIntent();
                startActivityForResult(signInIntent, RC_SIGN_IN);
            }
        });
    }

    private void clickTvCreateNewOne() {
        tv_createNewOne.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SignIn.this, SignUp.class);
                startActivity(intent);
                finish();
            }
        });
    }

    private void clickBtnSignIn() {
        btn_sign_in.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    boolean successLogin = loginAccount();
                    if (successLogin) {
                        Toast.makeText(getApplicationContext(), "Logged in successfully", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(SignIn.this, MainActivity.class);
                        startActivity(intent);
                        finish();
                    }
                    else {
                        Toast.makeText(getApplicationContext(), "Login failed", Toast.LENGTH_SHORT).show();
                    }
                } catch (Exception e) {
                    Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void registerAccountWithSocial(String email, String name) throws Exception {
        // Tạo tài khoản trên server của mình từ thông tin email, name
        /*
            Ghép backend
         */
        SharedPreferences sharedPreferences = getSharedPreferences("HeartCare", Context.MODE_PRIVATE);
        Backend.RegisterWithSocial(email, name, sharedPreferences);
    }

    private boolean loginAccountWithSocial(String email, String name) throws Exception {
        /*
            Kiểm tra là đã có tài khoản hay chưa?
         */
        registerAccountWithSocial(email, name);
        return true;
    }

    private boolean loginAccount() throws Exception {
        String username = txt_email.getText().toString().trim();
        String password = txt_password.getText().toString().trim();
        SharedPreferences sharedPreferences = getSharedPreferences("HeartCare", Context.MODE_PRIVATE);

        if (TextUtils.isEmpty(username)) {
            throw new Exception("Enter email address!");
        }

        if (TextUtils.isEmpty(password)) {
            throw new Exception("Enter password!");
        }

        if (password.length() < 6) {
            throw new Exception("Password too short, enter minimum 6 characters!");
        }


//        AuthInput auth = new AuthInput(username,password) ;
//        Optional<AuthInput> NewAuth = Optional.present(auth);
//
//        ApolloClient apolloClient = new ApolloClient.Builder()
//                .serverUrl("http://192.168.1.67:3000/graphql")
//                .build();
//
//        ApolloCall<LoginMutation.Data> queryCall = apolloClient.mutation(new LoginMutation( NewAuth));
//        Single<ApolloResponse<LoginMutation.Data>> queryResponse = Rx3Apollo.single(queryCall);
//        queryResponse.subscribe(response ->
//                login(response),
//                Throwable::printStackTrace
//        );


        // Kiểm tra đăng nhập, ghép nốt phần backendSignIn
        Backend.login(username,password, sharedPreferences);

        return true;
    }

//    private void login(ApolloResponse<LoginMutation.Data> response) {
//        System.out.println(response.data);
//        editor.putString(Constant.refresh,response.data.login.refresh_token);
//        editor.putString(Constant.Email,response.data.login.access_token);
//        editor.commit();
//        Toast.makeText(SignIn.this,"helo world",Toast.LENGTH_SHORT).show();
//        System.out.println("Co chay den day");
//    }

    private void getUserProfileFacebook(AccessToken accessToken) {
        /**
         Creating the GraphRequest to fetch user details
         1st Param - AccessToken
         2nd Param - Callback (which will be invoked once the request is successful)
         **/
        GraphRequest request = GraphRequest.newMeRequest(accessToken, new GraphRequest.GraphJSONObjectCallback() {
            //OnCompleted is invoked once the GraphRequest is successful
            @Override
            public void onCompleted(JSONObject object, GraphResponse response) {
                try {
                    String id = object.getString("id");
                    String name = object.getString("name");
                    String email = object.getString("email");
                    String image = object.getJSONObject("picture").getJSONObject("data").getString("url");
                    try {
                        boolean successLogin = loginAccountWithSocial(email, name);
                        if (successLogin) {
                            Toast.makeText(getApplicationContext(), "Logged in successfully", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(SignIn.this, MainActivity.class);
                            startActivity(intent);
                            finish();
                        }
                        else {
                            Toast.makeText(getApplicationContext(), "Login failed", Toast.LENGTH_SHORT).show();
                        }
                    } catch (Exception e) {
                        Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
        // We set parameters to the GraphRequest using a Bundle.
        Bundle parameters = new Bundle();
        parameters.putString("fields", "id,name,email,picture.width(200)");
        request.setParameters(parameters);
        // Initiate the GraphRequest
        request.executeAsync();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        callbackManager.onActivityResult(requestCode, resultCode, data);
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInClient.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            // The Task returned from this call is always completed, no need to attach
            // a listener.
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            handleSignInResult(task);
        }
    }

    private void handleSignInResult(Task<GoogleSignInAccount> completedTask) {
        try {
            GoogleSignInAccount account = completedTask.getResult(ApiException.class);
            String email = account.getEmail();
            String name = account.getDisplayName();
            Uri picture = account.getPhotoUrl();
            // Signed in successfully, show authenticated UI.
            try {
                boolean successLogin = loginAccountWithSocial(email, name);
                if (successLogin) {
                    Toast.makeText(getApplicationContext(), "Logged in successfully", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(SignIn.this, MainActivity.class);
                    startActivity(intent);
                    finish();
                }
                else {
                    Toast.makeText(getApplicationContext(), "Login failed", Toast.LENGTH_SHORT).show();
                }
            } catch (Exception e) {
                Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
            }
            System.out.println("Success");
        } catch (ApiException e) {
            // The ApiException status code indicates the detailed failure reason.
            // Please refer to the GoogleSignInStatusCodes class reference for more information.
            Log.w(TAG, "signInResult:failed code=" + e.getStatusCode());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(SignIn.this, MainActivity.class);
        startActivity(intent);
        finish();
    }
}