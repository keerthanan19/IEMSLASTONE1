package com.example.hamshi.iems;

import android.app.Activity;
import android.app.Application;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.mayuonline.tamilandroidunicodeutil.TamilUtil;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Vector;

public class ChatScreen extends Activity {
    SessionManager session;
    LinearLayout linearLayout;
    RelativeLayout relativeLayout;

    //ListView listView;
    ImageView imageView;
    Button btnBack;
    TextView textView;
    EditText editText;
    Vector<String> vChatMsg=new Vector<String>();

    HttpEntity entity;
    HttpGet httpget;
    HttpPost httppost;
    JSONObject jsonObject;
    StringBuilder stringBuilder = null;
    InputStream stream;
    HttpResponse response;
    HttpClient client;

    Handler handler = new Handler();
    String lastTs="0", fName, type, translatedText;
    boolean flag=true;
    Bitmap img;

    Vector<String> msgs=new Vector<String>();
    String id, name,text = "", chatMsg = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_screen);

        Intent i=getIntent();
        id=i.getStringExtra("id");
       // id="1";
        name=i.getStringExtra("name");

        //listView=(ListView)findViewById(R.id.listView_chatscreen);
        linearLayout=(LinearLayout)findViewById(R.id.chat_layout);
        //relativeLayout=(RelativeLayout)findViewById(R.id.chat_layout);
        textView=(TextView)findViewById(R.id.textView_chatscreen_name);
        editText=(EditText)findViewById(R.id.editText_chatscreen_typearea);

        textView.setText(name);
        loadChat();
    }

    public String getEncodedString(String str) {
        String encodedString = "";
        try {
            encodedString = URLEncoder.encode(str, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return encodedString;
    }

    //en to ta translation
    public void translateText(final String str) {
        new Thread(new Runnable()
        {
            public void run()
            {
                try
                {
                    httpget=new HttpGet("https://www.googleapis.com/language/translate/v2?key=AIzaSyBVzCABtBGjhCNjIfyY8ORm65NX9Af7b-k&q="+getEncodedString(str)+"&source=en&target="+getEncodedString("ta"));

                    client = new DefaultHttpClient();

                    stringBuilder = new StringBuilder();

                    response= client.execute(httpget);

                    stream=response.getEntity().getContent();

                    BufferedReader reader = new BufferedReader(new InputStreamReader(
                            stream,"UTF-8"));

                    // temp string that holds text line by line
                    String line;
                    String totalText="";

                    // read the contents of the reader/the page by line, until there are
                    // no lines left
                    while ((line = reader.readLine()) != null) {
                        // keep adding each line to totalText
                        totalText = totalText + line;
                    }
                    // remember to always close the reader
                    reader.close();

                    translatedText = totalText ;

					/*int ch;
					while ((ch = stream.read()) != -1)
					{
						stringBuilder.append((char) ch);
					}
					translatedText=stringBuilder.toString();*/
                    Log.d("translatedResponse", translatedText);

                    JSONObject jsonObject = new JSONObject(translatedText);
                    JSONObject data = new JSONObject(jsonObject.getString("data"));
                    JSONArray jsonArray = new JSONArray(data.getString("translations"));
                    JSONObject obj = (JSONObject) jsonArray.get(0);
                    translatedText = obj.getString("translatedText");
                    Log.d("translatedText", translatedText);


                } catch (ClientProtocolException e1) {
                    // TODO Auto-generated catch block
                    e1.printStackTrace();

                } catch (Exception e1) {
                    // TODO Auto-generated catch block
                    e1.printStackTrace();

                }
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        // TODO Auto-generated method stub
                        //newUser.setText(translatedText);

                        //LinearLayout linearLayoutChatMsg = new LinearLayout(ChatScreen.this);

                        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                                LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                        layoutParams.gravity = Gravity.RIGHT;

                        TextView tv = new TextView(ChatScreen.this);
                        //Typeface newFont = Typeface.createFromAsset(getAssets(), "bapc.TTF");
                        Typeface newFont = Typeface.createFromAsset(getAssets(), "mylai.ttf");
                        tv.setTypeface(newFont);

                        //String TSCIIString = TamilUtil.convertToTamil(TamilUtil.TSCII, "வணக்கம் அன்ரொயிட்");
                        String TSCIIString = TamilUtil.convertToTamil(TamilUtil.TSCII, translatedText);
                        //Setting the new string to TextView
                        tv.setText(TSCIIString);


                        //relativeLayoutParams.addRule(RelativeLayout.ALIGN_PARENT_RIGHT, tv.getId());
                        //tv.setBackgroundResource(R.drawable.backg_titlebar);
                        tv.setTextColor(Color.BLACK);
                        tv.setTextSize(20);

                        //tv.setText(translatedText);
                        tv.setLayoutParams(layoutParams);
                        tv.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
                        //tv.setBackgroundResource(R.drawable.back_text);
                        linearLayout.addView(tv);

                    }
                });
            }
        }).start();
    }


    //en to ta translation
    public void translateTatoEn(final String str) {

        new Thread(new Runnable()
        {
            public void run()
            {
                try
                {
                    httpget=new HttpGet("https://www.googleapis.com/language/translate/v2?key=AIzaSyBVzCABtBGjhCNjIfyY8ORm65NX9Af7b-k&q="+getEncodedString(str)+"&source=ta&target="+getEncodedString("en"));

                    client = new DefaultHttpClient();

                    stringBuilder = new StringBuilder();

                    response= client.execute(httpget);

                    stream=response.getEntity().getContent();

                    BufferedReader reader = new BufferedReader(new InputStreamReader(
                            stream,"UTF-8"));

                    // temp string that holds text line by line
                    String line;
                    String totalText="";

                    // read the contents of the reader/the page by line, until there are
                    // no lines left
                    while ((line = reader.readLine()) != null) {
                        // keep adding each line to totalText
                        totalText = totalText + line;
                    }
                    // remember to always close the reader
                    reader.close();

                    translatedText = totalText ;

                    Log.d("translatedResponse", translatedText);

                    JSONObject jsonObject = new JSONObject(translatedText);
                    JSONObject data = new JSONObject(jsonObject.getString("data"));
                    JSONArray jsonArray = new JSONArray(data.getString("translations"));
                    JSONObject obj = (JSONObject) jsonArray.get(0);
                    chatMsg = obj.getString("translatedText");
                    Log.d("translatedText", translatedText);


                } catch (ClientProtocolException e1) {
                    // TODO Auto-generated catch block
                    e1.printStackTrace();

                } catch (Exception e1) {
                    // TODO Auto-generated catch block
                    e1.printStackTrace();

                }
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        // TODO Auto-generated method stub
                        //newUser.setText(translatedText);

                        updateMsg();

                    }
                });
            }
        }).start();
    }


    public void loadChat()
    {
        session = new SessionManager(getApplicationContext());
        HashMap<String, String> user = session.getUserDetails();

        final String user_id = user.get(SessionManager.KEY_ID);
        new Thread(new Runnable() {
            public void run() {

                while(flag) {

                    try {
                       // if(Application_User_Type.userType.equals("doctor"))
                          //  httpget = new HttpGet(IPSettings.getBaseUrl()+"getchatmsgs.php?id1="+user_id+"&id2="+id+"&ts="+lastTs+"");
                       // else
                            httpget = new HttpGet(IPSettings.getBaseUrl()+"getchatmsgs.php?id1="+user_id+"&id2="+id+"&ts="+lastTs+"");

                        client = new DefaultHttpClient();
                        stringBuilder = new StringBuilder();
                        response = client.execute(httpget);
                        stream = response.getEntity().getContent();
                        int ch;
                        while ((ch = stream.read()) != -1) {
                            stringBuilder.append((char) ch);
                        }
                        String str = stringBuilder.toString();
                        Log.d("test", str);

                        JSONArray jArray1 = new JSONArray(str);

                        for (int i = 0; i < jArray1.length(); i++) {
                            jsonObject = jArray1.getJSONObject(i);

                            type=jsonObject.getString("type");
                            if(type.equals("text"))
                                msgs.add(jsonObject.getString("msg"));

                            lastTs=jsonObject.getString("ts");
                        }

                    } catch (ClientProtocolException e1) {
                        // TODO Auto-generated catch block
                        e1.printStackTrace();

                    } catch (IOException e1) {
                        // TODO Auto-generated catch block
                        e1.printStackTrace();

                    } catch (JSONException e1) {
                        // TODO Auto-generated catch block
                        e1.printStackTrace();

                    }
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            // TODO Auto-generated method stub
                            for(int i=0;i<msgs.size();i++)
                                if(type.equals("text"))
                                    addMsgToChat(msgs.elementAt(i), "text");

                            msgs.clear();
                        }
                    });

                    try {
                        Thread.sleep(2000);
                    } catch (InterruptedException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                }
            }
        }).start();
    }

    public void addMsgToChat(String chatMsg, String type)
    {

        Log.d("chat msg", chatMsg);
        //RelativeLayout.LayoutParams relativeLayoutParams = new RelativeLayout.LayoutParams(
        //	RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);

        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        layoutParams.gravity = Gravity.RIGHT;

        //linearLayout.setLayoutParams(layoutParams);
        String ut = Application_User_Type.userType;

        if(type.equals("text")) {
            if(ut.equals("patient")) {
                    //translateText(chatMsg);
            } else if(ut.equals("doctor")) {
				TextView tv = new TextView(this);
				//relativeLayoutParams.addRule(RelativeLayout.ALIGN_PARENT_RIGHT, tv.getId());
				//tv.setBackgroundResource(R.drawable.backg_titlebar);
				tv.setTextColor(Color.BLACK);
				tv.setTextSize(20);
				tv.setText(chatMsg);
				tv.setLayoutParams(layoutParams);
				//tv.setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
				//tv.setBackgroundResource(R.drawable.back_text);
				linearLayout.addView(tv);
				//relativeLayout.addView(tv, layoutParams);
            }
        }

    }

    public void onSendClick(View v)
    {
        chatMsg=editText.getText().toString();


        if(Application_User_Type.userType.equals("doctor")) {
            updateMsg();
        } else {
            try {
                translateTatoEn(new String(chatMsg.getBytes(), "UTF-8"));
            } catch(Exception e) {
                Log.d("tamil to en", e.getMessage());
            }
        }



    }

    public void updateMsg() {
        session = new SessionManager(getApplicationContext());
        HashMap<String, String> user = session.getUserDetails();

         final String user_id = user.get(SessionManager.KEY_ID);
        if(chatMsg.length()>0)
        {
            vChatMsg.add(chatMsg);
            //final String chatMsgStr=chatMsg;
            //addMsgToChat(chatMsg);
            //listView.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, vChatMsg));
            editText.setText("");

            new Thread(new Runnable() {
                public void run() {
                    try {
                        ///httpget = new HttpGet(IPSettings.getBaseUrl()+"chat.php?id1="+Login.id+"&id2="+uid+"&text="+chatMsgStr+"");

                        HttpPost httpPost=new HttpPost(IPSettings.getBaseUrl()+"chat.php");
                        List<NameValuePair> postParameters = new ArrayList<NameValuePair>();

                        if(Application_User_Type.userType.equals("doctor"))
                            postParameters.add(new BasicNameValuePair("id1", user_id));
                        else
                            postParameters.add(new BasicNameValuePair("id1", user_id));

                        postParameters.add(new BasicNameValuePair("id2", id));
                        postParameters.add(new BasicNameValuePair("ts", lastTs));
                        postParameters.add(new BasicNameValuePair("text", chatMsg));
                        UrlEncodedFormEntity urlEncodedFormEntity = new UrlEncodedFormEntity(postParameters);
                        httpPost.setEntity(urlEncodedFormEntity);

                        client = new DefaultHttpClient();
                        stringBuilder = new StringBuilder();
                        response = client.execute(httpPost);
                        stream = response.getEntity().getContent();
                        int ch;
                        while ((ch = stream.read()) != -1) {
                            stringBuilder.append((char) ch);
                        }
                        String str = stringBuilder.toString();
                        Log.d("test", str);

                        JSONArray jArray1 = new JSONArray(str);

                        for (int i = 0; i < jArray1.length(); i++) {
                            jsonObject = jArray1.getJSONObject(i);
                        }

                    } catch (ClientProtocolException e1) {
                        // TODO Auto-generated catch block
                        e1.printStackTrace();

                    } catch (IOException e1) {
                        // TODO Auto-generated catch block
                        e1.printStackTrace();

                    } catch (JSONException e1) {
                        // TODO Auto-generated catch block
                        e1.printStackTrace();

                    }
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            // TODO Auto-generated method stub
                        }
                    });
                }
            }).start();
        }
        else
            Toast.makeText(this, "Please enter somthing to send.", Toast.LENGTH_SHORT).show();

        }

}
