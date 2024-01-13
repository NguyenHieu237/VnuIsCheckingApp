package com.bkplus.scan_qrcode_barcode.ui.scanner

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.media.MediaPlayer
import android.os.*
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.camera.core.*
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.core.app.ActivityCompat
import androidx.core.os.bundleOf
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.ads.control.admob.AppOpenManager
import com.bkplus.scan_qrcode_barcode.R
import com.bkplus.scan_qrcode_barcode.Response.CheckinResponse
import com.bkplus.scan_qrcode_barcode.Service.APIService
import com.bkplus.scan_qrcode_barcode.base.API.CheckingAppAPi
import com.bkplus.scan_qrcode_barcode.base.BaseFragment
import com.bkplus.scan_qrcode_barcode.databinding.FragmentScannerBinding
import com.bkplus.scan_qrcode_barcode.manager.database.QRCodeResult
import com.bkplus.scan_qrcode_barcode.manager.database.QRCodeType
import com.bkplus.scan_qrcode_barcode.manager.qrcode.GenerateQRCodeResult
import com.bkplus.scan_qrcode_barcode.preferences.QRCodePreferences
import com.bkplus.scan_qrcode_barcode.ui.SuccessPopUp
import com.bkplus.scan_qrcode_barcode.ui.home.HomeActivity
import com.bkplus.scan_qrcode_barcode.utils.BitmapUtils
import com.bkplus.scan_qrcode_barcode.utils.extension.guardLet
import com.google.mlkit.vision.barcode.BarcodeScannerOptions
import com.google.mlkit.vision.barcode.BarcodeScanning
import com.google.mlkit.vision.barcode.common.Barcode
import com.google.mlkit.vision.common.InputImage
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Response
import java.util.concurrent.Executor

class ScannerFragment : BaseFragment<FragmentScannerBinding>() {
    private var isTorchEnabled = false
    private var camera: Camera? = null
    private var cameraProvider: ProcessCameraProvider? = null
    private var imageCapture: ImageCapture? = null
    private var lens: Int = CameraSelector.LENS_FACING_BACK
    private lateinit var mShutterSound: MediaPlayer
    private val viewModel by activityViewModels<ScanResultViewModel>()


    companion object {
        private const val PICK_IMAGE = 1234
        var scanSound = false
        var vibrator = false
        fun newInstance(): ScannerFragment {
            val args = Bundle()
            val fragment = ScannerFragment()
            fragment.arguments = args
            return fragment
        }
    }

    override val layoutId: Int
        get() = R.layout.fragment_scanner

    @RequiresApi(Build.VERSION_CODES.P)
    override fun onResume() {
        super.onResume()
        cameraProvider?.let { bindPreview(it) }
        binding.zoomSlider.value = 0f
        AppOpenManager.getInstance().enableAppResume()
    }

    override fun onPause() {
        super.onPause()
        unbindCamera()
        binding.torchBtn.setImageResource(R.drawable.ic_flashlight_off)
        isTorchEnabled = false
    }

