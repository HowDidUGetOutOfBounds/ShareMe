package com.example.mvvmalarm.UI

import android.net.Uri
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.mvvmalarm.data.CustomPoint
import com.example.mvvmalarm.databinding.RecyclerPointItemBinding


class PointAdapter(private val onItemClicked: (CustomPoint) -> Unit) :
    ListAdapter<CustomPoint, PointAdapter.PointViewHolder>(DiffCallback) {


    class PointViewHolder(private var binding: RecyclerPointItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(point: CustomPoint) {
            if(point.imgUri != null) {
                binding.image.setImageURI(Uri.parse(point.imgUri))
            }
            binding.title.text = point.message
            binding.date.text = point.date
            binding.latlon.text = String.format("Lat: %.4f , Lon: %.4f", point.lat, point.lon)
        }
    }


    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): PointAdapter.PointViewHolder {
        val viewHolder = PointViewHolder(
            RecyclerPointItemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
        viewHolder.itemView.setOnClickListener {
            val position = viewHolder.adapterPosition
            onItemClicked(getItem(position))
        }
        return viewHolder
    }

    override fun onBindViewHolder(holder: PointViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    companion object {
        private val DiffCallback = object : DiffUtil.ItemCallback<CustomPoint>() {
            override fun areItemsTheSame(oldItem: CustomPoint, newItem: CustomPoint): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(oldItem: CustomPoint, newItem: CustomPoint): Boolean {
                return oldItem == newItem
            }
        }
    }
}