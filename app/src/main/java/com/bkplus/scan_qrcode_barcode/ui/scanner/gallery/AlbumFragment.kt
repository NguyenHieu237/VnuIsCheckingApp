package com.bkplus.scan_qrcode_barcode.ui.scanner.gallery

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.os.bundleOf
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.bkplus.scan_qrcode_barcode.R
import com.bkplus.scan_qrcode_barcode.databinding.FragmentAlbumBinding
import com.bkplus.scan_qrcode_barcode.manager.database.QRCodeResult
import com.bkplus.scan_qrcode_barcode.manager.database.QRCodeType
import com.bkplus.scan_qrcode_barcode.manager.qrcode.GenerateQRCodeResult
import com.bkplus.scan_qrcode_barcode.ui.scanner.ScanResultViewModel
import com.bkplus.scan_qrcode_barcode.ui.scanner.ScannerFragmentDirections
import com.bkplus.scan_qrcode_barcode.ui.scanner.gallery.photos.PhotoAdapter
import com.bkplus.scan_qrcode_barcode.utils.BitmapUtils
import com.google.mlkit.vision.barcode.BarcodeScannerOptions
import com.google.mlkit.vision.barcode.BarcodeScanning
import com.google.mlkit.vision.barcode.common.Barcode
import com.google.mlkit.vision.common.InputImage

class AlbumFragment : Fragment(), AlbumAdapter.OnItemClickListener,
    PhotoAdapter.OnItemClickListener {
    private lateinit var binding: FragmentAlbumBinding
    private val viewModel: AlbumViewModel by viewModels()
    private lateinit var albumsAdapter: AlbumAdapter
    private lateinit var photosAdapter: PhotoAdapter
    var onItemSelected: ((uri: Uri) -> Unit)? = null
    private val ScanviewModel by activityViewModels<ScanResultViewModel>()

    companion object {
        fun newInstance(onItemSelected: ((uri: Uri) -> Unit)? = null): AlbumFragment {
            val fragment = AlbumFragment()

            fragment.onItemSelected = onItemSelected
            val args = Bundle()
            fragment.arguments = args
            return fragment
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_album, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        initView()

    }

    private fun initView() {
        binding.rvAlbum.layoutManager =
            GridLayoutManager(context, 3, GridLayoutManager.VERTICAL, false)
        albumsAdapter = AlbumAdapter(this)
        binding.rvAlbum.adapter = albumsAdapter

        context?.let {
            viewModel.getPhoneAlbums(it)
        }
        binding.rvPhoto.layoutManager =
            GridLayoutManager(context, 3, GridLayoutManager.VERTICAL, false)
        photosAdapter = PhotoAdapter(this)
        binding.rvPhoto.adapter = photosAdapter


        viewModel.getSuccessGetAllAlbum().observe(viewLifecycleOwner) {
            if (it.isNotEmpty()) {
                albumsAdapter.setData(it)
            }
        }

        binding.btnBack.setOnClickListener {
            if (binding.rvPhoto.visibility == View.VISIBLE) {
                binding.sceenTitle.setText(R.string.album)
                binding.rvAlbum.visibility = View.VISIBLE
                binding.rvPhoto.visibility = View.GONE
            } else {
                findNavController().popBackStack()
            }
        }
    }


    override fun onAlbumClick(album: Album) {
        album.albumPhotos?.let { photosAdapter.setData(it) }
        binding.sceenTitle.setText(R.string.photo)
        binding.rvAlbum.visibility = View.GONE
        binding.rvPhoto.visibility = View.VISIBLE
    }

    override fun onPhotoClick(uri: Uri) {
        val inputStream = requireContext().contentResolver.openInputStream(uri)
        var bitmap = BitmapFactory.decodeStream(inputStream)
        val barcodeOption = BarcodeScannerOptions.Builder()
            .setBarcodeFormats(Barcode.FORMAT_ALL_FORMATS)
            .build()
        val barcodeScanner = BarcodeScanning.getClient(barcodeOption)

        barcodeScanner.process(InputImage.fromBitmap(bitmap, 0))
            .addOnSuccessListener {
                if (it.isNotEmpty()) {
                    val barcode = it[0]
                    bitmap = BitmapUtils.getCroppedBitmap(bitmap, barcode.boundingBox!!)
                    ScanviewModel.barcode = barcode
                    gotoResult(bitmap, barcode)
                } else {
                    Toast.makeText(requireContext(),"Unable to Parse QR Code, please choose another picture",Toast.LENGTH_SHORT).show()
                }
            }
        inputStream?.close()
    }
    private fun gotoResult(bitmap: Bitmap, barcode: Barcode) {
        val result = GenerateQRCodeResult (
            qrCodeBitmap = bitmap,
            qrCodeContent = barcode.displayValue!!,
            resultType = QRCodeResult.SCAN.type,
            qrCodeType = getBarcodeType(barcode = barcode)
        )
        val direction = ScannerFragmentDirections.actionGoToScanResultFragment(result)
//			HomeNavFragmentDirections.actionGoToScanResultFragment(result)
        val bundle = bundleOf("result" to result)
        findNavController().navigate(R.id.scanResultFragment,bundle)
    }
    private fun getBarcodeType(barcode: Barcode): Int {
        return when(barcode.format) {
            Barcode.FORMAT_DATA_MATRIX,
            Barcode.FORMAT_AZTEC,
            Barcode.FORMAT_PDF417,
            Barcode.FORMAT_QR_CODE,
            -> QRCodeType.QRCODE.type

            else -> QRCodeType.BARCODE.type
        }
    }

}

