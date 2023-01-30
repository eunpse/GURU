package com.example.guru

import android.content.Intent
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.ActionBar
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton

class MainActivity : AppCompatActivity(), AnimalAdapter.AnimalListener {

    //DB
    lateinit var myHelper: MyDBHelper
    lateinit var sqlDB: SQLiteDatabase

    //반려동물 리스트 출력을 위한 변수
    lateinit var rView: RecyclerView
    lateinit var vAdpater: RecyclerView.Adapter<*>
    lateinit var vManager: RecyclerView.LayoutManager

    //새로운 반려동물 추가 버튼
    lateinit var newAnimalAdd: FloatingActionButton

    //유저 정보 출력 변수
    lateinit var userName: TextView
    lateinit var userPhoneNum: TextView

    //기타 설정 텍스트 버튼
    lateinit var calBtn: TextView
    lateinit var logoutTextBtn: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_setting)

        //액션바 설정
        val ac: ActionBar? = supportActionBar
        ac?.title = "홈"

        myHelper = MyDBHelper(this)
        userName = findViewById<TextView>(R.id.user_name)
        userPhoneNum = findViewById<TextView>(R.id.user_phone_num)
        newAnimalAdd = findViewById<FloatingActionButton>(R.id.new_animal_insert)
        calBtn = findViewById<TextView>(R.id.opensource_license)
        logoutTextBtn = findViewById<TextView>(R.id.logout)

        //유저 이름 설정
        //userName.text = ""
        //유저 핸드폰 번호 설정
        //userPhoneNum.text = ""

        //일정 등록하러가기
        calBtn.setOnClickListener {
            var intent = Intent(this, CalActivity::class.java)
            startActivity(intent)
        }
        //새로운 반려동물 등록 액티비티로 이동
        newAnimalAdd.setOnClickListener{
            var intent = Intent(this, AddDataActivity::class.java)
            startActivity(intent)
        }
        //로그아웃 버튼
        logoutTextBtn.setOnClickListener {
            //로그아웃 기능 수행

            //로그인 화면으로 이동
            var intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
        }

        showList() //반려동물 데이터 설정
        init() //반려동물 리스트 어댑터뷰 설정
    }

    //fun -----------------------------------------------------------------

    //반려동물 리스트에 출력될 데이터 설정
    private fun showList() {
        sqlDB = myHelper.readableDatabase
        var cursor: Cursor

        var id: Int
        var strNames: String = ""
        var strImgs: String = ""
        var strRegNums: String = ""
        var strGender: String = ""
        var strBreed: String = ""
        var strAge: String = ""
        var strBirth: String = ""
        var strFeature: String = ""

        //DB에 있는 반려동물 정보 가져옴
        cursor = sqlDB.rawQuery("SELECT id, animal_name, animal_img, animal_reg_num, animal_gender," +
                " animal_breed, animal_age, animal_birth, animal_feature FROM animalTBL;", null)

        animalList.clear()
        //animal 객체로 생성해서 리스트에 저장
        while(cursor.moveToNext()){
            id = cursor.getInt(0)
            strNames = cursor.getString(1)
            strImgs = cursor.getString(2)
            strRegNums = cursor.getString(3)
            strGender = cursor.getString(4)
            strBreed = cursor.getString(5)
            strAge = cursor.getString(6)
            strBirth = cursor.getString(7)
            strFeature = cursor.getString(8)

            animalList.add(Animal(id, strNames, Uri.parse(strImgs), strRegNums, strBreed, strAge, strGender, strBirth, strFeature))
        }
        cursor.close()
        sqlDB.close()
    }

    //어댑터 뷰 설정
    private fun init() {
        vManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)

        vAdpater = AnimalAdapter(animalList, this)

        rView = findViewById<RecyclerView?>(R.id.animalSelectView).apply {
            setHasFixedSize(true)
            layoutManager = vManager
            adapter = vAdpater
        }
    }

    //반려동물 프로필 액티비티로 이동
    override fun onClick(animal: Animal) {
        var intent = Intent(this, AnimalDetailActivity::class.java)
        intent.putExtra(ANIMAL_ID_EXTRA, animal.id)
        startActivity(intent)
    }

    //뒤로가기 했을때 추가 액티비티로 이동되는 것 방지
    override fun onBackPressed() {
        super.onBackPressed()
        var intent = Intent(this, BeforeSettingActivity::class.java)
        startActivity(intent)
    }
}