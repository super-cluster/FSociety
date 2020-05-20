package com.anurag.fsociety;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class ActiveUsersAdapter extends RecyclerView.Adapter<ActiveUsersAdapter.MyHolder> {
    private Context context;
    private List<User> users;

    public ActiveUsersAdapter(Context context, List<User> users) {
        this.context = context;
        this.users = users;
    }

    @NonNull
    @Override
    public MyHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(context).inflate(R.layout.row_active_users,parent,false);
        return new MyHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyHolder holder, int position) {

        holder.username.setText(users.get(position).getName());
        if(users.get(position).getStatus().equals("offline")){
            holder.status.setTextColor(128128128);
        }
        holder.status.setText(users.get(position).getStatus());
       if(users.get(position).getTyping().equals("typing....")) {
           holder.typing.setText(users.get(position).getTyping());
       }

    }

    @Override
    public int getItemCount() {
        return users.size();
    }

    public class MyHolder extends RecyclerView.ViewHolder{
        TextView username,status,typing;
        public MyHolder(@NonNull View itemView) {
            super(itemView);
            username=itemView.findViewById(R.id.user_name);
            status=itemView.findViewById(R.id.status);
            typing=itemView.findViewById(R.id.typing);

        }
    }
}
