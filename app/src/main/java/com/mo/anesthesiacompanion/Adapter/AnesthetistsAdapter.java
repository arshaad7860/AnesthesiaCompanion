package com.mo.anesthesiacompanion.Adapter;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.DocumentSnapshot;
import com.mo.anesthesiacompanion.Model.AnesthetistModel;
import com.mo.anesthesiacompanion.R;

public class AnesthetistsAdapter extends FirestoreRecyclerAdapter<AnesthetistModel, AnesthetistsAdapter.AnesthetistsViewHolder> {
    private AnesthetistsAdapter.OnItemClickListener listener;

    public AnesthetistsAdapter(@NonNull FirestoreRecyclerOptions<AnesthetistModel> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull AnesthetistsViewHolder holder, int position, @NonNull AnesthetistModel model) {

        holder.txt_first_name.setText(model.getFirstName());
        holder.txt_last_name.setText(model.getLastName());
        holder.txt_phone.setText(model.getPhone());
        holder.txt_email.setText(model.getEmail());
        holder.txt_required_qualification.setText(model.getQualification());
        holder.txt_experience.setText(model.getExperience());
        holder.txt_occupation.setText(model.getOccupation());
        holder.txt_hospital_name.setText(model.getHospital());

        if (model.isPending() && (!model.isAccepted())) {
            holder.txt_status.setText("Pending");
            holder.txt_status.setTextColor(Color.BLUE);
        } else if (model.isPending() && (model.isAccepted())) {
            holder.txt_status.setText("Accepted");
            holder.txt_status.setTextColor(Color.parseColor("#228B22"));
        } else if (!model.isPending() && !(model.isAccepted())){
            holder.txt_status.setText("Rejected");
            holder.txt_status.setTextColor(Color.RED);
        }
        else {
            holder.txt_status.setText("Canceled");
            holder.txt_status.setTextColor(Color.RED);
        }
    }

    @NonNull
    @Override
    public AnesthetistsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.anesthetists_item,
                parent,false);
        return new AnesthetistsViewHolder(v);
    }

    class AnesthetistsViewHolder extends RecyclerView.ViewHolder{
        private TextView txt_first_name,txt_last_name,txt_phone,txt_email,txt_required_qualification,txt_experience,txt_occupation,txt_hospital_name,txt_status;
        public AnesthetistsViewHolder(@NonNull View itemView) {
            super(itemView);
            txt_first_name=itemView.findViewById(R.id.txt_first_name);
            txt_last_name=itemView.findViewById(R.id.txt_last_name);
            txt_phone=itemView.findViewById(R.id.txt_phone);
            txt_email=itemView.findViewById(R.id.txt_email);
            txt_required_qualification=itemView.findViewById(R.id.txt_required_qualification);
            txt_experience=itemView.findViewById(R.id.txt_experience);
            txt_occupation=itemView.findViewById(R.id.txt_occupation);
            txt_hospital_name=itemView.findViewById(R.id.txt_hospital_name);
            txt_status=itemView.findViewById(R.id.txt_status);

            itemView.setOnClickListener(v -> {
                int position = getAdapterPosition();
                if (position!=RecyclerView.NO_POSITION && listener!=null)
                {
                    listener.onItemClick(getSnapshots().getSnapshot(position),position);
                }
            });
        }
    }

    public interface OnItemClickListener{
        void onItemClick(DocumentSnapshot documentSnapshot, int position);
    }
    public void setOnItemClickListener(AnesthetistsAdapter.OnItemClickListener listener){
        this.listener= (OnItemClickListener) listener;
    }
}
