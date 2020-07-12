package orederondoor.com.projectorder.Customer.ShoppingCart;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import orederondoor.com.projectorder.Customer.CustomerSecondPage.item_detail;
import orederondoor.com.projectorder.OnRefresh;
import orederondoor.com.projectorder.R;


public class CartAdapter extends RecyclerView.Adapter<CartAdapter.MyViewHolder> {

    private ArrayList<ItemsData> dataList;
    Context mcontext;
    OnRefresh refresh;
   // private  ItemsData data;


    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView item_name, item_price, item_quantity,shop_no,item_detail,tv_remove,tv_edit;
        ImageView item_image;


        public MyViewHolder(View view) {
            super(view);
            item_name = (TextView) view.findViewById(R.id.tv_item_name_shopping_Cart);
            item_price = (TextView) view.findViewById(R.id.tv_item_price_shoppin_cart);
            item_detail = (TextView) view.findViewById(R.id.tv_detail_item_Shopping_cart);
            item_image=(ImageView)view.findViewById(R.id.image_cartlist);
            tv_remove=(TextView)view.findViewById(R.id.tv_remove_cartItem);
            tv_edit=(TextView)view.findViewById(R.id.tv_edit_cartItem);
            item_quantity=(TextView) view.findViewById(R.id.tv_item_quantity_shopping_cart);
            //shp_name=(TextView)view.findViewById(R.id)
        }
    }


    public CartAdapter(Context mcontext, ArrayList<ItemsData> dataList, OnRefresh refresh) {
        //super();
        this.dataList = dataList;
        this.mcontext=mcontext;
        this.refresh = refresh;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.shping_cart_item, parent, false);

        return new MyViewHolder(itemView);
    }


    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {

        ItemsData items = dataList.get(position);

            Picasso.with(mcontext).load(dataList.get(position).getItem_image()).into(holder.item_image);
            holder.item_name.setText(items.getItemName());
            holder.item_price.setText(items.getPrice());
            holder.item_detail.setText(items.getItem_detail());
            holder.item_quantity.setText(holder.item_quantity.getText().toString() + "  " + items.getItem_quantity());
            final String name = items.getItemName();

            // Get Data in String Format
            final String item_name = items.getItemName().toString();
            final String item_detail = items.getItem_detail().toString();
            final String item_image = items.getItem_image().toString();
            final String item_price = items.getPrice().toString().trim();
            final String item_quan = items.getItem_quantity().toString();
            final String shop_phone = items.getShop_No();


            holder.tv_remove.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ShoppingCart sc = new ShoppingCart();
                    sc.delete(name);
                    refresh.refresh(true);
                }

            });

            holder.tv_edit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    Intent intent = new Intent(mcontext, orederondoor.com.projectorder.Customer.CustomerSecondPage.item_detail.class);
                    intent.putExtra("ItemName", item_name);
                    intent.putExtra("ItemDetail", item_detail);
                    intent.putExtra("ItemPrice", item_price);
                    intent.putExtra("ItemImage", item_image);
                    intent.putExtra("ÜserType", "UpdateItem");
                    intent.putExtra("ShopPhone", shop_phone);
                    mcontext.startActivity(intent);

                }
            });


       // notifyDataSetChanged();

    }

    @Override
    public int getItemCount() {
            return dataList.size();
    }


}