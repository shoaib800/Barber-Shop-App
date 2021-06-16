package com.shoaib.barbershopapp.Adapter;

import android.content.Context;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.squareup.picasso.Picasso;

import java.util.List;

import com.shoaib.barbershopapp.Common.Common;
import com.shoaib.barbershopapp.Database.CartDatabase;
import com.shoaib.barbershopapp.Database.CartItem;
import com.shoaib.barbershopapp.Database.DatabaseUtils;
import com.shoaib.barbershopapp.Interface.IRecyclerItemSelectedListener;
import com.shoaib.barbershopapp.Model.ShoppingItem;
import com.shoaib.barbershopapp.R;

public class MyShoppingItemAdapter extends RecyclerView.Adapter<MyShoppingItemAdapter.MyViewHolder> {

    Context context;
    List<ShoppingItem> shoppingItemList;
    CartDatabase cartDatabase;

    FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();

    public MyShoppingItemAdapter(Context context, List<ShoppingItem> shoppingItemList) {
        this.context = context;
        this.shoppingItemList = shoppingItemList;
        cartDatabase = CartDatabase.getInstance(context);
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View itemView = LayoutInflater.from(context).inflate(R.layout.layout_shopping_item,viewGroup, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder myViewHolder, int i) {

        Picasso.get().load(shoppingItemList.get(i).getImage()).into(myViewHolder.img_shopping_item);
        myViewHolder.txt_shopping_item_name.setText(Common.formatShoppingItemName(shoppingItemList.get(i).getName()));
        myViewHolder.txt_shopping_item_price.setText(new StringBuilder("Rs. ").append(shoppingItemList.get(i).getPrice()));

        //Add to cart
        myViewHolder.setiRecyclerItemSelectedListener(new IRecyclerItemSelectedListener() {
            @Override
            public void onItemSelectedListener(View view, int pos) {
                //Create Cart Item
                CartItem cartItem = new CartItem();
                cartItem.setProductId(shoppingItemList.get(pos).getId());
                cartItem.setProductName(shoppingItemList.get(pos).getName());
                cartItem.setProductImage(shoppingItemList.get(pos).getImage());
                cartItem.setProductQuantity(1);
                cartItem.setProductPrice(shoppingItemList.get(pos).getPrice());
                cartItem.setUserPhone(firebaseAuth.getCurrentUser().getPhoneNumber());

                //Insert To Database
                DatabaseUtils.insertToCart(cartDatabase, cartItem);
                Toast.makeText(context, "Added to Cart", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return shoppingItemList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView txt_shopping_item_name,txt_shopping_item_price,txt_add_to_cart;
        ImageView img_shopping_item;

        IRecyclerItemSelectedListener iRecyclerItemSelectedListener;

        public void setiRecyclerItemSelectedListener(IRecyclerItemSelectedListener iRecyclerItemSelectedListener) {
            this.iRecyclerItemSelectedListener = iRecyclerItemSelectedListener;
        }

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            img_shopping_item = (ImageView)itemView.findViewById(R.id.img_shopping_item);
            txt_shopping_item_name = (TextView)itemView.findViewById(R.id.txt_name_shopping_item);
            txt_shopping_item_price = (TextView)itemView.findViewById(R.id.txt_price_shopping_item);
            txt_add_to_cart = (TextView)itemView.findViewById(R.id.txt_add_to_cart);

            txt_add_to_cart.setOnClickListener(this);

        }

        @Override
        public void onClick(View view) {
            iRecyclerItemSelectedListener.onItemSelectedListener(view,getAdapterPosition());
        }
    }
}