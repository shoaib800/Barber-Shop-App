package shoaib.dev.barbershopapp.Interface;

import java.util.List;

import shoaib.dev.barbershopapp.Database.CartItem;

public interface ICartItemLoadListener {
    void onGetAllItemFromCartSuccess(List<CartItem> cartItemList);
}
