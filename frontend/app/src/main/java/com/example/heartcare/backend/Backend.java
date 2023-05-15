package com.example.heartcare.backend;

import android.content.Context;
import android.content.SharedPreferences;

import com.apollographql.apollo3.ApolloCall;
import com.apollographql.apollo3.ApolloClient;
import com.apollographql.apollo3.api.ApolloResponse;
import com.apollographql.apollo3.api.Optional;
import com.apollographql.apollo3.rx3.Rx3Apollo;
import com.example.heartcare.ChangePasswordMutation;
import com.example.heartcare.ConfirmForgotPasswordMutation;
import com.example.heartcare.CreateHistoryMutation;
import com.example.heartcare.CreateNoteMutation;
import com.example.heartcare.DeleteNoteMutation;
import com.example.heartcare.FindNotesQuery;
import com.example.heartcare.FindNotificationsWithEmailQuery;
import com.example.heartcare.ForgotPasswordMutation;
import com.example.heartcare.GetAdvisoryFirstMutation;
import com.example.heartcare.GetAdvisoryMutation;
import com.example.heartcare.LoginMutation;
import com.example.heartcare.LogoutMutation;
import com.example.heartcare.RefreshMutation;
import com.example.heartcare.RegisterWithSocialMutation;
import com.example.heartcare.ResetPasswordConfirmedMutation;
import com.example.heartcare.SignupMutation;
import com.example.heartcare.TodayHistoryStatisticsQuery;
import com.example.heartcare.UpdateNoteMutation;
import com.example.heartcare.WeekHistoryStatisticsQuery;
import com.example.heartcare.type.AuthInput;
import com.example.heartcare.type.NoteInput;
import com.example.heartcare.type.NoteUpdateInput;
import com.example.heartcare.type.UpdateProfileInput;
import com.example.heartcare.utilities.DateFormat;

import java.util.Date;
import java.util.concurrent.atomic.AtomicReference;

import io.reactivex.rxjava3.core.Single;
import okhttp3.Call;
import okhttp3.OkHttpClient;
import okhttp3.Request;

public class Backend {
    private static ApolloClient apolloClient = new ApolloClient.Builder()
            .serverUrl("http://192.168.1.17:3000/graphql")
            .build();

