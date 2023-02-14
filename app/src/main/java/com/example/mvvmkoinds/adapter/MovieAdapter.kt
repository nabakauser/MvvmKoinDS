package com.example.mvvmkoinds.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.mvvmkoinds.warehouse.Constants.IMAGE_URL
import com.example.mvvmkoinds.R
import com.example.mvvmkoinds.data.model.Result

class MovieAdapter(
    private val movieList: ArrayList<Result>,
    private val onItemClick: (Result) -> Unit,
    private val onFavouritesClicked: (Result) -> Unit,
) : RecyclerView.Adapter<MovieAdapter.MyViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(
            R.layout.movie_layout,
            parent,
            false
        )
        return MyViewHolder(view)
    }


    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val listPosition = movieList[position]

        with(holder) {
            uiTvId.text = "ID: " + listPosition.id.toString()
            uiTvTitle.text = listPosition.title
            uiTvLanguage.text = "Original Language: " + listPosition.original_language
            uiTvRating.text = "Rating: " + listPosition.vote_average.toString()
            Glide.with(itemView)
                .load(IMAGE_URL + listPosition.poster_path)
                .placeholder(R.drawable.ic_unsupported_image) // will display till the image is getting loading
                //.error(R.mipmap.ic_launcher) // will be displayed if the image cannot be loaded
                .into(holder.uiIvDisplayImage)
            if (listPosition.isSaved) {
                uiIvFavourites.setImageResource(R.drawable.ic_colored_favourite)
            } else {
                uiIvFavourites.setImageResource(R.drawable.ic_favourite)
            }
        }
    }

    override fun getItemCount(): Int {
        return movieList.size
    }

    fun onUserListChanged(list: List<Result>) {
        this.movieList.clear()
        list.let {this.movieList.addAll(it) }
        notifyDataSetChanged()
    }

    inner class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var uiTvTitle: TextView = itemView.findViewById(R.id.uiTvTitle)
        var uiIvDisplayImage: ImageView = itemView.findViewById(R.id.uiIvMovieImage)
        var uiTvId: TextView = itemView.findViewById(R.id.uiTvId)
        var uiTvLanguage: TextView = itemView.findViewById(R.id.uiTvLanguage)
        var uiTvRating: TextView = itemView.findViewById(R.id.uiTvRating)
        var uiIvFavourites: ImageView = itemView.findViewById(R.id.uiIvFavourites)

        init {
            uiIvDisplayImage.setOnClickListener {
                onItemClick(movieList[adapterPosition])
            }
            uiIvFavourites.setOnClickListener {
                onFavouritesClicked(movieList[adapterPosition])
                notifyDataSetChanged()
            }
        }
    }
}
