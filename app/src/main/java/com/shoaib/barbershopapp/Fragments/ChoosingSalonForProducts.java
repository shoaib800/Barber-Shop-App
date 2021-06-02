package com.shoaib.barbershopapp.Fragments;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.jaredrummler.materialspinner.MaterialSpinner;
import com.shoaib.barbershopapp.Adapter.MySalonAdapter;
import com.shoaib.barbershopapp.BookingActivity;
import com.shoaib.barbershopapp.Common.Common;
import com.shoaib.barbershopapp.Common.SpacesItemDecoration;
import com.shoaib.barbershopapp.Interface.IAllSalonLoadListener;
import com.shoaib.barbershopapp.Interface.IBranchLoadListener;
import com.shoaib.barbershopapp.Model.EventBus.EnableNextButton;
import com.shoaib.barbershopapp.Model.Salon;
import com.shoaib.barbershopapp.R;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import dmax.dialog.SpotsDialog;

import static com.shoaib.barbershopapp.Common.Common.step;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ChoosingSalonForProducts#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ChoosingSalonForProducts extends Fragment implements IAllSalonLoadListener, IBranchLoadListener {

    //Variable
    CollectionReference allSalonRef;
    CollectionReference branchRef;

    IAllSalonLoadListener iAllSalonLoadListener;
    IBranchLoadListener iBranchLoadListener;

    @BindView(R.id.spinner)
    MaterialSpinner spinner;
    @BindView(R.id.recycler_salon)
    RecyclerView recycler_salon;

    @BindView(R.id.btn_next_step)
    Button btn_next_step;



    Unbinder unbinder;
    AlertDialog dialog;

    static ChoosingSalonForProducts instance;

    public static ChoosingSalonForProducts getInstance()
    {
        if (instance == null)
            instance = new ChoosingSalonForProducts();
        return instance;
    }


    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public ChoosingSalonForProducts() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ChoosingSalonForProducts.
     */
    // TODO: Rename and change types and number of parameters
    public static ChoosingSalonForProducts newInstance(String param1, String param2) {
        ChoosingSalonForProducts fragment = new ChoosingSalonForProducts();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        allSalonRef = FirebaseFirestore.getInstance().collection("AllSalons");

        iAllSalonLoadListener = this;
        iBranchLoadListener = this;


        dialog = new SpotsDialog.Builder().setContext(getActivity()).setCancelable(false).build();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
//        return inflater.inflate(R.layout.fragment_choosing_salon_for_products, container, false);
        View itemView = inflater.inflate(R.layout.fragment_choosing_salon_for_products, container, false);
        unbinder = ButterKnife.bind(this, itemView);

        initView();
        loadAllSalon();



        return itemView;
    }



    private void initView() {
        recycler_salon.setHasFixedSize(true);
        recycler_salon.setLayoutManager(new GridLayoutManager(getActivity(), 2));
        recycler_salon.addItemDecoration(new SpacesItemDecoration(4));
    }

    private void loadAllSalon() {
        allSalonRef.get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful())
                        {
                            List<String> list = new ArrayList<>();
                            list.add("Please choose city");
                            for (QueryDocumentSnapshot documentSnapshot:task.getResult())
                                list.add(documentSnapshot.getId());
                            iAllSalonLoadListener.onAllSalonLoadSuccess(list);
                        }
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                iAllSalonLoadListener.onAllSalonLoadFailed(e.getMessage());
            }
        });
    }

    @Override
    public void onAllSalonLoadSuccess(List<String> areaNameList) {
        spinner.setItems(areaNameList);
        spinner.setOnItemSelectedListener(new MaterialSpinner.OnItemSelectedListener() {
            @Override
            public void onItemSelected(MaterialSpinner view, int position, long id, Object item) {
                if (position > 0)
                {
                    loadBranchOfCity(item.toString());

                }
                else
                    recycler_salon.setVisibility(View.GONE);
            }
        });

    }

    private void loadBranchOfCity(String cityName) {
        dialog.show();

        Common.city = cityName;

        branchRef = FirebaseFirestore.getInstance()
                .collection("AllSalons")
                .document(cityName)
                .collection("Branch");

        branchRef.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                List<Salon> list = new ArrayList<>();
                if (task.isSuccessful())
                {
                    for (QueryDocumentSnapshot documentSnapshot:task.getResult())
                    {
                        Salon salon = documentSnapshot.toObject(Salon.class);
                        salon.setSalonId(documentSnapshot.getId());
                        list.add(salon);

                    }
                    iBranchLoadListener.onBranchLoadSuccess(list);

                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                iBranchLoadListener.onBranchLoadFailed(e.getMessage());
            }
        });

    }

    @Override
    public void onAllSalonLoadFailed(String message) {
        Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();

    }

    @Override
    public void onBranchLoadSuccess(List<Salon> salonList) {
        MySalonAdapter adapter = new MySalonAdapter(getActivity(),salonList,btn_next_step,true);
        recycler_salon.setAdapter(adapter);
        recycler_salon.setVisibility(View.VISIBLE);

        dialog.dismiss();

    }

    @Override
    public void onBranchLoadFailed(String message) {
        Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();
        dialog.dismiss();

    }
}