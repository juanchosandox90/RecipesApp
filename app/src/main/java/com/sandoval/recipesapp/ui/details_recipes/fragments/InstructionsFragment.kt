package com.sandoval.recipesapp.ui.details_recipes.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebViewClient
import androidx.fragment.app.Fragment
import com.sandoval.recipesapp.data.models.Result
import com.sandoval.recipesapp.databinding.FragmentInstructionsBinding
import com.sandoval.recipesapp.utils.Constants.Companion.RECIPE_RESULT_KEY
import com.sandoval.recipesapp.utils.retrieveParcelable

class InstructionsFragment : Fragment() {

    private var _binding: FragmentInstructionsBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentInstructionsBinding.inflate(inflater, container, false)

        val args = arguments
        val myBundle: Result? = args?.retrieveParcelable(RECIPE_RESULT_KEY)

        if (myBundle != null) {
            binding.instructionsWebview.webViewClient = object : WebViewClient() {}
            val websiteUrl: String = myBundle.sourceUrl
            binding.instructionsWebview.loadUrl(websiteUrl)
        }

        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}