package com.shoaib.barbershopapp.Interface;

import java.util.List;

import com.shoaib.barbershopapp.Model.ShoppingItem;

public interface IShoppingDataLoadListener {
    void onShoppingDataLoadSuccess(List<ShoppingItem> shoppingItemList);
    void onShoppingDataLoadFailed(String message);
}
