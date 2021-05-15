package com.davoh.oauth2_twitch.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.davoh.oauth2_twitch.R
import com.davoh.oauth2_twitch.databinding.RowTopGameBinding
import com.davoh.oauth2_twitch.domain.Game

class TopGamesAdapter : ListAdapter<Game, RecyclerView.ViewHolder>(DiffCallback())  {

    private var listener: OnItemClickListener? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return ItemViewHolder(
            DataBindingUtil.inflate(LayoutInflater.from(parent.context), R.layout.row_top_game,parent, false)
        )
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        (holder as ItemViewHolder).bind(getItem(position))
    }

    inner class ItemViewHolder(private val binding: RowTopGameBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(item: Game) {
            binding.tvName.text = item.name
            binding.btnFav.setOnClickListener {
                listener?.onFavoriteBtnClick(item)
            }
            Glide.with(binding.root).load(item.urlImage).into(binding.imgGame)
        }
    }

    interface OnItemClickListener {
        fun onFavoriteBtnClick(game: Game)
    }
    fun setOnItemClickListener(listener: OnItemClickListener) {
        this.listener = listener
    }
}

class DiffCallback : DiffUtil.ItemCallback<Game>() {
    override fun areItemsTheSame(oldItem: Game, newItem: Game): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: Game, newItem: Game): Boolean {
        return oldItem.id == newItem.id
    }
}