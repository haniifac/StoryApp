package com.example.storyapp_intermediate_sub2.ui.customview

import android.content.Context
import android.graphics.Canvas
import android.text.Editable
import android.text.TextUtils
import android.text.TextWatcher
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatEditText
import com.example.storyapp_intermediate_sub2.R

class EmailEditText : AppCompatEditText {
    private var _isEmailValid = false
    val isEmailValid get() = _isEmailValid

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
                validateEmail(s)
            }
        })
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        hint = context.getString(R.string.email)

    }

    private fun validateEmail(s: Editable) {
        val isEmailValidCheck =
            !TextUtils.isEmpty(s) && android.util.Patterns.EMAIL_ADDRESS.matcher(s).matches()
        if (s.isEmpty()) {
            error = null
        } else if (!isEmailValidCheck) {
            error = context.getString(R.string.email_not_valid)
            _isEmailValid = false
        } else {
            error = null
            _isEmailValid = true
        }
    }

}