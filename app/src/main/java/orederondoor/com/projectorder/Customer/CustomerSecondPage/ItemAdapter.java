package orederondoor.com.projectorder.Customer.CustomerSecondPage;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import orederondoor.com.projectorder.Customer.CustomerFirstPage.RecyclerViewShop;
import orederondoor.com.projectorder.Customer.CustomerFirstPage.Shop;
import orederondoor.com.projectorder.R;


public class ItemAdapter extends RecyclerView.Adapter<ItemAdapter.MyViewHolder> {
    private Context mcontext;
    private List<Shop> mshop;

    private RecyclerViewShop recyclerViewShop;

    //constructor
    public ItemAdapter(Context mcontext, List<Shop> mshop) {
        this.mcontext = mcontext;
        this.mshop = mshop;

    }

    // View Holder Class which get the id from other layout to bind data
    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView item_name, item_price;
        ImageView item_image;
        CardView cardView;

        public MyViewHolder(View view) {
            super(view);
            item_name = (TextView) view.findViewById(R.id.shop_view_Sname);
            item_image = (ImageView) view.findViewById(R.id.shop_view_image);
            item_price = (TextView) view.findViewById(R.id.tv_shopView_shopType);
            cardView = (CardView) view.findViewById(R.id.shop_cardView);

        }


    }

    @NonNull
    @Override
    public ItemAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.shop_view, parent, false);

        return new ItemAdapter.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(ItemAdapter.MyViewHolder holder, int position) {
        // holder.item_image.setImageResource(R.drawable.d);


     /* *//*  int length=mshop.get(position).getShop_image().toString().trim().length();
        if ( length<12) {*//*
            holder.item_image.setImageResource(R.drawable.loading_logo);
        } else {*/
            Picasso.with(mcontext).load(mshop.get(position).getShop_image()).into(holder.item_image);
       // }/**/

        holder.item_name.setText(mshop.get(position).getShop_name());
        holder.item_price.setText(mshop.get(position).getShop_location());

        final String item_name = mshop.get(position).getShop_name();
        final String item_detail = mshop.get(position).getShp_type();
        final String item_price = mshop.get(position).getShop_location();
        final String item_image=mshop.get(position).getShop_image();
        final String user_type=mshop.get(position).getUser_type();
        final String shop_phone=mshop.get(position).getShop_phone();


        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mcontext, item_detail.class);
                intent.putExtra("ItemName", item_name);
                intent.putExtra("ItemDetail", item_detail);
                intent.putExtra("ItemPrice", item_price);
                intent.putExtra("ItemImage",item_image);
                intent.putExtra("ÜserType",user_type);
                intent.putExtra("ShopPhone",shop_phone);
                mcontext.startActivity(intent);

            }
        });
    }

    @Override
    public int getItemCount() {
        return mshop.size();
    }

    // Filter RecyclerView

    public void updateList(List<Shop> shopList)
    {
        mshop=new ArrayList<>();
        mshop.addAll(shopList);
        notifyDataSetChanged();
    }
}

