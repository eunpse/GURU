package com.example.guru

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.CalendarView
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import java.io.FileInputStream
import java.io.FileOutputStream

class CalendarFragment : Fragment() {

    var fname: String = ""
    var str: String = ""

    companion object{
        const val TAG : String = "로그"
    }

    lateinit var calendarView: CalendarView
    lateinit var cha_Btn: Button
    lateinit var del_Btn: Button
    lateinit var save_Btn: Button
    lateinit var diaryTextView: TextView
    lateinit var contextEditText: EditText
    lateinit var textView2: TextView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        Log.d(TAG, "CalendarFragment - onCreate() called" )

        return inflater.inflate(R.layout.fragment_calendar, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val calendarView = getView()?.findViewById<CalendarView>(R.id.calendarView)
        val diaryTextView = getView()?.findViewById<TextView>(R.id.diaryTextView)
        val contextEditText = getView()?.findViewById<TextView>(R.id.contextEditText)
        val textView2 = getView()?.findViewById<TextView>(R.id.textView2)
        val cha_Btn = getView()?.findViewById<Button>(R.id.cha_Btn)
        val del_Btn = getView()?.findViewById<Button>(R.id.del_Btn)
        val save_Btn = getView()?.findViewById<Button>(R.id.save_Btn)



        calendarView?.setOnDateChangeListener { view, year, month, dayOfMonth -> // 달력 날짜가 선택되면
            diaryTextView?.visibility = View.VISIBLE // 해당 날짜가 뜨는 textView가 Visible
            save_Btn?.visibility = View.VISIBLE // 저장 버튼이 Visible
            contextEditText?.visibility = View.VISIBLE // EditText가 Visible
            textView2?.visibility = View.INVISIBLE // 저장된 일기 textView가 Invisible
            cha_Btn?.visibility = View.INVISIBLE // 수정 Button이 Invisible
            del_Btn?.visibility = View.INVISIBLE // 삭제 Button이 Invisible

            diaryTextView?.text = String.format("%d / %d / %d", year, month + 1, dayOfMonth)
            // 날짜를 보여주는 텍스트에 해당 날짜를 넣는다.
            contextEditText?.setText("") // EditText에 공백값 넣기

            checkedDay(year, month, dayOfMonth) // checkedDay 메소드 호출
        }

        save_Btn?.setOnClickListener { // 저장 Button이 클릭되면
            saveDiary(fname) // saveDiary 메소드 호출

            str = contextEditText?.getText().toString() // str 변수에 edittext내용을 toString
            //형으로 저장
            textView2?.text = "${str}" // textView에 str 출력
            save_Btn?.visibility = View.INVISIBLE
            cha_Btn?.visibility = View.VISIBLE
            del_Btn?.visibility = View.VISIBLE
            contextEditText?.visibility = View.INVISIBLE
            textView2?.visibility = View.VISIBLE
        }
    }


    private fun checkedDay(cYear: Int, cMonth: Int, cDay: Int) {
        fname = "" + cYear + "-" + (cMonth + 1) + "" + "-" + cDay + ".txt" // 저장할 파일 이름 설정. Ex) 2019-01-20.txt
        var fis: FileInputStream? = null // FileStream fis 변수 설정

        try {
            fis = context?.openFileInput(fname)
            //fis = openFileInput(fname) // fname 파일 오픈!!

            val fileData = ByteArray(fis!!.available()) // fileData에 파이트 형식으로 저장
            fis?.read(fileData) // fileData를 읽음
            fis?.close()

            str = String(fileData) // str 변수에 fileData를 저장

            contextEditText.visibility = View.INVISIBLE
            textView2.visibility = View.VISIBLE
            textView2.text = "${str}" // textView에 str 출력

            save_Btn.visibility = View.INVISIBLE
            cha_Btn.visibility = View.VISIBLE
            del_Btn.visibility = View.VISIBLE

            cha_Btn.setOnClickListener { // 수정 버튼을 누를 시
                contextEditText.visibility = View.VISIBLE
                textView2.visibility = View.INVISIBLE
                contextEditText.setText(str) // editText에 textView에 저장된 내용을 출력
                save_Btn.visibility = View.VISIBLE
                cha_Btn.visibility = View.INVISIBLE
                del_Btn.visibility = View.INVISIBLE
                textView2.text = "${contextEditText.getText()}"
            }

            del_Btn.setOnClickListener {
                textView2.visibility = View.INVISIBLE
                contextEditText.setText("")
                contextEditText.visibility = View.VISIBLE
                save_Btn.visibility = View.VISIBLE
                cha_Btn.visibility = View.INVISIBLE
                del_Btn.visibility = View.INVISIBLE
                removeDiary(fname)

            }

            if(textView2.getText() == ""){
                textView2.visibility = View.INVISIBLE
                diaryTextView.visibility = View.VISIBLE
                save_Btn.visibility = View.VISIBLE
                cha_Btn.visibility = View.INVISIBLE
                del_Btn.visibility = View.INVISIBLE
                contextEditText.visibility = View.VISIBLE
            }

        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    @SuppressLint("WrongConstant")
    private fun saveDiary(readyDay: String) {
        var fos: FileOutputStream? = null

        try {
            fos = context?.openFileOutput(readyDay, AppCompatActivity.MODE_NO_LOCALIZED_COLLATORS)
            var content: String = contextEditText.getText().toString()
            fos?.write(content.toByteArray())
            fos?.close()

        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    @SuppressLint("WrongConstant")
    private fun removeDiary(readyDay: String) {
        var fos: FileOutputStream? = null

        try {
            fos = context?.openFileOutput(readyDay, AppCompatActivity.MODE_NO_LOCALIZED_COLLATORS)
            var content: String = ""
            fos?.write(content.toByteArray())
            fos?.close()

        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

}