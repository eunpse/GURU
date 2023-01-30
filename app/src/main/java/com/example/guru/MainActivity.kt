package com.example.guru

import android.app.AlertDialog
import android.content.Intent
import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.example.guru.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    lateinit var btnMove: Button
    lateinit var calendar_btn: Button
    lateinit var dog_note_btn: Button
    lateinit var setting_btn: Button

    companion object {

        const val TAG: String = "로그"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        Log.d(TAG, "MainActivity - onCreate() called")

        btnMove = findViewById<Button>(R.id.move_btn)
        calendar_btn = findViewById<Button>(R.id.calendar_btn)
        dog_note_btn = findViewById<Button>(R.id.dog_note_btn)
        setting_btn = findViewById<Button>(R.id.setting_btn)

        btnMove.setOnClickListener {
            //저장소 권한 설정
            if(ContextCompat.checkSelfPermission(this,
                    android.Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED){
                getPermission()
            }
            else{
                var intent = Intent(this, SettingActivity::class.java)
                startActivity(intent)
            }
        }

        calendar_btn.setOnClickListener {
            var intent = Intent(this, CalActivity::class.java)
            startActivity(intent)
        }

        dog_note_btn.setOnClickListener {
            var intent = Intent(this, CalActivity::class.java)
            startActivity(intent)
        }

        setting_btn.setOnClickListener {
            var intent = Intent(this, SettingActivity::class.java)
            startActivity(intent)
        }
    }

    //저장소 권한 얻기
    private fun getPermission(){
        //허용안함을 눌렀던 경우
        if(ActivityCompat.shouldShowRequestPermissionRationale(this, android.Manifest.permission.READ_EXTERNAL_STORAGE)){
            var dlg = AlertDialog.Builder(this)
            dlg.setTitle("권한이 필요한 이유")
            dlg.setMessage("사진 정보를 얻기 위해서는 외부 저장소 권한이 필수로 필요합니다")
            dlg.setPositiveButton("확인"){ _, _ ->
                ActivityCompat.requestPermissions(
                    this,
                    arrayOf(android.Manifest.permission.READ_EXTERNAL_STORAGE), 1000)}
            dlg.setNegativeButton("취소", null)
            dlg.show()
        }
        //권한 부여
        else{
            ActivityCompat.requestPermissions(this,
                arrayOf(android.Manifest.permission.READ_EXTERNAL_STORAGE), 1000)
        }
    }

}