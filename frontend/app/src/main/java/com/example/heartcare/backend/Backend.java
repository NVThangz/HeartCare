package com.example.heartcare.backend;

import android.content.SharedPreferences;

import com.apollographql.apollo3.ApolloCall;
import com.apollographql.apollo3.ApolloClient;
import com.apollographql.apollo3.api.ApolloResponse;
import com.apollographql.apollo3.api.Optional;
import com.apollographql.apollo3.rx3.Rx3Apollo;
import com.example.heartcare.LoginMutation;
import com.example.heartcare.SignupMutation;
import com.example.heartcare.type.AuthInput;
import com.example.heartcare.type.UpdateProfileInput;

import java.util.concurrent.atomic.AtomicReference;

import io.reactivex.rxjava3.core.Single;

public class Backend {
    private static ApolloClient apolloClient = new ApolloClient.Builder()
            .serverUrl("http://192.168.1.17:3000/graphql")
            .build();
    private static SharedPreferences.Editor editor;


    public static void login(String username, String password) throws Exception {
        AuthInput auth = new AuthInput(username,password) ;
        Optional<AuthInput> NewAuth = Optional.present(auth);
        ApolloCall<LoginMutation.Data> queryCall = apolloClient.mutation(new LoginMutation(NewAuth));
        Single<ApolloResponse<LoginMutation.Data>> queryResponse = Rx3Apollo.single(queryCall);
        ApolloResponse<LoginMutation.Data> response = queryResponse.blockingGet();
        if (response.hasErrors()) {
            throw new Exception(response.errors.get(0).getMessage());
        } else {
            System.out.println(response.data);
            editor.putString(Constant.refresh,response.data.login.refresh_token);
            editor.putString(Constant.Email,response.data.login.access_token);
            editor.commit();
        }
    }

    public static void signup(String username, String password) throws Exception {
        AtomicReference<String> error = null;
        AuthInput auth = new AuthInput(username,password) ;
        Optional<AuthInput> NewAuth = Optional.present(auth);
        UpdateProfileInput updateProfileInput = new UpdateProfileInput(username,Optional.present(username),Optional.absent(),Optional.absent(),Optional.absent(),Optional.absent(),Optional.absent());

        ApolloCall<SignupMutation.Data> queryCall = apolloClient.mutation(new SignupMutation( NewAuth,updateProfileInput));
        Single<ApolloResponse<SignupMutation.Data>> queryResponse = Rx3Apollo.single(queryCall);
        ApolloResponse<SignupMutation.Data> response = queryResponse.blockingGet();
        if (response.hasErrors()) {
            throw new Exception(response.errors.get(0).getMessage());
        } else {
            System.out.println(response.data);
        }
    }
}
