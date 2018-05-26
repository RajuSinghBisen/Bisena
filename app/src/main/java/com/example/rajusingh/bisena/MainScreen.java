package com.example.rajusingh.bisena;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.RequiresApi;
import android.support.v4.app.NotificationCompat;
import android.transition.Slide;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.jjoe64.graphview.DefaultLabelFormatter;
import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.concurrent.TimeUnit;

public class MainScreen extends Activity {
    SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yy");
    LineGraphSeries<DataPoint> series = new LineGraphSeries<>(new DataPoint[0]);
    LineGraphSeries<DataPoint> serie = new LineGraphSeries<>(new DataPoint[0]);
    int cibcint, tdint, scotiaint, otherint, total,eachexpenseamount, yearlyint, monthlyint, dailyint;
    int totalexpenseamount = 0;
    Database myDb;
    TextView warningtxt, dayleft, amountleft;
    int counter;
    double t1;
    int counterpreviousdate = 0;
    int day, mon, year, amt;
    SQLiteListAdapter ListAdapter;
    SQLiteExpenseAdapter ExpenseAdapter;
    ListView listView;
    ArrayList<String> ITEM_Array;
    ArrayList<String> ID_Array;
    ArrayList<String> NAME_Array;
    long daysDiff;
    long dates ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_screen);
      //Saving date for gym
      //  dates = new Date().getTime();




        //Database to insert values
        myDb = new Database(this);
        // Getting the headline for the motto
        // Casting Layouts items
        final GraphView graph = (GraphView) findViewById(R.id.graph);
        graph.addSeries(series);
        graph.setTitle("Savings V/S Date ");
        graph.setBackgroundColor(Color.argb(255, 100, 10, 200));
        series.resetData(getDataPoint());
        graph.getGridLabelRenderer().setHumanRounding(false);
        graph.getGridLabelRenderer().setNumHorizontalLabels(3);
        graph.getGridLabelRenderer().setNumVerticalLabels(6);
        graph.getViewport().setScalable(true);
        graph.getViewport().setScalableY(true);
        graph.getViewport().setScrollable(true);
        graph.getViewport().setScrollableY(true);
        series.setDrawDataPoints(true);
        series.setDataPointsRadius(10);
        graph.getGridLabelRenderer().setLabelFormatter(new DefaultLabelFormatter() {
            @Override
            public String formatLabel(double date, boolean isValueX) {
                if (isValueX) {
                    return sdf.format(new Date((long) date));
                } else {
                    return super.formatLabel(date, isValueX);
                }
            }
        });

        //graph for expense
        final GraphView graphs = (GraphView) findViewById(R.id.graphExpense);
        graphs.addSeries(serie);
        graphs.setTitle("Expenses V/S Date ");
        graphs.setBackgroundColor(Color.argb(255, 100, 10, 200));
        serie.resetData(getDataPoints());
        graphs.getGridLabelRenderer().setHumanRounding(false);
        graphs.getGridLabelRenderer().setNumHorizontalLabels(3);
        graphs.getGridLabelRenderer().setNumVerticalLabels(6);
        graphs.getViewport().setScalable(true);
        graphs.getViewport().setScalableY(true);
        graphs.getViewport().setScrollable(true);
        graphs.getViewport().setScrollableY(true);
        serie.setDrawDataPoints(true);
        serie.setDataPointsRadius(10);
        graphs.getGridLabelRenderer().setLabelFormatter(new DefaultLabelFormatter() {
            @Override
            public String formatLabel(double date, boolean isValueX) {
                if (isValueX) {
                    return sdf.format(new Date((long) date));
                } else {
                    return super.formatLabel(date, isValueX);
                }
            }
        });
        //Graph for expense ends here


        final LinearLayout headingtext = (LinearLayout) findViewById(R.id.warning);
        final LinearLayout layoutbank = (LinearLayout) findViewById(R.id.bankdisplay);
        final LinearLayout tddisplay = (LinearLayout) findViewById(R.id.td);
        final LinearLayout cibcdisplay = (LinearLayout) findViewById(R.id.cibc);
        final LinearLayout americandisplay = (LinearLayout) findViewById(R.id.american);
        final LinearLayout scotiadispaly = (LinearLayout) findViewById(R.id.scotia);
         final LinearLayout listExpense = (LinearLayout) findViewById(R.id.ViewExpense);
        final LinearLayout graphExpense = (LinearLayout) findViewById(R.id.expense);
        final LinearLayout eachbanklayout = (LinearLayout) findViewById(R.id.eachbank);
        final LinearLayout bankgraph = (LinearLayout) findViewById(R.id.bankgraph);
        final LinearLayout bankcounter = (LinearLayout) findViewById(R.id.counterbank);
        final LinearLayout ieltslayout = (LinearLayout) findViewById(R.id.ielts);


        SharedPreferences prefs = getPreferences(MODE_PRIVATE);
        int days = prefs.getInt("ddd", 21);
        int months = prefs.getInt("mmm", 8);
        int years = prefs.getInt("yyy", 2020);
        int amountfinal = prefs.getInt("finalamt", 100000);


        dayleft = (TextView) findViewById(R.id.dayautomaticleft);
        Calendar thatDay = Calendar.getInstance();
        thatDay.set(Calendar.DAY_OF_MONTH, days);
        thatDay.set(Calendar.MONTH, months); // 0-11 so 1 less
        thatDay.set(Calendar.YEAR, years);
        long msDiff = thatDay.getTimeInMillis() - Calendar.getInstance().getTimeInMillis();
        final long daysDiff = TimeUnit.MILLISECONDS.toDays(msDiff);
        dayleft.setText(String.valueOf(daysDiff));

        warningtxt = findViewById(R.id.warningText);
        amountleft = (TextView) findViewById(R.id.amountleft);
        Cursor res = myDb.getAllAccountData();
        if (res.getCount() <= 0) {
            amountleft.setText("Wait..");
            dayleft.setText("wait...");
            res.close();
        } else {
            res.moveToLast();
            int finamAmount = res.getInt(5);
            int reminingAmount = amountfinal - finamAmount;
            amountleft.setText(String.valueOf("$ " + reminingAmount));
            res.close();
        }

        SharedPreferences pref = getPreferences(MODE_PRIVATE);
        warningtxt.setText(pref.getString("text", "Enter Motto"));
        Animation myFadeInAnimation = AnimationUtils.loadAnimation(MainScreen.this, R.anim.move);
        warningtxt.startAnimation(myFadeInAnimation);
        Animation myFadeInAnim = AnimationUtils.loadAnimation(MainScreen.this, R.anim.blinkimage);
        dayleft.startAnimation(myFadeInAnim);
        Animation myFadeInAnims = AnimationUtils.loadAnimation(MainScreen.this, R.anim.blinkimage);
        amountleft.startAnimation(myFadeInAnims);
        layoutbank.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Main Layout
                layoutbank.setVisibility(View.VISIBLE);
                eachbanklayout.setVisibility(View.VISIBLE);
                ieltslayout.setVisibility(View.VISIBLE);


                //Sub Layout
                bankgraph.setVisibility(View.VISIBLE);
                bankcounter.setVisibility(View.VISIBLE);
                tddisplay.setVisibility(View.INVISIBLE);
                cibcdisplay.setVisibility(View.INVISIBLE);
                scotiadispaly.setVisibility(View.INVISIBLE);
                americandisplay.setVisibility(View.INVISIBLE);
                listExpense.setVisibility(View.INVISIBLE);
                graphExpense.setVisibility(View.INVISIBLE);



            }
        });

        eachbanklayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //main layout
                eachbanklayout.setVisibility(View.VISIBLE);
                layoutbank.setVisibility(View.VISIBLE);
                eachbanklayout.setVisibility(View.VISIBLE);
