package com.example.ehs.Closet

import android.graphics.Bitmap
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.widget.Toolbar
import androidx.core.content.ContextCompat
import androidx.core.view.WindowInsetsControllerCompat
import androidx.recyclerview.widget.GridLayoutManager
import com.example.ehs.R
import kotlinx.android.synthetic.main.activity_basic_clothes.*
import kotlinx.android.synthetic.main.fragment_closet.*

class BasicClothesActivity : AppCompatActivity() {

    val BasicClothesList = mutableListOf<BasicClothes>()
    var a_bitmap: Bitmap? = null

    var adapter = BasicClothesListAdapter(BasicClothesList)


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_basic_clothes)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            WindowInsetsControllerCompat(window, view).isAppearanceLightStatusBars = true
            this.window.statusBarColor = ContextCompat.getColor(this, R.color.white)
        }


        /**
         * 액션바 대신 툴바를 사용하도록 설정
         */
        val toolbar = findViewById(R.id.toolbar_basic) as Toolbar
        setSupportActionBar(toolbar)
        val ab = supportActionBar!!
        ab.setDisplayShowTitleEnabled(false)
        // 툴바에 뒤로 가기 버튼 생성
        ab.setDisplayHomeAsUpEnabled(true) // 여기까지 툴바 설정 완료



        BasicClothesList.add(BasicClothes(R.drawable.basic_tee))
        BasicClothesList.add(BasicClothes(R.drawable.basic_longtee))
        BasicClothesList.add(BasicClothes(R.drawable.basic_jean))
        BasicClothesList.add(BasicClothes(R.drawable.basic_whiteshortskirt))
        BasicClothesList.add(BasicClothes(R.drawable.basic_whitelongskirt))
        BasicClothesList.add(BasicClothes(R.drawable.basic_whiteshoes))

        BasicClothesList.add(BasicClothes(R.drawable.basic_blackjacket))
        BasicClothesList.add(BasicClothes(R.drawable.basic_whitejacket))
        BasicClothesList.add(BasicClothes(R.drawable.basic_tweed))
        BasicClothesList.add(BasicClothes(R.drawable.basic_trench))
        BasicClothesList.add(BasicClothes(R.drawable.basic_bluejacket))
        BasicClothesList.add(BasicClothes(R.drawable.basic_cardigan))

        BasicClothesList.add(BasicClothes(R.drawable.basic_grayknit))
        BasicClothesList.add(BasicClothes(R.drawable.basic_whiteknit))

        BasicClothesList.add(BasicClothes(R.drawable.basic_blackjeans))
        BasicClothesList.add(BasicClothes(R.drawable.basic_shortskirt))
        BasicClothesList.add(BasicClothes(R.drawable.basic_grayshortskirt))

        BasicClothesList.add(BasicClothes(R.drawable.basic_shoes))


        val gridLayoutManager = GridLayoutManager(applicationContext, 3)
        rv_basic.layoutManager = gridLayoutManager

        rv_basic.adapter = adapter
        adapter.notifyDataSetChanged()


    }

    /**
     * 툴바 뒤로가기 버튼 액션 설정
     */
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val id = item.itemId
        when (id) {
            android.R.id.home -> {

                finish()
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    } // 툴바 뒤로가기 액션 설정 끝

}