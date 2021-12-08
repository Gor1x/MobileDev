package com.postnikovegor.mobiledev.ui.signup

import android.app.AlertDialog
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.text.TextPaint
import android.text.method.LinkMovementMethod
import android.text.style.ClickableSpan
import android.view.View
import android.widget.CheckBox
import androidx.activity.OnBackPressedCallback
import androidx.core.text.buildSpannedString
import androidx.core.text.inSpans
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import by.kirich1409.viewbindingdelegate.viewBinding
import com.postnikovegor.mobiledev.R
import com.postnikovegor.mobiledev.databinding.FragmentSignUpBinding
import com.postnikovegor.mobiledev.entity.UserRegistrationData
import com.postnikovegor.mobiledev.ui.base.BaseFragment
import com.postnikovegor.mobiledev.util.getSpannedString
import com.postnikovegor.mobiledev.util.getString
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

@AndroidEntryPoint
class SignUpFragment : BaseFragment(R.layout.fragment_sign_up) {
    private val viewModel: SignUpViewModel by viewModels()
    private val viewBinding by viewBinding(FragmentSignUpBinding::bind)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requireActivity().onBackPressedDispatcher.addCallback(
            this,
            object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {
                    onBackButtonPressed()
                }
            }
        )

    }

    private fun subscribeToEvents() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.eventsFlow().collect { event ->
                    when (event) {
                        is SignUpViewModel.Event.SignUpEmailConfirmationRequired -> {
                            findNavController().navigate(R.id.emailConfirmationFragment)
                        }
                        else -> {
                            // Do nothing.
                        }
                    }
                }
            }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewBinding.apply {
            backButton.setOnClickListener {
                onBackButtonPressed()
            }
            signUpButton.setOnClickListener {
                viewModel.signUp(getUserRegistrationData())
            }
            termsAndConditionsCheckBox.setClubRulesText {
                startActivity(
                    Intent(
                        Intent.ACTION_VIEW,
                        Uri.parse("https://policies.google.com/terms")
                    )
                )
            }
        }

        subscribeToFormFields()
        subscribeToEvents()
    }

    private fun onBackButtonPressed() {
        if (getUserRegistrationData().allTextFieldsBlank()) {
            findNavController().popBackStack()
            return
        }
        AlertDialog.Builder(requireContext()).run {
            setTitle(R.string.common_back_alert_dialog_text)
            setNegativeButton(R.string.common_back_alert_dialog_cancel_button_text) { dialog, _ ->
                dialog?.dismiss()
            }
            setPositiveButton(R.string.common_back_alert_dialog_confirm_button_text) { _, _ ->
                findNavController().popBackStack()
            }
            show()
        }
    }

    private fun getUserRegistrationData(): UserRegistrationData {
        return viewBinding.run {
            UserRegistrationData(
                firstname = firstnameEditText.getString(),
                lastname = lastnameEditText.getString(),
                nickname = nicknameEditText.getString(),
                email = emailEditText.getString(),
                password = passwordEditText.getString(),
                termsIsChecked = termsAndConditionsCheckBox.isChecked
            )
        }
    }

    private fun subscribeToFormFields() {
        viewBinding.apply {
            val callDecideFunction = {
                decideSignUpButtonEnabledState(getUserRegistrationData())
            }
            callDecideFunction()
            firstnameEditText.doAfterTextChanged { callDecideFunction() }
            lastnameEditText.doAfterTextChanged { callDecideFunction() }
            nicknameEditText.doAfterTextChanged { callDecideFunction() }
            emailEditText.doAfterTextChanged { callDecideFunction() }
            passwordEditText.doAfterTextChanged { callDecideFunction() }
            termsAndConditionsCheckBox.setOnCheckedChangeListener { _, _ ->
                callDecideFunction()
            }
        }
    }

    private fun decideSignUpButtonEnabledState(registrationData: UserRegistrationData) =
        with(registrationData) {
            viewBinding.signUpButton.isEnabled = allTextFieldsNotBlank() && termsIsChecked
        }

    private fun UserRegistrationData.allTextFieldsBlank(): Boolean {
        return firstname.isBlank()
                && lastname.isBlank()
                && nickname.isBlank()
                && email.isBlank()
                && password.isBlank()
    }

    private fun UserRegistrationData.allTextFieldsNotBlank(): Boolean {
        return firstname.isNotBlank()
                && lastname.isNotBlank()
                && nickname.isNotBlank()
                && email.isNotBlank()
                && password.isNotBlank()
    }

    private fun CheckBox.setClubRulesText(
        clubRulesClickListener: () -> Unit
    ) {

        // Turn on ClickableSpan.
        movementMethod = LinkMovementMethod.getInstance()

        val clubRulesClickSpan =
            object : ClickableSpan() {
                override fun onClick(widget: View) = clubRulesClickListener()
                override fun updateDrawState(ds: TextPaint) {
                    super.updateDrawState(ds)
                    ds.color = resources.getColor(R.color.purple_200, null)
                }
            }

        text =
            resources.getSpannedString(
                R.string.sign_up_terms_and_conditions_template,
                buildSpannedString {
                    inSpans(clubRulesClickSpan) {
                        append(resources.getSpannedString(R.string.sign_up_club_rules))
                    }
                }
            )
    }
}