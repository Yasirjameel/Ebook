package com.example.ym.ecommerce.Viewholder;

import androidx.recyclerview.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.example.ym.ecommerce.R;
import com.example.ym.ecommerce.Interface.itemclicklistner;

    public class CartViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener
    {
        public TextView txtProductName, txtProductPrice, txtProductQuantity;
        private itemclicklistner itemClickListner;


        public CartViewHolder(View itemView)
        {
            super(itemView);

            txtProductName = itemView.findViewById(R.id.cart_product_name);
            txtProductPrice = itemView.findViewById(R.id.cart_product_price);
            txtProductQuantity = itemView.findViewById(R.id.cart_product_quantity);
        }

        @Override
        public void onClick(View view)
        {
            itemClickListner.onClick(view, getAdapterPosition(), false);
        }

        public void setItemClickListner(itemclicklistner itemClickListner)
        {
            this.itemClickListner = itemClickListner;
        }
    }


