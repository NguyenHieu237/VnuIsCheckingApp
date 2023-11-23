package com.bkplus.scan_qrcode_barcode.ui.settings.search_engine

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.*
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.DialogFragment
import androidx.recyclerview.widget.RecyclerView
import com.bkplus.scan_qrcode_barcode.R
import com.bkplus.scan_qrcode_barcode.databinding.FragmentSearchEnginePopupBinding
import com.bkplus.scan_qrcode_barcode.preferences.QRCodePreferences

class SearchEngineDialog(
    private val onConfirmChangeSearchEngine: (ItemSearchEngine) -> Unit
): DialogFragment(),SearchEngineAdapter.OnClickItemListener {
    private lateinit var binding: FragmentSearchEnginePopupBinding
    private lateinit var adapter : SearchEngineAdapter
    private var selectedSearchEngine: ItemSearchEngine? = null

    override fun onClickItem(item: ItemSearchEngine) {
        selectedSearchEngine = item
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_search_engine_popup,container, false)
        if (dialog != null) {
            dialog!!.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            dialog!!.window?.requestFeature(Window.FEATURE_NO_TITLE)
        }
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        setupRecyclerView()
        initiateView()
    }

    private fun initiateView() {
        binding.ok.setOnClickListener {
            selectedSearchEngine?.let { it1 -> onConfirmChangeSearchEngine.invoke(it1)
            }
            dialog?.dismiss()
        }
    }

    override fun onResume() {
        super.onResume()
        val params = dialog!!.window?.attributes
        if (params != null) {
            params.width = RecyclerView.LayoutParams.MATCH_PARENT
            params.height = RecyclerView.LayoutParams.WRAP_CONTENT
            params.gravity = Gravity.CENTER
            dialog?.window?.attributes = params
        }
    }

    private fun setupRecyclerView(){
        adapter = SearchEngineAdapter(makelist(),this)
        binding.rvSearchEngine.adapter = adapter
    }
    private fun makelist(): ArrayList<ItemSearchEngine>{
        val list = ArrayList<ItemSearchEngine>()
        list.add(ItemSearchEngine(R.string.google))
        list.add(ItemSearchEngine(R.string.bing))
        list.add(ItemSearchEngine(R.string.baidu))
        list.add(ItemSearchEngine(R.string.yahoo))
        list.add(ItemSearchEngine(R.string.duck))
        list.add(ItemSearchEngine(R.string.ecosia))
        list.add(ItemSearchEngine(R.string.yandex))

        return list
    }

}