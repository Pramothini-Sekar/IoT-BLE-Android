package com.example.researcher.iot_ble;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Iterator;

import cz.msebera.android.httpclient.Header;

public class OfferActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_offer);
        Intent i =getIntent();
        String MACAddr = i.getStringExtra("MACAddr");
        final TextView offerId = (TextView)findViewById(R.id.offerId);
        final TextView showText = (TextView)findViewById(R.id.showText);

        AsyncHttpClient httpClient = new AsyncHttpClient();
        httpClient.get("https://harsha555.pythonanywhere.com/iot_ggwp?id=" + MACAddr, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                if(responseBody!=null) {
                    String response = new String(responseBody);
                    offerId.setText(response);
                    try {
                        JSONObject jsonObject = new JSONObject(response);
                        JSONArray offerArray = jsonObject.getJSONArray("Offers");

                        if(offerArray.length()>0){
                            for(int i=0;i<offerArray.length();i++){
                                JSONObject eachOffer = offerArray.getJSONObject(i);
                                Iterator<String> stringKeys = eachOffer.keys();
                                /*
                                HashMap<String,String> offerList = new HashMap<>();
                                while(stringKeys.hasNext()){
                                    offerList.put(stringKeys.next(),eachOffer.getString(stringKeys.next()));
                                }
                                Set<String> keys = offerList.keySet();
                                for(String key: keys){
                                    Log.i("Hash Val",offerList.get(key));
                                }
                                */
                                showText.setText(stringKeys.next());
                                Log.i("Iter",stringKeys.next());
                            }
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }

            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                Toast.makeText(OfferActivity.this,"Failure",Toast.LENGTH_LONG).show();
                offerId.setText("Failure");

            }
        });






    }
}
