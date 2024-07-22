package com.broadcast.myapplication

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.ConcatAdapter
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.broadcast.myapplication.adapter.AdapterInteractionListener
import com.broadcast.myapplication.adapter.FingerprintAdapter
import com.broadcast.myapplication.adapter.Item
import com.broadcast.myapplication.adapter.animations.AddableItemAnimator
import com.broadcast.myapplication.adapter.animations.custom.SimpleCommonAnimator
import com.broadcast.myapplication.adapter.animations.custom.SlideInLeftCommonAnimator
import com.broadcast.myapplication.adapter.animations.custom.SlideInTopCommonAnimator
import com.broadcast.myapplication.adapter.decorations.FeedHorizontalDividerItemDecoration
import com.broadcast.myapplication.adapter.decorations.GroupVerticalItemDecoration
import com.broadcast.myapplication.adapter.fingerprints.HorizontalItemsFingerprint
import com.broadcast.myapplication.adapter.fingerprints.PostFingerprint
import com.broadcast.myapplication.adapter.fingerprints.TitleFingerprint
import com.broadcast.myapplication.databinding.ActivityMainBinding
import com.broadcast.myapplication.model.FeedTitle
import com.broadcast.myapplication.model.HorizontalItems
import com.broadcast.myapplication.model.UserPost
import com.broadcast.myapplication.utils.SwipeToDelete
import com.broadcast.myapplication.utils.getRandomFeed
import com.google.android.material.snackbar.Snackbar

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    private val postsList: MutableList<Item> by lazy {
        getRandomFeed(this)
    }
    private val fingerprints = listOf(
        TitleFingerprint(),
        PostFingerprint(::onSavePost),
        HorizontalItemsFingerprint(
            70,
            RecyclerView.RecycledViewPool(),
            ::onSavePost
        )
    )
    var oldList = listOf<Item>()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(LayoutInflater.from(this))
        setContentView(binding.root)

        with(binding.recyclerView) {
            layoutManager = LinearLayoutManager(this@MainActivity)

            addFingerprint(fingerprints)
            postsList.add(0, FeedTitle("Актуальное за сегодня:"))
            addItemDecoration(
                FeedHorizontalDividerItemDecoration(70, listOf(R.layout.item_horizontal_list)),
                GroupVerticalItemDecoration(R.layout.item_post, 100, 0),
                GroupVerticalItemDecoration(R.layout.item_title, 0, 100),
                GroupVerticalItemDecoration(R.layout.item_horizontal_list, 0, 150),
            ) // addable

            dataSource(object : AdapterDataSource {
                override fun getItemCount() = postsList.size

                override fun getItemByPosition(position: Int): Item = postsList.get(position)

                override fun getItems(): List<Item> = postsList
            })

            itemAnimator = AddableItemAnimator(SimpleCommonAnimator()).also { animator ->
                animator.addViewTypeAnimation(R.layout.item_post, SlideInLeftCommonAnimator())
                animator.addViewTypeAnimation(R.layout.item_title, SlideInTopCommonAnimator())
                animator.addDuration = 500L
                animator.removeDuration = 500L
            }

            update()
        }

        initSwipeToDelete()
        submitInitialListWithDelayForAnimation()
    }

    private fun onSavePost(adapterPosition: Int) {
        val post = postsList.getOrNull(adapterPosition)
        if (post != null && post is UserPost) {
            Log.e("anime", postsList.toString())
            val newItem = post.copy(isSaved = post.isSaved.not())

            postsList.removeAt(index = adapterPosition)
            postsList.add(adapterPosition, newItem)
            update()
        }
    }

    private fun onSavePost(outAdapterPosition: Int, innerAdapterPosition: Int) {
        val horizontalItems = postsList.getOrNull(outAdapterPosition)

        if (horizontalItems != null && horizontalItems is HorizontalItems) {
            Log.e("anime", "outPos: $outAdapterPosition, innerPos: $innerAdapterPosition")
            val post =
                (horizontalItems.items.getOrNull(innerAdapterPosition) as? UserPost) ?: return

            val newItem = post.copy(isSaved = post.isSaved.not())
            horizontalItems.items.removeAt(index = innerAdapterPosition)
            horizontalItems.items.add(innerAdapterPosition, newItem)

            val horizontal = horizontalItems.copy(isChange = horizontalItems.isChange.not())

            postsList.removeAt(outAdapterPosition)
            postsList.add(outAdapterPosition, horizontal)

            Log.e("anime", postsList.contains(horizontal).toString())
            update()
            binding.recyclerView.print()
        }
    }


    private fun submitInitialListWithDelayForAnimation() {
        binding.recyclerView.postDelayed({
//            titleAdapter.submitList(titlesList.toList())
//            postAdapter.submitList(postsList.toList())
        }, 300L)
    }

    private fun initSwipeToDelete() {
        val onItemSwipedToDelete = { positionForRemove: Int ->
            val removedItem = postsList[positionForRemove]
            postsList.removeAt(positionForRemove)
//            postAdapter.submitList(postsList.toList())

            showRestoreItemSnackbar(positionForRemove, removedItem)

        }
        val swipeToDeleteCallback = SwipeToDelete(onItemSwipedToDelete)
        ItemTouchHelper(swipeToDeleteCallback).attachToRecyclerView(binding.recyclerView)
    }

    private fun showRestoreItemSnackbar(position: Int, item: Item) {
        Snackbar.make(binding.recyclerView, "Item was deleted", Snackbar.LENGTH_LONG)
            .setAction("Undo") {
//                postsList.add(position, item)
//                postAdapter.submitList(postsList.toList())
            }.show()
    }

    fun update() {
        val fingerprintDiffUtil =
            FingerprintDiffUtil(fingerprints, oldList, postsList)
        oldList = postsList.toList()
        binding.recyclerView.update(fingerprintDiffUtil)
    }
}