//                gymlayout.setVisibility(View.VISIBLE);
                ieltslayout.setVisibility(View.VISIBLE);



                //Sub Layout
                bankgraph.setVisibility(View.INVISIBLE);
                bankcounter.setVisibility(View.INVISIBLE);
                tddisplay.setVisibility(View.VISIBLE);
                cibcdisplay.setVisibility(View.VISIBLE);
                scotiadispaly.setVisibility(View.VISIBLE);
                americandisplay.setVisibility(View.VISIBLE);
                listExpense.setVisibility(View.INVISIBLE);
                graphExpense.setVisibility(View.INVISIBLE);


            }
        });



        ieltslayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Main Layout
                layoutbank.setVisibility(View.VISIBLE);
                eachbanklayout.setVisibility(View.VISIBLE);
                ieltslayout.setVisibility(View.VISIBLE);

                //Sub Layout
                listExpense.setVisibility(View.VISIBLE);
                graphExpense.setVisibility(View.VISIBLE);
                bankgraph.setVisibility(View.INVISIBLE);
                bankcounter.setVisibility(View.INVISIBLE);
                tddisplay.setVisibility(View.INVISIBLE);
                cibcdisplay.setVisibility(View.INVISIBLE);
                scotiadispaly.setVisibility(View.INVISIBLE);
                americandisplay.setVisibility(View.INVISIBLE);



            }
        });

