package com.udemy.kidsdrawingapp

import android.Manifest
import android.app.Activity
import android.app.Dialog
import android.app.Notification
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.media.MediaScannerConnection
import android.os.AsyncTask
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.view.View
import android.widget.ImageButton
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.view.get
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.dailog_brush_size.*
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileOutputStream
import java.lang.Exception

class MainActivity : AppCompatActivity() {
    private var mImageButtonCurrentPaint: ImageButton? = null

    companion object{
        private const val STORAGE_PERMISSION_CODE = 1
        private const val GALLERY = 2
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Brushのサイズを設定する
        drawing_view.setSizeForBrush(20.toFloat())

        // CurrentPaintColorを設定する
        mImageButtonCurrentPaint = ll_paint_colors[1] as ImageButton
        mImageButtonCurrentPaint!!.setImageDrawable(
            ContextCompat.getDrawable(this, R.drawable.pallet_pressed)
        )

        // BrushImageをクリックする場合、BrushSizeChooserダイアログを表示する
        ib_brush.setOnClickListener {
            showBrushSizeChooserDialog()
        }

        // Galleryをクリックする場合、
        ib_gallery.setOnClickListener {
            // アクセス権限がある場合、
            if(isReadStorageAllowed()){
                // Galleryから画像を読み込む
                val pickPhotoIntent = Intent(Intent.ACTION_PICK,
                    MediaStore.Images.Media.EXTERNAL_CONTENT_URI)

                startActivityForResult(pickPhotoIntent, GALLERY)
            }
            // アクセス権限がない場合、
            else{
                // アクセス権限を要求する
                requestStoragePermission()
            }
        }

        // Undoボタンを押す場合、
        ib_undo.setOnClickListener {
            drawing_view.onClickUndo()
        }

        // Saveボタンを押す場合、
        ib_save.setOnClickListener {
            // ExternalStorageをアクセス権限がある場合
            if(isReadStorageAllowed()){
                // ViewからBitmapに変更し、BitmapからFileに変更してDeviceに保存する
                BitMapAsyncTask(getBitmapFromView(fl_drawing_view_container)).execute()
            }
        }
    }

    // BrushSizeChooserダイアログを作成する
    private fun showBrushSizeChooserDialog(){
        // BrushSizeChooserダイアログを作成する
        var brushDialog = Dialog(this)
        brushDialog.setContentView(R.layout.dailog_brush_size)
        brushDialog.setTitle("Brush size: ")

        // SmallBrushButtonをクリックする場合、
        val smallBtn = brushDialog.ib_small_brush
        smallBtn.setOnClickListener {
            // Brushのサイズを設定する
            drawing_view.setSizeForBrush(10.toFloat())

            // BrushSizeChooserダイアログを非表示する
            brushDialog.dismiss()
        }

        // MediumBrushButtonをクリックする場合、
        val mediumBtn = brushDialog.ib_medium_brush
        mediumBtn.setOnClickListener {
            // Brushのサイズを設定する
            drawing_view.setSizeForBrush(20.toFloat())

            // BrushSizeChooserダイアログを非表示する
            brushDialog.dismiss()
        }

        // LargeBrushButtonをクリックする場合、
        val largeBtn = brushDialog.ib_large_brush
        largeBtn.setOnClickListener {
            // Brushのサイズを設定する
            drawing_view.setSizeForBrush(30.toFloat())

            // BrushSizeChooserダイアログを非表示する
            brushDialog.dismiss()
        }

        // BrushSizeChooserダイアログを表示する
        brushDialog.show()
    }

    // ColorButtonをクリックする場合、
    fun paintClicked(view: View){
        if(view !== mImageButtonCurrentPaint){
            // CurrentImageButtonを取得する
            val imageButton = view as ImageButton

            // ColorTagを取得してDrawingViewのColorに設定する
            val colorTag = imageButton.tag.toString()
            drawing_view.setColor(colorTag)

            // CurrentImageButtonを選択する
            imageButton.setImageDrawable(
                ContextCompat.getDrawable(this, R.drawable.pallet_pressed)
            )

            // 前選択したImageButtonを元に戻す
            mImageButtonCurrentPaint!!.setImageDrawable(
                ContextCompat.getDrawable(this, R.drawable.pallet_normal)
            )
            mImageButtonCurrentPaint = view
        }
    }

    // Storage権限を要求する
    private fun requestStoragePermission(){
        if(ActivityCompat.shouldShowRequestPermissionRationale(this,
                arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE).toString())){

            Toast.makeText(this,
                "Need permission to add a Background", Toast.LENGTH_SHORT).show()
        }

