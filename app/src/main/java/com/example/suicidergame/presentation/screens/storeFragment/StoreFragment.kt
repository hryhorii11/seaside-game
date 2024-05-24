package com.example.suicidergame.presentation.screens.storeFragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.content.res.AppCompatResources
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.example.suicidergame.R
import com.example.suicidergame.databinding.FragmentStoreBinding
import com.example.suicidergame.presentation.utils.ext.hide
import com.example.suicidergame.presentation.utils.ext.show
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class StoreFragment : Fragment() {

    private var _binding: FragmentStoreBinding? = null
    private val binding get() = _binding!!

    private val viewModel: StoreViewModel by viewModels()

    private lateinit var boughtItemsList: List<Int>
    private var userMoneyAmount = 0
    private var chosenLifePreserver: Int = 0
    private var lifePreservesIndex = 0
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentStoreBinding.inflate(inflater, container, false)
        setListeners()
        setContent()
        boughtItemsList = viewModel.getBoughtLifePreserves().toMutableList()
        return binding.root
    }

    private fun setContent() {
        userMoneyAmount = viewModel.getUserMoneyAmount()
        binding.textViewMoney.text = userMoneyAmount.toString()
        chosenLifePreserver = viewModel.getChosenLifePreserver()
        if (chosenLifePreserver == lifePreservesIndex)
            binding.buttonChoseOrBuy.text = getString(R.string.chosen_button_text)
    }

    private fun setListeners() {
        with(binding) {
            buttonNextItem.setOnClickListener {
                if (lifePreservesIndex < LifePreserves.entries.size - 1) {
                    lifePreservesIndex++
                } else lifePreservesIndex = 0
                setChosenLifePreserverContent()
            }
            buttonPreviousItem.setOnClickListener {
                if (lifePreservesIndex > 0) {
                    lifePreservesIndex--
                } else lifePreservesIndex = 2
                setChosenLifePreserverContent()
            }

            buttonChoseOrBuy.setOnClickListener {
                if (boughtItemsList.contains(lifePreservesIndex)) {
                    choseLifePreserver()
                } else {
                    buyLifePreserver()
                }
                setChosenLifePreserverContent()

            }
        }
    }

    private fun buyLifePreserver() {
        if (userMoneyAmount >= LifePreserves.entries[lifePreservesIndex].price)
            boughtItemsList =
                boughtItemsList.toMutableList().apply { add(lifePreservesIndex) }
        viewModel.updateMoneyAmount(userMoneyAmount - LifePreserves.entries[lifePreservesIndex].price)
        binding.textViewMoney.text =
            (userMoneyAmount - LifePreserves.entries[lifePreservesIndex].price).toString()
        viewModel.saveBoughtPreserves(boughtItemsList)
    }

    private fun choseLifePreserver() {
        viewModel.saveChosenLifePreserver(lifePreservesIndex)
        chosenLifePreserver = lifePreservesIndex
    }

    private fun setChosenLifePreserverContent() {
        with(binding) {
            imageViewLifePreserver.setImageDrawable(
                AppCompatResources.getDrawable(
                    requireContext(),
                    LifePreserves.entries[lifePreservesIndex].id
                )
            )
            if (boughtItemsList.contains(lifePreservesIndex)) {
                textViewPrice.hide()
                imageViewCoinPrice.hide()
                buttonChoseOrBuy.text =
                    if (lifePreservesIndex != chosenLifePreserver) getString(R.string.button_choshe_item_text)
                    else getString(R.string.chosen_button_text)
            } else {
                textViewPrice.show()
                imageViewCoinPrice.show()
                textViewPrice.text = LifePreserves.entries[lifePreservesIndex].price.toString()
                buttonChoseOrBuy.text = getString(R.string.buy_item_button_text)
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}