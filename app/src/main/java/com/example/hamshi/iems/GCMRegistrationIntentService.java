package com.example.hamshi.iems;


import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import com.google.android.gms.gcm.GoogleCloudMessaging;
import com.google.android.gms.iid.InstanceID;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;


/**
 * Created by NgocTri on 4/8/2016.
 */
public class GCMRegistrationIntentService extends IntentService {
    public static final String REGISTRATION_SUCCESS = "RegistrationSuccess";
    public static final String REGISTRATION_ERROR = "RegistrationError";
    public static final String TAG = "GCMTOKEN";
    SessionManager session;
    private String id;

    public GCMRegistrationIntentService() {
        super("");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        registerGCM();
    }

    private void registerGCM() {
        Intent registrationComplete = null;
        String token = null;
        String llid = null;
        String who = null;
        SharedPreferences sharedPreferences = getSharedPreferences("GCM", Context.MODE_PRIVATE);//Define shared reference file name
        SharedPreferences.Editor editor = sharedPreferences.edit();
        session = new SessionManager(getApplicationContext());
        HashMap<String, String> user = session.getUserDetails();

        llid = user.get(SessionManager.KEY_ID);
        who = user.get(SessionManager.KEY_LOG);
        try {
            InstanceID instanceID = InstanceID.getInstance(getApplicationContext());
            token = instanceID.getToken(getString(R.string.gcm_defaultSenderId), GoogleCloudMessaging.INSTANCE_ID_SCOPE, null);
            Log.w("GCMRegIntentService", "token:" + token);
           // Log.w("GCMRegIntentService", "lid:" + llid);
            //notify to UI that registration complete success
            registrationComplete = new Intent(REGISTRATION_SUCCESS);
            registrationComplete.putExtra("token", token);
            //registrationComplete.putExtra("lid", llid);

            String oldToken = sharedPreferences.getString(TAG, "");//Return "" when error or key not exists
            //Only request to save token when token is new
            saveTokenToServer(token,llid,who);
            if(!"".equals(token) && !oldToken.equals(token)) {
                saveTokenToServer(token,llid,who);
                //Save new token to shared reference
                editor.putString(TAG, token);
                editor.commit();
            } else {
                Log.w("GCMRegistrationService", "Old token");
            }
        } catch (Exception e) {
            Log.w("GCMRegIntentService", "Registration error");
            registrationComplete = new Intent(REGISTRATION_ERROR);
        }
        //Send broadcast
        LocalBroadcastManager.getInstance(this).sendBroadcast(registrationComplete);
    }

    private void saveTokenToServer(String token,String iid,String who){
        Map paramPost = new HashMap();
        paramPost.put("action","add");
        paramPost.put("tokenid", token);
        paramPost.put("lid",iid);
        paramPost.put("whoo",who);
        try {
            String msgResult = getStringResultFromService_POST("http://192.168.8.100/gcm/gcm.php", paramPost);
            Log.w("ServiceResponseMsg", msgResult);
        }catch (Exception e){
            e.printStackTrace();
        }
    }
    public String getStringResultFromService_POST(String serviceURL, Map<String, String> params) {
        HttpURLConnection cnn = null;
        String line = null;
        URL url;
        try{
            url = new URL(serviceURL);
        } catch (MalformedURLException e){
            throw  new IllegalArgumentException("URL invalid:"+serviceURL);
        }
        StringBuilder bodyBuilder = new StringBuilder();
        Iterator<Map.Entry<String, String>> iterator = params.entrySet().iterator();
        //Construct the post body using the parameter
        while (iterator.hasNext()){
            Map.Entry<String, String> param = iterator.next();
            bodyBuilder.append(param.getKey()).append('=').append(param.getValue());
            if(iterator.hasNext()){
                bodyBuilder.append('&');
            }
        }
        String body = bodyBuilder.toString(); //format same to arg1=val1&arg2=val2
        Log.w("AccessService", "param:" + body);
        byte[]bytes = body.getBytes();
        try{
            cnn = (HttpURLConnection)url.openConnection();
            cnn.setDoOutput(true);
            cnn.setUseCaches(false);
            cnn.setFixedLengthStreamingMode(bytes.length);
            cnn.setRequestMethod("POST");
            cnn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded;charset=UTF-8");
            //Post the request
            OutputStream outputStream = cnn.getOutputStream();
            outputStream.write(bytes);
            outputStream.close();

            //Handle the response
            int status = cnn.getResponseCode();
            if(status!=200){
                throw  new IOException("Post fail with error code:" + status);
            }
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(cnn.getInputStream()));
            StringBuilder stringBuilder = new StringBuilder();
            while ((line = bufferedReader.readLine())!=null){
                stringBuilder.append(line+'\n');
            }
            return stringBuilder.toString();
        }catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }
}
