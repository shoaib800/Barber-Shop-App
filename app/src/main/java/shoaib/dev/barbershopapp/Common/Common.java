package shoaib.dev.barbershopapp.Common;

import shoaib.dev.barbershopapp.Model.User;

public class Common {

    public static String IS_LOGIN ="IsLogin";
    public static User currentUser;
    public static String formatShoppingItemName;

    public static String formatShoppingItemName(String name) {
        return name.length() > 13 ? new StringBuilder(name.substring(0, 10)).append("...").toString():name;
    }
}
