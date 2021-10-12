package com.example.appgithub

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.appgithub.data.response.DetailUserResponse
import com.example.appgithub.databinding.ItemLayoutUserBinding

class RecyclerViewUserAdapter(val context: Context, val listUser: ArrayList<DetailUserResponse>) : RecyclerView.Adapter<RecyclerViewUserAdapter.ViewHolder>() {

    //untuk onItemClick
    private var onItemClickCallback : OnItemClickCallback? = null
    fun setOnItemClickCallback(onItemClickCallback: OnItemClickCallback){
        this.onItemClickCallback = onItemClickCallback
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemLayoutUserBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(listUser.get(position))
    }

    override fun getItemCount(): Int {
        return listUser.size
    }

    // awalnya parameternya adalah view, diganti menjadi binding. tambahkan juga extends RecyclerView.ViewHolder(binding.root)
    inner class ViewHolder(private val binding: ItemLayoutUserBinding) : RecyclerView.ViewHolder(binding.root) {
        private var viewContext: Context = binding.root.context

        fun bind(user: DetailUserResponse){
            binding.tvUsername.text = user.login

            if (user.company == null) {
                binding.tvCompany.text = "-"
            }
            else{
                binding.tvCompany.text = user.company
            }

            if (user.location == null){
                binding.tvLocation.text = "-"
            }
            else{
                binding.tvLocation.text = user.location
            }

            if (user.name == null){
                binding.tvNama.text = "-"
            }
            else {
                binding.tvNama.text = user.name
            }

            Glide.with(viewContext)
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
        fun onItemClickCallback(user: DetailUserResponse)
    }
}