package shoaib.dev.barbershopapp.Fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import shoaib.dev.barbershopapp.Interface.IShoppingDataLoadListener;
import shoaib.dev.barbershopapp.Model.ShoppingItem;
import shoaib.dev.barbershopapp.R;

public class ShoppingFragment extends Fragment implements IShoppingDataLoadListener {
// Model for Shopping Item
    CollectionReference shoppingItemRef;
    IShoppingDataLoadListener iShoppingDataLoadListener;

    Unbinder unbinder;

    @BindView(R.id.chip_group)
    ChipGroup chipGroup;
    @BindView(R.id.chip_wax)
    Chip chip_wax;
    @OnClick(R.id.chip_wax)
    void waxChipClick(){
        setSelectedChip(chip_wax);
        loadShoppingItem(chip_wax.getText().toString());
    }

    private void loadShoppingItem(String itemMenu) {
        shoppingItemRef = FirebaseFirestore.getInstance().collection("Shopping")
                        .document(itemMenu)
                        .collection("Items");

        // Get Data

                shoppingItemRef.get()
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                iShoppingDataLoadListener.onShoppingDataLoadFailed(e.getMessage());
                            }
                        }).addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            if (task.isSuccessful())
                            {
                                List<ShoppingItem> shoppingItems = new ArrayList<>();
                                for(DocumentSnapshot itemSnapShot:task.getResult())
                                {
                                    ShoppingItem shoppingItem = itemSnapShot.toObject(ShoppingItem.class);
                                    shoppingItems.add(shoppingItem);
                                }
                                iShoppingDataLoadListener.onShoppingDataLoadSuccess(shoppingItems);
                            }
                    }
                });

    }

    private void setSelectedChip(Chip chip) {
        // Setting Color of chips on selection
        for (int i=0; i<chipGroup.getChildCount();i++){

            Chip chipItem = (Chip)chipGroup.getChildAt(i);
            if (chipItem.getId() != chip.getId())   // If Not Selected
            {
                chipItem.setChipBackgroundColorResource(android.R.color.darker_gray);
                chipItem.setTextColor(getResources().getColor(android.R.color.white));
            }
            else // If Selected
            {
                chipItem.setChipBackgroundColorResource(android.R.color.holo_orange_dark);
                chipItem.setTextColor(getResources().getColor(android.R.color.black));
            }
        }
    }

    public ShoppingFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View itemView = inflater.inflate(R.layout.fragment_shopping, container, false);

        unbinder = ButterKnife.bind(this, itemView);
        iShoppingDataLoadListener = this;
        return itemView;
    }

    @Override
    public void onShoppingDataLoadSuccess(List<ShoppingItem> shoppingItemList) {

    }

    @Override
    public void onShoppingDataLoadFailed(String message) {
        Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();
    }
}