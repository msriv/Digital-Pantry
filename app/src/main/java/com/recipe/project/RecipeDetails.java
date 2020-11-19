package com.recipe.project;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;

import static android.view.View.GONE;

public class RecipeDetails extends AppCompatActivity {
    Toolbar mToolbar;
    private TextView desc, url;
    private LinearLayout l_desc;
    List<String> iList = new ArrayList<String>();
    private RecyclerView rv, rv2;
    private IngAdapter mAdapter, vAdapter;
    private ImageView imv;
    List<String> nuts = new ArrayList<String>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_details);
        mToolbar = findViewById(R.id.main_toolbar);
        setSupportActionBar(mToolbar);
        Intent intent = getIntent();

        final Recipe recipe = getIntent().getParcelableExtra("Recipe");
        rv = findViewById(R.id.inRecyclerview);
        rv2 = findViewById(R.id.vitaminsRecyclerview);
        imv = findViewById(R.id.main_backdrop);

        rv.setLayoutManager(new LinearLayoutManager(this));
        rv.setItemAnimator(new DefaultItemAnimator());
        rv2.setLayoutManager(new LinearLayoutManager(this));
        rv2.setItemAnimator(new DefaultItemAnimator());


        rv.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
        rv2.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
        mAdapter = new IngAdapter(iList, this);
        Glide.with(this).load(recipe.getImg()).into(imv);
        /*Bitmap b = BitmapFactory.decodeByteArray(
                getIntent().getByteArrayExtra("byteArray"),0,getIntent().getByteArrayExtra("byteArray").length);
        imv.setImageBitmap(b);*/


        desc = findViewById(R.id.desc);
        url = findViewById(R.id.url);
        l_desc = findViewById(R.id.layout_desc);


        try {
            Nutrition nutrition = recipe.getNutrition();

            if (!nutrition.getCarbohydrateContent().equals("abc")) {
                String s = "Carbohydrate Content:" + nutrition.getCarbohydrateContent();
                nuts.add(s);
            }
            if (!nutrition.getCholesterolContent().equals("")) {
                String s = "Cholesterol Content:" + nutrition.getCholesterolContent();
                nuts.add(s);
            }
            if (!nutrition.getFatContent().equals("")) {
                String s = "Fat Content:" + nutrition.getFatContent();
                nuts.add(s);
            }
            if (!nutrition.getFiberContent().equals("")) {
                String s = "Fiber Content:" + nutrition.getFiberContent();
                nuts.add(s);
            }
            if (!nutrition.getProteinContent().equals("")) {
                String s = "Protien Content:" + nutrition.getProteinContent();
                nuts.add(s);
            }
            if (!nutrition.getSugarContent().equals("")) {
                String s = "Sugar Content:" + nutrition.getSugarContent();
                nuts.add(s);
            }

            vAdapter = new IngAdapter(nuts, this);

        } catch (Exception e) {
            //

        }


        String[] arr = recipe.getIngridient();

        for (int i = 0; i < arr.length; i++) {
            iList.add(arr[i]);
        }

        rv.setAdapter(mAdapter);
        rv2.setAdapter(vAdapter);
        String d = (recipe.getDesc());
        desc.setText(d);

        if (d == null) {
            l_desc.setVisibility(GONE);
        }

        url.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Uri uri = Uri.parse(recipe.getUrl());
                Intent i = new Intent(Intent.ACTION_VIEW, uri);
                startActivity(i);
            }
        });
        getSupportActionBar().setTitle(recipe.getName());
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


    }

}
