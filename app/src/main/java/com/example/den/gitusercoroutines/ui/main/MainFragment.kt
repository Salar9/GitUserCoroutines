package com.example.den.gitusercoroutines.ui.main

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.den.gitusercoroutines.R
import com.example.den.gitusercoroutines.databinding.GitUserListItemBinding
import com.example.den.gitusercoroutines.databinding.MainFragmentBinding
import com.example.den.gitusercoroutines.model.GitUser
import kotlinx.coroutines.runBlocking
import java.lang.Exception

private const val TAG = "MainFragment"

class MainFragment : Fragment() {
    private lateinit var binding: MainFragmentBinding

    private val viewModel: MainViewModel by lazy {
        ViewModelProvider(this).get(MainViewModel::class.java)
    }
    companion object {
        fun newInstance() = MainFragment()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = MainFragmentBinding.inflate(inflater, container, false)
        binding.recyclerView.apply {
            layoutManager = LinearLayoutManager(requireActivity())
        }
        return binding.root
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        runBlocking {
            try {
                binding.recyclerView.adapter = GitUserAdapter(viewModel.gitRepo.getUsers())
            }
            catch (e: Exception){
                Log.i(TAG, e.toString())
            }
        }
    }

    private inner class GitUserHolder(private val binding: GitUserListItemBinding) : RecyclerView.ViewHolder(binding.root) {
        init {
            binding.gitUserViewModel = GitUserViewModel()
        }

        fun bind(user: GitUser) {
            binding.apply {
                gitUserViewModel?.user = user
                //помойму не совсем првильно загружать аватары тут, имхо это ломает логику MVVM, но как по другому пока не придумал :(
                viewModel.gitRepo.getBitmapAvatar(user.avatar_url,imageView)
                executePendingBindings()
            }
        }
    }
    private inner class GitUserAdapter(private val users: List<GitUser>) : RecyclerView.Adapter<GitUserHolder>() {
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GitUserHolder {
            val binding = DataBindingUtil.inflate<GitUserListItemBinding>(layoutInflater, R.layout.git_user_list_item, parent,false)
            return GitUserHolder(binding)
        }
        override fun onBindViewHolder(holder: GitUserHolder, position: Int) {
            holder.bind(users[position])

        }
        override fun getItemCount() = users.size
    }
}