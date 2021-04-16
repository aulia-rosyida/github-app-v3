package com.dicoding.auliarosyida.githubuser.adapter

import android.app.Activity
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.dicoding.auliarosyida.githubuser.CustomClickListener
import com.dicoding.auliarosyida.githubuser.FavAddUpdateActivity
import com.dicoding.auliarosyida.githubuser.R
import com.dicoding.auliarosyida.githubuser.databinding.ItemRowUserBinding
import com.dicoding.auliarosyida.githubuser.entity.User

class FavoriteAdapter(private val activity: Activity): RecyclerView.Adapter<FavoriteAdapter.FavoriteViewHolder>() {

    var listFavorites = ArrayList<User>()
        set(listFavorites) {
            if (listFavorites.size > 0) {
                this.listFavorites.clear()
            }
            this.listFavorites.addAll(listFavorites)
            notifyDataSetChanged()
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FavoriteViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_row_user, parent, false)
        return FavoriteViewHolder(view)
    }
    override fun onBindViewHolder(holder: FavoriteViewHolder, position: Int) {
        holder.bind(listFavorites[position])
    }
    override fun getItemCount(): Int = this.listFavorites.size

    inner class FavoriteViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val binding = ItemRowUserBinding.bind(itemView)

        fun bind(user: User) {
            with(binding){
                Glide.with(itemView.context)
                    .load(user.photo)
                    .apply(RequestOptions().override(55, 55))
                    .into(imgItemPhoto)

                if(user.username.equals("Please try with another username")) {
                    tvItemName.text = user.name
                    tvItemUsername.text = user.username
                }
                else {
                    tvItemName.text = user.username
                    tvItemUsername.text = "@${user.username}"
                }

                itemView.setOnClickListener(CustomClickListener(adapterPosition, object : CustomClickListener.OnItemClickCallback {
                    override fun onItemClicked(view: View, position: Int) {
                        val intent = Intent(activity, FavAddUpdateActivity::class.java)
                        intent.putExtra(FavAddUpdateActivity.EXTRA_POSITION, position)
                        intent.putExtra(FavAddUpdateActivity.EXTRA_FAVORITE_USER, user)
                        activity.startActivityForResult(intent, FavAddUpdateActivity.REQUEST_UPDATE)
                        Toast.makeText(activity, "ke klik bang", Toast.LENGTH_SHORT).show()
                    }
                }))
            }
        }
    }

    fun addItem(favUser: User) {
        this.listFavorites.add(favUser)
        notifyItemInserted(this.listFavorites.size - 1)
    }
    fun updateItem(position: Int, favUser: User) {
        this.listFavorites[position] = favUser
        notifyItemChanged(position, favUser)
    }
    fun removeItem(position: Int) {
        this.listFavorites.removeAt(position)
        notifyItemRemoved(position)
        notifyItemRangeChanged(position, this.listFavorites.size)
    }
}