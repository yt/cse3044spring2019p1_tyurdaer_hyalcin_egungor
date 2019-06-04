package com.example.inventorymanagementapp;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;
import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.IOError;
import java.util.HashMap;
import java.util.Map;

public class ApiHandler extends AsyncTask<String,String,JSONObject> {
    Context context;
    String email;
    String type;
    String card_no;
    final String url = "https://9gjgvaty2l.execute-api.eu-central-1.amazonaws.com/dev";

    public ApiHandler(Context ctx){
        context = ctx;
    }

    @Override
    protected JSONObject doInBackground(String... params) {
        this.type = params[0];
        switch(type)
        {
            case "login":
                try {
                    this.email = params[1];
                    String login_url = url + "/person/auth";
                    JSONObject jsonBody = new JSONObject();
                    jsonBody.put("email", params[1]);
                    jsonBody.put("password", params[2]);
                    httpPost(login_url,jsonBody);
                }catch (IOError e){
                    e.printStackTrace();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                break;

            case "register":
                try {
                String register_url = url + "/person";
                JSONObject jsonBody = new JSONObject();
                jsonBody.put("email", params[1]);
                jsonBody.put("password", params[2]);
                jsonBody.put("name", params[3]);
                jsonBody.put("surname", params[4]);
                httpPost(register_url,jsonBody);
                }catch (IOError e){
                    e.printStackTrace();
                }catch (JSONException e) {
                e.printStackTrace();
                }
                break;

            case "list_products":
                this.email = params[1];
                String list_url = url + "/product?email=" + email;
                list_products(list_url);
                break;
            case "view_product":
                this.email = params[1];
                this.card_no = params[2];
                String view_url = url + "/product?cardno=" + this.card_no;
                httpGet(view_url);
                break;
            case "new_product":
                try {
                    this.email = params[1];
                    this.card_no = params[3];
                    String new_product_url = url + "/product";
                    JSONObject jsonBody = new JSONObject();
                    jsonBody.put("username", params[1]);
                    jsonBody.put("product_name", params[2]);
                    jsonBody.put("card_no", params[3]);
                    jsonBody.put("model_no", params[4]);
                    jsonBody.put("price_buy", params[5]);
                    jsonBody.put("price_sell", params[6]);
                    jsonBody.put("place", params[7]);
                    jsonBody.put("description", params[8]);
                    jsonBody.put("quantity", params[9]);
                    jsonBody.put("log", new JSONObject());
                    httpPost(new_product_url,jsonBody);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                break;
            case "update_product":
                break;
            case "delete_product":
                this.email = params[1];
                this.card_no = params[2];
                String delete_url = url + "/product?cardno=" + this.card_no;
                httpDelete(delete_url);
                break;
            case "add_remove_unit":
                break;
            default:
                System.out.println("no match");
        }

        return null;
    }

    private void list_products(String url) {

        JsonArrayRequest req = new JsonArrayRequest(url,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        Log.d("jsonarrayreq", response.toString());

                        LinearLayout tl = ((Activity)context).findViewById(R.id.all_products_table_layout);
                        tl.removeAllViews();
                        View tablerow;
                        TextView product_name_txt, card_no_txt, place_txt, quantity_txt;

                        try {
                            // Parsing json array response
                            // loop through each json object

                            for (int i = 0; i < response.length(); i++) {

                                // Get json object from response
                                JSONObject product = (JSONObject) response.get(i);

                                // Parse json object
                                String name = product.getString("modelName");
                                String quantity = product.getString("quantity");
                                final String cardNo = product.getString("cardNo");
                                final String username = product.getString("username");
                                //String place = product.getString("place");

                                // Create table row for each product, add it to the list
                                tablerow = View.inflate(context, R.layout.product_list_row, null);
                                product_name_txt = tablerow.findViewById(R.id.product_name);
                                card_no_txt= tablerow.findViewById(R.id.card_no);
                                place_txt = tablerow.findViewById(R.id.place);
                                quantity_txt = tablerow.findViewById(R.id.quantity);

                                // initialize text views
                                product_name_txt.setText(name);
                                card_no_txt.setText(cardNo);
                                place_txt.setText("place"); //TODO !!!
                                quantity_txt.setText(quantity);

                                // table row listener
                                tablerow.setTag(i);
                                tablerow.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        Intent intent = new Intent(context,ProductView.class);
                                        intent.putExtra("email",username);
                                        intent.putExtra("c_no",cardNo);
                                        context.startActivity(intent);
                                        ((Activity)context).finish();
                                    }
                                });

                                //add TableRows to Layout
                                tl.addView(tablerow);
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(context, "Error: " + e.getMessage(), Toast.LENGTH_LONG).show();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d("jsonarrayError", "Error: " + error.getMessage());
                Toast.makeText(context, error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

        // Adding request to request queue
        MySingleton.getmInstance(context).addToRequestQue(req);
    }

    private void httpGet(String url){
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                (Request.Method.GET, url, null, new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            Log.v("Get Success", response.toString());
                            processResponse(response);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("Get Error", error.toString());
                    }
                }) {
            @Override
            public Map<String, String> getHeaders() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("Content-Type", "application/json");
                return params;
            }

            @Override
            public String getBodyContentType(){
                return "application/json";
            }
        };
        MySingleton.getmInstance(context).addToRequestQue(jsonObjectRequest);
    }

