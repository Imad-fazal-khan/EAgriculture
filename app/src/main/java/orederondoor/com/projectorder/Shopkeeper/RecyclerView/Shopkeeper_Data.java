package orederondoor.com.projectorder.Shopkeeper.RecyclerView;

public class Shopkeeper_Data {
    private String name, description, price,image;

    public  Shopkeeper_Data()
    {

    }
        public Shopkeeper_Data(String title, String price, String description) {
            this.name = title;
            this.description = description;
            this.price = price;
        }

    public Shopkeeper_Data(String name, String description, String price, String image) {
        this.name = name;
        this.description = description;
        this.price = price;
        this.image = image;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getName() {
            return this.name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getDescription() {
            return this.description;
        }

        public void setDescription(String description) {
            this.description = description;
        }

        public String getPrice() {
            return price;
        }

        public void setPrice(String price) {
            this.price = price;
        }
}
