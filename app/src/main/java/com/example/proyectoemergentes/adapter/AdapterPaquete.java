package com.example.proyectoemergentes.adapter;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.proyectoemergentes.R;
import com.example.proyectoemergentes.dataBase.DataBaseHandler;
import com.example.proyectoemergentes.pojos.Paquete;
import com.example.proyectoemergentes.ui.PaqueteActivity;

import java.util.ArrayList;

public class AdapterPaquete extends RecyclerView.Adapter<AdapterPaquete.MyViewHolder>{
    private ArrayList<Paquete> listPaquete;
    private Context context;
    private DataBaseHandler localDB;
    public AdapterPaquete(Context context, ArrayList<Paquete> listPaquete) {
        this.context = context;
        this.listPaquete = listPaquete;
        localDB = new DataBaseHandler(context);
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent,
                                                        int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_view_paquete, parent, false);
        return new MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {
        final Paquete paquete = listPaquete.get(position);

        Glide.with(context)
                .load(paquete.getUrlImagen())
                .error(R.drawable.ic_cloud_off_black_24dp)
                .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC)
                .into(holder.imageView);
        holder.textViewNombre.setText(paquete.getNombre());
        Resources res = context.getResources();
        String precio = String.format(res.getString(R.string.paquete_precio_individual), paquete.getPrecio());

        holder.textViewPrecio.setText(precio);

        holder.imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, PaqueteActivity.class);
                /*Bundle b=new Bundle();
                b.putParcelable("PAQUETE",paquete);
                b.putStringArrayList("LUGARES",paquete.getLugares());
                b.putString("ID_PAQUETE",paquete.getId());
                intent.putExtras(b);
                */
                intent.putExtra("PAQUETECLASS", paquete);

                context.startActivity(intent);

            }
        });
    }
    @Override
    public int getItemCount() {
        return listPaquete.size();
    }
    public  class MyViewHolder extends RecyclerView.ViewHolder {
        private ImageView imageView;
        private TextView textViewNombre;
        private TextView textViewPrecio;
        private View view;
        public MyViewHolder(View itemView) {
            super(itemView);
            view = itemView;
            imageView = itemView.findViewById(R.id.paquete_img);
            textViewNombre = itemView.findViewById(R.id.paquete_nombre);
            textViewPrecio = itemView.findViewById(R.id.paquete_precio);
        }
    }
}
