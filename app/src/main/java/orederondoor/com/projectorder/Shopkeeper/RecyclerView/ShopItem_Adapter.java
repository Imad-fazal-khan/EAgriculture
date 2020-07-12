package orederondoor.com.projectorder.Shopkeeper.RecyclerView;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import orederondoor.com.projectorder.R;


public class ShopItem_Adapter extends RecyclerView.Adapter<ShopItem_Adapter.ViewHolder> {

private List<Item_Details> itemList;

    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView itemName,itemPrice,itemDes;
        public ViewHolder(@NonNull View itemsView) {
            super(itemsView);
            itemName=(TextView)itemsView.findViewById(R.id.tv_item_inShop_name);
            itemPrice=(TextView)itemsView.findViewById(R.id.tv_item_inShop_price);
            itemDes=(TextView)itemsView.findViewById(R.id.tv_item_inShop_description);


        }
    }
    public ShopItem_Adapter(List<Item_Details> itemsList) {
        this.itemList = itemsList;
    }

        @NonNull
    @Override
    public ShopItem_Adapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView= LayoutInflater.from(parent.getContext()).inflate(R.layout.item_in_shop , parent,false);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ShopItem_Adapter.ViewHolder holder, int position) {
        Item_Details item=itemList.get(position);
        holder.itemName.setText(item.getItemName());
        holder.itemPrice.setText(item.getItemPrice());
        holder.itemDes.setText(item.getItemDescription());

    }

    @Override
    public int getItemCount() {
        return itemList.size();
    }


}
