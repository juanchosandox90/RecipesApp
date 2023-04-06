package com.sandoval.recipesapp.ui.details_recipes

import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.sandoval.recipesapp.R
import com.sandoval.recipesapp.databinding.ActivityDetailsBinding

class DetailsRecipeActivity : AppCompatActivity() {

    private var _detailsRecipesActivitybinding: ActivityDetailsBinding? = null
    private val detailsRecipesActivitybinding get() = _detailsRecipesActivitybinding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _detailsRecipesActivitybinding =
            ActivityDetailsBinding.inflate(layoutInflater)
        setContentView(detailsRecipesActivitybinding.root)

        setSupportActionBar(detailsRecipesActivitybinding.toolbar)
        detailsRecipesActivitybinding.toolbar.setTitleTextColor(
            ContextCompat.getColor(
                this, R.color.white
            )
        )
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) finish()
        return super.onOptionsItemSelected(item)
    }
}