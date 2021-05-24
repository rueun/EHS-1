package com.example.ehs

import android.Manifest
import android.content.pm.PackageManager
import android.database.Cursor
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.text.TextUtils
import android.util.Base64
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.FragmentManager
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.gun0912.tedpermission.PermissionListener
import com.gun0912.tedpermission.TedPermission
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.fragment_closet.*
import java.io.ByteArrayOutputStream
import java.util.*

class MainActivity : AppCompatActivity() {


    val TAG :String = "메인페이지"

    // 메인액티비티 클래스가 가지고 있는 멤버들
    private lateinit var homeFragment: HomeFragment
    private lateinit var fashionistaFragment: FashionistaFragment
    private lateinit var closetFragment: ClosetFragment
    private lateinit var feedFragment: FeedFragment
    private lateinit var mypageFragment: MypageFragment

    var userId :String? = ""
    var userPw :String? = ""
    var userName :String? = ""
    var userEmail :String? = ""
    var userBirth :String? = ""
    var userGender :String? = ""
    var userLevel :String? = ""
    val bundle = Bundle()

    // 화면이 메모리에 올라갔을 때
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        //LoginActivity의 인텐트를 받아서 text값을 저장
        val intent = getIntent()
        userId = intent.getStringExtra("userId")
        userPw = intent.getStringExtra("userPw")
        userName = intent.getStringExtra("userName")
        userEmail = intent.getStringExtra("userEmail")
        userBirth = intent.getStringExtra("userBirth")
        userGender = intent.getStringExtra("userGender")
        userLevel = intent.getStringExtra("userLevel")

        bundle.putString("userId", userId);
        bundle.putString("userPw", userPw);
        bundle.putString("userName", userName);
        bundle.putString("userEmail", userEmail);
        bundle.putString("userBirth", userBirth);
        bundle.putString("userGender", userGender);
        bundle.putString("userLevel", userLevel);

        // 바텀 네비게이션
        bottom_nav.setOnNavigationItemSelectedListener(onBottomNavItemSeletedListener)

        homeFragment = HomeFragment.newInstance()
        supportFragmentManager.beginTransaction().add(R.id.fragments_frame, homeFragment).commit() // add는 프레그먼트 추가해주는 것

        //권한설정
        setPermission()
//
//        //애뮬레이터 갤러리 권한 설정
//        checkSelfPermission()
    }


    // 바텀 네비게이션 아이템 클릭 리스너 설정
    private val onBottomNavItemSeletedListener = BottomNavigationView.OnNavigationItemSelectedListener {
        // when은 코틀린에서 switch문
        when(it.itemId) {
            R.id.menu_home -> {
                Log.d(TAG, "MainActivity - 홈버튼 클릭!")
                homeFragment = HomeFragment.newInstance()
                supportFragmentManager.beginTransaction().replace(
                    R.id.fragments_frame,
                    homeFragment
                ).commit() // replace는 다른 프레그먼트로 교체해주는 것
            }
            R.id.menu_fashionista -> {
                Log.d(TAG, "MainActivity - 패셔니스타 버튼 클릭!")
                fashionistaFragment = FashionistaFragment.newInstance()
                supportFragmentManager.beginTransaction().replace(
                    R.id.fragments_frame,
                    fashionistaFragment
                ).commit() // replace는 다른 프레그먼트로 교체해주는 것
            }
            R.id.menu_closet -> {
                Log.d(TAG, "MainActivity - 옷장 버튼 클릭!")
                closetFragment = ClosetFragment.newInstance()
                supportFragmentManager.beginTransaction().replace(
                    R.id.fragments_frame,
                    closetFragment
                ).commit() // replace는 다른 프레그먼트로 교체해주는 것

            }
            R.id.menu_feed -> {
                Log.d(TAG, "MainActivity - 피드 버튼 클릭!")
                feedFragment = FeedFragment.newInstance()
                supportFragmentManager.beginTransaction().replace(
                    R.id.fragments_frame,
                    feedFragment
                ).commit() // replace는 다른 프레그먼트로 교체해주는 것
            }
            R.id.menu_mypage -> {
                Log.d(TAG, "MainActivity - 마이페이지 버튼 클릭!")
                mypageFragment = MypageFragment.newInstance()
                supportFragmentManager.beginTransaction().replace(
                    R.id.fragments_frame,
                    mypageFragment
                ).commit() // replace는 다른 프레그먼트로 교체해주는 것

                Log.d(TAG, "아이야 제발로 나와줘라" + userId)

                //프래그먼트로 번들 전달
                mypageFragment.setArguments(bundle)
            }
        } // when문 끝
        true
    }

    /**
     * 테드 퍼미션 설정
     */
    private fun setPermission() {
        val permission = object : PermissionListener {
            override fun onPermissionGranted() { // 설정해놓은 위험 권한들이 허용되었을 경우 이곳을 수행함.
                Toast.makeText(this@MainActivity, "권한이 혀용 되었습니다.", Toast.LENGTH_SHORT).show()
            }
            override fun onPermissionDenied(deniedPermissions: MutableList<String>?) { // 설정해놓은 위험 권한들 중 거부를 한 경우 이곳을 수행함.
                Toast.makeText(this@MainActivity, "권한이 거부 되었습니다.", Toast.LENGTH_SHORT).show()
            }
        }
        TedPermission.with(this)
                .setPermissionListener(permission)
                .setRationaleMessage("카메라 앱을 사용하시려면 권한을 허용해주세요.")
                .setDeniedMessage("권한을 거부하셨습니다. [앱 설정] -> [권한] 항목에서 허용해주세요.")
                .setPermissions(
                    android.Manifest.permission.WRITE_EXTERNAL_STORAGE,
                    android.Manifest.permission.CAMERA
                )
                .check()
    }



    //에뮬레이터 갤러리 권한설정
    fun checkSelfPermission() {
        var temp = "" //파일 읽기 권한 확인


        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            temp += Manifest.permission.READ_EXTERNAL_STORAGE.toString() + " "
        } //파일 쓰기 권한 확인


        if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            temp += Manifest.permission.WRITE_EXTERNAL_STORAGE.toString() + " "
        }

        if (TextUtils.isEmpty(temp) == false) {
            // 권한 요청
            ActivityCompat.requestPermissions(this, temp.trim { it <= ' ' }.split(" ").toTypedArray(), 1)
        } else {
            // 모두 허용 상태
            Toast.makeText(this, "권한을 모두 허용", Toast.LENGTH_SHORT).show()
        }


    }



}