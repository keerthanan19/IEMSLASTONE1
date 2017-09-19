package com.example.hamshi.iems;

import android.app.AlertDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.widget.Toast;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;

/**
 * Created by prabeesh on 7/14/2015.
 */
public class BackgroundTask3 extends AsyncTask<String,Void,String> {
    AlertDialog alertDialog;
    Context ctx;
    BackgroundTask3(Context ctx)
    {
        this.ctx =ctx;
    }
    @Override
    protected void onPreExecute() {
        alertDialog = new AlertDialog.Builder(ctx).create();
        alertDialog.setTitle("Login Information....");
    }
    @Override
    protected String doInBackground(String... params) {
        String reg_url = "http://192.168.1.9/Add_UserMoreDetails.php";

        String method = params[0];
        if (method.equals("register")) {
            String Disease = params[1];
            String Age = params[2];
            String Status = params[3];
            String Aname = params[4];
            String Ayear = params[5];
            String user_id = params[6];
            String Atype = params[7];
            String Aproblem = params[8];
            String Sname = params[9];
            String Syear = params[10];
            String Stype = params[11];
            String Sproblem = params[12];
            String Mname = params[13];
            String Mvalue = params[14];

            try {
                URL url = new URL(reg_url);
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setDoOutput(true);
                //httpURLConnection.setDoInput(true);
                OutputStream OS = httpURLConnection.getOutputStream();
                BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(OS, "UTF-8"));
                String data = URLEncoder.encode("Disease", "UTF-8") + "=" + URLEncoder.encode(Disease, "UTF-8") + "&" +
                        URLEncoder.encode("Age", "UTF-8") + "=" + URLEncoder.encode(Age, "UTF-8") + "&" +
                        URLEncoder.encode("Status", "UTF-8") + "=" + URLEncoder.encode(Status, "UTF-8")+ "&" +
                        URLEncoder.encode("Aname", "UTF-8") + "=" + URLEncoder.encode(Aname, "UTF-8")+ "&" +
                        URLEncoder.encode("Ayear", "UTF-8") + "=" + URLEncoder.encode(Ayear, "UTF-8")+ "&" +
                        URLEncoder.encode("user_id", "UTF-8") + "=" + URLEncoder.encode(user_id, "UTF-8")+ "&" +
                        URLEncoder.encode("Atype", "UTF-8") + "=" + URLEncoder.encode(Atype, "UTF-8")+ "&" +
                        URLEncoder.encode("Aproblem", "UTF-8") + "=" + URLEncoder.encode(Aproblem, "UTF-8")+ "&" +
                        URLEncoder.encode("Sname", "UTF-8") + "=" + URLEncoder.encode(Sname, "UTF-8")+ "&" +
                        URLEncoder.encode("Syear", "UTF-8") + "=" + URLEncoder.encode(Syear, "UTF-8")+ "&" +
                        URLEncoder.encode("Stype", "UTF-8") + "=" + URLEncoder.encode(Stype, "UTF-8")+ "&" +
                        URLEncoder.encode("Sproblem", "UTF-8") + "=" + URLEncoder.encode(Sproblem, "UTF-8")+ "&" +
                        URLEncoder.encode("Mname", "UTF-8") + "=" + URLEncoder.encode(Mname, "UTF-8")+ "&" +
                        URLEncoder.encode("Mvalue", "UTF-8") + "=" + URLEncoder.encode(Mvalue, "UTF-8")

                        ;
                bufferedWriter.write(data);
                bufferedWriter.flush();
                bufferedWriter.close();
                OS.close();
                InputStream IS = httpURLConnection.getInputStream();
                IS.close();
                //httpURLConnection.connect();
                httpURLConnection.disconnect();
                return "Registration Success...";
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return null;
    }
    @Override
    protected void onProgressUpdate(Void... values) {
        super.onProgressUpdate(values);
    }
    @Override
    protected void onPostExecute(String result) {
        if(result.equals("Registration Success..."))
        {
            Toast.makeText(ctx, result, Toast.LENGTH_LONG).show();
        }
        else
        {
            alertDialog.setMessage(result);
            alertDialog.show();
        }
    }
}