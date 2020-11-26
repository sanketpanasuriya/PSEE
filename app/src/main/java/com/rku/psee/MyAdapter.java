package com.rku.psee;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class MyAdapter extends RecyclerView.Adapter<com.rku.psee.MyAdapter.myViewHolder> {
    JSONArray jsonArray;

    public MyAdapter(JSONArray jsonArray) throws JSONException{
        this.jsonArray = jsonArray;
    }

    @NonNull
    @Override
    public com.rku.psee.MyAdapter.myViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.listitem,parent,false);
        return new myViewHolder(view);

    }

    @Override
    public void onBindViewHolder(@NonNull com.rku.psee.MyAdapter.myViewHolder holder, int position) {
        try {
            JSONObject data = jsonArray.getJSONObject(position);
            //JSONArray dataArray = jsonObject.getJSONArray("data");
            //JSONObject data = dataArray.getJSONObject(0);
            holder.txtName.setText(data.getString("NAME"));
            holder.txtYear.setText(data.getString("YEAR"));
            holder.txtColor.setText(data.getString("COLOR"));
            holder.txtPantone_value.setText(data.getString("PANTONE_VALUE"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public int getItemCount() {
        return jsonArray.length();
    }

    public static class myViewHolder extends RecyclerView.ViewHolder{
        TextView txtName,txtYear,txtColor,txtPantone_value;
        public myViewHolder(@NonNull View itemView) {
            super(itemView);
            txtName = itemView.findViewById(R.id.txtName);
            txtYear = itemView.findViewById(R.id.txtYear);
            txtColor = itemView.findViewById(R.id.txtColor);
            txtPantone_value = itemView.findViewById(R.id.txtPantone_value);
        }
    }
}