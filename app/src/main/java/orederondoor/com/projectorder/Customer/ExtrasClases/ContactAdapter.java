package orederondoor.com.projectorder.Customer.ExtrasClases;


import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import java.util.ArrayList;
import java.util.List;

import orederondoor.com.projectorder.Customer.CustomerFirstPage.Shop;
import orederondoor.com.projectorder.R;

/**
 * Created by ravi on 16/11/17.
 */

class ContactsAdapter extends RecyclerView.Adapter<ContactsAdapter.MyViewHolder>
        implements Filterable {
    private Context context;
    private List<Shop> contactList;
    private List<Shop> contactListFiltered;
    private ContactsAdapterListener listener;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView shop_name;
        public ImageView shop_image;
        CardView cardView;
        public MyViewHolder(View view) {
            super(view);
            shop_name = (TextView) view.findViewById(R.id.shop_view_Sname);
            shop_image = (ImageView) view.findViewById(R.id.shop_view_image);


            cardView=(CardView)view.findViewById(R.id.shop_cardView);

            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    // send selected contact in callback
                    listener.onContactSelected(contactListFiltered.get(getAdapterPosition()));
                }
            });
        }
    }


    public ContactsAdapter(Context context, List<Shop> contactList, ContactsAdapterListener listener) {
        this.context = context;
        this.listener = listener;
        this.contactList = contactList;
        this.contactListFiltered = contactList;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.shop_view, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {
        final Shop contact = contactListFiltered.get(position);
        holder.shop_name.setText(contact.getShop_name());
        //holder.phone.setText(contact.getPhone());

        Glide.with(context)
                .load(contact.getShop_image())
                .apply(RequestOptions.circleCropTransform())
                .into(holder.shop_image);
    }

    @Override
    public int getItemCount() {
        return contactListFiltered.size();
    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {
                String charString = charSequence.toString();
                if (charString.isEmpty()) {
                    contactListFiltered = contactList;
                } else {
                    List<Shop> filteredList = new ArrayList<>();
                    for (Shop row : contactList) {

                        // name match condition. this might differ depending on your requirement
                        // here we are looking for name or phone number match
                        if (row.getShop_image().toLowerCase().contains(charString.toLowerCase())) {
                            filteredList.add(row);
                        }
                    }

                    contactListFiltered = filteredList;
                }

                FilterResults filterResults = new FilterResults();
                filterResults.values = contactListFiltered;
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                contactListFiltered = (ArrayList<Shop>) filterResults.values;
                notifyDataSetChanged();
            }
        };
    }

    public interface ContactsAdapterListener {
        void onContactSelected(Shop contact);
    }
}