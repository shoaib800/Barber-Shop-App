package com.shoaib.barbershopapp.Database;

import android.database.sqlite.SQLiteConstraintException;
import android.os.AsyncTask;

import androidx.annotation.NonNull;

import com.google.firebase.auth.FirebaseAuth;

import java.util.List;

import com.shoaib.barbershopapp.Common.Common;
import com.shoaib.barbershopapp.Interface.ICartItemLoadListener;
import com.shoaib.barbershopapp.Interface.ICountItemInCartListener;
import com.shoaib.barbershopapp.Interface.ISumCartListener;

public class DatabaseUtils {
    //Because all room handle need work on other thread

    public static void clearCart(CartDatabase db)
    {
        ClearCartAsync task = new ClearCartAsync(db);
        task.execute();
    }

    public static void sumCart(CartDatabase db, ISumCartListener iSumCartListener)
    {
        SumCartAsync task = new SumCartAsync(db,iSumCartListener);
        task.execute();
    }


    public static void getAllCart(CartDatabase db, ICartItemLoadListener cartItemLoadListener)
    {
        GetAllCartAsync task = new GetAllCartAsync(db, cartItemLoadListener);
        task.execute();
    }

    public static void updateCart(CartDatabase db,CartItem cartItem)
    {
        UpdateCartAsync task = new UpdateCartAsync(db);
        task.execute(cartItem);
    }

    public static void insertToCart(CartDatabase db,CartItem...cartItems)
    {
        InsetToCartAsync task = new  InsetToCartAsync(db);
        task.execute(cartItems);
    }

    public static void countItemInCart(CartDatabase db, ICountItemInCartListener iCountItemInCartListener)
    {
        CountItemInCartAsync task = new  CountItemInCartAsync(db, iCountItemInCartListener);
        task.execute();
    }

    public static void deleteCart(@NonNull final CartDatabase db, CartItem cartItem)
    {
        DeleteCartAsync task = new DeleteCartAsync(db);
        task.execute(cartItem);
    }

    /*
    =====================================================================================
    ASYNC TASK DEIFNE
    =====================================================================================
     */

    private static class SumCartAsync extends AsyncTask<Void,Void,Long> {
        private final CartDatabase db;
        private final ISumCartListener listener;

        public SumCartAsync(CartDatabase db, ISumCartListener listener) {
            this.db = db;
            this.listener = listener;
        }

        @Override
        protected Long doInBackground(Void... voids) {
            FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
            return db.cartDAO().sumPrice(firebaseAuth.getCurrentUser().getPhoneNumber());
        }

        @Override
        protected void onPostExecute(Long aLong) {
            super.onPostExecute(aLong);
            listener.onSumCartSuccess(aLong);
        }
    }

    private static class UpdateCartAsync extends AsyncTask<CartItem,Void,Void>{

        private final CartDatabase db;

        public UpdateCartAsync(CartDatabase db) {
            this.db = db;
        }

        @Override
        protected Void doInBackground(CartItem... cartItems) {
            db.cartDAO().update(cartItems[0]);
            return null;
        }
    }

    private static class GetAllCartAsync extends AsyncTask<String,Void,List<CartItem>> {

        CartDatabase db;
        ICartItemLoadListener listener;
        public GetAllCartAsync(CartDatabase cartDatabase,ICartItemLoadListener iCartItemLoadListener){
            db = cartDatabase;
            listener= iCartItemLoadListener;

        }



        @Override
        protected List<CartItem> doInBackground(String... strings) {
            FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
            return db.cartDAO().getAllItemFromCart(firebaseAuth.getCurrentUser().getPhoneNumber());
        }

        @Override
        protected void onPostExecute(List<CartItem> cartItems) {
            super.onPostExecute(cartItems);
            listener.onGetAllItemFromCartSuccess(cartItems);
        }
    }

    private static class InsetToCartAsync extends AsyncTask<CartItem,Void,Void>{

        CartDatabase db;
        public InsetToCartAsync(CartDatabase cartDatabase) {
            db = cartDatabase;
        }

        @Override
        protected Void doInBackground(CartItem... cartItems) {
            insertToCart(db,cartItems[0]);
            return null;
        }

        private void insertToCart(CartDatabase db, CartItem cartItem) {

            FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
            //If item already available in the cart, just increase quantity
            try {
                db.cartDAO().insert(cartItem);
            }catch (SQLiteConstraintException exception)
            {


                CartItem updateCartItem = db.cartDAO().getProductInCart(cartItem.getProductId(),
                        firebaseAuth.getCurrentUser().getPhoneNumber());
                updateCartItem.setProductQuantity(updateCartItem.getProductQuantity()+1);
                db.cartDAO().update(updateCartItem);
            }
        }
    }

    private static class CountItemInCartAsync extends AsyncTask<Void,Void,Integer>{

        CartDatabase db;
        ICountItemInCartListener listener;
        public CountItemInCartAsync(CartDatabase cartDatabase,ICountItemInCartListener iCountItemInCartListener) {
            db = cartDatabase;
            listener = iCountItemInCartListener;
        }


        @Override
        protected Integer doInBackground(Void... voids) {
            countItemInCartRun(db);
            return Integer.parseInt(String.valueOf(countItemInCartRun(db)));
        }

        @Override
        protected void onPostExecute(Integer integer) {
            super.onPostExecute(integer);

            listener.onCartItemCountSuccess(integer.intValue());
        }

        private int countItemInCartRun(CartDatabase db) {
            FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
            return db.cartDAO().countItemInCart(firebaseAuth.getCurrentUser().getPhoneNumber());
        }
    }

    private static class DeleteCartAsync extends AsyncTask<CartItem,Void,Void>{

        private final CartDatabase db;

        public DeleteCartAsync(CartDatabase db) {
            this.db = db;
        }

        @Override
        protected Void doInBackground(CartItem... cartItems) {
            db.cartDAO().delete(cartItems[0]);
            return null;
        }
    }

    private static class ClearCartAsync extends AsyncTask<Void,Void,Void>{

        private final CartDatabase db;

        public ClearCartAsync(CartDatabase db) {
            this.db = db;
        }

        @Override
        protected Void doInBackground(Void... voids) {
            clearAllItemFromCart(db);
            return null;
        }

        private void clearAllItemFromCart(CartDatabase db) {
            FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
            db.cartDAO().clearCart(firebaseAuth.getCurrentUser().getPhoneNumber());
        }
    }


}
