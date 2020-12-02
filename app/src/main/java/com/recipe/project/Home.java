package com.recipe.project;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.gson.JsonElement;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import ai.api.android.AIConfiguration;
import ai.api.android.AIService;
import ai.api.model.AIError;
import ai.api.model.AIResponse;
import ai.api.model.Result;

public class Home extends AppCompatActivity {
    boolean doubleBackToExitPressedOnce = false;
    private EditText ip;
    private FloatingActionButton add;
    private RecyclerView rv;
    Button go;
    private AIService aiService;

    private final List<String> iList = new ArrayList<>();
    InputAdapter mAdapter;

    private static final int CAMERA_PERMISSION_REQUEST_CODE = 1000;
    private static final int CAMERA_REQEUST_CODE = 10001;
    /**
     * UI Elements
     */
    private ImageView imageView;
    private ListView listView;
    private ImageClassifier imageClassifier;

    /**
     * Method to initalize UI Elements. this method adds the on click
     */
    private void initializeUIElements() {
        imageView = findViewById(R.id.modelimage);
//        listView = findViewById(R.id.list);
        FloatingActionButton takepicture = findViewById(R.id.camera);
        /*
         * Creating an instance of our tensor image classifier
         */
        try {
            imageClassifier = new ImageClassifier(this);
        } catch (IOException e) {
            Log.e("Image Classifier Error", "ERROR: " + e);
        }
        // adding on click listener to button
        takepicture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // checking whether camera permissions are available.
                // if permission is avaialble then we open camera intent to get picture
                // otherwise reqeusts for permissions
                if (hasPermission()) {
                    openCamera();
                } else {
                    requestPermission();
                }
            }
        });
    }

    /**
     * checks whether all the needed permissions have been granted or not
     *
     * @param grantResults the permission grant results
     * @return true if all the reqested permission has been granted,
     * otherwise returns false
     */
    private boolean hasAllPermissions(int[] grantResults) {
        for (int result : grantResults) {
            if (result == PackageManager.PERMISSION_DENIED)
                return false;
        }
        return true;
    }

    /**
     * Method requests for permission if the android version is marshmallow or above
     */
    private void requestPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            // whether permission can be requested or on not
            if (shouldShowRequestPermissionRationale(Manifest.permission.CAMERA)) {
                Toast.makeText(this, "Camera Permission Required", Toast.LENGTH_SHORT).show();
            }
            // request the camera permission permission
            requestPermissions(new String[]{Manifest.permission.CAMERA}, CAMERA_PERMISSION_REQUEST_CODE);
        }
    }

    /**
     * checks whether camera permission is available or not
     *
     * @return true if android version is less than marshmallo,
     * otherwise returns whether camera permission has been granted or not
     */
    private boolean hasPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            return checkSelfPermission(Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED;
        }
        return true;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        // if this is the result of our camera permission request
        if (requestCode == CAMERA_PERMISSION_REQUEST_CODE) {
            if (hasAllPermissions(grantResults)) {
                openCamera();
            } else {
                requestPermission();
            }
        }
    }

    /**
     * creates and starts an intent to get a picture from camera
     */
    private void openCamera() {
        Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(cameraIntent, CAMERA_REQEUST_CODE);
    }

    private Bitmap ARGBBitmap(Bitmap img) {
        return img.copy(Bitmap.Config.ARGB_8888,true);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        // if this is the result of our camera image request
        if (requestCode == CAMERA_REQEUST_CODE) {
            // getting bitmap of the image
            Bitmap photo = (Bitmap) Objects.requireNonNull(Objects.requireNonNull(data).getExtras()).get("data");
            // displaying this bitmap in imageview
            imageView.setImageBitmap(photo);
//            Log.d("photo",photo.toString());
            // pass this bitmap to classifier to make prediction
            ImageClassifier.Recognition predicitons = imageClassifier.recognizeImage(
                    ARGBBitmap(photo), 0);

            // creating a list of string to display in list view
            final List<String> predicitonsList = new ArrayList<>();
//            for (ImageClassifier.Recognition recog : predicitons) {
//                predicitonsList.add(recog.getName() + "  ::::::::::  " + recog.getConfidence());
//            }
            Log.d("classified as:", predicitons.getName());
            // creating an array adapter to display the classification result in list view
            ip.setText(predicitons.getName());
            ArrayAdapter<String> predictionsAdapter = new ArrayAdapter<>(
                    this, R.layout.support_simple_spinner_dropdown_item, predicitonsList);
//            listView.setAdapter(predictionsAdapter);

        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    //EditText
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        ip = findViewById(R.id.input);
        add = findViewById(R.id.add);
        rv = findViewById(R.id.list);
        rv.setLayoutManager(new LinearLayoutManager(this));
        rv.setItemAnimator(new DefaultItemAnimator());

        go = findViewById(R.id.go);
        rv.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));

        mAdapter = new InputAdapter(iList, this);
        initializeUIElements();
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                aiService.startListening();

            }
        });


        go.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (iList.size() != 0) {
                    StringBuilder sb = new StringBuilder();
                    for (int i = 0; i < iList.size(); i++) {
                        sb.append(iList.get(i));
                        sb.append("+");
                    }
                    String str = sb.toString();
                    Intent intent = new Intent(Home.this, MainActivity.class);
                    intent.putExtra("str", str);
                    startActivity(intent);
                } else {
                    Toast.makeText(Home.this, "Please Add Atleast One Ingredient", Toast.LENGTH_SHORT).show();
                }

            }
        });
        ip.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                String i = ip.getText().toString().trim();
                if (!i.equals("")) {
                    if (!iList.contains(i)) {
                        iList.add(i);
                        mAdapter.notifyDataSetChanged();
                        rv.setAdapter(mAdapter);
                        ip.setText("");
                        ip.requestFocus();
                    }
                }
                return false;
            }
        });


        final AIConfiguration config = new AIConfiguration("54b8a1cfad6e4a7e870d33d8cc732a73",
                AIConfiguration.SupportedLanguages.English,
                AIConfiguration.RecognitionEngine.System);
        /*AIDialog aiDialog = new AIDialog(this, config);
        aiDialog.setResultsListener(new AIClass(Home.this));
        aiDialog.showAndListen();*/
        aiService = AIService.getService(this, config);
        aiService.setListener(new ai.api.AIListener() {
            @Override
            public void onResult(AIResponse response) {

                Result result = response.getResult();

                // Get parameters
                String parameterString = "";
                if (result.getParameters() != null && !result.getParameters().isEmpty()) {
                    for (final Map.Entry<String, JsonElement> entry : result.getParameters().entrySet()) {
                        parameterString += "(" + entry.getKey() + ", " + entry.getValue() + ") ";
                    }
                }

                // Show results in TextView.

                if (!parameterString.equals("")) {
                    String ip = result.getResolvedQuery();
                    iList.add(ip);
                    mAdapter.notifyDataSetChanged();
                    rv.setAdapter(mAdapter);
                }


                String s = "Query:" + result.getResolvedQuery() +
                        "\nAction: " + result.getAction() +
                        "\nparams" + parameterString;

                Toast.makeText(getApplicationContext(), s, Toast.LENGTH_SHORT).show();
                /*resultTextView.setText("Query:" + result.getResolvedQuery() +
                        "\nAction: " + result.getAction() +
                        "\nParameters: " + parameterString);*/
            }

            @Override
            public void onError(final AIError error) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(Home.this, error.toString(), Toast.LENGTH_SHORT).show();
                    }
                });
            }

            @Override
            public void onAudioLevel(float level) {

            }

            @Override
            public void onListeningStarted() {
                Toast.makeText(Home.this, "Started", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onListeningCanceled() {

            }

            @Override
            public void onListeningFinished() {

            }
        });
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
            startActivity(new Intent(Home.this, AboutUs.class));
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {



        if (doubleBackToExitPressedOnce) {
            Intent intent = new Intent(Intent.ACTION_MAIN);
            intent.addCategory(Intent.CATEGORY_HOME);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
            finish();
            super.onBackPressed();
            return;
        }
        this.doubleBackToExitPressedOnce = true;
        Toast.makeText(this, "Please click BACK again to exit", Toast.LENGTH_SHORT).show();

        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                doubleBackToExitPressedOnce = false;
            }
        }, 2000);
    }


}



