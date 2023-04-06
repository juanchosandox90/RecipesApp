package com.sandoval.recipesapp.ui.details_recipes

import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.navigation.navArgs
import com.sandoval.recipesapp.R
import com.sandoval.recipesapp.databinding.ActivityDetailsBinding
import com.sandoval.recipesapp.ui.details_recipes.adapter.ViewPagerDetailRecipesAdapter
import com.sandoval.recipesapp.ui.details_recipes.fragments.IngredientsFragment
import com.sandoval.recipesapp.ui.details_recipes.fragments.InstructionsFragment
import com.sandoval.recipesapp.ui.details_recipes.fragments.OverViewFragment

class DetailsRecipeActivity : AppCompatActivity() {

    private val args: DetailsRecipeActivityArgs by navArgs()

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

        val fragments = ArrayList<Fragment>()
        fragments.add(OverViewFragment())
        fragments.add(IngredientsFragment())
        fragments.add(InstructionsFragment())

        val titles = ArrayList<String>()
        titles.add("Overview")
        titles.add("Ingredients")
        titles.add("Instructions")

        val resultBundle = Bundle()
        resultBundle.putParcelable("recipeBundle", args.result)

        val pagerAdapter = ViewPagerDetailRecipesAdapter(
            resultBundle, fragments, titles, supportFragmentManager
        )

        detailsRecipesActivitybinding.viewPager.adapter = pagerAdapter
        detailsRecipesActivitybinding.tabLayout.setupWithViewPager(
            detailsRecipesActivitybinding.viewPager
        )
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) finish()
        return super.onOptionsItemSelected(item)
    }
}