//  Creating alert dialog for income graph
        graph.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences preferences = getPreferences(MODE_PRIVATE);
                int value = preferences.getInt("finalamt", 0);
                if (value == 0) {
                    Toast.makeText(getApplicationContext(), "Please Set The Total Amount and Days", Toast.LENGTH_LONG).show();
                } else {
                    // handle the value


                    graph.setBackgroundColor(Color.argb(255, 100, 10, 200));
                    final AlertDialog.Builder mBuilder = new AlertDialog.Builder(MainScreen.this);
                    final View mView = getLayoutInflater().inflate(R.layout.graphpopup, null);
                    Cursor res = myDb.getAllAccountData();
                    TextView recentdate = (TextView) mView.findViewById(R.id.recentolddate);
                    TextView recentamount = (TextView) mView.findViewById(R.id.recentoldamount);
                    final EditText cibcamt = (EditText) mView.findViewById(R.id.cibcamount);
                    final EditText tdamt = (EditText) mView.findViewById(R.id.tdamount);
                    final EditText scotiaamt = (EditText) mView.findViewById(R.id.scotiaamount);
                    final EditText otheramt = (EditText) mView.findViewById(R.id.otheramount);
                    Button updateamount = (Button) mView.findViewById(R.id.updatecurrentamount);
                    Button deleteeamount = (Button) mView.findViewById(R.id.deletecurrentamount);
                    if (res.getCount() <= 0) {
                        recentdate.setText("wait");
                        res.close();
                    } else {
                        res.moveToLast();
                        int finamAmount = res.getInt(5);
                        Long s = res.getLong(6);
                        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
                        recentdate.setText(dateFormat.format(s));
                        recentamount.setText(String.valueOf(finamAmount));
                        res.close();
                    }
                    mBuilder.setView(mView);
                    final AlertDialog dialog = mBuilder.create();
                    dialog.show();
                    updateamount.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {


                            if (!cibcamt.getText().toString().isEmpty() && !tdamt.getText().toString().isEmpty() &&
                                    !scotiaamt.getText().toString().isEmpty() && !otheramt.getText().toString().isEmpty()) {
                                dialog.show();

                                String tdaamt = tdamt.getText().toString();
                                // tdint = Integer.parseInt(tdaamt);
                                String cibcaamt = cibcamt.getText().toString();
                                // cibcint = Integer.parseInt(cibcaamt);
                                String scotiaaamt = scotiaamt.getText().toString();
                                //scotiaint = Integer.parseInt(scotiaaamt);
                                String otheraamt = otheramt.getText().toString();
                                // otherint = Integer.parseInt(otheraamt);
                                long date = new Date().getTime();
                                tdint = Integer.parseInt(tdaamt);
                                cibcint = Integer.parseInt(cibcaamt);
                                scotiaint = Integer.parseInt(scotiaaamt);
                                otherint = Integer.parseInt(otheraamt);
                                total = cibcint + tdint + scotiaint + otherint;
                                boolean isinserted = myDb.insertAccountData(cibcint, tdint, scotiaint, otherint, total, date);
                                if (isinserted = true) {
                                    Toast.makeText(MainScreen.this, "successfuly inserted", Toast.LENGTH_LONG).show();

                                } else {
                                    Toast.makeText(MainScreen.this, "not inserted ", Toast.LENGTH_LONG).show();
                                }

                                dialog.dismiss();
                                recreate();


                            } else {
                                Toast.makeText(MainScreen.this, "please fill each field", Toast.LENGTH_LONG).show();
                            }
                        }
                    });

                    deleteeamount.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                            myDb = new Database(MainScreen.this);
                            Cursor res = myDb.getAllAccountData();
                            if (res.moveToLast()) {
                                if (res.getCount() > 1) {
                                    int index = res.getInt(0);
                                    myDb.deleteRow(index);
                                    res.close();
                                    // dialog.show();
                                    dialog.dismiss();
                                    recreate();
                                } else {
                                    Toast.makeText(MainScreen.this, "No Data Left In  Your Account", Toast.LENGTH_LONG).show();
                                    int index = res.getInt(0);
                                    myDb.deleteRow(index);
                                    res.close();
                                    // dialog.show();
                                    dialog.dismiss();
                                    recreate();
                                    SharedPreferences preffs = getPreferences(MODE_PRIVATE);
                                    preffs.edit().remove("finalamt").commit();
                                }
                            } else {
                                Toast.makeText(MainScreen.this, "No Data Left In  Your Account", Toast.LENGTH_LONG).show();

                                SharedPreferences preffs = getPreferences(MODE_PRIVATE);
                                preffs.edit().remove("finalamt").commit();
                            }
                        }
                    });
                }
            }
        });
