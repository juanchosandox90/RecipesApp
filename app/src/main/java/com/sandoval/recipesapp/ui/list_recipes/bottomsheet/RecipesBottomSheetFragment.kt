package com.sandoval.recipesapp.ui.list_recipes.bottomsheet

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.asLiveData
import androidx.navigation.fragment.findNavController
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.android.material.chip.Chip
import com.google.android.material.chip.ChipGroup
import com.sandoval.recipesapp.databinding.FragmentRecipesBottomSheetBinding
import com.sandoval.recipesapp.ui.list_recipes.viewmodels.RecipesViewModel
import com.sandoval.recipesapp.utils.Constants.Companion.DEFAULT_DIET_TYPE
import com.sandoval.recipesapp.utils.Constants.Companion.DEFAULT_MEAL_TYPE
import java.util.*

class RecipesBottomSheetFragment : BottomSheetDialogFragment() {

    private lateinit var recipesViewModel: RecipesViewModel

    private var _bottomSheetRecipesFragmentBinding: FragmentRecipesBottomSheetBinding? =
        null
    private val bottomSheetRecipesFragmentBinding get() = _bottomSheetRecipesFragmentBinding!!

    private var mealTypeChip = DEFAULT_MEAL_TYPE
    private var mealTypeChipId = 0
    private var dietTypeChip = DEFAULT_DIET_TYPE
    private var dietTypeChipId = 0


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        recipesViewModel =
            ViewModelProvider(requireActivity())[RecipesViewModel::class.java]
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _bottomSheetRecipesFragmentBinding =
            FragmentRecipesBottomSheetBinding.inflate(layoutInflater)

        recipesViewModel.readMealAndDietType?.asLiveData()
            ?.observe(viewLifecycleOwner) { value ->
                mealTypeChip = value.selectedMealType
                dietTypeChip = value.selectedDietType
                updateChip(
                    value.selectedMealTypeId,
                    bottomSheetRecipesFragmentBinding.mealTypeChipGroup
                )
                updateChip(
                    value.selectedDietTypeId,
                    bottomSheetRecipesFragmentBinding.dietTypeChipGroup
                )
            }

        bottomSheetRecipesFragmentBinding.mealTypeChipGroup.setOnCheckedChangeListener { group, selectedChipId ->
            val chip = group.findViewById<Chip>(selectedChipId)
            val selectedMealType =
                chip.text.toString().lowercase(Locale.getDefault())
            mealTypeChip = selectedMealType
            mealTypeChipId = selectedChipId
        }

        bottomSheetRecipesFragmentBinding.dietTypeChipGroup.setOnCheckedChangeListener { group, selectedChipId ->
            val chip = group.findViewById<Chip>(selectedChipId)
            val selectedDietTypeChip =
                chip.text.toString().lowercase(Locale.getDefault())
            dietTypeChip = selectedDietTypeChip
            dietTypeChipId = selectedChipId
        }

        bottomSheetRecipesFragmentBinding.applyButton.setOnClickListener {
            recipesViewModel.saveMealAndDietType(
                mealTypeChip, mealTypeChipId, dietTypeChip, dietTypeChipId
            )
            val action =
                RecipesBottomSheetFragmentDirections.actionRecipesBottomSheetToRecipesFragment(
                    true
                )
            findNavController().navigate(action)
        }

        return bottomSheetRecipesFragmentBinding.root


    }

    private fun updateChip(chipId: Int, chipGrouP: ChipGroup) {
        if (chipId != 0) {
            try {
                chipGrouP.findViewById<Chip>(chipId).isChecked = true
            } catch (e: Exception) {
                Log.e("Recipes Bottom Sheet:", e.message.toString())
            }
        }
    }

}