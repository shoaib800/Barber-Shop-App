package com.shoaib.barbershopapp.Adapter;


import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import com.shoaib.barbershopapp.Common.Common;
import com.shoaib.barbershopapp.Interface.IRecyclerItemSelectedListener;
import com.shoaib.barbershopapp.Model.EventBus.EnableNextButton;
import com.shoaib.barbershopapp.Model.TimeSlot;
import com.shoaib.barbershopapp.R;

import org.greenrobot.eventbus.EventBus;

public class MyTimeSlotAdapter extends RecyclerView.Adapter<MyTimeSlotAdapter.MyViewHolder> {

        Context context;
        List<TimeSlot> timeSlotList;
        List<CardView> cardViewList;

public MyTimeSlotAdapter(Context context) {
        this.context = context;
        this.timeSlotList = new ArrayList<>();
        cardViewList = new ArrayList<>();
        }

public MyTimeSlotAdapter(Context context, List<TimeSlot> timeSlotList) {
        this.context = context;
        this.timeSlotList = timeSlotList;
        cardViewList = new ArrayList<>();
        }

@NonNull
@Override
public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View itemView = LayoutInflater.from(context)
        .inflate(R.layout.layout_time_slot, viewGroup, false);
        return new MyViewHolder(itemView);
        }

@Override
public void onBindViewHolder(@NonNull final MyViewHolder myViewHolder, int i) {
        myViewHolder.txt_time_slot.setText(new StringBuilder(Common.convertTimeSlotToString(i)).toString());
        if (timeSlotList.size() == 0)//if all position are available, just show list
        {
            myViewHolder.card_time_slot.setEnabled(true);
        myViewHolder.card_time_slot.setCardBackgroundColor(context.getResources().getColor(android.R.color.white));

        myViewHolder.txt_time_slot_description.setText("Available");
        myViewHolder.txt_time_slot_description.setTextColor(context.getResources()
        .getColor(android.R.color.black));
        myViewHolder.txt_time_slot.setTextColor(context.getResources().getColor(android.R.color.black));


        }
        else //If have position is full (booked)
        {
        for (TimeSlot slotValue:timeSlotList)
        {
        //Loop all time slot from server and set different color
        int slot = Integer.parseInt(slotValue.getSlot().toString());
        if (slot == i)// if slot == position
        {
            //we will set tag for all time slot full
            //so base on tag, we can set all remain card background without changing full time slot.
            myViewHolder.card_time_slot.setEnabled(false);
            myViewHolder.card_time_slot.setTag(Common.DISABLE_TAG);
            myViewHolder.card_time_slot.setCardBackgroundColor(context.getResources().getColor(android.R.color.holo_red_light));

            myViewHolder.txt_time_slot_description.setText("Full");
            myViewHolder.txt_time_slot_description.setTextColor(context.getResources()
            .getColor(android.R.color.white));
            myViewHolder.txt_time_slot.setTextColor(context.getResources().getColor(android.R.color.white));
        }
        }
        }

        //Add all card to list ( 20 cards because we have 20 slots )
        // dont add card already in cardViewList
        if (!cardViewList.contains(myViewHolder.card_time_slot))
        cardViewList.add(myViewHolder.card_time_slot);

        //check if card time is available
        if(!timeSlotList.contains(i))
            {
                myViewHolder.setiRecyclerItemSelectedListener(new IRecyclerItemSelectedListener() {
                    @Override
                    public void onItemSelectedListener(View view, int pos) {
                        //Loop all card in card list
                        for (CardView cardView:cardViewList)
                        {
                            if (cardView.getTag() == null) // only available card time slot be change
                                cardView.setCardBackgroundColor(context.getResources()
                                        .getColor(android.R.color.white));
                        }

                        //our selected card will change color
                        myViewHolder.card_time_slot.setCardBackgroundColor(context.getResources()
                                .getColor(android.R.color.holo_orange_light));

                        //==================================================
                        //Event Bus
                        EventBus.getDefault().postSticky(new EnableNextButton(3,i));

                        //=================================================

                    }
                });
            }
        }

@Override
public int getItemCount() {
        return Common.TIME_SLOT_TOTAL;
        }

public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

    TextView txt_time_slot,txt_time_slot_description;
    CardView card_time_slot;

    IRecyclerItemSelectedListener iRecyclerItemSelectedListener;

    public void setiRecyclerItemSelectedListener(IRecyclerItemSelectedListener iRecyclerItemSelectedListener) {
        this.iRecyclerItemSelectedListener = iRecyclerItemSelectedListener;
    }

    public MyViewHolder(@NonNull View itemView) {
        super(itemView);

        card_time_slot = (CardView)itemView.findViewById(R.id.card_time_slot);
        txt_time_slot = (TextView)itemView.findViewById(R.id.txt_time_slot);
        txt_time_slot_description = (TextView)itemView.findViewById(R.id.txt_time_slot_description);

        itemView.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        iRecyclerItemSelectedListener.onItemSelectedListener(view, getAdapterPosition());
    }
}
}
