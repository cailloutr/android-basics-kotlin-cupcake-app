/*
 * Copyright (C) 2020 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.example.cupcake.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.example.cupcake.R
import com.example.cupcake.databinding.FragmentPickupBinding
import com.example.cupcake.model.DELIVERY_OPTION
import com.example.cupcake.model.OrderViewModel
import com.example.cupcake.model.PRICE_FOR_SAME_DAY_PICKUP
import com.google.android.material.textfield.TextInputLayout
import java.text.NumberFormat

/**
 * [PickupFragment] allows the user to choose a pickup date for the cupcake order.
 */

const val TAG = "PickupFragment"

class PickupFragment : Fragment() {

    // Binding object instance corresponding to the fragment_pickup.xml layout
    // This property is non-null between the onCreateView() and onDestroyView() lifecycle callbacks,
    // when the view hierarchy is attached to the fragment.
    private var binding: FragmentPickupBinding? = null

    // Use the 'by activityViewModels()' Kotlin property delegate from the fragment-ktx artifact
    private val sharedViewModel: OrderViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        val fragmentBinding = FragmentPickupBinding.inflate(inflater, container, false)
        binding = fragmentBinding
        return fragmentBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding?.apply {
            lifecycleOwner = viewLifecycleOwner
            viewModel = sharedViewModel
            pickupFragment = this@PickupFragment

            tvObservationsPickup.text =
                getString(R.string.pickup_options_obs, NumberFormat.getCurrencyInstance().format(
                    PRICE_FOR_SAME_DAY_PICKUP))
        }

        sharedViewModel.pickupOption.observe(viewLifecycleOwner) {
            if (it == DELIVERY_OPTION) {
                binding?.addressField?.visibility = View.VISIBLE
                binding?.divider2?.visibility = View.VISIBLE
            } else {
                binding?.addressField?.visibility = View.GONE
                binding?.divider2?.visibility = View.GONE
            }
        }
    }

    /**
     * Navigate to the next screen to see the order summary.
     */
    private fun goToNextScreen() {
        findNavController().navigate(R.id.action_pickupFragment_to_summaryFragment)
    }

    fun verifyAddressBeforeGotoNextScreen() {
        // Client choose the the delivery option
        if (sharedViewModel.pickupOption.value == DELIVERY_OPTION) {

            // If the address fields in the layout are empty
            when {
                binding?.tilAddress?.editText?.text.toString() == "" -> {
                    cleanErrorMessageInTextInputLayout()

                    setErrorMessageInAddressForm(
                        binding?.tilAddress, "Inform a valid address!"
                    )
                }

                binding?.tilCity?.editText?.text.toString() == "" -> {
                    cleanErrorMessageInTextInputLayout()

                    setErrorMessageInAddressForm(
                        binding?.tilCity, "Inform a valid city!"
                    )
                }

                binding?.tilState?.editText?.text.toString() == "" -> {
                    cleanErrorMessageInTextInputLayout()

                    setErrorMessageInAddressForm(
                        binding?.tilState, "Inform a valid state!"
                    )
                }

                binding?.tilZip?.editText?.text.toString() == "" -> {
                    cleanErrorMessageInTextInputLayout()

                    setErrorMessageInAddressForm(
                        binding?.tilZip, "Inform a valid zip code!"
                    )
                }

                else -> {
                    saveStateAndGoToNextScreen()
                }
            }
        } else {
            saveStateAndGoToNextScreen()
        }
    }

    // Set the error message in the empty textInputs layout
    private fun setErrorMessageInAddressForm(
        textInputLayout: TextInputLayout?,
        errorMessage: String,
    ) {
        textInputLayout?.requestFocus()
        textInputLayout?.error = errorMessage
    }

    // Clean the error messages in the editTextLayouts that are not empty
    private fun cleanErrorMessageInTextInputLayout() {
        binding?.tilAddress?.isErrorEnabled = false
        binding?.tilCity?.isErrorEnabled = false
        binding?.tilState?.isErrorEnabled = false
        binding?.tilZip?.isErrorEnabled = false
    }


    private fun saveStateAndGoToNextScreen() {
        saveState()
        goToNextScreen()
    }


    /**
     * Save the Location object in the OrderViewModel and set the list with the items ordered
     * */
    private fun saveState() {
        sharedViewModel.setOrderList()
        sharedViewModel.setLocation(
            binding?.tilAddress?.editText?.text.toString(),
            binding?.tilCity?.editText?.text.toString(),
            binding?.tilState?.editText?.text.toString(),
            binding?.tilZip?.editText?.text.toString()
        )
    }

    /**
     * Cancel the order and start over.
     */
    fun cancelOrder() {
        // Reset order in view model
        sharedViewModel.resetOrder()

        // Navigate back to the [StartFragment] to start over
        findNavController().navigate(R.id.action_pickupFragment_to_startFragment)
    }

    /**
     * This fragment lifecycle method is called when the view hierarchy associated with the fragment
     * is being removed. As a result, clear out the binding object.
     */
    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }
}