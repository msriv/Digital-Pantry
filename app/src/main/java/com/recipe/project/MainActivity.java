package com.recipe.project;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MainActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    RecAdapter rAdapter;
    List<Recipe> rList = new ArrayList<Recipe>();
    String f_url;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Button bt = findViewById(R.id.go);

        recyclerView = findViewById(R.id.list_rec);

        String baseurl = "https://mighty-waters-29256.herokuapp.com/";
        Intent intent = getIntent();
        String str = intent.getStringExtra("str");
        f_url = baseurl + str;


        //recyclerView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
        rAdapter = new RecAdapter(rList, this);

        recyclerView.setAdapter(rAdapter);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        setData(f_url);
        //rAdapter.notifyDataSetChanged();

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

    }

    private void setData(String url) {

        final ProgressDialog pDialog = new ProgressDialog(this);

        pDialog.setMessage("Loading...");
        pDialog.show();

        StringRequest strReq = new StringRequest(Request.Method.GET,
                url, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                JSONObject result = null;
                JSONArray tokenList;


                try {
                    result = new JSONObject(response);

                    tokenList = result.getJSONArray("itemListElement");
                    Log.d("tokenList", tokenList.toString());
                    for (int i = 0; i < tokenList.length(); i++) {
                        Recipe r = new Recipe();
                        JSONObject temp;
                        JSONObject nut;
                        Nutrition nutrition = new Nutrition();

                        temp = tokenList.getJSONObject(i);
                        String img = temp.getString("image");
                        Pattern pattern = Pattern.compile("\"(.*?)\"");
                        Matcher matcher = pattern.matcher(img);
                        if (matcher.find()) {
                            r.setImg(matcher.group(1).replace("\\", ""));
                        }
                        r.setName(temp.getString("name"));
//                        r.setImg(temp.getString("image"));
                        try {
                            r.setDesc(temp.getString("description"));
                        } catch (Exception e) {
                            r.setDesc(null);
                        }
                        try {
                            nut = temp.getJSONObject("nutrition");
                            try {
                                nutrition.setCarbohydrateContent(nut.getString("carbohydrateContent"));
                            } catch (Exception e) {
                                nutrition.setCarbohydrateContent("");
                            }

                            try {
                                nutrition.setCholesterolContent(nut.getString("cholesterolContent"));
                            } catch (Exception e) {
                                nutrition.setCholesterolContent("");
                            }
                            try {
                                nutrition.setFatContent(nut.getString("fatContent"));
                            } catch (Exception e) {
                                nutrition.setFatContent("");
                            }
                            try {
                                nutrition.setFiberContent(nut.getString("fiberContent"));
                            } catch (Exception e) {
                                nutrition.setFiberContent("");
                            }
                            try {
                                nutrition.setProteinContent(nut.getString("proteinContent"));
                            } catch (Exception e) {
                                nutrition.setProteinContent("");
                            }
                            try {
                                nutrition.setSaturatedFatContent(nut.getString("saturatedFatContent"));
                            } catch (Exception e) {
                                nutrition.setSaturatedFatContent("");
                            }

                            try {
                                nutrition.setSodiumContent(nut.getString("sodiumContent"));
                            } catch (Exception e) {
                                nutrition.setSodiumContent("");
                            }

                            try {
                                nutrition.setSugarContent(nut.getString("sugarContent"));
                            } catch (Exception e) {
                                nutrition.setSugarContent("");
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                        r.setNutrition(nutrition);


                        JSONObject res = temp.getJSONObject("aggregateRating");
                        r.setRating(res.getInt("ratingValue"));


                        try {
                            JSONArray arrJson = temp.getJSONArray("recipeIngredient");
                            String[] arr = new String[arrJson.length()];
                            for (int j = 0; j < arrJson.length(); j++)
                                arr[j] = arrJson.getString(j);

                            r.setIngridient(arr);
                        } catch (Exception e) {
                            r.setIngridient(null);
                        }

                        try {
                            r.setUrl(temp.getString("url"));
                        } catch (Exception e) {
                            r.setUrl(null);
                        }

                        rList.add(r);

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                } finally {
                    rAdapter.notifyDataSetChanged();
                }
                pDialog.dismiss();

            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d("tis: ", "Error: " + error.getMessage());
                pDialog.dismiss();
            }
        });

        RequestQueue queue = Volley.newRequestQueue(this);
        queue.add(strReq);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        if (id == R.id.action_about) {
            startActivity(new Intent(MainActivity.this, AboutUs.class));
            return true;
        }
        if (item.getItemId() == android.R.id.home) {
            Intent intent = new Intent(MainActivity.this, Home.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
            // close this activity and return to preview activity (if there is any)
        }

        return super.onOptionsItemSelected(item);
    }
}