// bank display method ends here...




        //  Creating alert dialog for income graph
        bankcounter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                graph.setBackgroundColor(Color.argb(255, 100, 10, 200));
                final AlertDialog.Builder mBuilder = new AlertDialog.Builder(MainScreen.this);
                final View mView = getLayoutInflater().inflate(R.layout.graphdisplay, null);

                final TextView recentdate = (TextView) mView.findViewById(R.id.recentolddates);
                final TextView recentamount = (TextView) mView.findViewById(R.id.recentoldamounts);
                final TextView yearlyamount = (TextView) mView.findViewById(R.id.avgyearly);
                final TextView monthlyamount = (TextView) mView.findViewById(R.id.avgmonthly);
                final TextView dailyamount = (TextView) mView.findViewById(R.id.avgdaily);
                final Button counterbutt = (Button) mView.findViewById(R.id.editCounter);
                mBuilder.setView(mView);
                final AlertDialog dialog = mBuilder.create();
                dialog.show();
                Cursor res = myDb.getAllAccountData();
                SharedPreferences prefs = getPreferences(MODE_PRIVATE);
                int amountfinal = prefs.getInt("finalamt", 100000);
                if (res.getCount() <= 0) {
                    amountleft.setText("Wait..");
                    res.close();
                } else {
                    res.moveToLast();
                    Long s = res.getLong(6);
                    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
                    int finamAmount = res.getInt(5);
                    int reminingAmount = amountfinal - finamAmount;
                    int dailyamt = (int) (reminingAmount / daysDiff);
                    int yearlyamt = dailyamt * 365;
                    int monthlyamt = dailyamt * 30;
                    recentamount.setText(String.valueOf(finamAmount));
                    recentdate.setText(dateFormat.format(s));
                    Animation myFadeInAnim = AnimationUtils.loadAnimation(MainScreen.this, R.anim.blinkimage);
                    yearlyamount.startAnimation(myFadeInAnim);
                    yearlyamount.setText(String.valueOf(yearlyamt));
                    monthlyamount.startAnimation(myFadeInAnim);
                    monthlyamount.setText(String.valueOf(monthlyamt));
                    dailyamount.startAnimation(myFadeInAnim);
                    dailyamount.setText(String.valueOf(dailyamt));
                    res.close();
                }

                //counter setting
                counterbutt.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                        final AlertDialog.Builder mmBuilder = new AlertDialog.Builder(MainScreen.this);
                        final View mmView = getLayoutInflater().inflate(R.layout.counterincome, null);
                        final EditText dayupdate = (EditText) mmView.findViewById(R.id.dd);
                        final EditText monupdate = (EditText) mmView.findViewById(R.id.mm);
                        final EditText yearupdate = (EditText) mmView.findViewById(R.id.yy);
                        final EditText amtupdate = (EditText) mmView.findViewById(R.id.finalamt);
                        final Button submit = (Button) mmView.findViewById(R.id.submitbutt);
                        mmBuilder.setView(mmView);
                        final AlertDialog mmdialog = mmBuilder.create();
                        mmdialog.show();
                        submit.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                SharedPreferences prefs = getPreferences(MODE_PRIVATE);
                                int previosAmount = prefs.getInt("finalamt", 0);
                                if (!dayupdate.getText().toString().isEmpty() && !monupdate.getText().toString().isEmpty() && !yearupdate.getText().toString().isEmpty() && !amtupdate.getText().toString().isEmpty()) {
                                    day = Integer.parseInt(dayupdate.getText().toString());
                                    mon = Integer.parseInt(monupdate.getText().toString());
                                    year = Integer.parseInt(yearupdate.getText().toString());
                                    amt = Integer.parseInt(amtupdate.getText().toString());
                                    Calendar thatDay = Calendar.getInstance();
                                    thatDay.set(Calendar.DAY_OF_MONTH, day);
                                    thatDay.set(Calendar.MONTH, mon); // 0-11 so 1 less
                                    thatDay.set(Calendar.YEAR, year);
                                    if (thatDay.getTimeInMillis() > Calendar.getInstance().getTimeInMillis() && day < 32 && mon < 12 && amt > previosAmount) {

                                        SharedPreferences.Editor editor = getPreferences(MODE_PRIVATE).edit();
                                        editor.putInt("ddd", day);
                                        editor.putInt("mmm", mon);
                                        editor.putInt("yyy", year);
                                        editor.putInt("finalamt", amt);
                                        editor.apply();
                                        mmdialog.dismiss();
                                        recreate();
                                    } else {
                                        Toast.makeText(getApplicationContext(), "Please fill as recommended", Toast.LENGTH_SHORT).show();
                                        Toast.makeText(getApplicationContext(), "Your Target Amount Should be more than previous", Toast.LENGTH_LONG).show();
                                        Toast.makeText(getApplicationContext(), "Your Target Date Should Be Greater Than Current Date", Toast.LENGTH_LONG).show();
                                    }
                                } else {
                                    Toast.makeText(getApplicationContext(), "Please fill each field", Toast.LENGTH_LONG).show();
                                }

                            }
                        });
                    }
                });

