package com.postnikovegor.mobiledev.ui.emailconfirmation

import android.content.Context
import android.graphics.drawable.AnimationDrawable
import android.text.Editable
import android.text.InputFilter
import android.text.InputFilter.LengthFilter
import android.text.TextWatcher
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.constraintlayout.widget.ConstraintSet
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import com.postnikovegor.mobiledev.R
import com.postnikovegor.mobiledev.databinding.ViewVerificationCodeEditTextBinding
import kotlin.properties.Delegates


class VerificationCodeEditText @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0,
    defStyleRes: Int = 0
) : ConstraintLayout(context, attrs, defStyleAttr, defStyleRes) {

    private val viewBinding =
        ViewVerificationCodeEditTextBinding.inflate(LayoutInflater.from(context), this)

    private var slotViews: List<VerificationCodeSlotView> = emptyList()

    private var numberOfSlots: Int by Delegates.observable(0) { _, _, newValue ->
        viewBinding.realVerificationCodeEditText.filters =
            arrayOf<InputFilter>(LengthFilter(newValue))

        val layout = viewBinding.root as ConstraintLayout
        slotViews.forEach {
            layout.removeView(it)
        }
        slotViews = List(newValue) {
            VerificationCodeSlotView(context).also {
                layout.addView(it)
                it.setUpAttrs()
                (it.viewBinding.cursorView.background as AnimationDrawable).start()
            }
        }

        ConstraintSet().apply {
            clone(layout)
            createHorizontalChain(
                ConstraintSet.PARENT_ID, ConstraintSet.LEFT,
                ConstraintSet.PARENT_ID, ConstraintSet.RIGHT,
                slotViews.map { it.id }.toIntArray(),
                null,
                ConstraintSet.CHAIN_PACKED
            )
            slotViews.forEach {
                setMargin(
                    it.id,
                    ConstraintSet.LEFT,
                    resources.getDimension(R.dimen.verification_code_slot_margin_left).toInt()
                )
            }
            applyTo(layout)
        }
        slotValues = Array(newValue) { null }
        slotValues.fillWith(viewBinding.realVerificationCodeEditText.text)
        slotViews.render(slotValues)
    }

    private var slotValues: Array<Char?> = Array(numberOfSlots) { null }

    var onVerificationCodeFilledListener: (String) -> Unit = {}
    var onVerificationCodeFilledChangeListener: (Boolean) -> Unit = {}

    init {
        viewBinding.realVerificationCodeEditText.transformationMethod = null
        viewBinding.realVerificationCodeEditText.addTextChangedListener(
            object : TextWatcher {
                private var wasClearedLastSlot = false

                override fun beforeTextChanged(
                    s: CharSequence,
                    start: Int,
                    count: Int,
                    after: Int
                ) {
                }

                override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                    wasClearedLastSlot =
                        !wasClearedLastSlot && start + before == slotViews.size && count == 0
                }

                override fun afterTextChanged(s: Editable) {
                    slotValues.fillWith(s)
                    slotViews.render(slotValues)
                    slotViews.moveCursorToFirstEmptySlot(slotValues)
                    val filled = slotValues.isFilled()
                    onVerificationCodeFilledChangeListener(filled)
                    if (filled) onVerificationCodeFilledListener(slotValues.toCodeString())
                    // Uncomment if we need to clear the whole field on backspace.
                    // if (wasClearedLastSlot) viewBinding.realVerificationCodeEditText.setText("")
                }
            }
        )
        viewBinding.realVerificationCodeEditText.setOnFocusChangeListener { _, focused ->
            if (focused) {
                slotViews.moveCursorToFirstEmptySlot(slotValues)
            }
        }

        slotValues.fillWith(viewBinding.realVerificationCodeEditText.text)
        slotViews.render(slotValues)

        context.theme.obtainStyledAttributes(
            attrs,
            R.styleable.VerificationCodeEditText,
            defStyleAttr,
            defStyleRes
        ).apply {
            numberOfSlots = getInt(R.styleable.VerificationCodeEditText_vcet_numberOfSlots, 6)
        }
    }

    fun getCode(): String {
        return slotValues.toCodeString()
    }

    fun clear() {
        viewBinding.realVerificationCodeEditText.setText("")
    }

    fun isFilled(): Boolean =
        slotValues.isFilled()

    private fun List<VerificationCodeSlotView>.render(values: Array<Char?>) {
        values.forEachIndexed { index, value ->
            val slotView = this[index]
            slotView.viewBinding.slotValueTextView.text = value?.toString() ?: ""
            slotView.viewBinding.root.isActivated = (value != null)
        }
    }

    private fun List<VerificationCodeSlotView>.moveCursorToFirstEmptySlot(values: Array<Char?>) {
        val indexOfFirstEmptySlot = values.indexOfFirst { it == null }
        forEachIndexed { index, slotView ->
            slotView.viewBinding.cursorView.isVisible = (index == indexOfFirstEmptySlot)
        }
    }

    private fun Array<Char?>.fillWith(s: Editable?): Array<Char?> {
        fill(null)
        if (s == null) {
            return this
        }
        s.forEachIndexed { index, char ->
            this[index] = char
        }
        return this
    }


    private fun Array<Char?>.isFilled(): Boolean =
        all { it != null }

    private fun Array<Char?>.toCodeString(): String =
        filterNotNull().joinToString(
            separator = "",
            prefix = "",
            postfix = "",
            limit = -1,
            truncated = ""
        )

    private fun VerificationCodeSlotView.setUpAttrs() {
        this.layoutParams.width =
            resources.getDimension(R.dimen.verification_code_slot_width).toInt()
        this.layoutParams.height =
            resources.getDimension(R.dimen.verification_code_slot_height).toInt()
        this.background =
            ContextCompat.getDrawable(context, R.drawable.selector_bg_verification_code_slot)
        this.id = View.generateViewId()
    }
}