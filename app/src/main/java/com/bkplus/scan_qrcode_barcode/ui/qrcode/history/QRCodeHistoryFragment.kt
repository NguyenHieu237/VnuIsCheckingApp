package com.bkplus.scan_qrcode_barcode.ui.qrcode.history

import android.annotation.SuppressLint
import android.content.ActivityNotFoundException
import android.content.Intent
import android.content.pm.PackageManager
import android.content.pm.ResolveInfo
import android.graphics.Bitmap
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.view.View
import androidx.core.content.FileProvider
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.ads.control.admob.AppOpenManager
import com.bkplus.scan_qrcode_barcode.BuildConfig
import com.bkplus.scan_qrcode_barcode.R
import com.bkplus.scan_qrcode_barcode.base.BaseFragment
import com.bkplus.scan_qrcode_barcode.databinding.FragmentQrcodeHistoryBinding
import com.bkplus.scan_qrcode_barcode.manager.qrcode.GenerateQRCodeResult
import com.bkplus.scan_qrcode_barcode.model.QRCodeHistoryItemChild
import com.bkplus.scan_qrcode_barcode.ui.qrcode.history.adapter.QRCodeHistoryAdapter
import com.bkplus.scan_qrcode_barcode.ui.qrcode.history.adapter.QRCodeHistoryItemListener
import com.bkplus.scan_qrcode_barcode.ui.qrcode.history.dialog.DialogDeleteAll
import com.bkplus.scan_qrcode_barcode.ui.qrcode.history.dialog.DialogDeleteItem
import com.bkplus.scan_qrcode_barcode.ui.scanner.ScanResultViewModel
import com.bkplus.scan_qrcode_barcode.utils.extension.disposedBy
import com.bkplus.scan_qrcode_barcode.utils.extension.guardLet
import com.bkplus.scan_qrcode_barcode.utils.extension.hiddenIf
import java.io.File
import java.io.FileOutputStream
import java.io.IOException

class QRCodeHistoryFragment : BaseFragment<FragmentQrcodeHistoryBinding>() {
    private val viewModel by activityViewModels<QRCodeHistoryViewModel>()
    private var dialogDeleteItem: DialogDeleteItem? = null
    private var dialogDeleteAll: DialogDeleteAll? = null

    override val layoutId: Int
        get() = R.layout.fragment_qrcode_history

    companion object {
        var isAllItemsSelected = false
        fun newInstance(): QRCodeHistoryFragment {
            val args = Bundle()
            val fragment = QRCodeHistoryFragment()
            fragment.arguments = args
            return fragment
        }
    }

    override fun setupUI() {
        binding.toolbar.setTitle(context?.getString(R.string.history))
        binding.toolbar.hideBackButton()
        binding.toolbar.setIconRight3(R.drawable.ic_delete_default)
        binding.toolbar.imgIconRight3.visibility = View.VISIBLE
        setupRecyclerView()
    }

    override fun setupViewModel() {
        viewModel.getQRCodeHistoryItemsObservable()
            .subscribe {
                binding.rcvHistory.hiddenIf { it.isEmpty() }
                binding.containerEmpty.hiddenIf { it.isNotEmpty() }
                viewModel._adapter.setListItem(it)
            }
            .disposedBy(bag = bag)

        viewModel.loadItemCompletionObservable()
            .subscribe {
                makeUIScanResult(result = it)
            }
            .disposedBy(bag = bag)

        viewModel.startFetchData()

        /// setup ads native list when fetch data list history
    }

    override fun setupListener() {
        binding.toolbar.onTapIconRight3Listener = {
            dialogDeleteAll = DialogDeleteAll()
            dialogDeleteAll!!.show(parentFragmentManager, null)

        }
    }

    private fun setupRecyclerView() {
        viewModel._adapter = QRCodeHistoryAdapter(activity = requireActivity(), object : QRCodeHistoryItemListener {
            override fun onTapItem(item: QRCodeHistoryItemChild) {
                item.itemData?.let { viewModel.loadItemHistory(item = it) }
            }

                @SuppressLint("NotifyDataSetChanged")
                override fun onDeleteItem(item: QRCodeHistoryItemChild) {
                    dialogDeleteItem = DialogDeleteItem()
                    dialogDeleteItem!!.item = item
                    binding.rcvHistory.adapter?.notifyDataSetChanged()
                    dialogDeleteItem!!.show(parentFragmentManager, null)
                }

                override fun onShareItem(item: QRCodeHistoryItemChild) {
                    shareItemHistory(item)
                }
            })
        val llm = LinearLayoutManager(context)
        llm.orientation = LinearLayoutManager.VERTICAL
        binding.rcvHistory.layoutManager = llm
        binding.rcvHistory.adapter = viewModel._adapter
    }

    private fun makeUIScanResult(result: GenerateQRCodeResult) {
        val direction = QRCodeHistoryFragmentDirections.actionGoToScanResultFragment(result)
        findNavController().navigate(direction)
    }

    private  fun shareItemHistory(item: QRCodeHistoryItemChild){
        val i = Intent(Intent.ACTION_SEND)
        var chooser: Intent? = null

        i.setType("image/*")
        context?.let { ctx ->
            item.itemData?.getPath()?.let { path ->
                val uri = FileProvider.getUriForFile(ctx, BuildConfig.APPLICATION_ID + ".provider", File(path))

                i.putExtra(Intent.EXTRA_STREAM, uri)
                chooser = Intent.createChooser(i, "Share QR Code")

                val resInfoList: List<ResolveInfo> =
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                        ctx.packageManager.queryIntentActivities(
                            Intent(
                                Intent.ACTION_MAIN,
                                null
                            ).addCategory(Intent.CATEGORY_LAUNCHER),
                            PackageManager.ResolveInfoFlags.of(PackageManager.MATCH_DEFAULT_ONLY.toLong())
                        )
                    } else {
                        ctx.packageManager.queryIntentActivities(
                            Intent(
                                Intent.ACTION_MAIN,
                                null
                            ).addCategory(Intent.CATEGORY_LAUNCHER),
                            PackageManager.MATCH_DEFAULT_ONLY
                        )
                    }

                for (resolveInfo in resInfoList) {
                    val packageName = resolveInfo.activityInfo.packageName
                    ctx.grantUriPermission(
                        packageName,
                        uri,
                        Intent.FLAG_GRANT_WRITE_URI_PERMISSION or Intent.FLAG_GRANT_READ_URI_PERMISSION
                    )
                }
            }
        }

        try {
            startActivity(chooser)
        } catch (ex: ActivityNotFoundException) {
            ex.printStackTrace()
        }
    }

}
