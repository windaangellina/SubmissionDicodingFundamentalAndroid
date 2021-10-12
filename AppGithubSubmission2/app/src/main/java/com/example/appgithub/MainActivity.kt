package com.example.appgithub

import android.app.SearchManager
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.appgithub.data.RetrofitClient
import com.example.appgithub.data.response.DetailUserResponse
import com.example.appgithub.data.response.SearchUserResponse
import com.example.appgithub.databinding.ActivityMainBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainActivity : AppCompatActivity() {
    private lateinit var binding : ActivityMainBinding
    private var profileUsername = ""

    // data from API response
    private val listDetailUserResponses = ArrayList<DetailUserResponse>()
    private lateinit var recyclerViewUserAdapter : RecyclerViewUserAdapter

    //misc props
    var inSearchMode = false
    var searchCount : Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // component binding
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        profileUsername = "windaangellina"
        recyclerViewUserAdapter = RecyclerViewUserAdapter(applicationContext,
            listDetailUserResponses)

        setUpComponent()
    }

    fun setUpVisibility(
        isLoading: Boolean,
        isError: Boolean,
        idDrawableError: Int?,
        errorMessage: String?
    ){
        if (isLoading){
            // sedang proses search
            binding.progressBar.visibility = View.VISIBLE
            binding.tvLabelShowKeyword.visibility = View.VISIBLE

            binding.imgKeteranganSearch.visibility = View.INVISIBLE
            binding.tvKeteranganSearch.visibility = View.INVISIBLE

        }else{
            // sudah selesai / belum pernah search
            binding.progressBar.visibility = View.INVISIBLE

            if (!inSearchMode){
                binding.tvLabelShowKeyword.visibility = View.INVISIBLE
            }

            // tampilkan keterangan search apabila sudah pernah melakukan pencarian
            if (inSearchMode && listDetailUserResponses.size == 0){
                binding.imgKeteranganSearch.visibility = View.VISIBLE
                binding.tvKeteranganSearch.visibility = View.VISIBLE

                if (!isError){
                    binding.imgKeteranganSearch.setImageResource(R.drawable.svg_empty)
                    binding.tvKeteranganSearch.text = resources.getString(R.string.no_results_found)
                }
                else{
                    if (idDrawableError != null) {
                        binding.imgKeteranganSearch.setImageResource(idDrawableError)
                    }
                    binding.tvKeteranganSearch.text = errorMessage
                    binding.tvLabelShowKeyword.visibility = View.INVISIBLE
                }
            }
        }
    }

    private fun setUpComponent(){
        //atur lainnya
        setUpVisibility(false, false, null, null)

        // atur profile aktif
        binding.imgProfilAktif.setOnClickListener {
            goToMyProfile()
        }

        // atur tampilan list user
        setUpRecyclerList()
        setUpSearchView()
    }

    private fun goToMyProfile(){
        val intentProfil = Intent(applicationContext, DetailUserActivity::class.java)
        intentProfil.putExtra(DetailUserActivity.EXTRA_USERNAME, profileUsername)
        startActivity(intentProfil)
    }

    fun makeToast(pesan : String){
        Toast.makeText(applicationContext, pesan, Toast.LENGTH_SHORT).show()
    }

    private fun setUpSearchView(){
        val searchManager = getSystemService(Context.SEARCH_SERVICE) as SearchManager
        val searchView = binding.searchView

        searchView.setSearchableInfo(searchManager.getSearchableInfo(componentName))
        searchView.queryHint = resources.getString(R.string.search_hint)

        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            //Gunakan method ini ketika search selesai atau OK
            override fun onQueryTextSubmit(query: String): Boolean {
                inSearchMode = true

                // get data
                setUpVisibility(true, false, null, null)
                getSearchDataResponsesRetrofit(query)
                dismissKeyboard()

                return true
            }

            //Gunakan method ini untuk merespon tiap perubahan huruf pada searchView
            override fun onQueryTextChange(newText: String): Boolean {
                var txt = resources.getString(R.string.typeKeyword)

                if (newText.isNotEmpty()){
                    txt = "${resources.getString(R.string.showing_results_for)} '$newText'"
                }

                binding.tvLabelShowKeyword.text = txt
                binding.tvLabelShowKeyword.visibility = View.VISIBLE
                return false
            }
        })
    }

    fun dismissKeyboard(){
        val imm: InputMethodManager = getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(this.currentFocus?.windowToken, InputMethodManager.HIDE_IMPLICIT_ONLY)
    }

    fun getSearchDataResponsesRetrofit(keywordUsername: String){
        // atur tampilan
        listDetailUserResponses.clear()

        RetrofitClient.instanceSearch.getSearchResults(keywordUsername).enqueue(object :
            Callback<SearchUserResponse> {
            override fun onResponse(
                call: Call<SearchUserResponse>,
                response: Response<SearchUserResponse>,
            ) {
                // get data user
                Log.d("responseSearch", response.body().toString())
                response.body().let {
                    if (it != null) {
                        // atur tampilan
                        if (it.items!!.isEmpty())
                            setUpVisibility(false, false, null, null)
                        else {
                            searchCount = it.items!!.size
                            for (itemDataUser in it.items!!) getDetailUserResponsesRetrofit(itemDataUser.login)
                        }

                    }
                }
            }

            override fun onFailure(call: Call<SearchUserResponse>, t: Throwable) {
                Log.d("error", t.message.toString())
            }
        })
    }

    fun getDetailUserResponsesRetrofit(username: String){
        RetrofitClient.instanceDetailUser.findUserDetailByUsername(username).enqueue(
            object : Callback<DetailUserResponse> {
                override fun onResponse(
                    call: Call<DetailUserResponse>,
                    response: Response<DetailUserResponse>,
                ) {
                    val statusCode = response.code()

                    if (response.isSuccessful) {
                        Log.d("responseDetail", response.body().toString())
                        response.body().let {
                            //mencegah duplicate data user
                            if (it !in listDetailUserResponses) {
                                listDetailUserResponses.add(it!!)
                            }
                        }

                        recyclerViewUserAdapter.notifyDataSetChanged()

                        // atur tampilan
                        if (listDetailUserResponses.size == searchCount){
                            val txt ="${listDetailUserResponses.size} ${resources.getString(R.string.x_results_found)}"
                            binding.tvLabelShowKeyword.text = txt

                            setUpVisibility(false, false, null, null)
                        }

                    } else {
                        setUpErrorHandlerRetrofit(statusCode)
                    }
                }

                override fun onFailure(call: Call<DetailUserResponse>, t: Throwable) {
                    Log.d("error", t.message.toString())
                }

            })
    }

    fun setUpErrorHandlerRetrofit(statusCode: Int){
        var errorMessage = "encountered an error"
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

    private fun goToDetailUser(user: DetailUserResponse){
        val intentDetail = Intent(applicationContext, DetailUserActivity::class.java)
        intentDetail.putExtra(DetailUserActivity.EXTRA_USERNAME, user.login)
        startActivity(intentDetail)
    }

    private fun setUpRecyclerList(){
        // set up awal
        binding.recyclerViewUser.setHasFixedSize(true)
        //set layout
        binding.recyclerViewUser.layoutManager = LinearLayoutManager(applicationContext)

        //set adapter
        binding.recyclerViewUser.adapter = recyclerViewUserAdapter

        //set onItemClick
        recyclerViewUserAdapter.setOnItemClickCallback(object :
            RecyclerViewUserAdapter.OnItemClickCallback {
            override fun onItemClickCallback(user: DetailUserResponse) {
                goToDetailUser(user)
            }
        })
    }

}