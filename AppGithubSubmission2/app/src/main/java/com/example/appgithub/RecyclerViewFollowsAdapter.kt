package com.example.appgithub

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.appgithub.data.response.FollowsResponseItem
import com.example.appgithub.databinding.ItemLayoutFollowsBinding

class RecyclerViewFollowsAdapter(val context: Context, val listUser: ArrayList<FollowsResponseItem>) :
    RecyclerView.Adapter<RecyclerViewFollowsAdapter.ViewHolder>() {

    //untuk onItemClick
    private var onItemClickCallback : RecyclerViewFollowsAdapter.OnItemClickCallback? = null
    fun setOnItemClickCallback(onItemClickCallback: RecyclerViewFollowsAdapter.OnItemClickCallback){
        this.onItemClickCallback = onItemClickCallback
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemLayoutFollowsBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(listUser.get(position))
    }

    override fun getItemCount(): Int {
        return listUser.size
    }

    inner class ViewHolder(private val binding : ItemLayoutFollowsBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(user : FollowsResponseItem){
            binding.tvUsername.text = user.login

            Glide.with(context)
                .load(user.avatarUrl)
                .apply(RequestOptions().override(80, 80))
                .placeholder(R.drawable.app_github_octocat)
                .error(R.drawable.app_github_mark_120px) // will be displayed if the image cannot be loaded
                .dontAnimate()
                .into(binding.imgFotoUser)

            //set onItemClick
            itemView.setOnClickListener {
                onItemClickCallback?.onItemClickCallback(user)
            }
        }
    }

    interface OnItemClickCallback{
        fun onItemClickCallback(user: FollowsResponseItem)
    }
}