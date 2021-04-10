package shoaib.dev.barbershopapp.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.List;

import shoaib.dev.barbershopapp.Common.Common;
import shoaib.dev.barbershopapp.Model.ShoppingItem;
import shoaib.dev.barbershopapp.R;

public class MyShoppingItemAdapter extends RecyclerView.Adapter<MyShoppingItemAdapter.MyViewHolder> {

    Context context;
    List<ShoppingItem> shoppingItemList;

    public MyShoppingItemAdapter(Context context, List<ShoppingItem> shoppingItemList) {
        this.context = context;
        this.shoppingItemList = shoppingItemList;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View itemView = LayoutInflater.from(context).inflate(R.layout.layout_shopping_item, viewGroup , false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder myViewHolder, int i) {
        Picasso.get().load(shoppingItemList.get(i).getImage()).into(myViewHolder.image_shopping_item);
        myViewHolder.txt_shopping_item_name.setText(Common.formatShoppingItemName);
        myViewHolder.txt_shopping_item_price.setText(new StringBuilder("$").append(shoppingItemList.get(i).getPrice()));

    }

    @Override
    public int getItemCount() {
        return shoppingItemList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{

        TextView txt_shopping_item_name,txt_shopping_item_price,txt_add_to_cart;
        ImageView image_shopping_item;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            image_shopping_item = (ImageView)itemView.findViewById(R.id.image_shopping_item);
            txt_shopping_item_name = (TextView)itemView.findViewById(R.id.txt_name_shopping_item);
            txt_add_to_cart = (TextView)itemView.findViewById(R.id.txt_add_to_cart);
        }
    }
}
