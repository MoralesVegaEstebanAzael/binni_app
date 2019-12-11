package com.example.proyectoemergentes.adapter;

import android.content.Context;
import android.content.res.Resources;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.proyectoemergentes.MainActivity;
import com.example.proyectoemergentes.R;
import com.example.proyectoemergentes.pojos.ItemShoppingCart;

import java.util.ArrayList;

public class AdapterItemShoppingCart extends RecyclerView.Adapter<AdapterItemShoppingCart.MyViewHolder> {
    private ArrayList<ItemShoppingCart> listItemShopping;
    private Context context;

    public AdapterItemShoppingCart(Context context, ArrayList<ItemShoppingCart> listItemShoppingCarts) {
        this.context = context;
        this.listItemShopping = listItemShoppingCarts;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_view_shopping_cart, parent, false);
        return new MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, final int position) {
        final ItemShoppingCart item = listItemShopping.get(position);

        Glide.with(context)
                .load(context.getString(R.string.url_imagen))
                .error(R.drawable.ic_cloud_off_black_24dp)
                .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC)
                .into(holder.imageView);

        Resources res = context.getResources();
        String precio = String.format(res.getString(R.string.paquete_price), item.getPrecio());
        holder.textViewPrecio.setText(precio);

        String personas = String.format(res.getString(R.string.paquete_item_personas), item.getPersonas());
        holder.textViewPersonas.setText(personas);

        holder.textViewNombre.setText(item.getNombre());
        holder.textViewFecha.setText(item.getFecha());
        holder.iconDelete.setImageResource(R.drawable.ic_delete_black_24dp);
        holder.iconDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MainActivity.dataBaseHandler.removeShoppingCart(item.getId());
                removeAt(position);
            }
        });
    }
    public void removeAt(int position) {
        listItemShopping.remove(position);
        notifyItemRemoved(position);
        notifyItemRangeChanged(position, listItemShopping.size());
    }
    @Override
    public int getItemCount() {
        return listItemShopping.size();
    }

    public  class MyViewHolder extends RecyclerView.ViewHolder {
        private ImageView imageView;
        private ImageView iconDelete;
        private TextView textViewNombre;
        private TextView textViewFecha;
        private TextView textViewPrecio;
        private TextView textViewPersonas;
        private View view;
        public MyViewHolder(View itemView) {
            super(itemView);
            view = itemView;
            imageView = itemView.findViewById(R.id.shoppingImgPaquete);
            iconDelete = itemView.findViewById(R.id.shoppingDeleteItem);
            textViewNombre = itemView.findViewById(R.id.shoppingNombrePaquete);
            textViewFecha = itemView.findViewById(R.id.shoppingFechaPaquete);
            textViewPrecio = itemView.findViewById(R.id.shoppingPrecioPaquete);
            textViewPersonas = itemView.findViewById(R.id.shoppingPersonas);
        }
    }
}
