package com.example.share4care.loginAndRegisterForm

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import androidx.databinding.DataBindingUtil
import com.example.share4care.R
import com.example.share4care.databinding.ActivityLoginBinding
import com.example.share4care.databinding.FragmentRegAccountInfoBinding
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout

class reg_account_info : Fragment() {
    private lateinit var binding:FragmentRegAccountInfoBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?

    ): View? {

        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_reg_account_info, container, false)



        return inflater.inflate(R.layout.fragment_reg_account_info, container, false)
    }

}


