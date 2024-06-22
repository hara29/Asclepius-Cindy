// BookmarkActivity.kt
package com.dicoding.asclepius.view

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.dicoding.asclepius.R
import com.dicoding.asclepius.data.local.entity.Bookmark
import com.dicoding.asclepius.databinding.ActivityBookmarkBinding
import com.dicoding.asclepius.utils.BookmarkAdapter
import com.dicoding.asclepius.utils.BookmarkViewModel

class BookmarkActivity : AppCompatActivity() {
    private lateinit var binding: ActivityBookmarkBinding
    private lateinit var viewModel: BookmarkViewModel
    private val adapter = BookmarkAdapter(object : BookmarkAdapter.OnItemClickCallback {
        override fun onItemClicked(data: Bookmark) {
            val moveDataIntent = Intent(this@BookmarkActivity, ResultActivity::class.java).apply {
                putExtra(ResultActivity.EXTRA_SOURCE_ACTIVITY, BookmarkActivity::class.java.simpleName)
                putExtra(ResultActivity.EXTRA_RESULT_LABEL, data.label)
                putExtra(ResultActivity.EXTRA_RESULT_SCORE, data.score)
                putExtra(ResultActivity.EXTRA_IMAGE_URI, data.image)
            }
            startActivity(moveDataIntent)
        }
    })

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityBookmarkBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.title = getString(R.string.bookmark_page)

        viewModel = ViewModelProvider(this)[BookmarkViewModel::class.java]

        binding.apply {
            rvHeroes.setHasFixedSize(true)
            rvHeroes.layoutManager = LinearLayoutManager(this@BookmarkActivity)
            rvHeroes.adapter = adapter

            tvEmptyBookmark.visibility = if (adapter.itemCount == 0) View.VISIBLE else View.GONE
        }

        viewModel.readAllData.observe(this) { bookmarks ->
            bookmarks?.let {
                adapter.submitList(it)

                binding.tvEmptyBookmark.visibility = if (it.isEmpty()) View.VISIBLE else View.GONE
            }
        }
    }
}