        // Storageの権限を要求する
        ActivityCompat.requestPermissions(this,
            arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.WRITE_EXTERNAL_STORAGE), STORAGE_PERMISSION_CODE)
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>,
                                            grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if(requestCode == STORAGE_PERMISSION_CODE){
            // Storageのアクセス権限を与えられた場合、
            if(grantResults.isNotEmpty()
                && grantResults[0] == PackageManager.PERMISSION_GRANTED){

                Toast.makeText(this,
                    "Permission granted now you can read the storage",
                    Toast.LENGTH_LONG).show()
            }
            // Storageのアクセス権限を否定した場合、
            else {
                Toast.makeText(this,
                    "Oops you just denied the permission", Toast.LENGTH_LONG).show()
            }
        }
    }

    // Storageの権限を与えたかどうかをチェックする
    private fun isReadStorageAllowed(): Boolean{
        val result = ContextCompat.checkSelfPermission(this,
            Manifest.permission.READ_EXTERNAL_STORAGE)
        return result == PackageManager.PERMISSION_GRANTED
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(resultCode == Activity.RESULT_OK){
            if(requestCode == GALLERY){
                try {
                    if(data!!.data != null){
                        iv_background.visibility = View.VISIBLE
                        iv_background.setImageURI(data.data)
                    }else {
                        Toast.makeText(this@MainActivity,
                            "Error in parsing image or its corrupted.",
                            Toast.LENGTH_SHORT).show()
                    }
                }catch (e: Exception){
                    e.printStackTrace()
                }
            }
        }
    }

    // 画像をDeviceに保存するため、ViewからBitmapに変更する
    private fun getBitmapFromView(view: View): Bitmap {
        val returnedBitmap = Bitmap.createBitmap(view.width,
            view.height, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(returnedBitmap)
        val bgDrawable = view.background
        if(bgDrawable != null){
            bgDrawable.draw(canvas)
        }else {
            canvas.drawColor(Color.WHITE)
        }
        view.draw(canvas)
        return  returnedBitmap
    }

    // FileをDeviceに保存する関数
    private inner class BitMapAsyncTask(val mBitmap: Bitmap):
        AsyncTask<Any, Void, String>() {

        private lateinit var mProgressDialog: Dialog

        // Fileを保存する前にProgressDialogを表示する
        override fun onPreExecute() {
            super.onPreExecute()
            showProgressDialog()
        }

        override fun doInBackground(vararg params: Any?): String {
            var result = ""

            if(mBitmap != null){
                try {
                    val bytes = ByteArrayOutputStream()
                    // BitMapからPNGに変更する
                    mBitmap.compress(Bitmap.CompressFormat.PNG, 90, bytes)

                    // Deviceに保存するためFileに変更する
                    val f = File(externalCacheDir!!.absoluteFile.toString()
                            + File.separator + "KidDrawingApp_"
                            + System.currentTimeMillis() / 1000 + ".png")

                    // Write File To Device
                    val fos = FileOutputStream(f)
                    fos.write(bytes.toByteArray())
                    fos.close()

                    result = f.absolutePath
                }catch (e: Exception){
                    result = ""
                    e.printStackTrace()
                }
            }
            return result
        }

        // Fileを保存した後、呼び出す関数
        override fun onPostExecute(result: String?) {
            super.onPostExecute(result)

            // Fileを保存した後ProgressDialogを閉じる
            cancelProgressDialog()

            // Fileが成功に保存された場合
            if(result!!.isNotEmpty()){
                Toast.makeText(this@MainActivity,
                    "File Save Successfully : $result", Toast.LENGTH_SHORT).show()
            }
            // File保存に失敗した場合
            else {
                Toast.makeText(this@MainActivity,
                    "Something went wrong while saving the file.", Toast.LENGTH_SHORT).show()
            }
            // ImageFileをEメールなどにシェアする
            MediaScannerConnection.scanFile(this@MainActivity, arrayOf(result), null){
                path, uri ->  val shareIntent = Intent()
                shareIntent.action = Intent.ACTION_SEND
                shareIntent.putExtra(Intent.EXTRA_STREAM, uri)
                shareIntent.type = "image/png"

                startActivity(
                    Intent.createChooser(
                        shareIntent, "Share"
                    )
                )
            }
        }

        // ProgressDialogを表示する
        private fun showProgressDialog(){
            mProgressDialog = Dialog(this@MainActivity)
            mProgressDialog.setContentView(R.layout.dialog_custom_progress)
            mProgressDialog.show()
        }

        // ProgressDialogを閉じる
        private fun cancelProgressDialog(){
            mProgressDialog.dismiss()
        }
    }
}