// End counter setting


            }

        });
// bank display method ends here...
        //Each bank display
        tddisplay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final AlertDialog.Builder mBuilder = new AlertDialog.Builder(MainScreen.this);
                LinearLayout ll = new LinearLayout(v.getContext());

                ListView lv = new ListView(MainScreen.this);
                ll.setBackgroundColor(Color.WHITE);
                TextView textView = new TextView(MainScreen.this);
                textView.setText("TD Account Detais");
                textView.setTextColor(Color.BLUE);
                textView.setGravity(Gravity.CENTER_HORIZONTAL);
                lv.addHeaderView(textView);
                lv.setBackgroundColor(Color.YELLOW);
                ll.addView(lv, LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);

                myDb = new Database(MainScreen.this);
                Cursor res = myDb.getAllAccountData();
                ID_Array = new ArrayList<String>();
                NAME_Array = new ArrayList<String>();
                ID_Array.clear();
                NAME_Array.clear();
                if (res.moveToFirst()) {
                    do {

                        NAME_Array.add("$ " + res.getString(2));
                        Long s = res.getLong(6);
                        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
                        ID_Array.add((dateFormat.format(s)));

                    } while (res.moveToNext());

                }

                ListAdapter = new SQLiteListAdapter(MainScreen.this,

                        ID_Array,
                        NAME_Array
                );

                lv.setAdapter(ListAdapter);

                res.close();
                mBuilder.setView(ll);
                final AlertDialog dialog = mBuilder.create();
                dialog.show();

            }
        });

        // Dynamic heading setting
        headingtext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final AlertDialog.Builder mBuilder = new AlertDialog.Builder(MainScreen.this);
                final View mView = getLayoutInflater().inflate(R.layout.mottoedition, null);
                final EditText newText = (EditText) mView.findViewById(R.id.editTxt);

                Button updateText = (Button) mView.findViewById(R.id.submittxt);
                mBuilder.setView(mView);
                final AlertDialog dialog = mBuilder.create();
                dialog.show();
                updateText.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {


                        if (!newText.getText().toString().isEmpty()) {
                            dialog.show();

                            String currentTxt = newText.getText().toString();
                            SharedPreferences.Editor editor = getPreferences(MODE_PRIVATE).edit();
                            editor.putString("text", currentTxt);
                            editor.apply();
                            SharedPreferences prefs = getPreferences(MODE_PRIVATE);
                            warningtxt.setText(prefs.getString("text", "Enter Motto"));
                            Animation myFadeInAnimation = AnimationUtils.loadAnimation(MainScreen.this, R.anim.move);
                            warningtxt.startAnimation(myFadeInAnimation);
                            dialog.dismiss();

                        } else {
                            Toast.makeText(MainScreen.this, "please fill the Text", Toast.LENGTH_LONG).show();
                            dialog.dismiss();
                        }
                    }
                });

            }
        });

        cibcdisplay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final AlertDialog.Builder mBuilder = new AlertDialog.Builder(MainScreen.this);
                LinearLayout ll = new LinearLayout(v.getContext());
                AlertDialog.Builder builder = new AlertDialog.Builder(v.getContext());
                builder.setView(ll);

                ListView lv = new ListView(MainScreen.this);
                ll.setBackgroundColor(Color.WHITE);
                TextView textView = new TextView(MainScreen.this);
                textView.setText("CIBC Account Detais");
                textView.setTextColor(Color.BLUE);
                textView.setGravity(Gravity.CENTER_HORIZONTAL);
                lv.addHeaderView(textView);
                lv.setBackgroundColor(Color.YELLOW);
                ll.addView(lv, LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);

                myDb = new Database(MainScreen.this);
                Cursor res = myDb.getAllAccountData();
                ID_Array = new ArrayList<String>();
                NAME_Array = new ArrayList<String>();
                ID_Array.clear();
                NAME_Array.clear();
                if (res.moveToFirst()) {
                    do {

                        NAME_Array.add("$ " + res.getString(1));
                        Long s = res.getLong(6);
                        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
                        ID_Array.add((dateFormat.format(s)));

                    } while (res.moveToNext());

                }
                ListAdapter = new SQLiteListAdapter(MainScreen.this,

                        ID_Array,
                        NAME_Array
                );

                lv.setAdapter(ListAdapter);

                res.close();
                mBuilder.setView(ll);
                final AlertDialog dialog = mBuilder.create();
                dialog.show();

                //   }

            }
        });
        scotiadispaly.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final AlertDialog.Builder mBuilder = new AlertDialog.Builder(MainScreen.this);
                LinearLayout ll = new LinearLayout(v.getContext());
                AlertDialog.Builder builder = new AlertDialog.Builder(v.getContext());
                builder.setView(ll);

                ListView lv = new ListView(MainScreen.this);
                ll.setBackgroundColor(Color.WHITE);
                TextView textView = new TextView(MainScreen.this);
                textView.setText("Scotia Account Detais");
                textView.setTextColor(Color.BLUE);
                textView.setGravity(Gravity.CENTER_HORIZONTAL);
                lv.addHeaderView(textView);
                lv.setBackgroundColor(Color.YELLOW);
                ll.addView(lv, LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
                myDb = new Database(MainScreen.this);
                Cursor res = myDb.getAllAccountData();
                ID_Array = new ArrayList<String>();
                NAME_Array = new ArrayList<String>();
                ID_Array.clear();
                NAME_Array.clear();
                if (res.moveToFirst()) {
                    do {

                        NAME_Array.add("$ " + res.getString(3));
                        Long s = res.getLong(6);
                        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
                        ID_Array.add((dateFormat.format(s)));

                    } while (res.moveToNext());

                }
                ListAdapter = new SQLiteListAdapter(MainScreen.this,

                        ID_Array,
                        NAME_Array
                );

                lv.setAdapter(ListAdapter);

                res.close();
                mBuilder.setView(ll);
                final AlertDialog dialog = mBuilder.create();
                dialog.show();

                //   }

            }
        });
        americandisplay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final AlertDialog.Builder mBuilder = new AlertDialog.Builder(MainScreen.this);
                LinearLayout ll = new LinearLayout(v.getContext());
                AlertDialog.Builder builder = new AlertDialog.Builder(v.getContext());
                builder.setView(ll);

                ListView lv = new ListView(MainScreen.this);
                ll.setBackgroundColor(Color.WHITE);
                TextView textView = new TextView(MainScreen.this);
                textView.setText("Other Account Detais");
                textView.setTextColor(Color.BLUE);
                textView.setGravity(Gravity.CENTER_HORIZONTAL);
                lv.addHeaderView(textView);
                lv.setBackgroundColor(Color.YELLOW);
                ll.addView(lv, LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);

                myDb = new Database(MainScreen.this);
                Cursor res = myDb.getAllAccountData();
                ID_Array = new ArrayList<String>();
                NAME_Array = new ArrayList<String>();
                ID_Array.clear();
                NAME_Array.clear();
                if (res.moveToFirst()) {
                    do {

                        NAME_Array.add("$ " + res.getString(4));
                        Long s = res.getLong(6);
                        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
                        ID_Array.add((dateFormat.format(s)));

                    } while (res.moveToNext());

                }
                ListAdapter = new SQLiteListAdapter(MainScreen.this,

                        ID_Array,
                        NAME_Array
                );

                lv.setAdapter(ListAdapter);

                res.close();
                mBuilder.setView(ll);
                final AlertDialog dialog = mBuilder.create();
                dialog.show();

                //   }

            }
        });
        //Each bank display ends

