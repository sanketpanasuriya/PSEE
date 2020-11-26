package com.rku.psee;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.animation.AnimationUtils;
import android.view.animation.LayoutAnimationController;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import static android.widget.Toast.LENGTH_SHORT;

public class MainActivity extends AppCompatActivity {
    RecyclerView rcvUsers;
    MyAdapter userAdapter;
    RequestQueue requestQueue;
    JsonArrayRequest jsonArrayRequest;
    ProgressDialog dialog;
    StringRequest stringRequest;
    RecyclerView.LayoutManager layoutManager;
    DatabaseHelper dbHelper;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        rcvUsers = findViewById(R.id.rcvUsers);

        rcvUsers.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        rcvUsers.setLayoutManager(layoutManager);

        DividerItemDecoration dividerItemDecoration =
                new DividerItemDecoration(rcvUsers.getContext(), LinearLayoutManager.VERTICAL);
        rcvUsers.addItemDecoration(dividerItemDecoration);
        int resId = R.anim.layout_animation_fall_down;
        LayoutAnimationController animation = AnimationUtils.loadLayoutAnimation(getApplicationContext(),resId);
        rcvUsers.setLayoutAnimation(animation);

        dbHelper = new DatabaseHelper(MainActivity.this);

        volleyNetworkCallAPI();

    }



    private void volleyNetworkCallAPI() {
        requestQueue = Volley.newRequestQueue(MainActivity.this);
        stringRequest = new StringRequest(
                Request.Method.GET,
                MyUtil.USER_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            MyUtil.UserData = new JSONObject( response);
                            JSONArray jsonArray=MyUtil.UserData.getJSONArray("data");
                            for (int i=0;i<jsonArray.length();i++){
                                JSONObject jObject = jsonArray.getJSONObject(i);
                                long res=dbHelper.insert(jObject.getString("name"),jObject.getInt("year"),jObject.getString("color"),jObject.getString("pantone_value"));
                                Log.e("insertRes", String.valueOf(res));
                            }
                            JSONArray jsonArray1 = dbHelper.getData();
                            userAdapter = new MyAdapter(jsonArray1);
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(getApplicationContext(),"Something went wrong", Toast.LENGTH_SHORT).show();
                        }
                        rcvUsers.setAdapter(userAdapter);
                        userAdapter.notifyDataSetChanged();
                        Log.e("Data", MyUtil.UserData.toString());
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(),"Something went wrong", Toast.LENGTH_SHORT).show();
            }
        }
        );
        requestQueue.add(stringRequest);
    }

}