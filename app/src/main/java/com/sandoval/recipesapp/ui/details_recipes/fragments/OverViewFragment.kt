package com.sandoval.recipesapp.ui.details_recipes.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import coil.load
import com.sandoval.recipesapp.R
import com.sandoval.recipesapp.databinding.FragmentOverViewBinding
import com.sandoval.recipesapp.data.models.Result
import com.sandoval.recipesapp.utils.Constants.Companion.RECIPE_RESULT_KEY
import org.jsoup.Jsoup

class OverViewFragment : Fragment() {

    private var _fragmentOverViewBinding: FragmentOverViewBinding? = null
    private val fragmentOverViewBinding get() = _fragmentOverViewBinding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _fragmentOverViewBinding =
            FragmentOverViewBinding.inflate(layoutInflater)

        val args = arguments

        val myBundle: Result? = args?.getParcelable(RECIPE_RESULT_KEY)

        fragmentOverViewBinding.recipeImgView.load(myBundle?.image)
        fragmentOverViewBinding.recipeDetailTitleText.text = myBundle?.title
        fragmentOverViewBinding.likesText.text =
            myBundle?.aggregateLikes.toString()
        fragmentOverViewBinding.minutesText.text =
            myBundle?.readyInMinutes.toString()

        /** Fix the HTML tags not parsed that came inside the Api Spoonacular
        mView.summaryText.text = myBundle?.summary **/

        myBundle?.summary.let {
            val summaryParsed = Jsoup.parse(it).text()
            fragmentOverViewBinding.summaryText.text = summaryParsed
        }

        if (myBundle?.vegetarian == true) {
            fragmentOverViewBinding.vegetarianImgView.setColorFilter(
                ContextCompat.getColor(
                    requireContext(), R.color.green
                )
            )
            fragmentOverViewBinding.vegetarianText.setTextColor(
                ContextCompat.getColor(
                    requireContext(), R.color.green
                )
            )
        }

        if (myBundle?.vegan == true) {
            fragmentOverViewBinding.veganImgView.setColorFilter(
                ContextCompat.getColor(
                    requireContext(), R.color.green
                )
            )
            fragmentOverViewBinding.veganText.setTextColor(
                ContextCompat.getColor(
                    requireContext(), R.color.green
                )
            )
        }

        if (myBundle?.glutenFree == true) {
            fragmentOverViewBinding.glutenFreeImgView.setColorFilter(
                ContextCompat.getColor(
                    requireContext(), R.color.green
                )
            )
            fragmentOverViewBinding.glutenFreeText.setTextColor(
                ContextCompat.getColor(
                    requireContext(), R.color.green
                )
            )
        }

        if (myBundle?.dairyFree == true) {
            fragmentOverViewBinding.dairyFreeImgView.setColorFilter(
                ContextCompat.getColor(
                    requireContext(), R.color.green
                )
            )
            fragmentOverViewBinding.dairyFreeText.setTextColor(
                ContextCompat.getColor(
                    requireContext(), R.color.green
                )
            )
        }

        if (myBundle?.veryHealthy == true) {
            fragmentOverViewBinding.healthyImgView.setColorFilter(
                ContextCompat.getColor(
                    requireContext(), R.color.green
                )
            )
            fragmentOverViewBinding.healthyText.setTextColor(
                ContextCompat.getColor(
                    requireContext(), R.color.green
                )
            )
        }

        if (myBundle?.cheap == true) {
            fragmentOverViewBinding.cheapImgView.setColorFilter(
                ContextCompat.getColor(
                    requireContext(), R.color.green
                )
            )
            fragmentOverViewBinding.cheapText.setTextColor(
                ContextCompat.getColor(
                    requireContext(), R.color.green
                )
            )
        }

        return fragmentOverViewBinding.root
    }

}