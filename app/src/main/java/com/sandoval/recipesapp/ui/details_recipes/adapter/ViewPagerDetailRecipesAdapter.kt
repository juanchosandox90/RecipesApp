package com.sandoval.recipesapp.ui.details_recipes.adapter

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter

class ViewPagerDetailRecipesAdapter (
    private val resultBundle: Bundle,
    private val fragments: ArrayList<Fragment>,
    private val title: ArrayList<String>,
    fm: FragmentManager
) : FragmentPagerAdapter(fm, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {
    override fun getCount(): Int {
        return fragments.size
    }

    override fun getItem(position: Int): Fragment {
        fragments[position].arguments = resultBundle
        return fragments[position]
    }

    override fun getPageTitle(position: Int): CharSequence {
        return title[position]
    }
}