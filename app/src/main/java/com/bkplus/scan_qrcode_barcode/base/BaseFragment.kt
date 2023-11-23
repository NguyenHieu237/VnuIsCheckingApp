package com.bkplus.scan_qrcode_barcode.base

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import io.reactivex.disposables.CompositeDisposable

abstract class BaseFragment<T : ViewDataBinding> : Fragment() {

    protected val bag: CompositeDisposable by lazy {
        CompositeDisposable()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, layoutId, container, false)
        setupData()
        setupUI()
        bindUI()
        bindData()
        setupViewModel()
        setupListener()

        return binding.root
    }

    override fun onDestroyView() {
        bag.clear()
        super.onDestroyView()
    }

    override fun onDestroy() {
        bag.dispose()
        super.onDestroy()
    }

    override fun onPause() {
        super.onPause()
    }

    override fun onResume() {
        super.onResume()
    }

    protected abstract val layoutId: Int
    protected lateinit var binding: T
    protected open fun setupData() {}
    protected open fun setupUI() {}
    protected open fun bindUI() {}
    protected open fun bindData() {}
    protected open fun setupViewModel() {}
    protected open fun setupListener() {}

    fun navigateToDestination(destination: Int, bundle: Bundle? = null) {
        findNavController().apply {
            bundle?.let {
                navigate(destination, it)
            } ?: navigate(destination)
        }
    }
}