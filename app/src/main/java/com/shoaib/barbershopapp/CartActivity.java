package com.shoaib.barbershopapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.reactivex.SingleObserver;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.rxjava3.functions.Consumer;
import io.reactivex.rxjava3.schedulers.Schedulers;

import com.shoaib.barbershopapp.Adapter.MyCartAdapter;
import com.shoaib.barbershopapp.Common.Common;
import com.shoaib.barbershopapp.Database.CartDatabase;
import com.shoaib.barbershopapp.Database.CartItem;
import com.shoaib.barbershopapp.Database.DatabaseUtils;
import com.shoaib.barbershopapp.Interface.ICartItemLoadListener;
import com.shoaib.barbershopapp.Interface.ICartItemUpdateListener;
import com.shoaib.barbershopapp.Interface.ISumCartListener;

public class CartActivity extends AppCompatActivity implements ICartItemLoadListener, ICartItemUpdateListener, ISumCartListener {

    @BindView(R.id.recycler_cart)
    RecyclerView recycler_cart;
    @BindView(R.id.txt_total_price)
    TextView txt_total_price;
    @BindView(R.id.btn_clear_cart)
    Button btn_clear_cart;

    @OnClick(R.id.btn_clear_cart)
    void clearCart(){

        DatabaseUtils.clearCart((cartDatabase));

        //Update Adapter
        DatabaseUtils.getAllCart(cartDatabase,this);

//        AlertDialog.Builder builder = new AlertDialog.Builder(this)
//                .setTitle("Clear Cart")
//                .setMessage("Do you really want to clear Cart ? ")
//                .setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialog, int which) {
//                        dialog.dismiss();
//                    }
//                }).setPositiveButton("CLEAR", new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialog, int which) {
//                        //DatabaseUtils.clearCart(cartDatabase);
//                        cartDataSource.clearCart(Common.currentUser.getPhoneNumber())
//                                .subscribeOn(Schedulers.io())
//                                .observeOn(AndroidSchedulers.mainThread())
//                                .subscribe(new SingleObserver<Integer>() {
//                                    @Override
//                                    public void onSubscribe(Disposable d) {
//
//                                    }
//
//                                    @Override
//                                    public void onSuccess(Integer integer) {
//
//                                        Toast.makeText(CartActivity.this, "Cart has been Clear!", Toast.LENGTH_SHORT).show();
//                                        //After Done, Just Sum
//
//                                        //We need to Load All Cart, once It gets Cleared
//
//                                        compositeDisposable.add(cartDataSource.getAllItemFromCart(Common.currentUser.getPhoneNumber())
//                                                .subscribeOn(Schedulers.io())
//                                                .observeOn(AndroidSchedulers.mainThread())
//                                                .subscribe(new Consumer<List<CartItem>>() {
//                                                    @Override
//                                                    public void accept(List<CartItem> cartItems) throws Exception {
//                                                        cartDataSource.sumPrice(Common.currentUser.getPhoneNumber())
//                                                                .subscribeOn(Schedulers.io())
//                                                                .observeOn(AndroidSchedulers.mainThread())
//                                                                .subscribe(updatePrice());
//                                                    }
//                                                }, new Consumer<Throwable>() {
//                                                    @Override
//                                                    public void accept(Throwable throwable) throws Exception {
//                                                        Toast.makeText(CartActivity.this, ""+throwable.getMessage(), Toast.LENGTH_SHORT).show();
//                                                    }
//                                                })
//                                        );
//
//                                    }
//
//                                    @Override
//                                    public void onError(Throwable e) {
//                                        Toast.makeText(CartActivity.this, ""+e.getMessage(), Toast.LENGTH_SHORT).show();
//                                    }
//                                });
////
////        //Update Cart
////        DatabaseUtils.getAllCart(cartDatabase,this);
//                        getAllCart();
//                    }
//                });
//
//        AlertDialog dialog = builder.create();
//        dialog.show();

    }


    CartDatabase cartDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);

        ButterKnife.bind(CartActivity.this);

        cartDatabase = CartDatabase.getInstance(this);

        DatabaseUtils.getAllCart(cartDatabase,this);

        //View
        recycler_cart.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        recycler_cart.setLayoutManager(linearLayoutManager);
        recycler_cart.addItemDecoration(new DividerItemDecoration(this, linearLayoutManager.getOrientation()));
    }

    @Override
    public void onGetAllItemFromCartSuccess(List<CartItem> cartItemList) {
        //Here, after we get all cart item from DB
        //We will display by Recycler View
        MyCartAdapter adapter = new MyCartAdapter(this,cartItemList,this);
        recycler_cart.setAdapter(adapter);
    }

    @Override
    public void onCartItemUpdateSuccess() {
        DatabaseUtils.sumCart(cartDatabase,this);
    }

    @Override
    public void onSumCartSuccess(Long value) {
        txt_total_price.setText(new StringBuilder("$").append(value));
    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

    }
}