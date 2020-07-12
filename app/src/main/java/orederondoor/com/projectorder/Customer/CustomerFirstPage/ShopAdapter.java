package orederondoor.com.projectorder.Customer.CustomerFirstPage;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.DrawableRes;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.List;

import orederondoor.com.projectorder.Customer.CustomerSecondPage.SecondActivity;
import orederondoor.com.projectorder.R;
import orederondoor.com.projectorder.Shopkeeper.RecyclerView.RecyclerViewListner;


public class ShopAdapter extends RecyclerView.Adapter<ShopAdapter.MyViewHolder>  {

    private Context mcontext;
    private List<Shop> mshop;
    //private List<Shop> shopList;


    private RecyclerViewShop recyclerViewShop;

    //constructor
    public ShopAdapter(Context mcontext, List<Shop> mshop) {
        this.mcontext = mcontext;
        this.mshop = mshop;
       // shopList = new ArrayList<>(mshop);

    }

    // View Holder Class which get the id from other layout to bind data
    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView shop_name, shop_type ;
        ImageView shop_image;
        CardView cardView;

        public MyViewHolder(View view) {
            super(view);
            shop_name = (TextView) view.findViewById(R.id.shop_view_Sname);
            shop_type=(TextView)view.findViewById(R.id.tv_shopView_shopType);
            shop_image = (ImageView) view.findViewById(R.id.shop_view_image);


            cardView = (CardView) view.findViewById(R.id.shop_cardView);

        }


    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.shop_view, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {
        Picasso.with(mcontext).load(mshop.get(position).getShop_image()).into(holder.shop_image);
        holder.shop_name.setText(mshop.get(position).getShop_name());
        holder.shop_type.setText(mshop.get(position).getShp_type());
        final String shop_phone = mshop.get(position).getShop_phone();
        final String shop_name = mshop.get(position).getShop_name();
        final String shop_location = mshop.get(position).getShop_location();
        final String shop_type = mshop.get(position).getShp_type();
        final String shop_image = mshop.get(position).getShop_image();


        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mcontext, SecondActivity.class);
                intent.putExtra("PhoneNo", shop_phone);
                intent.putExtra("ShopName", shop_name);
                intent.putExtra("ShopLocation", shop_location);
                intent.putExtra("ShopType", shop_type);
                intent.putExtra("ShopImage", shop_image);

                mcontext.startActivity(intent);

            }
        });
    }

    @Override
    public int getItemCount() {
        return mshop.size();



    }



    public void updateList(List<Shop> shopList)
    {
        mshop=new ArrayList<>();
        mshop.addAll(shopList);
        notifyDataSetChanged();
    }
}