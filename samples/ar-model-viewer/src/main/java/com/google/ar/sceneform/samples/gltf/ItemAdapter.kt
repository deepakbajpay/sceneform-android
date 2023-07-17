package com.google.ar.sceneform.samples.gltf

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder

class ItemAdapter(val itemList: ArrayList<Int>,val callback : (Int)->Unit) :RecyclerView.Adapter<ItemAdapter.ItemViewHolder> (){

    inner class ItemViewHolder(view : View):ViewHolder(view){

        val image:ImageView = view.findViewById(R.id.item_iv)
        fun bindView(item:Int){
            image.setImageResource(item)
            image.setOnClickListener { callback(item) }
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        return ItemViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_layout,parent,false))
    }

    override fun getItemCount(): Int { return itemList.size }

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        holder.bindView(itemList[position])
    }
}