    override fun setupUI() {
        mShutterSound = MediaPlayer.create(context, R.raw.shutter)
    }

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    override fun setupListener() {
        binding.torchBtn.setOnClickListener {
            isTorchEnabled = !isTorchEnabled
            val flashIcon: Int
            if (isTorchEnabled) {
                flashIcon = R.drawable.ic_flashlight_on
                camera?.cameraControl?.enableTorch(true)
            } else {
                flashIcon = R.drawable.ic_flashlight_off
                camera?.cameraControl?.enableTorch(false)
            }
            binding.torchBtn.setImageResource(flashIcon)
        }

        binding.switchCamera.setOnClickListener {
            switchCamera()
        }

        binding.chooseFromGalery.setOnClickListener {
//			loadAdsInterSelectPhotoIfNeed()
            if (checkPermissionForReadExtertalStorage()) {
                findNavController().navigate(R.id.selectAlbumFragment)

            } else {
                requestPermissionForReadExtertalStorage()
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.P)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        view.post {
            (activity as HomeActivity).requestPermission()
            var check = true
            (activity as HomeActivity).checkCameraPermission {
                check = false
                openCamera(view.context)
            }
            if (check) {
                openCamera(view.context)
            }
        }
    }

    fun unbindCamera() {
        cameraProvider?.unbindAll()
    }

    @SuppressLint("UnsafeOptInUsageError", "RestrictedApi", "SuspiciousIndentation")
    @RequiresApi(Build.VERSION_CODES.P)
    private fun bindPreview(cameraProvider: ProcessCameraProvider) {
        val preview = Preview.Builder().build()
        val cameraSelector = CameraSelector.Builder()
            .requireLensFacing(lens)
            .build()
        val imageAnalysis = getBarcodeDetector(onBarcodeDetected = { barcode, image ->
            if (scanSound) mShutterSound.start()
            if (vibrator) {
                vibratePhone()
            }
            val bitmap = BitmapUtils.getCroppedBitmap(image, barcode.boundingBox!!)
            if (barcode.rawValue != viewModel.barcode?.rawValue) {
                viewModel.barcode = barcode
                gotoResult(bitmap, barcode)
                Log.e("scannnnnnn", "scannnn")
            } else {

            }
        })
        imageCapture = ImageCapture.Builder()
            .setCaptureMode(ImageCapture.CAPTURE_MODE_MAXIMIZE_QUALITY)
            .build()

        preview.setSurfaceProvider(binding.previewView.surfaceProvider)
        val useCaseGroup = UseCaseGroup.Builder()
            .addUseCase(preview)
            .addUseCase(imageAnalysis)
            .addUseCase(imageCapture!!)
            .build()

        cameraProvider.unbindAll()
        camera =
            cameraProvider.bindToLifecycle(this as LifecycleOwner, cameraSelector, useCaseGroup)
        setupZoomControl(camera!!)
    }

    private fun getBarcodeType(barcode: Barcode): Int {
        return when (barcode.format) {
            Barcode.FORMAT_DATA_MATRIX,
            Barcode.FORMAT_AZTEC,
            Barcode.FORMAT_PDF417,
            Barcode.FORMAT_QR_CODE,
            -> QRCodeType.QRCODE.type

            else -> QRCodeType.BARCODE.type
        }
    }

    private fun setupZoomControl(camera: Camera) {
        binding.zoomSlider.addOnChangeListener { _, value, _ ->
            camera.cameraControl.setLinearZoom(value / 100f)
        }
    }

    @SuppressLint("UnsafeOptInUsageError")
    private fun getBarcodeDetector(onBarcodeDetected: (barcode: Barcode, image: ImageProxy) -> Unit): ImageAnalysis {
        val barcodeOption = BarcodeScannerOptions.Builder()
            .setBarcodeFormats(Barcode.FORMAT_ALL_FORMATS)
            .build()
        val barcodeScanner = BarcodeScanning.getClient(barcodeOption)

        val analyzer = ImageAnalysis.Analyzer { imageProxy ->
            if (imageProxy.image == null) return@Analyzer
            barcodeScanner.process(InputImage.fromMediaImage(imageProxy.image!!, 0))
                .addOnSuccessListener {
                    if (it.isNotEmpty()) {
                        onBarcodeDetected.invoke(it[0], imageProxy)
                    }
                }
                .addOnSuccessListener {
                    imageProxy.close()
                }
        }

        val imageAnalysis = ImageAnalysis.Builder().build()
        imageAnalysis.setAnalyzer(MyExecutor(), analyzer)

        return imageAnalysis
    }

    fun vibratePhone() {
        val vibrator = context?.getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
        vibrator.vibrate(VibrationEffect.createOneShot(200, VibrationEffect.DEFAULT_AMPLITUDE))
    }

    // TODO Call back of fragment
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == PICK_IMAGE && resultCode == Activity.RESULT_OK) {
            val dataResult = data?.data.guardLet { return }!!
            val inputStream = requireContext().contentResolver.openInputStream(dataResult)
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
                        gotoResult(bitmap, it[0])
                    }
                }
            inputStream?.close()
        }
    }

    @RequiresApi(Build.VERSION_CODES.P)
    private fun openCamera(context: Context) {
        val cameraProviderFuture = ProcessCameraProvider.getInstance(context)
        cameraProviderFuture.addListener(
            {
                cameraProvider = cameraProviderFuture.get()
                bindPreview(cameraProvider!!)
            },
            MyExecutor()
        )
    }

    private fun openGallery() {
        AppOpenManager.getInstance().disableAppResume()
        val intent = Intent()
        intent.type = "image/*"
        intent.action = Intent.ACTION_GET_CONTENT
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE)

    }

    // TODO Navigation
    private fun gotoResult(bitmap: Bitmap, barcode: Barcode) {
        val result = GenerateQRCodeResult(
            qrCodeBitmap = bitmap,
            qrCodeContent = barcode.displayValue!!,
            resultType = QRCodeResult.SCAN.type,
            qrCodeType = getBarcodeType(barcode = barcode),
        )
        val bundle = bundleOf("result" to result)
        var token = QRCodePreferences.getPrefsInstance().token
        var id = result.qrCodeContent.toInt()
        Log.e("scannnnnnn", "$token")
        Log.e("scannnnnnn", "${id}")
        if (token != null) {
            checkEvent(id, token)
        }
    }

    private fun makeUICreateQRCode() {
        findNavController().navigate(R.id.generatorFragment)
    }

    @RequiresApi(Build.VERSION_CODES.P)
    private fun switchCamera() {
        if (lens == CameraSelector.LENS_FACING_BACK) lens = CameraSelector.LENS_FACING_FRONT
        else lens = CameraSelector.LENS_FACING_BACK
        unbindCamera()
        cameraProvider?.let { bindPreview(it) }
        binding.zoomSlider.value = 0f
    }

    inner class MyExecutor : Executor {
        private val handler = Handler(Looper.getMainLooper())
        override fun execute(runnable: Runnable?) {
            handler.post(runnable!!)
        }
    }

    private val READ_STORAGE_PERMISSION_REQUEST_CODE = 41
    fun checkPermissionForReadExtertalStorage(): Boolean {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            val result = context?.checkSelfPermission(Manifest.permission.READ_MEDIA_IMAGES)
            return result == PackageManager.PERMISSION_GRANTED
        } else {
            val result = context?.checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE)
            return result == PackageManager.PERMISSION_GRANTED
        }
    }

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    @Throws(Exception::class)
    fun requestPermissionForReadExtertalStorage() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            try {
                ActivityCompat.requestPermissions(
                    (context as Activity?)!!, arrayOf(Manifest.permission.READ_MEDIA_IMAGES),
                    READ_STORAGE_PERMISSION_REQUEST_CODE
                )
            } catch (e: Exception) {
                e.printStackTrace()
                throw e
            }
        } else {
            try {
                ActivityCompat.requestPermissions(
                    (context as Activity?)!!, arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE),
                    READ_STORAGE_PERMISSION_REQUEST_CODE
                )
            } catch (e: Exception) {
                e.printStackTrace()
                throw e

            }
        }
    }

    fun checkEvent(eventId: Int, token: String) {
        viewModel.checkEvent(eventId, token)

        // Observe changes in checkEventResponse LiveData
        viewModel.checkEventResponse.observe(viewLifecycleOwner) { checkEventResponse ->
            var message = checkEventResponse.message
            var successPopUp = SuccessPopUp()
            successPopUp!!.show(parentFragmentManager, null)
        }
    }

}