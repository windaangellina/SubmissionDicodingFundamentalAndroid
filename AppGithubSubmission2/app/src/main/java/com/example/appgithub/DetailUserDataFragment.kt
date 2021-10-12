package com.example.appgithub

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.appgithub.data.RetrofitClient
import com.example.appgithub.data.response.DetailUserResponse
import com.example.appgithub.databinding.FragmentDetailUserDataBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


private const val ARG_USERNAME = "arg_username"

class DetailUserDataFragment : Fragment() {
    lateinit var binding : FragmentDetailUserDataBinding
    private var username : String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            username = it.getString(ARG_USERNAME)
        }
    }

    companion object {
        fun newInstance(username: String): DetailUserDataFragment {
            val args = Bundle().apply {
                putString(ARG_USERNAME, username)
            }

            val fragment = DetailUserDataFragment()
            fragment.arguments = args
            return fragment
        }
    }

    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        // Inflate binding for this fragment
        binding = FragmentDetailUserDataBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        getDetailUserResponsesRetrofit(username!!)
    }

    fun getDetailUserResponsesRetrofit(username: String){
        //loading ProgressBar
        binding.progressBar.visibility = View.VISIBLE

        RetrofitClient.instanceDetailUser.findUserDetailByUsername(username).enqueue(object :
                Callback<DetailUserResponse> {
            override fun onResponse(
                    call: Call<DetailUserResponse>,
                    response: Response<DetailUserResponse>,
            ) {
                val statusCode = response.code()

                if (response.isSuccessful) {
                    Log.d("responseDetail", response.body().toString())
                    response.body().let {
                        // berhenti loading ProgressBar
                        binding.progressBar.visibility = View.INVISIBLE

                        if (it != null) {
                            setComponentValue(it)
                        }
                    }
                } else {
                    binding.progressBar.visibility = View.INVISIBLE
                }
            }

            override fun onFailure(call: Call<DetailUserResponse>, t: Throwable) {
                Log.d("error", t.message.toString())
            }

        })
    }


    private fun setComponentValue(user: DetailUserResponse){
        binding.tvUsername.text = user.login
        binding.tvRepository.text = user.publicRepos.toString()
        binding.tvFollowers.text = user.followers.toString()
        binding.tvFollowing.text = user.following.toString()

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
        else{
            binding.tvNama.text = user.name
        }

        Glide.with(requireContext())
            .load(user.avatarUrl)
            .apply(RequestOptions().override(80, 80))
            .dontAnimate()
            .into(binding.imgFotoUser)

        //event listeners
        binding.buttonOpenGithub.setOnClickListener {
            openGithub(user)
        }

        binding.iconShare.setOnClickListener {
            shareUser(user)
        }

        binding.iconBack.setOnClickListener {
            backToListUser()
        }
    }

    private fun shareUser(user: DetailUserResponse){
        val urlGitHub : String = "https://github.com/" + user.login
        val shareBody : String = "Find ${user.name} on github now! \n $urlGitHub"

        val shareIntent = Intent()
        shareIntent.action = Intent.ACTION_SEND
        shareIntent.type = "text/plain"
        shareIntent.putExtra(Intent.EXTRA_SUBJECT, "GitHub @${user.login}")
        shareIntent.putExtra(Intent.EXTRA_TEXT, shareBody)
        startActivity(Intent.createChooser(shareIntent, "Share GitHub profile using"))
    }

    private fun openGithub(user: DetailUserResponse){
        val urlGitHub : String = "https://github.com/" + user.login
        val i = Intent(Intent.ACTION_VIEW)
        i.data = Uri.parse(urlGitHub)
        startActivity(i)
    }

    private fun backToListUser(){
        val intentBack = Intent(requireContext(), MainActivity::class.java)
        startActivity(intentBack)
    }



}