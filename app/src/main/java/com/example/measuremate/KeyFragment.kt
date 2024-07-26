package com.example.measuremate

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment

class KeyFragment : Fragment() {

    private var keyListener: KeyListener? = null

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is KeyListener) {
            keyListener = context
        } else {
            throw RuntimeException("$context must implement KeyListener")
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.keypad_fragment, container, false)
        setupKeypad(view)
        return view
    }

    private fun setupKeypad(view: View) {
        val buttonIds = listOf(
            R.id.seven_key, R.id.eight_key, R.id.nine_key,
            R.id.four_key, R.id.five_key, R.id.six_key,
            R.id.one_key, R.id.two_key, R.id.three_key,
            R.id.double_zero_key, R.id.zero_key, R.id.dot_key,
            R.id.ac_key, R.id.clear_key
        )
        for (id in buttonIds) {
            view.findViewById<Button>(id).setOnClickListener { button ->
                val text = (button as Button).text.toString()
                keyListener?.onKeyPressed(text)
            }
        }
    }

    override fun onDetach() {
        super.onDetach()
        keyListener = null
    }
}
