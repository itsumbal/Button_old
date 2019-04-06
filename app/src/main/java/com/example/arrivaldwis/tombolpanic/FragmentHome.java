package com.example.arrivaldwis.tombolpanic;

import android.content.Context;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.telephony.SmsManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;

public class FragmentHome extends Fragment implements LocationListener {
    private MainActivity ma;
    ImageButton btnSOS;
    SQLiteHelper sqLiteHelper;
    ArrayList<HashMap<String,String>> itemWhitelists;
    double lat;
    double lng;
    long minTime;
    float minDistance;
    String locProvider;
    LocationManager locMgr;
    String message="";
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        sqLiteHelper = new SQLiteHelper(getActivity());
        itemWhitelists = sqLiteHelper.getAllKontak();
        View view = inflater.inflate(R.layout.fragment_main, container, false);
        btnSOS = (ImageButton) view.findViewById(R.id.btnSOS);
        btnSOS.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (itemWhitelists.size() != 0) {
                    for(int i=0; i<itemWhitelists.size(); i++) {
                        HashMap<String, String> hashmapData = itemWhitelists.get(i);
                        String nama = hashmapData.get("nama");
                        String notelp = hashmapData.get("notelp");
                        getLokasi(nama,notelp);
                    }
                } else {
                    Toast.makeText(getActivity().getApplicationContext(), "You don't have added any contacts yet.",
                            Toast.LENGTH_LONG).show();
                }
                Toast.makeText(getActivity(),"Message sent to the contact list.",Toast.LENGTH_LONG).show();
            }
        });
        return view;
    }

    private void getLokasi(String nama, String phoneNumber){

        ConnectivityManager cm = (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo ni = cm.getActiveNetworkInfo();
        if (ni == null) {
            message = "I'm in Emergency. Please help me! \n\nADDRESS:  null";
        } else {
            locMgr = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);
            locProvider = LocationManager.NETWORK_PROVIDER;
            Location lastKnownLocation = locMgr.getLastKnownLocation(locProvider);
            lat = lastKnownLocation.getLatitude();
            lng = lastKnownLocation.getLongitude();

            Criteria cr = new Criteria();
            cr.setAccuracy(Criteria.ACCURACY_FINE);

            locProvider = locMgr.getBestProvider(cr, false);
            minTime = 1 * 60 * 1000;
            minDistance = 1;

            String googleUrl = "http://maps.google.com/?q="+lat+","+lng;
            message = "I'm in Emergency please help me! \n\nADDRESS:  " + googleUrl;
        }

        sendSMS(nama,phoneNumber,message);
    }

    private void sendSMS(String nama, String phoneNumber, String message)
    {
        String nm = nama;
        String phoneNo = phoneNumber;
        String msg = message;

        try {
            SmsManager smsManager = SmsManager.getDefault();
            smsManager.sendTextMessage(phoneNo, null, msg, null, null);
            Toast.makeText(getActivity().getApplicationContext(), "Message to "+nm+" Sent",
                    Toast.LENGTH_LONG).show();
        } catch (Exception ex) {
            Toast.makeText(getActivity().getApplicationContext(),
                    ex.getMessage().toString(),
                    Toast.LENGTH_LONG).show();
            ex.printStackTrace();
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        // setUserVisibleHint(true);
    }

    @Override
    public void onLocationChanged(Location location) {
        lat = location.getLatitude();
        lng = location.getLongitude();
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }
}


