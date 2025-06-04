package com.example;

import org.json.JSONObject;

import java.net.HttpURLConnection;
import java.net.URL;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.Temporal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

import org.json.JSONArray;

import static java.time.temporal.ChronoUnit.DAYS;


public class TestJson {
    public static void main(String[] args) {


        try {
            //set up URL
            URL url = new URL("https://api.nbp.pl/api/exchangerates/rates/A/USD/2025-04-01/?format=json");
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            con.setRequestMethod("GET");

            //read the response
            BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
            String inputLine;
            StringBuilder content = new StringBuilder();
            while ((inputLine = in.readLine()) != null) {
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
//            JSONArray outerArray = new JSONArray(content.toString());
//            JSONObject obj = outerArray.getJSONObject(0);
//
//            JSONArray rates = obj.getJSONArray("rates");
//
//            System.out.println(rates + "\n");
//            JSONObject rateObj = rates.getJSONObject(1);
//            System.out.println(rateObj + "\n");
//
//            System.out.println(rateObj.get("code"));
//            System.out.println(rateObj.get("currency"));
//            System.out.println(rateObj.get("mid"));
//            System.out.println(obj.getString("effectiveDate"));
//
//            for(int i = 0; i < rates.length(); i++){
//                JSONObject rateObj1 = rates.getJSONObject(i);
//                if(rateObj1.getString("code").equals("USD")){
//                    show(rateObj1);
//                    break;
//                }
//            }

//            formaty api
//            https://api.nbp.pl/api/exchangerates/tables/{table}/{date}/                              z konkretnego dnia
//            https://api.nbp.pl/api/exchangerates/rates/A/USD/2025-04-01/?format=json

//            https://api.nbp.pl/api/exchangerates/tables/{table}/{startDate}/{endDate}/               Seria tabel kursów opublikowanych w zakresie dat od {startDate} do {endDate}
//            https://api.nbp.pl/api/exchangerates/tables/A/2025-03-01/2025-05-01/?format=json


            String date1 = "2025-03-01";
            String date2 = "2025-05-02";
            String link = String.format("https://api.nbp.pl/api/exchangerates/tables/A/2025-03-01/2025-05-01/?format=json", date1, date2);
            getCurrencyRate(link);
            System.out.println(getCurrencyRate(link));

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void show(JSONObject obj) {
        System.out.println(obj.get("code"));
        System.out.println(obj.get("currency"));
        System.out.println(obj.get("mid"));
    }

    public static HashMap<String, Double> getCurrencyRate(String linkToApi) {
        HashMap<String, Double> map = new HashMap<>();

        StringBuilder content = new StringBuilder();

        try {
            URL url = new URL(linkToApi);
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            con.setRequestMethod("GET");

            //read the response
            BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
            String inputLine;
            while ((inputLine = in.readLine()) != null) {
                content.append(inputLine);
            }
            in.close();
            con.disconnect();
        } catch (Exception e) {
            e.printStackTrace();
        }

        //write here types of handling data from API based on link user provided

        //Single date
        String jsonString = content.toString().trim();
        if (jsonString.startsWith("{")) {

            JSONObject outerArray = new JSONObject(content.toString());
            JSONArray innerArray = outerArray.getJSONArray("rates");
            JSONObject obj = innerArray.getJSONObject(0);

            String s = outerArray.get("code").toString() + "-" + obj.get("effectiveDate").toString();
            map.put(s, obj.getDouble("mid"));
            return map;
        }

        //Date to date
        else {
            //User must choose which currency to show
            String chosenCurrency = "USD";
            JSONArray outerArray1 = new JSONArray(content.toString());

            for (int i = 0; i < outerArray1.length(); i++) {
                JSONObject obj1 = outerArray1.getJSONObject(i);
                JSONArray innerArray1 = obj1.getJSONArray("rates");

                for (int j = 0; j < innerArray1.length(); j++) {
                    JSONObject rates1 = innerArray1.getJSONObject(j);

                    if (rates1.getString("code").equals(chosenCurrency)) {

                        String s = rates1.getString("code") + "-" + obj1.getString("effectiveDate");
                        map.put(s, rates1.getDouble("mid"));
                    }

                }
            }
            return map;
        }
    }

}
