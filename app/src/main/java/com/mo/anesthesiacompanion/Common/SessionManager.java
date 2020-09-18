package com.mo.anesthesiacompanion.Common;

import android.content.Context;
import android.content.SharedPreferences;

import java.util.HashMap;

public class SessionManager {

    SharedPreferences userSession;
    SharedPreferences.Editor editor;
    Context context;

    //session names
    public static final String SESSION_USER_SESSION="userLoginSession";
    public static final String SESSION_REMEMBER_ME="rememberMe";

    //user session variables
    private static final String IS_LOGIN = "IsLoggedIn";

    public static final String KEY_UID = "uid";
    public static final String KEY_PHONE = "phone";
    public static final String KEY_EMAIL = "email";
    public static final String KEY_PASSWORD = "password";
    public static final String KEY_FIRSTNAME = "firstname";
    public static final String KEY_LASTNAME = "lastname";
    public static final String KEY_ADDRESS = "address";
    public static final String KEY_SUBURB = "suburb";
    public static final String KEY_CITY = "city";
    public static final String KEY_QUALIFICATION = "qualification";
    public static final String KEY_EXPERIENCE = "experience";
    public static final String KEY_OCCUPATION = "occupation";
    public static final String KEY_HOSPITAL = "hospital";

    //remember me session variables
    private static final String IS_REMEMBER_ME = "IsRememberMe";
    public static final String KEY_PHONE_REMEMBER_ME = "phone";
    public static final String KEY_PASSWORD_REMEMBER_ME = "password";

    public SessionManager(Context _context, String sessionName) {
        context = _context;
        userSession = context.getSharedPreferences(sessionName, Context.MODE_PRIVATE);
        editor = userSession.edit();
    }

    public void createLoginSession(String uid, String phone, String email, String password, String firstname, String lastname, String address, String suburb, String city, String qualification, String experience, String occupation, String hospital) {
        editor.putBoolean(IS_LOGIN, true);

        editor.putString(KEY_UID, uid);
        editor.putString(KEY_PHONE, phone);
        editor.putString(KEY_EMAIL, email);
        editor.putString(KEY_PASSWORD, password);
        editor.putString(KEY_FIRSTNAME, firstname);
        editor.putString(KEY_LASTNAME, lastname);
        editor.putString(KEY_ADDRESS, address);
        editor.putString(KEY_SUBURB, suburb);
        editor.putString(KEY_CITY, city);
        editor.putString(KEY_QUALIFICATION, qualification);
        editor.putString(KEY_EXPERIENCE, experience);
        editor.putString(KEY_OCCUPATION, occupation);
        editor.putString(KEY_HOSPITAL, hospital);

        editor.commit();

    }

    public HashMap<String, String> getUserDetailFromSession() {
        HashMap<String, String> userData = new HashMap<>();
        userData.put(KEY_UID, userSession.getString(KEY_UID, null));
        userData.put(KEY_PHONE, userSession.getString(KEY_PHONE, null));
        userData.put(KEY_EMAIL, userSession.getString(KEY_EMAIL, null));
        userData.put(KEY_PASSWORD, userSession.getString(KEY_PASSWORD, null));
        userData.put(KEY_FIRSTNAME, userSession.getString(KEY_FIRSTNAME, null));
        userData.put(KEY_LASTNAME, userSession.getString(KEY_LASTNAME, null));
        userData.put(KEY_ADDRESS, userSession.getString(KEY_ADDRESS, null));
        userData.put(KEY_SUBURB, userSession.getString(KEY_SUBURB, null));
        userData.put(KEY_CITY, userSession.getString(KEY_CITY, null));
        userData.put(KEY_QUALIFICATION, userSession.getString(KEY_QUALIFICATION, null));
        userData.put(KEY_EXPERIENCE, userSession.getString(KEY_EXPERIENCE, null));
        userData.put(KEY_OCCUPATION, userSession.getString(KEY_OCCUPATION, null));
        userData.put(KEY_HOSPITAL, userSession.getString(KEY_HOSPITAL, null));

        return userData;
    }

    public boolean checkLogin() {
        if (userSession.getBoolean(IS_LOGIN, false)) {
            return true;
        } else {
            return false;
        }
    }

    public void logOutUserFromSession() {
        editor.clear();
        editor.commit();
    }
    //remember me
    public void createRememberMeSession(String phone, String password) {
        editor.putBoolean(IS_REMEMBER_ME, true);

        editor.putString(KEY_PHONE_REMEMBER_ME, phone);
        editor.putString(KEY_PASSWORD_REMEMBER_ME, password);

        editor.commit();

    }

    public HashMap<String, String> getRememberMeDetailFromSession() {
        HashMap<String, String> userData = new HashMap<>();

        userData.put(KEY_PHONE, userSession.getString(KEY_PHONE_REMEMBER_ME, null));
        userData.put(KEY_PASSWORD, userSession.getString(KEY_PASSWORD_REMEMBER_ME, null));

        return userData;
    }

    public boolean checkRememberMe() {
        if (userSession.getBoolean(IS_REMEMBER_ME, false)) {
            return true;
        } else {
            return false;
        }
    }
}