//  List for the Expeses
        listExpense.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final AlertDialog.Builder mBuilder = new AlertDialog.Builder(MainScreen.this);
                LinearLayout ll = new LinearLayout(v.getContext());

                ListView lv = new ListView(MainScreen.this);
                ll.setBackgroundColor(Color.WHITE);
                TextView textView = new TextView(MainScreen.this);
                textView.setText("Expenses Overview");
                textView.setTextColor(Color.BLUE);
                textView.setGravity(Gravity.CENTER_HORIZONTAL);
                lv.addHeaderView(textView);
                lv.setBackgroundColor(Color.YELLOW);
                ll.addView(lv, LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);

                myDb = new Database(MainScreen.this);
                Cursor res = myDb.getAllExpenseData();
                ID_Array = new ArrayList<String>();
                NAME_Array = new ArrayList<String>();
                ITEM_Array = new ArrayList<String>();
                ID_Array.clear();
                ITEM_Array.clear();
                NAME_Array.clear();
                if (res.moveToFirst()) {
                    do {

                        NAME_Array.add("$ " + res.getString(1));
                        ITEM_Array.add(res.getString(4));
                        Long s = res.getLong(3);
                        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
                        ID_Array.add((dateFormat.format(s)));

                    } while (res.moveToNext());

                }

                ExpenseAdapter = new SQLiteExpenseAdapter(MainScreen.this,

                        ID_Array,
                        NAME_Array,
                        ITEM_Array
                );

                lv.setAdapter(ExpenseAdapter);

                res.close();
                mBuilder.setView(ll);
                final AlertDialog dialog = mBuilder.create();
                dialog.show();

            }
        });
