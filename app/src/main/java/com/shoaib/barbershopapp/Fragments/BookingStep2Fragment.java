package com.shoaib.barbershopapp.Fragments;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import com.shoaib.barbershopapp.Adapter.MyBarberAdapter;
import com.shoaib.barbershopapp.Common.Common;
import com.shoaib.barbershopapp.Common.SpacesItemDecoration;
import com.shoaib.barbershopapp.Model.Barber;
import com.shoaib.barbershopapp.Model.EventBus.BarberDoneEvent;
import com.shoaib.barbershopapp.R;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

public class BookingStep2Fragment extends Fragment {

    Unbinder unbinder;
//    LocalBroadcastManager localBroadcastManager;

    @BindView(R.id.recycler_barber)
    RecyclerView recycler_barber;

//    private BroadcastReceiver barberDoneReceiver = new BroadcastReceiver() {
//        @Override
//        public void onReceive(Context context, Intent intent) {
//            ArrayList<Barber> barberArrayList = intent.getParcelableArrayListExtra(Common.KEY_BARBER_LOAD_DONE);
//            // Create adapter late
//            MyBarberAdapter adapter = new MyBarberAdapter(getContext(), barberArrayList);
//            recycler_barber.setAdapter(adapter);
//        }
//    };

    //================================================================
    //EVENT BUS START

    @Override
    public void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
    }

    @Override
    public void onStop() {
        EventBus.getDefault().unregister(this);
        super.onStop();
    }

    @Subscribe(sticky = true, threadMode = ThreadMode.MAIN)
    public void setBarberAdapter(BarberDoneEvent event)
    {
        MyBarberAdapter adapter = new MyBarberAdapter(getContext(), event.getBarberList());
        recycler_barber.setAdapter(adapter);
    }

    //================================================================

    static BookingStep2Fragment instance;

    public static BookingStep2Fragment getInstance()
    {
        if (instance == null)
            instance = new BookingStep2Fragment();
        return instance;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);

        View itemView = inflater.inflate(R.layout.fragment_booking_step_two, container, false);

        unbinder = ButterKnife.bind(this, itemView);

        initView();
        return itemView;
    }
    private void initView() {
        recycler_barber.setHasFixedSize(true);
        recycler_barber.setLayoutManager(new GridLayoutManager(getActivity(), 2));
        recycler_barber.addItemDecoration(new SpacesItemDecoration(4));
    }
}
