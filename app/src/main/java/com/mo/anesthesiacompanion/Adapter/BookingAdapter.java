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
import com.mo.anesthesiacompanion.Model.BookingModel;
import com.mo.anesthesiacompanion.R;

public class BookingAdapter extends FirestoreRecyclerAdapter<BookingModel, BookingAdapter.BookingViewHolder> {
    private OnItemClickListener listener;

    public BookingAdapter(@NonNull FirestoreRecyclerOptions<BookingModel> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull BookingViewHolder holder, int position, @NonNull BookingModel model) {
        holder.txt_dr_name.setText(model.getDrName());
        holder.txt_phone.setText(model.getPhone());
        holder.txt_anesthesia_type.setText(model.getAnestheticType());
        holder.txt_hospital_name.setText(model.getHospitalName());
        holder.txt_required_qualification.setText(model.getQualificationType());
        holder.txt_surgery_date.setText(model.getSurgeryDate());
        holder.txt_surgery_time.setText(model.getSurgeryTime());
        holder.txt_surgery_type.setText(model.getSurgeryType());
        if (model.getEmergency()) {
            holder.txt_emergency.setText("EMERGENCY");
            holder.txt_emergency.setTextColor(Color.RED);
        }
        else
        {
            holder.txt_emergency.setText("No");
            holder.txt_emergency.setTextColor(Color.BLUE);
        }
    }

    @NonNull
    @Override
    public BookingViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.booking_item,
                parent,false);
        return new BookingViewHolder(v);
    }

    class BookingViewHolder extends RecyclerView.ViewHolder{
        private TextView txt_phone,txt_dr_name,txt_surgery_type,txt_hospital_name,txt_anesthesia_type,txt_required_qualification,txt_surgery_time,txt_surgery_date,txt_emergency;
        public BookingViewHolder(@NonNull View itemView) {
            super(itemView);
            txt_dr_name=itemView.findViewById(R.id.txt_dr_name);
            txt_phone=itemView.findViewById(R.id.txt_phone);
            txt_surgery_type=itemView.findViewById(R.id.txt_surgery_type);
            txt_hospital_name=itemView.findViewById(R.id.txt_hospital_name);
            txt_anesthesia_type=itemView.findViewById(R.id.txt_anesthesia_type);
            txt_required_qualification=itemView.findViewById(R.id.txt_required_qualification);
            txt_surgery_time=itemView.findViewById(R.id.txt_surgery_time);
            txt_surgery_date=itemView.findViewById(R.id.txt_surgery_date);
            txt_emergency=itemView.findViewById(R.id.txt_emergency);

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
        void onItemClick(DocumentSnapshot documentSnapshot,int position);
    }
    public void setOnItemClickListener(OnItemClickListener listener){
        this.listener=listener;
    }
}
