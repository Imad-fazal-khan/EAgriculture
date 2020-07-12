package orederondoor.com.projectorder.Customer.ShoppingCart;

public class ItemsData {
    String itemName,price,shop_id,item_image,item_quantity,item_detail,shop_No;

    public ItemsData(String itemName, String price, String item_image, String item_detail) {
        this.itemName = itemName;
        this.price = price;
        this.item_image = item_image;
        this.item_detail = item_detail;
    }

    public ItemsData(String itemName, String price, String item_image, String item_detail,String item_quantity,String shop_No,int id) {
        this.itemName = itemName;
        this.price = price;
        this.item_image = item_image;
        this.item_detail = item_detail;
        this.item_quantity=item_quantity;
        this.shop_No=shop_No;
    }


    public ItemsData(String itemName, String price, String shop_id, String item_image, String item_quantity, String item_detail) {
        this.itemName = itemName;
        this.price = price;
        this.shop_id = shop_id;
        this.item_image = item_image;
        this.item_quantity = item_quantity;
        this.item_detail = item_detail;
    }

    public String getShop_No() {
        return shop_No;
    }

    public void setShop_No(String shop_No) {
        this.shop_No = shop_No;
    }

    public String getShop_id() {
        return shop_id;
    }

    public void setShop_id(String shop_id) {
        this.shop_id = shop_id;
    }

    public String getItem_image() {
        return item_image;
    }

    public void setItem_image(String item_image) {
        this.item_image = item_image;
    }

    public String getItem_quantity() {
        return item_quantity;
    }

    public void setItem_quantity(String item_quantity) {
        this.item_quantity = item_quantity;
    }

    public String getItem_detail() {
        return item_detail;
    }

    public void setItem_detail(String item_detail) {
        this.item_detail = item_detail;
    }

    public ItemsData() {
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }


}
