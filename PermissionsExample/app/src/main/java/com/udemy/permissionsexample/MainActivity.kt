package com.udemy.permissionsexample

import android.Manifest
import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    companion object{
        // カメラ権限コード
        private const val CAMERA_PERMISSION_CODE = 1
        // GPS権限コード
        private const val FINE_LOCATION_PERMISSION_CODE = 2
        // GPSとカメラ権限コード
        private const val CAMERA_AND_FINE_LOCATION_PERMISSION_CODE = 12
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // 「Request Camera Permission」ボタンを押す場合、
        // 権限がない場合、権限を要求して
        // 権限がある場合、アラートメッセージを表示する
        btnCameraPermission.setOnClickListener {
            // カメラとGPSの権限がある場合、
            if(ContextCompat.checkSelfPermission(this,
                    Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED &&
                ContextCompat.checkSelfPermission(this,
                    Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED){

                Toast.makeText(this,
                    "You already have the permission for camera and gps", Toast.LENGTH_SHORT).show()
            }
            // カメラの権限がない場合、権限を要求する
            else{
                // RequestPermissions関数は自動的にonRequestPermissionsResult関数を呼び出す
                ActivityCompat.requestPermissions(this,
                    arrayOf(Manifest.permission.CAMERA, Manifest.permission.ACCESS_FINE_LOCATION),
                    CAMERA_AND_FINE_LOCATION_PERMISSION_CODE)
            }
        }
    }

    // 権限を与えたのかどうかをチェックする
    override fun onRequestPermissionsResult(requestCode: Int,permissions: Array<out String>,grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        if(requestCode == CAMERA_PERMISSION_CODE){
            // カメラの権限を与えられた場合、
            if(grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                Toast.makeText(this,
                    "Permission granted for camera", Toast.LENGTH_SHORT).show()
            }
            // カメラの権限を否定した場合、
            else {
                Toast.makeText(this,
                    "Oops you just denied the permission for camera. You can also allow it from settings",
                    Toast.LENGTH_SHORT).show()
            }
        }
    }
}