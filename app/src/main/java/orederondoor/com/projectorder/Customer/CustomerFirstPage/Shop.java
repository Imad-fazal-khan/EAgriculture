package orederondoor.com.projectorder.Customer.CustomerFirstPage;

public class Shop {
    String shop_name,shp_type,shop_location,shop_image,shop_phone,user_type;
    int x;




    public Shop(String shop_name, String shp_type, String shop_location, String shop_image, String shop_phone) {
        this.shop_name = shop_name;
        this.shp_type = shp_type;
        this.shop_location = shop_location;
        this.shop_image = shop_image;

        this.shop_phone = shop_phone;
    }


    public Shop(String shop_name, String shp_type, String shop_location, String shop_image,String user_type,String shop_phone) {
        this.shop_name = shop_name;
        this.shp_type = shp_type;
        this.shop_location = shop_location;
        this.shop_image =  shop_image;
        this.user_type=user_type;
        this.shop_phone=shop_phone;
    }

    public Shop(String shop_name, String shp_type, String shop_location, String shop_image,String user_type,int x) {
        this.shop_name = shop_name;
        this.shp_type = shp_type;
        this.shop_location = shop_location;
        this.shop_image =  shop_image;
        this.user_type=user_type;
        //this.shop_phone=shop_phone;
    }

    public String getShop_name() {
        return shop_name;
    }

    public void setShop_name(String shop_name) {
        this.shop_name = shop_name;
    }

    public String getShp_type() {
        return shp_type;
    }

    public void setShp_type(String shp_type) {
        this.shp_type = shp_type;
    }

    public String getShop_location() {
        return shop_location;
    }

    public void setShop_location(String shop_location) {
        this.shop_location = shop_location;
    }

    public String getShop_image() {
        return shop_image;
    }

    public void setShop_image(String shop_image) {
        this.shop_image = shop_image;
    }

    public String getShop_phone() {
        return shop_phone;
    }

    public void setShop_phone(String shop_phone) {
        this.shop_phone = shop_phone;
    }
    public String getUser_type() {
        return user_type;
    }

    public void setUser_type(String user_type) {
        this.user_type = user_type;
    }
}