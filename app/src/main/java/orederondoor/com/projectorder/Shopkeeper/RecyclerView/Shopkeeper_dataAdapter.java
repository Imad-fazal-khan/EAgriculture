package orederondoor.com.projectorder.Shopkeeper.RecyclerView;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import orederondoor.com.projectorder.Customer.CustomerFirstPage.RecyclerViewShop;
import orederondoor.com.projectorder.Customer.CustomerFirstPage.Shop;
import orederondoor.com.projectorder.R;
import orederondoor.com.projectorder.Shopkeeper.Shopkeeper_Pages.Shopkeeper_Page;


public class Shopkeeper_dataAdapter extends RecyclerView.Adapter<Shopkeeper_dataAdapter.MyViewHolder>{


        private List<Shopkeeper_Data> itemList;
        Context context;
        RecyclerViewShop listner;

  /*  public Shopkeeper_dataAdapter(Context context, ArrayList<Shopkeeper_Data> itemList) {
        this.context=context;
        this.itemList=itemList;
    }*/

    public class MyViewHolder extends RecyclerView.ViewHolder {
            public TextView title, price, detail;
            Button btn_add;
            ImageView image;

            public MyViewHolder(View view) {
                super(view);
                title = (TextView)view.findViewById(R.id.tv_item_toAddShop_name) ;
                detail = (TextView) view.findViewById(R.id.tv_item_toAddShop_description);
                price = (TextView) view.findViewById(R.id.tv_item_toAddShop_price);
                btn_add=(Button) view.findViewById(R.id.btn_add_item_toShop);
                image=(ImageView) view.findViewById(R.id.iv_item_toAddShop_image);
            }
        }


        public Shopkeeper_dataAdapter(Context mcontext, List<Shopkeeper_Data> itemList, RecyclerViewShop listner) {
            this.itemList = itemList;
            this.listner=listner;
            context=mcontext;
        }

        @Override
        public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View itemView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.items_for_shopkeeper, parent, false);

            return new MyViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(MyViewHolder holder, final int position) {
            Shopkeeper_Data movie = itemList.get(position);
            holder.title.setText(movie.getName());
            holder.detail.setText(movie.getDescription());
            holder.price.setText(movie.getPrice());
            Picasso.with(context).load(movie.getImage()).into(holder.image);

           final String item_name=movie.getName().toString().trim();
           final String item_detail=movie.getDescription().toString().trim();
           final String item_price=movie.getPrice().toString().trim();
           final String item_image=movie.getImage().toString().trim();

            holder.btn_add.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                // Shopkeeper_Page shopkeeper_page=new Shopkeeper_Page();
                 //shopkeeper_page.startPosting(item_name,item_price,item_detail,item_image);
                    Intent intent= new Intent(context,Shopkeeper_Page.class);
                    intent.putExtra("Name",item_name);
                    intent.putExtra("Price",item_price);
                    intent.putExtra("Detail",item_detail);

                            listner.onClickItem(v, position);


                }
            });

        }

        @Override
        public int getItemCount() {
            return itemList.size();
        }

    public void updateList(List<Shopkeeper_Data> shopList)
    {
        itemList=new ArrayList<>();
        itemList.addAll(shopList);
        notifyDataSetChanged();
    }
    }

