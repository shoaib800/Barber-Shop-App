package com.shoaib.barbershopapp.Adapter;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import com.shoaib.barbershopapp.Common.Common;
import com.shoaib.barbershopapp.Fragments.ChoosingSalonForProducts;
import com.shoaib.barbershopapp.Fragments.ShoppingFragment;
import com.shoaib.barbershopapp.Interface.IRecyclerItemSelectedListener;
import com.shoaib.barbershopapp.Model.EventBus.EnableNextButton;
import com.shoaib.barbershopapp.Model.Salon;
import com.shoaib.barbershopapp.R;

import org.greenrobot.eventbus.EventBus;

public class MySalonAdapter extends RecyclerView.Adapter<MySalonAdapter.MyViewHolder> {

    Context context;
    List<Salon> salonList;
    List<CardView> cardViewList;
    Button btn_next_step;
    Boolean value;

    public MySalonAdapter(Context context, List<Salon> salonList,Button btn_next_step,Boolean value) {
        this.context = context;
        this.salonList = salonList;
        this.btn_next_step = btn_next_step;
        cardViewList = new ArrayList<>();
        this.value=value;
    }
    public MySalonAdapter(Context context, List<Salon> salonList,Boolean value) {
        this.context = context;
        this.salonList = salonList;
        cardViewList = new ArrayList<>();
        this.btn_next_step = btn_next_step;
        this.value=value;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View itemView = LayoutInflater.from(context)
                .inflate(R.layout.layout_salon, viewGroup, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder myViewHolder, int i) {
        myViewHolder.txt_salon_name.setText(salonList.get(i).getName());
        myViewHolder.txt_salon_address.setText(salonList.get(i).getAddress());

        if (!cardViewList.contains(myViewHolder.card_salon))
            cardViewList.add(myViewHolder.card_salon);

        myViewHolder.setiRecyclerItemSelectedListener(new IRecyclerItemSelectedListener() {
            @Override
            public void onItemSelectedListener(View view, int pos) {
                //Set white Background for all card not selected
                for (CardView cardView:cardViewList)
                    cardView.setCardBackgroundColor(context.getResources().getColor(android.R.color.white));
                //set selected BG for only selected item
                myViewHolder.card_salon.setCardBackgroundColor(context.getResources()
                        .getColor(android.R.color.holo_orange_light));

                //==================================================
                //Event Bus
                EventBus.getDefault().postSticky(new EnableNextButton(1,salonList.get(pos)));
                //=================================================

            }
        });

    }

    @Override
    public int getItemCount() {
        return salonList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView txt_salon_name, txt_salon_address;
        CardView card_salon;

        IRecyclerItemSelectedListener iRecyclerItemSelectedListener;

        public void setiRecyclerItemSelectedListener(IRecyclerItemSelectedListener iRecyclerItemSelectedListener) {
            this.iRecyclerItemSelectedListener = iRecyclerItemSelectedListener;
        }

        public MyViewHolder(@NonNull View itemView) {
              super(itemView);

              card_salon = (CardView)itemView.findViewById(R.id.card_salon);
              txt_salon_address = (TextView)itemView.findViewById(R.id.txt_salon_address);
              txt_salon_name = (TextView)itemView.findViewById(R.id.txt_salon_name);

            itemView.setOnClickListener(this);
          }

        @Override
        public void onClick(View view) {
            iRecyclerItemSelectedListener.onItemSelectedListener(view,getAdapterPosition());
            SharedPreferences sharedPref =
                    context.getSharedPreferences("SalounId",
                            Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPref.edit();
            editor.putString("salounId",salonList.get(getAdapterPosition()).getSalonId());
            editor.apply();

            if(value == true){

                btn_next_step.setEnabled(true);
                btn_next_step.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        Fragment fragment = new ShoppingFragment();
                        FragmentManager fragmentManager =((FragmentActivity)context).getSupportFragmentManager();
                        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                        fragmentTransaction.replace(R.id.fragment_container, fragment);
                        fragmentTransaction.addToBackStack(null);
                        fragmentTransaction.commit();
                    }
                });


            }
        }
    }

}
