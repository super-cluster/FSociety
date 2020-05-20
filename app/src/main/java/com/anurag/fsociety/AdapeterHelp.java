package com.anurag.fsociety;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class AdapeterHelp extends RecyclerView.Adapter<AdapeterHelp.MyHolder> {
    private Context context;
    private List<HelpModel> helpModels;

    public AdapeterHelp(Context context, List<HelpModel> helpModels) {
        this.context = context;
        this.helpModels = helpModels;
    }

    @NonNull
    @Override
    public MyHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.help_row,parent,false);
        return new MyHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyHolder holder, int position) {
        holder.textView.setText(helpModels.get(0).getTitle());
        holder.textView1.setText(helpModels.get(0).getMessage());
    }

    @Override
    public int getItemCount() {
        return 0;
    }

    public class MyHolder extends RecyclerView.ViewHolder{
        private TextView textView,textView1;
        public MyHolder(@NonNull View itemView) {
            super(itemView);
            textView=itemView.findViewById(R.id.help_title);
            textView1=itemView.findViewById(R.id.message_help);
        }
    }
}
