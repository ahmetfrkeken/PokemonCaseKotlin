package com.ahmetfarukeken.pokemoncasekotlin.view.activity.request_permission

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.Settings
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.ahmetfarukeken.pokemoncasekotlin.databinding.RequestPermissionActivityBinding
import com.ahmetfarukeken.pokemoncasekotlin.view.activity.main.MainActivity

class RequestPermissionActivity : AppCompatActivity() {
    private lateinit var binding: RequestPermissionActivityBinding
    private lateinit var viewModel: RequestPermissionViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        initBinding()
        initViewModel()
        initActivity()
    }

    override fun onResume() {
        super.onResume()
        viewModel.getOverlayPermissin(this)
    }

    private fun initBinding() {
        binding = RequestPermissionActivityBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }

    private fun initViewModel() {
        viewModel = ViewModelProvider(this).get(RequestPermissionViewModel::class.java)
        viewModel.isHaveOverlayPermisson().observe(this, Observer {
            println(it)
            if (it){
                startMainActivity()
            }
        })
    }

    private fun initActivity() {
        binding.overlayPermissionButton.setOnClickListener {
            getPermission()
        }
    }

    private fun getPermission(){
        val intent = Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION, Uri.parse("package:$packageName"))
        startActivity(intent)
    }

    private fun startMainActivity(){
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
    }
}