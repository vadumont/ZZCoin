package com.zzcoin.taca;

import android.app.Activity;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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

public class ChatActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
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

    public void sendMessage(View v){
        ViewGroup w = (ViewGroup)v.getParent();
        EditText et = (EditText)w.getChildAt(0);
        String message = et.getText().toString();

        if(!isEmptyString(message)){
            LayoutInflater inflater = LayoutInflater.from(getApplicationContext());
            LinearLayout layout = (LinearLayout) inflater.inflate(R.layout.messageuser, null, false);
            TextView tv = (TextView)layout.findViewById(R.id.contentUserMessage);
            tv.setText(message);

            LinearLayout linear = (LinearLayout)findViewById(R.id.messagehall);
            linear.addView(layout);

            String answer = sendAnswer(message);

            LinearLayout ansLayout = createAnswer(answer);
            linear.addView(ansLayout);
        }

        et.setText("");

        et.clearFocus();
        hideKeyboard(this);

        ScrollView sv = (ScrollView)findViewById(R.id.scrollbar);
        sv.fullScroll(View.FOCUS_DOWN);



    }

    private LinearLayout createAnswer(String answer){
        LayoutInflater inflater = LayoutInflater.from(getApplicationContext());
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

        return layout;
    }

    private String sendAnswer(String message){
        // To be implemented
        return "To be implemented";
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
