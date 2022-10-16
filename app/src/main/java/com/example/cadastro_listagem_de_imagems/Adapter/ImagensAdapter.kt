package com.example.appembaixadores.ui.Adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.cadastro_listagem_de_imagems.R
import kotlinx.android.synthetic.main.item_image_cadastro.view.*

class ImagensAdapter(val urls: List<String>): RecyclerView.Adapter<ImagensAdapter.ImageViewHolder>()
{

    inner class ImageViewHolder(itemView: View): RecyclerView.ViewHolder(itemView)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ImageViewHolder
    {
        return ImageViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.item_image_cadastro, parent, false)
        )
    }

    override fun getItemCount(): Int
    {
        return urls.size
    }

    override fun onBindViewHolder(holder: ImageViewHolder, position: Int) {
        val url = urls[position]
        Glide.with(holder.itemView).load(url).into(holder.itemView.image)
    }
}