package com.example.den.gitusercoroutines

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.den.gitusercoroutines.databinding.FragmentFavoritesBinding
import com.example.den.gitusercoroutines.databinding.GitUserListItemBinding
import com.example.den.gitusercoroutines.model.GitUser
import com.example.den.gitusercoroutines.model.GitUserDB
import com.example.den.gitusercoroutines.ui.main.GitUserViewModel
import com.example.den.gitusercoroutines.ui.main.MainViewModel
import kotlinx.coroutines.runBlocking

private const val TAG = "FavoritesFragment"

//TODO: Добавить кнопку удаления из избранного и необходимые методы
class FavoritesFragment : Fragment() {
    private lateinit var binding: FragmentFavoritesBinding

    private val viewModel: MainViewModel by lazy {
        ViewModelProvider(this).get(MainViewModel::class.java)
    }

    companion object {
        fun newInstance() = FavoritesFragment()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        runBlocking {
            val favoritesUsers = viewModel.gitUserDaoRepo.getFavoritesUser()
            Log.i(TAG,"Favorites size - ${favoritesUsers.size}")
            binding.favoritesRecyclerView.adapter = GitUserDBAdapter(favoritesUsers)
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = FragmentFavoritesBinding.inflate(inflater, container, false)

        binding.favoritesRecyclerView.apply {
            layoutManager = LinearLayoutManager(requireActivity())
        }

        return binding.root
    }

    private inner class GitUserDBHolder(val binding: GitUserListItemBinding) : RecyclerView.ViewHolder(binding.root) {
        init {
            binding.gitUserViewModel = GitUserViewModel()
        }

        fun bind(user: GitUserDB) {
            val convertedUser =  GitUser(user.login,user.id,user.avatar_url)
            binding.apply {
                gitUserViewModel?.user = convertedUser
                viewModel.gitUserRepo.getBitmapAvatar(convertedUser.avatar_url,imageView)
                executePendingBindings()
            }
        }
    }

    private inner class GitUserDBAdapter(private val users: List<GitUserDB>) : RecyclerView.Adapter<FavoritesFragment.GitUserDBHolder>() {
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FavoritesFragment.GitUserDBHolder {
            val binding = DataBindingUtil.inflate<GitUserListItemBinding>(layoutInflater, R.layout.git_user_list_item, parent,false)
            return GitUserDBHolder(binding)
        }

        override fun onBindViewHolder(holder: FavoritesFragment.GitUserDBHolder, position: Int) {
            holder.binding.checkBox2.isVisible = true
            holder.bind(users[position])
        }
        override fun getItemCount() = users.size
    }
}