// List for expenses ends

        //  Creating alert dialog for Expense graph
        graphs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                SharedPreferences prefs = getSharedPreferences("agent", MODE_PRIVATE);
//                int value = prefs.getInt("totalamts", 0);
//                if (value == 0) {
//                    Toast.makeText(getApplicationContext(), "Please enter first data", Toast.LENGTH_LONG).show();
//                } else {
//


                    graphs.setBackgroundColor(Color.argb(255, 100, 10, 200));
                    final AlertDialog.Builder mBuilder = new AlertDialog.Builder(MainScreen.this);
                    final View mView = getLayoutInflater().inflate(R.layout.graphexpense, null);
                    Cursor res = myDb.getAllExpenseData();
                    TextView recentdate = (TextView) mView.findViewById(R.id.recentolddateexpense);
                    TextView recentamount = (TextView) mView.findViewById(R.id.recentoldamountexpense);
                    final EditText expenseItem = (EditText) mView.findViewById(R.id.expenseitem);
                    final EditText expenseAmount = (EditText) mView.findViewById(R.id.expenseamount);

                    Button updateamounts = (Button) mView.findViewById(R.id.updateexpenseamount);
                    Button deleteeamounts = (Button) mView.findViewById(R.id.deleteexpenseamount);
                    if (res.getCount() <= 0) {
                        res.close();
                    } else {
                        res.moveToLast();
                        int finamAmount = res.getInt(4);
                        Long s = res.getLong(2);
                        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
                        recentdate.setText(dateFormat.format(s));
                        recentamount.setText(String.valueOf(finamAmount));
                        res.close();
                    }
                    mBuilder.setView(mView);
                    final AlertDialog dialog = mBuilder.create();
                    dialog.show();
                    updateamounts.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                            Cursor res = myDb.getAllExpenseData();
                            if (!expenseItem.getText().toString().isEmpty() && !expenseAmount.getText().toString().isEmpty()) {
                                dialog.show();
                                if (res.getCount() > 0) {
                                    res.moveToLast();
                                    int ss = res.getInt(4);
                                    totalexpenseamount = ss;
                                    res.close();
                                } else {
                                    totalexpenseamount = 0;
                                }
                                String item = expenseItem.getText().toString();
                                String amount = expenseAmount.getText().toString();
                                long date = new Date().getTime();
                                eachexpenseamount = Integer.parseInt(amount);

                                total = totalexpenseamount + eachexpenseamount;

                                boolean isinserted = myDb.insertExpenseData(eachexpenseamount, total, item, date);
                                res.close();
                                if (isinserted = true) {
                                    SharedPreferences.Editor editor = getSharedPreferences("agent", MODE_PRIVATE).edit();
                                    editor.putInt("totalamts", eachexpenseamount);

                                    editor.apply();
                                    Toast.makeText(MainScreen.this, "successfuly inserted", Toast.LENGTH_LONG).show();

                                } else {
                                    Toast.makeText(MainScreen.this, "not inserted ", Toast.LENGTH_LONG).show();
                                }

                                dialog.dismiss();
                                recreate();


                            } else {
                                Toast.makeText(MainScreen.this, "please fill each field", Toast.LENGTH_LONG).show();
                            }

                        }
                    });
