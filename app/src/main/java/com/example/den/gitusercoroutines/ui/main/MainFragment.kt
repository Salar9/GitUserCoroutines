package com.example.den.gitusercoroutines.ui.main

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import android.util.Log
import android.view.*
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.den.gitusercoroutines.FavoritesFragment
import com.example.den.gitusercoroutines.R
import com.example.den.gitusercoroutines.model.GitUserDB
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

    var isSelectMode = false

    companion object {
        fun newInstance() = MainFragment()
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.main_menu, menu)
        menu.findItem(R.id.add_favorites).isVisible = isSelectMode  //в зависимости от режима (обображение/выбор элементов) отображать пункт меню
    }
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.add_favorites -> { //какой пункт меню выбран
                processSelect()
                val favorites = mutableListOf<GitUserDB>()
                viewModel.gitUsers.filter { it.Selected }.forEach{gitUser -> favorites.add(GitUserDB(gitUser.id, gitUser.login, gitUser.avatar_url))}
                Log.i(TAG,"Insert size ${favorites.size}")
                runBlocking { viewModel.gitUserDaoRepo.addToFavorites(favorites) }
                true
            }
            R.id.show_favorites->{
                requireActivity().supportFragmentManager.beginTransaction()
                    .replace(R.id.container, FavoritesFragment())
                    .addToBackStack(null)
                    .commit()
                true
            }
            else -> return super.onOptionsItemSelected(item)
        }
    }
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {

        binding = MainFragmentBinding.inflate(inflater, container, false)
        setHasOptionsMenu(true)

        val gestureDetector = GestureDetector(context,MyGestureDetector())  //Распознование жестов, меня интересует только длительное нажатие

        binding.recyclerView.apply {
            layoutManager = LinearLayoutManager(requireActivity())
            addOnItemTouchListener(object : RecyclerView.OnItemTouchListener {
                override fun onInterceptTouchEvent(rv: RecyclerView, e: MotionEvent): Boolean {
                    gestureDetector.onTouchEvent(e)
                    return false    //передать обработку тача глубже, т.е. чтоб скрол обработался и прочее
                }
                override fun onTouchEvent(rv: RecyclerView, e: MotionEvent) { }
                override fun onRequestDisallowInterceptTouchEvent(disallowIntercept: Boolean) { }
            })
        }
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        //TODO: попробовать пененести это или в репозиторий или во viewModel
        runBlocking {
            try {
                viewModel.gitUsers = viewModel.gitUserRepo.getUsers()
                binding.recyclerView.adapter = GitUserAdapter(viewModel.gitUsers)
            }
            catch (e: Exception){
                Log.i(TAG, e.toString())
            }
        }
    }

    private inner class GitUserHolder(val binding: GitUserListItemBinding) : RecyclerView.ViewHolder(binding.root) {
        init {
            binding.gitUserViewModel = GitUserViewModel()
        }

        fun bind(user: GitUser) {
            binding.apply {
                gitUserViewModel?.user = user
                //помойму не совсем првильно загружать аватары тут, имхо это ломает логику MVVM, но как по другому пока не придумал :(
                viewModel.gitUserRepo.getBitmapAvatar(user.avatar_url,imageView)
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
            holder.binding.checkBox2.isVisible = isSelectMode
            if(isSelectMode) {
                holder.binding.checkBox2.setOnCheckedChangeListener { checkBox, _ ->
                    val name = users[position].login
                    users[position].Selected = checkBox.isChecked   //сохраняю состояние для дальшейшего сохранения в Room только выбранных элементов
                    Log.i(TAG, "Pos - $position Name - $name ${checkBox.isChecked}")
                }
            }
            else users[position].Selected = false   //если убрали флажки то также сбросить и поле в списке

            holder.bind(users[position])
        }
        override fun getItemCount() = users.size
    }

    private inner class MyGestureDetector : GestureDetector.SimpleOnGestureListener() {
        override fun onLongPress(p0: MotionEvent?) { processSelect() }
    }

    private fun processSelect(){
        isSelectMode = !isSelectMode
        requireActivity().invalidateOptionsMenu()
        binding.recyclerView.adapter?.notifyDataSetChanged()
    }
}


