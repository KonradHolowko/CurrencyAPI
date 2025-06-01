package com.example;

import org.json.JSONObject;
import java.net.HttpURLConnection;
import java.net.URL;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;

import org.json.JSONObject;
import org.json.JSONArray;


public class TestJson {
    public static void main(String[] args) {


        try{
            //set up URL
            URL url = new URL("https://api.nbp.pl/api/exchangerates/tables/A/?format=json");
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            con.setRequestMethod("GET");

            //read the response
            BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
            String inputLine;
            StringBuilder content = new StringBuilder();
            while((inputLine = in.readLine()) != null){
                content.append(inputLine);
            }
            in.close();
            con.disconnect();

            //parse JSON
//            JSONObject obj = new JSONObject(content.toString());
//            JSONArray rates = obj.getJSONArray("rates");
//            JSONObject firstRate = rates.getJSONObject(0);
//
//            double rate = firstRate.getDouble("mid");
//            String date = firstRate.getString("effectiveDate");

//            System.out.println("EUR rate: " + rate);
//            System.out.println("Date: " + date);

            //parse more data from website -- konrad


            JSONArray outerArray = new JSONArray(content.toString());
            JSONObject obj = outerArray.getJSONObject(0);

            JSONArray rates = obj.getJSONArray("rates");

//            System.out.println(rates + "\n");
//            JSONObject rateObj = rates.getJSONObject(1);
//            System.out.println(rateObj + "\n");
//
//            System.out.println(rateObj.get("code"));
//            System.out.println(rateObj.get("currency"));
//            System.out.println(rateObj.get("mid"));
//            System.out.println(obj.getString("effectiveDate"));

            for(int i = 0; i < rates.length(); i++){
                JSONObject rateObj1 = rates.getJSONObject(i);
                if(rateObj1.getString("code").equals("USD")){
                    show(rateObj1);
                    break;
                }
            }

        }
        catch (Exception e){
            e.printStackTrace();
        }
    }

    public static void show(JSONObject obj){
        System.out.println(obj.get("code"));
        System.out.println(obj.get("currency"));
        System.out.println(obj.get("mid"));
    }
}
