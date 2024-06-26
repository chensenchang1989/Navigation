package com.scc.navigation.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.scc.navigation.data.SearchAddress
import com.scc.navigation.databinding.ItemSearchDestinationBinding


class SearchAdapter : RecyclerView.Adapter<SearchAdapter.ItemViewHolder>() {

    private var onItemClickListener : ((SearchAddress) -> Unit)? = null

    fun setOnItemClickListener(listener : (SearchAddress) -> Unit){
        onItemClickListener = listener
    }

    private var address = emptyList<SearchAddress>()

    fun setAddress(address : List<SearchAddress>){
        this.address = address
        notifyDataSetChanged()
    }

    inner class ItemViewHolder(private val binding : ItemSearchDestinationBinding) : RecyclerView.ViewHolder(binding.root){
        fun bind(address : SearchAddress){
            with(binding){
                tvLocation.text = address.address
                tvName.text =address.name

                root.setOnClickListener {
                    onItemClickListener?.invoke(address)
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder = ItemViewHolder(
        ItemSearchDestinationBinding.inflate(LayoutInflater.from(parent.context),parent,false))

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) = holder.bind(address = address[position])

    override fun getItemCount(): Int = address.size
}