//
//                    deleteeamount.setOnClickListener(new View.OnClickListener() {
//                        @Override
//                        public void onClick(View v) {
//
//                            myDb = new Database(MainScreen.this);
//                            Cursor res = myDb.getAllAccountData();
//                            if (res.moveToLast()) {
//                                if (res.getCount() > 1) {
//                                    int index = res.getInt(0);
//                                    myDb.deleteRow(index);
//                                    res.close();
//                                    // dialog.show();
//                                    dialog.dismiss();
//                                    recreate();
//                                } else {
//                                    Toast.makeText(MainScreen.this, "No Data Left In  Your Account", Toast.LENGTH_LONG).show();
//                                    int index = res.getInt(0);
//                                    myDb.deleteRow(index);
//                                    res.close();
//                                    // dialog.show();
//                                    dialog.dismiss();
//                                    recreate();
//                                    SharedPreferences preffs = getPreferences(MODE_PRIVATE);
//                                    preffs.edit().remove("finalamt").commit();
//                                }
//                            } else {
//                                Toast.makeText(MainScreen.this, "No Data Left In  Your Account", Toast.LENGTH_LONG).show();
//
//                                SharedPreferences preffs = getPreferences(MODE_PRIVATE);
//                                preffs.edit().remove("finalamt").commit();
//                            }
//                        }
//                    });

            }
//            }
        });
// bExpense Graph method ends here...

    }

    private DataPoint[] getDataPoint() {
        Cursor res = myDb.getAllAccountData();

        DataPoint[] dp = new DataPoint[res.getCount()];
        for (int i = 0; i < res.getCount(); i++) {
            res.moveToNext();
            dp[i] = new DataPoint(res.getLong(6), res.getInt(5));
        }
        return dp;
    }

    private DataPoint[] getDataPoints() {
        Cursor res = myDb.getAllExpenseData();

        DataPoint[] dp = new DataPoint[res.getCount()];
        for (int i = 0; i < res.getCount(); i++) {
            res.moveToNext();
            dp[i] = new DataPoint(res.getLong(3), res.getInt(2));
        }
        return dp;
    }
}




