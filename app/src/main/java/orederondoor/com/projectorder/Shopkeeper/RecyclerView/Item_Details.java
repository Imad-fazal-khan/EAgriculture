package orederondoor.com.projectorder.Shopkeeper.RecyclerView;

public class Item_Details {
    private String itemName;
    private String itemPrice;
    private String itemDescription;

    public Item_Details()
    {

    }
    public Item_Details(String itemname,String itemprice,String itemdescription)
    {
        this.itemName=itemname;
        this.itemDescription=itemdescription;
        this.itemPrice=itemprice;
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public String getItemPrice() {
        return itemPrice;
    }
    public void setItemPrice(String itemPrice) {
        this.itemPrice = itemPrice;
    }

    public void setItemDescription(String itemDescription) {
        this.itemDescription = itemDescription;
    }

    public String getItemDescription() {
        return itemDescription;
    }
}
