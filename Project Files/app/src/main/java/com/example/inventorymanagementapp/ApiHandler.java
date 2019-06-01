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
                makeJsonArrayRequest(list_url);
                break;
            case "view_product":
                break;
            case "new_product":
                break;
            case "update_product":
                break;
            case "delete_product":
                break;
            case "add_remove_unit":
                break;
            default:
                System.out.println("no match");
        }

        return null;
    }

    private void makeJsonArrayRequest(String url) {

        JsonArrayRequest req = new JsonArrayRequest(url,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        Log.d("jsonarrayreq", response.toString());

                        LinearLayout tl = ((Activity)context).findViewById(R.id.all_products_table_layout);
                        tl.removeAllViews();
                        View tablerow = null;
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
                                //String place = product.getString("place");

                                /*String priceBuy = product.getString("priceBuy");
                                String priceSell = product.getString("priceSell");
                                String modelno = product.getString("modelNo");
                                String description = product.getString("description");*/

                                //JSONObject product_log = product.getJSONObject("log");

                                // Create table row for each product, add it to the list
                                tablerow = View.inflate(context, R.layout.product_list_row, null);
                                product_name_txt = tablerow.findViewById(R.id.product_name);
                                card_no_txt= tablerow.findViewById(R.id.card_no);
                                place_txt = tablerow.findViewById(R.id.place);
                                quantity_txt = tablerow.findViewById(R.id.quantity);

                                // initialize text views
                                product_name_txt.setText(name);
                                card_no_txt.setText(cardNo);
                                place_txt.setText("place");     //TODO change "place" with response API
                                quantity_txt.setText(quantity);

                                // table row listener
                                tablerow.setTag(i);
                                tablerow.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        Intent intent = new Intent(context,ProductView.class);
                                        intent.putExtra("clicked_item",cardNo);
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
        }else if (this.type.equals("list_products")){
            Log.v("List Products",response.toString());
            if(response != null){
                Toast.makeText(context, response.toString(), Toast.LENGTH_LONG).show();
            }
            else{
                Toast.makeText(context, "List Products Failed!", Toast.LENGTH_SHORT).show();
            }
        }

    }
}