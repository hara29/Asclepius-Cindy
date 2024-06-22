package com.dicoding.asclepius.view

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import com.dicoding.asclepius.R
import com.dicoding.asclepius.databinding.ActivityResultBinding
import com.dicoding.asclepius.utils.BookmarkViewModel
import java.io.File
import java.io.FileOutputStream
import java.util.UUID

class ResultActivity : AppCompatActivity() {
    private lateinit var binding: ActivityResultBinding
    private lateinit var mBookmarkViewModel: BookmarkViewModel
    private lateinit var sourceActivity: String
    private lateinit var label: String
    private lateinit var score: String
    private lateinit var imageUri: String
    private var isBookmarked = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityResultBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.title = getString(R.string.result_activity)
        sourceActivity = intent.getStringExtra(EXTRA_SOURCE_ACTIVITY) ?: MainActivity::class.java.simpleName
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)

        imageUri = intent.getStringExtra(EXTRA_IMAGE_URI).toString()
        label = intent.getStringExtra(EXTRA_RESULT_LABEL).toString()
        score = intent.getStringExtra(EXTRA_RESULT_SCORE).toString()

        binding.resultImage.setImageURI(Uri.parse(imageUri))
        binding.resultText.text = getString(R.string.result_string, label, score)

        mBookmarkViewModel = ViewModelProvider(this)[BookmarkViewModel::class.java]

        mBookmarkViewModel.resultDeleteBookmark.observe(this) {
            binding.fabBookmark.setImageResource(R.drawable.ic_bookmark_border)
            isBookmarked = false
        }

        mBookmarkViewModel.resultSuccesBookmark.observe(this) {
            binding.fabBookmark.setImageResource(R.drawable.ic_bookmark_fill)
            isBookmarked = true
        }

        mBookmarkViewModel.findBookmarkItem(imageUri.hashCode()) {
            binding.fabBookmark.setImageResource(R.drawable.ic_bookmark_fill)
            isBookmarked = true
        }

        binding.fabBookmark.setOnClickListener {
            if (isBookmarked) {
                mBookmarkViewModel.setBookmark(imageUri, label, score, isBookmarked)
            } else {
                saveImageAndBookmark()
            }
        }
    }

    private fun saveImageAndBookmark() {
        val fileName = "${UUID.randomUUID()}.jpg"
        val imageFile = File(filesDir, fileName)

        try {
            contentResolver.openInputStream(Uri.parse(imageUri))?.use { inputStream ->
                FileOutputStream(imageFile).use { outputStream ->
                    inputStream.copyTo(outputStream)
                }
            }

            val newImageUri = Uri.fromFile(imageFile).toString()
            mBookmarkViewModel.setBookmark(newImageUri, label, score, false)
        } catch (e: Exception) {
            showToast(getString(R.string.image_save_failed))
        }
    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }
    override fun onSupportNavigateUp(): Boolean {
        when (sourceActivity) {
            MainActivity::class.java.simpleName -> {
                val intent = Intent(this, MainActivity::class.java)
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                startActivity(intent)
            }
            BookmarkActivity::class.java.simpleName -> {
                val intent = Intent(this, BookmarkActivity::class.java)
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                startActivity(intent)
            }
        }
        return true
    }

    companion object {
        const val EXTRA_SOURCE_ACTIVITY = "extra_source_activity"
        const val EXTRA_IMAGE_URI = "extra_image_uri"
        const val EXTRA_RESULT_LABEL = "extra_result_label"
        const val EXTRA_RESULT_SCORE = "extra_result_score"
    }
}
