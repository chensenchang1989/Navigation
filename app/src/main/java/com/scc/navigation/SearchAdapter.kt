package com.scc.navigation

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.scc.navigation.data.SearchAddress
import com.scc.navigation.databinding.ItemSearchDestinationBinding


class SearchAdapter : RecyclerView.Adapter<SearchAdapter.ItemViewHolder>() {

    private var onItemClickListener : ((String) -> Unit)? = null

    fun setOnItemClickListener(listener : (String) -> Unit){
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

                root.setOnClickListener {
                    onItemClickListener?.invoke(address.address.orEmpty())
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder = ItemViewHolder(
        ItemSearchDestinationBinding.inflate(LayoutInflater.from(parent.context),parent,false))

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) = holder.bind(address = address[position])

    override fun getItemCount(): Int = address.size
}