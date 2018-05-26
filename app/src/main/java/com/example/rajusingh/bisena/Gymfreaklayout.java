package com.example.rajusingh.bisena;

import android.app.Activity;
import android.app.AlertDialog;
import android.database.Cursor;
import android.graphics.Color;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.transition.Fade;
import android.view.Gravity;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;

public class Gymfreaklayout extends Activity {
    ArrayList<String> ID_Array;
    ArrayList<String > NAME_Array;
    SQLiteListAdapterGym ListAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gymfreaklayout);
        LinearLayout ll = (LinearLayout) findViewById(R.id.gymcontainer);

        ListView lv = new ListView(Gymfreaklayout.this);
        ll.setBackgroundColor(Color.WHITE);
        TextView textView = new TextView(Gymfreaklayout.this);
        textView.setText("Other Account Detais");
        textView.setTextColor(Color.BLUE);
        textView.setGravity(Gravity.CENTER_HORIZONTAL);
        lv.addHeaderView(textView);
        lv.setBackgroundColor(Color.YELLOW);
        ll.addView(lv, LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);

        //myDb = new Database(MainScreen.this);
     //   Cursor res = myDb.getAllAccountData();
        ID_Array = new ArrayList<String>();
        NAME_Array = new ArrayList<String>();
        ID_Array.clear();
        NAME_Array.clear();
//        if (res.moveToFirst()) {
//            do {
//
//                NAME_Array.add("$ " + res.getString(4));
//                Long s = res.getLong(6);
//                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
//                ID_Array.add((dateFormat.format(s)));
//
//            } while (res.moveToNext());
//
//        }
        ListAdapter = new SQLiteListAdapterGym(Gymfreaklayout.this,

                ID_Array,
                NAME_Array
        );

        lv.setAdapter(ListAdapter);

//        res.close();
//        mBuilder.setView(ll);
//        final AlertDialog dialog = mBuilder.create();
//        dialog.show();



    }


}
