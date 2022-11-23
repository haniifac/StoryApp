package com.example.storyapp_intermediate_sub2.ui.view.customview

import android.content.Context
import android.graphics.Canvas
import android.text.Editable
import android.text.TextWatcher
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatEditText
import com.example.storyapp_intermediate_sub2.R

class PasswordEditText : AppCompatEditText {
    private var _isPassValid = false
    val isPassValid get() = _isPassValid

    constructor(context: Context) : super(context) {
        init()
    }

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        init()
    }

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    ) {
        init()
    }

    private fun init() {
        addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {
                // Do nothing.
            }

            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                // Do nothing.

            }

            override fun afterTextChanged(s: Editable) {
                validatePassword(s)
            }
        })
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        hint = context.getString(R.string.password)

    }

    private fun validatePassword(s: Editable) {
        if (s.isEmpty()) {
            error = null
        } else if (s.toString().length < 6) {
            error = context.getString(R.string.pass_min_length)
            _isPassValid = false
        } else {
            error = null
            _isPassValid = true
        }
    }
}