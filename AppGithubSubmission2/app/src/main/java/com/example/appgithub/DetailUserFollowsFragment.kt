package com.example.appgithub

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.appgithub.data.RetrofitClient
import com.example.appgithub.data.response.FollowsResponseItem
import com.example.appgithub.databinding.FragmentDetailUserFollowsBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

private const val ARG_USERNAME = "arg_username"
private const val ARG_MODE = "arg_follows_mode"

class DetailUserFollowsFragment : Fragment() {
    lateinit var binding : FragmentDetailUserFollowsBinding

    private var username : String? = null
    private var tampilkanFollowers : Boolean = false

    // data from API
    private val listUserFollows = ArrayList<FollowsResponseItem>()

    // adapter
    lateinit var recyclerFollowsAdapter : RecyclerViewFollowsAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            username = it.getString(ARG_USERNAME)
            tampilkanFollowers = it.getBoolean(ARG_MODE)
        }
    }

    companion object {
        fun newInstance(username: String, tampilkanFollowers : Boolean): DetailUserFollowsFragment{
            val args = Bundle().apply {
                putString(ARG_USERNAME, username)
                putBoolean(ARG_MODE, tampilkanFollowers)
            }

            val fragment = DetailUserFollowsFragment()
            fragment.arguments = args
            return fragment
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate binding for this fragment
        binding = FragmentDetailUserFollowsBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        recyclerFollowsAdapter = RecyclerViewFollowsAdapter(requireContext(), listUserFollows)

        //tampilan awal
        setUpVisibility(true, false, null, null)

        setUpRecyclerList()
        setComponentValue()
    }

    fun setComponentValue(){
        when {
            tampilkanFollowers -> {
                showListFollowers(username!!)
            }
            else -> {
                showListFollowings(username!!)
            }
        }

        if (listUserFollows.size > 0){
            recyclerFollowsAdapter.notifyDataSetChanged()
        }
    }

    private fun setUpRecyclerList(){
        // set up awal
        binding.recyclerViewFollows.setHasFixedSize(true)
        //set layout
        binding.recyclerViewFollows.layoutManager = LinearLayoutManager(requireContext())

        //set adapter
        binding.recyclerViewFollows.adapter = recyclerFollowsAdapter

        //set onItemClick
        recyclerFollowsAdapter.setOnItemClickCallback(object : RecyclerViewFollowsAdapter.OnItemClickCallback{
            override fun onItemClickCallback(user: FollowsResponseItem) {
                goToDetailUser(user)
            }
        })
    }

    fun showListFollowers(username : String){
        // loading
        setUpVisibility(true, false, null, null)

        RetrofitClient.instanceFollows.getFollowersByUsername(username)
            .enqueue(object : Callback<List<FollowsResponseItem>>{
            override fun onResponse(
                call: Call<List<FollowsResponseItem>>,
                response: Response<List<FollowsResponseItem>>,
            ) {
                if (response.isSuccessful){
                    Log.d("responseFollowers", response.body().toString())
                    response.body()?.let { listUserFollows.addAll(it) }

                    // tampilkan data
                    recyclerFollowsAdapter.notifyDataSetChanged()

                    // atur tampilan
                    setUpVisibility(false, false, null, null)
                }
                else{
                    setUpErrorHandlerRetrofit(response.code())
                }
            }

            override fun onFailure(call: Call<List<FollowsResponseItem>>, t: Throwable) {
                Log.d("error", t.message.toString())
            }

        })
    }

    fun showListFollowings(username : String){
        // loading
        setUpVisibility(true, false, null, null)

        RetrofitClient.instanceFollows.getFollowingsByUsername(username)
            .enqueue(object : Callback<List<FollowsResponseItem>>{
                override fun onResponse(
                    call: Call<List<FollowsResponseItem>>,
                    response: Response<List<FollowsResponseItem>>,
                ) {
                    if (response.isSuccessful){
                        Log.d("responseFollowings", response.body().toString())
                        response.body()?.let { listUserFollows.addAll(it) }

                        // tampilkan data
                        recyclerFollowsAdapter.notifyDataSetChanged()

                        // atur tampilan
                        setUpVisibility(false, false, null, null)
                    }
                    else{
                        setUpErrorHandlerRetrofit(response.code())
                    }
                }

                override fun onFailure(call: Call<List<FollowsResponseItem>>, t: Throwable) {
                    Log.d("error", t.message.toString())
                }

            })
    }

    fun setUpVisibility(isLoading : Boolean, isError : Boolean, idDrawableError : Int?, errorMessage : String?){
        if (isLoading){
            // sedang proses search
            binding.progressBar.visibility = View.VISIBLE

            binding.imgKeteranganSearch.visibility = View.INVISIBLE
            binding.tvKeterangan.visibility = View.INVISIBLE

        }else{
            // sudah selesai / belum pernah search
            binding.progressBar.visibility = View.INVISIBLE

            // tampilkan keterangan search apabila sudah pernah melakukan pencarian
            if (listUserFollows.size == 0){
                binding.imgKeteranganSearch.visibility = View.VISIBLE
                binding.tvKeterangan.visibility = View.VISIBLE

                if (!isError){
                    binding.imgKeteranganSearch.setImageResource(R.drawable.svg_empty)
                    if (tampilkanFollowers){
                        binding.tvKeterangan.text = resources.getString(R.string.this_user_has_no_followers)
                    }
                    else{
                        binding.tvKeterangan.text = resources.getString(R.string.this_user_doesnt_follow_anyone)
                    }
                }
                else{
                    if (idDrawableError != null) {
                        binding.imgKeteranganSearch.setImageResource(idDrawableError)
                    }
                    binding.tvKeterangan.text = errorMessage
                }
            }
        }
    }

    fun setUpErrorHandlerRetrofit(statusCode: Int){
        var errorMessage = resources.getString(R.string.encountered_an_error)
        val idDrawableError : Int?
        when (statusCode) {
            401 -> {
                idDrawableError = R.drawable.svg_warning
                errorMessage = "$statusCode : Bad Request"
            }
            403 -> {
                idDrawableError = R.drawable.svg_403_access_denied
                errorMessage = "$statusCode : Forbidden"
            }
            404 -> {
                idDrawableError = R.drawable.svg_404
                errorMessage = "$statusCode : Not Found"
            }
            else -> {
                idDrawableError = R.drawable.svg_error_umum
            }
        }

        setUpVisibility(false, true, idDrawableError, errorMessage)
    }

    private fun goToDetailUser(user : FollowsResponseItem){
        val intentDetail = Intent(requireContext(), DetailUserActivity::class.java)
        intentDetail.putExtra(DetailUserActivity.EXTRA_USERNAME, user.login)
        startActivity(intentDetail)
    }
}