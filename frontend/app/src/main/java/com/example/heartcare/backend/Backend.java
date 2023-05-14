package com.example.heartcare.backend;

import android.content.Context;
import android.content.SharedPreferences;

import com.apollographql.apollo3.ApolloCall;
import com.apollographql.apollo3.ApolloClient;
import com.apollographql.apollo3.api.ApolloResponse;
import com.apollographql.apollo3.api.Optional;
import com.apollographql.apollo3.rx3.Rx3Apollo;
import com.example.heartcare.LoginMutation;
import com.example.heartcare.LogoutMutation;
import com.example.heartcare.RefreshMutation;
import com.example.heartcare.SignupMutation;
import com.example.heartcare.type.AuthInput;
import com.example.heartcare.type.UpdateProfileInput;

import java.util.concurrent.atomic.AtomicReference;

import io.reactivex.rxjava3.core.Single;
import okhttp3.Call;
import okhttp3.OkHttpClient;
import okhttp3.Request;

public class Backend {
    private static ApolloClient apolloClient = new ApolloClient.Builder()
            .serverUrl("http://192.168.1.17:3000/graphql")
            .build();

    private static String email = "";

    public static void login(String username, String password, SharedPreferences sharedPreferences) throws Exception {
        AuthInput auth = new AuthInput(username,password) ;
        Optional<AuthInput> NewAuth = Optional.present(auth);
        ApolloCall<LoginMutation.Data> queryCall = apolloClient.mutation(new LoginMutation(NewAuth));
        Single<ApolloResponse<LoginMutation.Data>> queryResponse = Rx3Apollo.single(queryCall);
        ApolloResponse<LoginMutation.Data> response = queryResponse.blockingGet();
        if (response.hasErrors()) {
            throw new Exception(response.errors.get(0).getMessage());
        } else {
//            System.out.println(response.data);
            email = response.data.login.user.email;
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putString("refresh_token", response.data.login.refresh_token);
            editor.apply();
        }
    }

    public static void signup(String username, String password,SharedPreferences sharedPreferences) throws Exception {
        AuthInput auth = new AuthInput(username,password) ;
        Optional<AuthInput> NewAuth = Optional.present(auth);
        UpdateProfileInput updateProfileInput = new UpdateProfileInput(username,Optional.present(username),Optional.absent(),Optional.absent(),Optional.absent(),Optional.absent(),Optional.absent());

        ApolloCall<SignupMutation.Data> queryCall = apolloClient.mutation(new SignupMutation( NewAuth,updateProfileInput));
        Single<ApolloResponse<SignupMutation.Data>> queryResponse = Rx3Apollo.single(queryCall);
        ApolloResponse<SignupMutation.Data> response = queryResponse.blockingGet();
        if (response.hasErrors()) {
            throw new Exception(response.errors.get(0).getMessage());
        } else {
            email = response.data.signup.user.email;
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putString("refresh_token", response.data.signup.refresh_token);
            editor.apply();
        }
    }

    public static void refresh(String refreshToken, SharedPreferences sharedPreferences) throws Exception {
        ApolloCall<RefreshMutation.Data> queryCall = apolloClient.mutation(new RefreshMutation())
                                    .addHttpHeader("Authorization", "Bearer " + refreshToken);
        Single<ApolloResponse<RefreshMutation.Data>> queryResponse = Rx3Apollo.single(queryCall);
        ApolloResponse<RefreshMutation.Data> response = queryResponse.blockingGet();
        if (response.hasErrors()) {
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putString("refresh_token", null);
            editor.apply();
            throw new Exception(response.errors.get(0).getMessage());
        } else {
            System.out.println(response.data);
            email = response.data.refresh.user.email;
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putString("refresh_token", response.data.refresh.refresh_token);
            editor.apply();
        }
    }

    public static void logout(String refreshToken, SharedPreferences sharedPreferences) throws Exception {
        ApolloCall<LogoutMutation.Data> queryCall = apolloClient.mutation(new LogoutMutation())
                .addHttpHeader("Authorization", "Bearer " + refreshToken);
        Single<ApolloResponse<LogoutMutation.Data>> queryResponse = Rx3Apollo.single(queryCall);
        ApolloResponse<LogoutMutation.Data> response = queryResponse.blockingGet();
        if (response.hasErrors()) {
            throw new Exception(response.errors.get(0).getMessage());
        } else {
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putString("refresh_token", null);
            editor.apply();
        }
    }

}
