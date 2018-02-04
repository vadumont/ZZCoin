package com.zzcoin.taca;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

public class ChatActivity extends AppCompatActivity {
    Context mycontext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        mycontext=getApplicationContext();

        //Adding toolbar to the activity
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        SharedPreferences  mPrefs = getPreferences(MODE_PRIVATE);
        Gson gson = new Gson();
        String json = mPrefs.getString("chatBox", "");
        if(!isEmptyString(json)){
            List<String> obj = gson.fromJson(json, List.class);
            for(String s : obj){
                createMessage(s);
            }

        }



    }



    @Override
    protected void onStop() {
        super.onStop();
        SharedPreferences  mPrefs = getPreferences(MODE_PRIVATE);
        LinearLayout ll = (LinearLayout)findViewById(R.id.chatBox);

        List<String> messages = getMessageFromChat();
        SharedPreferences.Editor prefsEditor = mPrefs.edit();
        Gson gson = new Gson();
        String json = gson.toJson(messages);
        prefsEditor.clear();
        prefsEditor.putString("chatBox", json);
        prefsEditor.commit();


    }

    private List<String> getMessageFromChat(){
        LinearLayout ll = (LinearLayout)findViewById(R.id.messagehall);
        int nb = ((ViewGroup)ll).getChildCount();
        int nbMessage = 0;
        List<String> messages = new ArrayList<>();
        for(int index=0; index<((ViewGroup)ll).getChildCount(); ++index) {
            View v = (View)ll.getChildAt(index);
            if(((ViewGroup)v).getChildCount() == 1 && index != 0){
                String question = textMessage(((ViewGroup) v).getChildAt(0));
                messages.add(question);
            }
        }
        return messages;
    }

    private void upgradeStar(View v){
        ImageButton im = (ImageButton)v;
        im.setImageResource(R.drawable.etoiles2);
    }
    private void downgradeStar(View v){
        ImageButton im = (ImageButton)v;
        im.setImageResource(R.drawable.etoiles);
    }

    public void vote(View v) {
        int position=-1;
        ViewGroup w = (ViewGroup)v.getParent();
        for(int index=0; index<((ViewGroup)w).getChildCount(); ++index) {
            if(position == -1){
                upgradeStar(w.getChildAt(index));
            }else{
                downgradeStar(w.getChildAt(index));
            }
            if(v.equals(w.getChildAt(index))){
                position = index;
            }

        }
    }

    private String textMessage(View v){
        ViewGroup w = (ViewGroup)v.getParent();
        TextView tv = (TextView)w.getChildAt(0);
        String message = tv.getText().toString();
        return message;
    }

    public void createMessage(String message) {
        LayoutInflater inflater = LayoutInflater.from(getApplicationContext());
        LinearLayout layout = (LinearLayout) inflater.inflate(R.layout.messageuser, null, false);
        TextView tv = (TextView)layout.findViewById(R.id.contentUserMessage);
        tv.setText(message);

        LinearLayout linear = (LinearLayout)findViewById(R.id.messagehall);
        linear.addView(layout);

        sendAnswer(message,this,linear);

    }

    public void sendMessage(View v){
        ViewGroup w = (ViewGroup)v.getParent();
        EditText et = (EditText)w.getChildAt(0);
        String message = et.getText().toString();

        if(!isEmptyString(message)){
           createMessage(message);
        }

        et.setText("");

        et.clearFocus();
        hideKeyboard(this);

        ScrollView sv = (ScrollView)findViewById(R.id.scrollbar);
        sv.fullScroll(View.FOCUS_DOWN);


    }

    public void createAnswer(String answer,LinearLayout linear,Context context){
        LayoutInflater inflater = LayoutInflater.from(context);
        LinearLayout layout = (LinearLayout) inflater.inflate(R.layout.messagemark, null, false);

        LinearLayout messageLayout = (LinearLayout)layout.findViewById(R.id.contentMessage);
        TextView tw = (TextView)messageLayout.findViewById(R.id.botText);

        LinearLayout voteLayout = (LinearLayout)layout.findViewById(R.id.noteUser);
        for(int index=0; index<((ViewGroup)voteLayout).getChildCount(); ++index) {
            ImageButton im = (ImageButton)voteLayout.getChildAt(index);
            im.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    vote(view);
                }
            });
        }
        tw.setText(answer);
        linear.addView(layout);
    }

    private void sendAnswer(String message,Context context,LinearLayout linear){
        try {
            wordRecognition.Highlight(message,getApplicationContext(),linear);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private boolean isEmptyString(String text) {
        return (text == null || text.trim().equals("null") || text.trim()
                .length() <= 0);
    }

    private void hideKeyboard(Activity activity) {
        InputMethodManager imm = (InputMethodManager) activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
        //Find the currently focused view, so we can grab the correct window token from it.
        View view = activity.getCurrentFocus();
        //If no view currently has focus, create a new one, just so we can grab a window token from it
        if (view == null) {
            view = new View(activity);
        }
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

}