    public static String email = "";

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
            editor.putString("access_token", response.data.login.access_token);
            editor.putString("refresh_token", response.data.login.refresh_token);
            editor.apply();
        }
    }

    public static void signup(String username, String password,SharedPreferences sharedPreferences) throws Exception {
        AuthInput auth = new AuthInput(username,password) ;
        Optional<AuthInput> NewAuth = Optional.present(auth);
        UpdateProfileInput updateProfileInput = new UpdateProfileInput(username,Optional.present(username),Optional.absent(),Optional.absent(),Optional.absent(),Optional.absent(),Optional.absent(),Optional.absent());

        ApolloCall<SignupMutation.Data> queryCall = apolloClient.mutation(new SignupMutation( NewAuth,updateProfileInput));
        Single<ApolloResponse<SignupMutation.Data>> queryResponse = Rx3Apollo.single(queryCall);
        ApolloResponse<SignupMutation.Data> response = queryResponse.blockingGet();
        if (response.hasErrors()) {
            throw new Exception(response.errors.get(0).getMessage());
        } else {
            email = response.data.signup.user.email;
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putString("access_token", response.data.signup.access_token);
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
            editor.putString("access_token", response.data.refresh.access_token);
            editor.putString("refresh_token", response.data.refresh.refresh_token);
            editor.apply();
        }
    }

    public static void logout(SharedPreferences sharedPreferences) {
        ApolloCall<LogoutMutation.Data> queryCall = apolloClient.mutation(new LogoutMutation(email));
        Single<ApolloResponse<LogoutMutation.Data>> queryResponse = Rx3Apollo.single(queryCall);
        ApolloResponse<LogoutMutation.Data> response = queryResponse.blockingGet();
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("refresh_token", null);
        editor.putString("access_token", null);
        editor.apply();
    }

    public static void forgotPassword(String email) throws Exception {
        ApolloCall<ForgotPasswordMutation.Data> queryCall = apolloClient.mutation(new ForgotPasswordMutation(email));
        Single<ApolloResponse<ForgotPasswordMutation.Data>> queryResponse = Rx3Apollo.single(queryCall);
        ApolloResponse<ForgotPasswordMutation.Data> response = queryResponse.blockingGet();
        if (response.hasErrors()) {
            throw new Exception(response.errors.get(0).getMessage());
        } else {
            Backend.email = email;
        }
    }

    public static void confirmForgotPassword(String code) throws Exception {
        ApolloCall<ConfirmForgotPasswordMutation.Data> queryCall = apolloClient.mutation(new ConfirmForgotPasswordMutation(email,code));
        Single<ApolloResponse<ConfirmForgotPasswordMutation.Data>> queryResponse = Rx3Apollo.single(queryCall);
        ApolloResponse<ConfirmForgotPasswordMutation.Data> response = queryResponse.blockingGet();
        if (response.hasErrors()) {
            throw new Exception(response.errors.get(0).getMessage());
        }
    }

    public static void resetPasswordConfirmed(String email, String newPassword, SharedPreferences sharedPreferences) throws Exception {
        ApolloCall<ResetPasswordConfirmedMutation.Data> queryCall = apolloClient.mutation(new ResetPasswordConfirmedMutation(email, newPassword));
        Single<ApolloResponse<ResetPasswordConfirmedMutation.Data>> queryResponse = Rx3Apollo.single(queryCall);
        ApolloResponse<ResetPasswordConfirmedMutation.Data> response = queryResponse.blockingGet();
        if (response.hasErrors()) {
            throw new Exception(response.errors.get(0).getMessage());
        } else {
            email = response.data.resetPasswordConfirmed.user.email;
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putString("access_token", response.data.resetPasswordConfirmed.access_token);
            editor.putString("refresh_token", response.data.resetPasswordConfirmed.refresh_token);
            editor.apply();
        }
    }

    public static void createHistory(int bpm) {
        ApolloCall<CreateHistoryMutation.Data> queryCall = apolloClient.mutation(new CreateHistoryMutation(email, bpm));
        Single<ApolloResponse<CreateHistoryMutation.Data>> queryResponse = Rx3Apollo.single(queryCall);
        ApolloResponse<CreateHistoryMutation.Data> response = queryResponse.blockingGet();
        if (response.hasErrors()) {
            System.out.println(response.errors.get(0).getMessage());
        }
    }

    public static TodayHistoryStatisticsQuery.Data getTodayHistoryStatistics() {
        ApolloCall<TodayHistoryStatisticsQuery.Data> queryCall = apolloClient.query(new TodayHistoryStatisticsQuery(email));
        Single<ApolloResponse<TodayHistoryStatisticsQuery.Data>> queryResponse = Rx3Apollo.single(queryCall);
        ApolloResponse<TodayHistoryStatisticsQuery.Data> response = queryResponse.blockingGet();
        if (response.hasErrors()) {
            System.out.println(response.errors.get(0).getMessage());
        }
        return response.data;
    }

    public static WeekHistoryStatisticsQuery.Data getWeekHistoryStatistics() {
        ApolloCall<WeekHistoryStatisticsQuery.Data> queryCall = apolloClient.query(new WeekHistoryStatisticsQuery(email));
        Single<ApolloResponse<WeekHistoryStatisticsQuery.Data>> queryResponse = Rx3Apollo.single(queryCall);
        ApolloResponse<WeekHistoryStatisticsQuery.Data> response = queryResponse.blockingGet();
        if (response.hasErrors()) {
            System.out.println(response.errors.get(0).getMessage());
        }
        return response.data;
    }

    public static void changePassword(String oldPassword, String newPassword) throws Exception {
        ApolloCall<ChangePasswordMutation.Data> queryCall = apolloClient.mutation(new ChangePasswordMutation(email, oldPassword, newPassword));
        Single<ApolloResponse<ChangePasswordMutation.Data>> queryResponse = Rx3Apollo.single(queryCall);
        ApolloResponse<ChangePasswordMutation.Data> response = queryResponse.blockingGet();
        if (response.hasErrors()) {
            throw new Exception(response.errors.get(0).getMessage());
        }
    }

    public static void createNote(String content, String startDate, String endDate) {
        ApolloCall<CreateNoteMutation.Data> queryCall = apolloClient.mutation(new CreateNoteMutation(new NoteInput(Optional.present(email), Optional.present(content), Optional.present(startDate), Optional.present(endDate))));
        Single<ApolloResponse<CreateNoteMutation.Data>> queryResponse = Rx3Apollo.single(queryCall);
        ApolloResponse<CreateNoteMutation.Data> response = queryResponse.blockingGet();
        if (response.hasErrors()) {
            System.out.println(response.errors.get(0).getMessage());
        }
    }

    public static FindNotesQuery.Data getNotes(Date date) {
        String dateIso = DateFormat.ISO8601format(date);
        ApolloCall<FindNotesQuery.Data> queryCall = apolloClient.query(new FindNotesQuery(email, dateIso));
        Single<ApolloResponse<FindNotesQuery.Data>> queryResponse = Rx3Apollo.single(queryCall);
        ApolloResponse<FindNotesQuery.Data> response = queryResponse.blockingGet();
        if (response.hasErrors()) {
            System.out.println(response.errors.get(0).getMessage());
        }
        return response.data;
    }

    public static void updateNotes(int id, String content, String startDate, String endDate) {
        ApolloCall<UpdateNoteMutation.Data> queryCall = apolloClient.mutation(new UpdateNoteMutation(new NoteUpdateInput(Optional.present(id), Optional.present(content), Optional.present(startDate), Optional.present(endDate))));
        Single<ApolloResponse<UpdateNoteMutation.Data>> queryResponse = Rx3Apollo.single(queryCall);
        ApolloResponse<UpdateNoteMutation.Data> response = queryResponse.blockingGet();
        if (response.hasErrors()) {
            System.out.println(response.errors.get(0).getMessage());
        }
    }

    public static void deleteNote(int id) {
        ApolloCall<DeleteNoteMutation.Data> queryCall = apolloClient.mutation(new DeleteNoteMutation(id));
        Single<ApolloResponse<DeleteNoteMutation.Data>> queryResponse = Rx3Apollo.single(queryCall);
        ApolloResponse<DeleteNoteMutation.Data> response = queryResponse.blockingGet();
        if (response.hasErrors()) {
            System.out.println(response.errors.get(0).getMessage());
        }
    }

    public static void RegisterWithSocial(String email, String name, SharedPreferences sharedPreferences) throws Exception {
        ApolloCall<RegisterWithSocialMutation.Data> queryCall = apolloClient.mutation(new RegisterWithSocialMutation(email, Optional.present(name)));
        Single<ApolloResponse<RegisterWithSocialMutation.Data>> queryResponse = Rx3Apollo.single(queryCall);
        ApolloResponse<RegisterWithSocialMutation.Data> response = queryResponse.blockingGet();
        if (response.hasErrors()) {
            throw new Exception(response.errors.get(0).getMessage());
        } else {
            email = response.data.registerWithSocial.user.email;
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putString("access_token", response.data.registerWithSocial.access_token);
            editor.putString("refresh_token", response.data.registerWithSocial.refresh_token);
            editor.apply();
        }
    }

    public static FindNotificationsWithEmailQuery.Data getNotificationsWithEmail() {
        ApolloCall<FindNotificationsWithEmailQuery.Data> queryCall = apolloClient.query(new FindNotificationsWithEmailQuery(email));
        Single<ApolloResponse<FindNotificationsWithEmailQuery.Data>> queryResponse = Rx3Apollo.single(queryCall);
        ApolloResponse<FindNotificationsWithEmailQuery.Data> response = queryResponse.blockingGet();
        if (response.hasErrors()) {
            System.out.println(response.errors.get(0).getMessage());
        }
        return response.data;
    }

    public static String getAdvisoryFirst() {
        ApolloCall<GetAdvisoryFirstMutation.Data> queryCall = apolloClient.mutation(new GetAdvisoryFirstMutation(email));
        Single<ApolloResponse<GetAdvisoryFirstMutation.Data>> queryResponse = Rx3Apollo.single(queryCall);
        ApolloResponse<GetAdvisoryFirstMutation.Data> response = queryResponse.blockingGet();
        if (response.hasErrors()) {
            System.out.println(response.errors.get(0).getMessage());
        }
        return response.data.getAdvisoryFirst;
    }

    public static String getAdvisory(String question) {
        ApolloCall<GetAdvisoryMutation.Data> queryCall = apolloClient.mutation(new GetAdvisoryMutation(email, question));
        Single<ApolloResponse<GetAdvisoryMutation.Data>> queryResponse = Rx3Apollo.single(queryCall);
        ApolloResponse<GetAdvisoryMutation.Data> response = queryResponse.blockingGet();
        if (response.hasErrors()) {
            System.out.println(response.errors.get(0).getMessage());
        }
        return response.data.getAdvisory;
    }
}
