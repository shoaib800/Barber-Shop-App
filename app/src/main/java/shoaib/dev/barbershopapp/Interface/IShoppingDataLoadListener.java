package shoaib.dev.barbershopapp.Interface;

import java.util.List;

import shoaib.dev.barbershopapp.Model.ShoppingItem;

public interface IShoppingDataLoadListener {
    void onShoppingDataLoadSuccess(List<ShoppingItem> shoppingItemList);
    void onShoppingDataLoadFailed(String message);
}
