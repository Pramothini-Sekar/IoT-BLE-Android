package com.example.researcher.iot_ble;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;

import java.util.ArrayList;
import java.util.List;

import cz.msebera.android.httpclient.Header;

public class OfferActivity extends AppCompatActivity {
    List<String> list = new ArrayList<String>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_offer);
        Intent i =getIntent();
        String MACAddr = i.getStringExtra("MACAddr");
        Toast.makeText(OfferActivity.this,MACAddr,Toast.LENGTH_LONG).show();

        ListView l = (ListView)findViewById(R.id.list);
        final TextView offerId = (TextView)findViewById(R.id.offerId);

        AsyncHttpClient httpClient = new AsyncHttpClient();
        httpClient.get("https://harsha555.pythonanywhere.com/iot_ggwp?id=" + MACAddr, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {

                if(responseBody!=null) {

                    String response = new String(responseBody);
                    offerId.setText(response);
                    /*
                    ArrayList<OfferJson> offerJsonArrayList = new ArrayList<>();

                    try {
                        JSONObject jsonObject = new JSONObject(response);
                        JSONArray jsonArray = jsonObject.getJSONArray("Offers");
                        for(int jsonLength = 0; jsonLength<jsonArray.length(); jsonLength++){
                            JSONObject offerList = jsonArray.getJSONObject(jsonLength);
                            String itemName = offerList.getString("item");
                            String discountVal = offerList.getString("discount");
                            OfferJson offerJson = new OfferJson();
                            offerJson.setItem(itemName +" : "+itemName);
                            offerJson.setDiscount(discountVal);
                            offerJsonArrayList.add(offerJson);
                            list.add(itemName+ ":" +discountVal);
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }


                    Toast.makeText(OfferActivity.this,offerJsonArrayList.get(0).toString(),Toast.LENGTH_LONG).show();
                    */
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
