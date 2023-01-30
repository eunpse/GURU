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

    // 프레그먼트 선언

    lateinit var binding: ActivityMainBinding

    companion object {

        const val TAG: String = "로그"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        Log.d(TAG, "MainActivity - onCreate() called")

        binding = ActivityMainBinding.inflate(layoutInflater)
        // 레이아웃과 연결
        setContentView(R.layout.activity_main)
        setContentView(binding.root)

        val bottomBar = binding.bnvMain

        bottomBar.setOnItemSelectedListener { item ->
            Log.d(TAG,"MainActivity - setOnItemSelectedListener() called")
            changeFragment(
                when (item.itemId) {
                    R.id.menu_calendar-> {
                        CalendarFragment()
                    }
                    R.id.menu_setting->{
                        SettingFragment()
                    }
                    else -> {
                        DogNoteFragment()
                    }
                }
            )
            true
        }
        bottomBar.selectedItemId = R.id.menu_dog_note

        btnMove = findViewById<Button>(R.id.move_btn)

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
    }

    private fun changeFragment(fragment: Fragment) {
        supportFragmentManager
            .beginTransaction()
            .replace(R.id.fl_container, fragment)
            .commit()
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