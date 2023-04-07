package com.sandoval.recipesapp.ui.details_recipes

import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.navigation.navArgs
import com.google.android.material.tabs.TabLayoutMediator
import com.sandoval.recipesapp.R
import com.sandoval.recipesapp.databinding.ActivityDetailsBinding
import com.sandoval.recipesapp.ui.details_recipes.adapter.ViewPagerDetailRecipesAdapter
import com.sandoval.recipesapp.ui.details_recipes.fragments.IngredientsFragment
import com.sandoval.recipesapp.ui.details_recipes.fragments.InstructionsFragment
import com.sandoval.recipesapp.ui.details_recipes.fragments.OverViewFragment
import com.sandoval.recipesapp.utils.Constants.Companion.RECIPE_RESULT_KEY

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
        resultBundle.putParcelable(RECIPE_RESULT_KEY, args.result)

        val pagerAdapter = ViewPagerDetailRecipesAdapter(
            resultBundle, fragments, this
        )
        detailsRecipesActivitybinding.viewPager2.isUserInputEnabled = false
        detailsRecipesActivitybinding.viewPager2.apply {
            adapter = pagerAdapter
        }

        TabLayoutMediator(
            detailsRecipesActivitybinding.tabLayout,
            detailsRecipesActivitybinding.viewPager2
        ) { tab, position ->
            tab.text = titles[position]
        }.attach()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) finish()
        return super.onOptionsItemSelected(item)
    }
}