    private void httpPost(String url, JSONObject jsonBody){
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                (Request.Method.POST, url, jsonBody, new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            Log.v("Post Success", response.toString());
                            processResponse(response);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("Post Error", error.toString());
                    }
                }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("Content-Type", "application/json");
                return params;
            }

            @Override
            public String getBodyContentType(){
                return "application/json";
            }
        };
        MySingleton.getmInstance(context).addToRequestQue(jsonObjectRequest);
    }

    private void httpDelete(String url){
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                (Request.Method.DELETE, url, null, new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            Log.v("Delete Success", response.toString());
                            processResponse(response);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("Delete Error", error.toString());
                    }
                }) {
            @Override
            public Map<String, String> getHeaders() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("Content-Type", "application/json");
                return params;
            }

            @Override
            public String getBodyContentType(){
                return "application/json";
            }
        };
        MySingleton.getmInstance(context).addToRequestQue(jsonObjectRequest);
    }

    private void processResponse(JSONObject response) throws JSONException {

        if (this.type.equals("login")){
            if (response.getString("result").equals("true")){
                Intent intent = new Intent(context, ListProducts.class);
                intent.putExtra("email",this.email);
                Toast.makeText(context, "Welcome " + this.email, Toast.LENGTH_SHORT).show();
                context.startActivity(intent);
                ((Activity)context).finish();
            }
            else{
                Toast.makeText(context, "Login Failed!", Toast.LENGTH_SHORT).show();
            }

        }else if (this.type.equals("register")){
            if (response.getString("result").equals("true")){
                Intent intent = new Intent(context, Login.class);
                Toast.makeText(context, "Register Success!", Toast.LENGTH_LONG).show();
                context.startActivity(intent);
                ((Activity)context).finish();
            }
            else{
                Toast.makeText(context, "Register Failed!", Toast.LENGTH_SHORT).show();
            }
        }else if (this.type.equals("new_product")){
            if (response.getString("result").equals("true")){
                Intent intent = new Intent(context, ProductView.class);
                intent.putExtra("c_no", this.card_no);
                intent.putExtra("email", this.email);
                Toast.makeText(context, "Product added!", Toast.LENGTH_SHORT).show();
                context.startActivity(intent);
                ((Activity)context).finish();
            }
            else {
                Toast.makeText(context, "This product already in list!", Toast.LENGTH_LONG).show();
            }
        }else if (this.type.equals("view_product")){
            try {
                String fail = response.getString("result");
                if (fail.equals("true") || fail.equals("false"))
                    Toast.makeText(context, "View product failed!", Toast.LENGTH_SHORT).show();
            }catch (JSONException e){
                ((ProductView)context).fill(response);
            }
        }else if(this.type.equals(("delete_product"))){
            if (response.getString("result").equals("true")){
                Toast.makeText(context, "Product Deleted!", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(context, ListProducts.class);
                intent.putExtra("email", this.email);
                context.startActivity(intent);
                ((Activity)context).finish();
            }else {
                Toast.makeText(context, "Product Delete Failed!", Toast.LENGTH_SHORT).show();
            }
        }

    }
}
