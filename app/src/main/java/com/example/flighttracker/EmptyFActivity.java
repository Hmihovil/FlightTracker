package com.example.flighttracker;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class EmptyFActivity extends AppCompatActivity {  //EMPTY ACTIVITY CLASS---------------------------------------------------------------------------

    private Button backButton, saveButton, delButton;
    private FlightMain.DatabaseHelper db;
    private ListView fragList;
    private FlightMain.FlightFragListAdapter fragAdapter;
    private Flight flight;
    private List<Flight> flights = new ArrayList<>();
    private FlightFragment fFragment;
    private int itemPosition = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.frag_flight);

        if(db == null) {

            db = new FlightMain.DatabaseHelper(this, null, null, 1);
        }

        final Bundle dataToPass = getIntent().getExtras();

        fFragment = new FlightFragment();

        if(dataToPass != null) {
            fFragment.setArguments(dataToPass);
            getSupportFragmentManager()
                    .beginTransaction()
                    .add(R.id.frag_flightFrame, fFragment)
                    .addToBackStack("Flight")
                    .commit();
        }

        fragList = (ListView)findViewById(R.id.frag_listView);
        backButton = (Button)findViewById(R.id.frag_backButton);
        saveButton = (Button)findViewById(R.id.frag_buttonSave);
        delButton = (Button)findViewById(R.id.frag_buttonDel);



        backButton.setOnClickListener(new View.OnClickListener() {  //Back Button------------
            @Override
            public void onClick(View v) {

                Intent back = new Intent(EmptyFActivity.this, FlightMain.class);
                flights.clear();
                startActivity(back);
            }
        });

        saveButton.setOnClickListener(new View.OnClickListener() {  //Save Button-------------
            @Override
            public void onClick(View v) {

                flight = new Flight();

                try {

                    flight.setDeparture(dataToPass.getString("departure"));
                    flight.setArrival(dataToPass.getString("arrival"));
                    flight.setSpeed(dataToPass.getString("speed"));
                    flight.setAltitude(dataToPass.getString("altitude"));
                    flight.setStatus(dataToPass.getString("status"));

                    db.insertFlight(dataToPass.getString("departure"),
                            dataToPass.getString("arrival"),
                            dataToPass.getString("speed"),
                            dataToPass.getString("altitude"),
                            dataToPass.getString("status"));

                    flights.add(flight);
                    fragList.setAdapter(fragAdapter);
                    fragAdapter.notifyDataSetChanged();

                }catch(Exception e){
                    Log.e("Already Saved", " error");
                }
            }
        });

        fragList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Bundle fragBundle = new Bundle();

                fFragment = new FlightFragment();

                fragBundle.putString("departure", flights.get(position).getDeparture());
                fragBundle.putString("arrival", flights.get(position).getArrival());
                fragBundle.putString("speed", flights.get(position).getSpeed());
                fragBundle.putString("altitude", flights.get(position).getAltitude());
                fragBundle.putString("status", flights.get(position).getStatus());

                fFragment.setArguments(fragBundle);
                getSupportFragmentManager()
                        .beginTransaction()
                        .add(R.id.frag_flightFrame, fFragment)
                        .addToBackStack("Flight")
                        .commit();

                itemPosition = position + 1;
                Log.e("Position of Item ", "" + itemPosition);
            }
        });

        delButton.setOnClickListener(new View.OnClickListener() {   //Delete Button-----------
            @Override
            public void onClick(View v) {

                Snackbar.make(findViewById(R.id.frag_toolbar),
                        "Confirm Delete: ", Snackbar.LENGTH_LONG)
                        .setAction("Confirm", new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {

                                long tempID = flights.get(itemPosition -1).getId();

                                db.open();
                                try{

                                    db.deleteID(tempID);
                                    db.close();
                                    if(flights!= null) {
                                        flights.remove(itemPosition - 1);
                                    }
                                    fragList.setAdapter(fragAdapter);
                                    fragAdapter.notifyDataSetChanged();
                                }catch(NullPointerException e){

                                    e.printStackTrace();
                                    Toast.makeText(getApplicationContext(), "Please select item to delete", Toast.LENGTH_SHORT).show();
                                }

                            }
                        }).show();
            }
        });

        db.open();
        Cursor c = db.getFlights();

        for (c.moveToFirst(); !c.isAfterLast(); c.moveToNext()) {

            flights.add(new Flight(c.getLong(c.getColumnIndex(db.KEY_ROW_ID)),
                    c.getString(c.getColumnIndex(db.KEY_ROW_DEP)),
                    c.getString(c.getColumnIndex(db.KEY_ROW_ARR)),
                    c.getString(c.getColumnIndex(db.KEY_ROW_SPEED)),
                    c.getString(c.getColumnIndex(db.KEY_ROW_ALT)),
                    c.getString(c.getColumnIndex(db.KEY_ROW_STATUS))));

        }
        db.close();

        fragAdapter = new FlightMain.FlightFragListAdapter(this, flights);
        fragList.setAdapter(fragAdapter);
        fragAdapter.notifyDataSetChanged();

    }
}