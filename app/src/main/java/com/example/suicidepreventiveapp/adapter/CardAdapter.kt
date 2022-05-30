package com.example.suicidepreventiveapp.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.suicidepreventiveapp.data.DiaryCard
import com.example.suicidepreventiveapp.databinding.ItemRowCardsBinding

class CardAdapter(private var listCard: ArrayList<DiaryCard>) : RecyclerView.Adapter<CardAdapter.ListViewHolder>() {
    class ListViewHolder(private val binding: ItemRowCardsBinding) : RecyclerView.ViewHolder(binding.root) {
        fun getCard(data: DiaryCard) {
            binding.apply {
                Glide.with(itemView)
                    .load(data.photo)
                    .circleCrop()
                    .into(imgItemCard)
                tvItemCard.text = data.title
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CardAdapter.ListViewHolder {
        return ListViewHolder(ItemRowCardsBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
        holder.getCard(listCard[position])
    }

    override fun getItemCount(): Int {
        return listCard.size
    }
}