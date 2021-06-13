package com.example.ehs.AI

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.ehs.R
import com.example.ehs.ml.ModelUnquant
import com.gun0912.tedpermission.PermissionListener
import com.gun0912.tedpermission.TedPermission
import kotlinx.android.synthetic.main.activity_ai.*
import org.tensorflow.lite.DataType
import org.tensorflow.lite.support.image.TensorImage
import org.tensorflow.lite.support.tensorbuffer.TensorBuffer
import java.nio.ByteBuffer
import java.util.*


class AIActivity : AppCompatActivity() {


    val REQUEST_IMAGE_CAPTURE = 1 // 카메라 사진 촬영 요청코드, 한번 지정되면 값이 바뀌지 않음
    val REQUEST_OPEN_GALLERY = 2


    var bitmap : Bitmap? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_ai)

        //권한설정
        setPermission()

        btn_album.setOnClickListener {
            openGallery()
        }

        btn_ai.setOnClickListener {

            Log.d("평가하기", "버튼클릭")


            if(bitmap==null) {
                Toast.makeText(this, "사진을 선택하시오", Toast.LENGTH_SHORT).show()
            }
            else {
                Log.d("평가하기", bitmap.toString())

                var resized : Bitmap = Bitmap.createScaledBitmap(bitmap!!, 224, 224, true)
                Log.d("111111", resized.toString())

                var model = ModelUnquant.newInstance(this)
                Log.d("22222", model.toString())

                var inputFeature0 = TensorBuffer.createFixedSize(intArrayOf(1, 224, 224, 3), DataType.FLOAT32)
                Log.d("33333", inputFeature0.toString())

                var buffer = ByteBuffer.allocate(224 * 224 * 3 * DataType.FLOAT32.byteSize())
                Log.d("44444", buffer.toString())

                var asdf = resized.copyPixelsToBuffer(buffer)
                Log.d("55555", asdf.toString())


                var outputs = model.process(inputFeature0)
                Log.d("66666", outputs.toString())


                var outputFeature0 = outputs.outputFeature0AsTensorBuffer
                Log.d("aaa", outputFeature0.floatArray[0].toString())
                Log.d("bbb", outputFeature0.floatArray[1].toString())

                tv_result.text = outputFeature0.floatArray[1].toString()

                model.close()

            }





        }
    }



    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK) {
            when (requestCode) { //resultCode가 Ok이고
                REQUEST_IMAGE_CAPTURE -> { // requestcode가 REQUEST_IMAGE_CAPTURE이면

                }
                REQUEST_OPEN_GALLERY -> { // requestcode가 REQUEST_OPEN_GALLERY이면
                    tv_result.text=""
                    val currentImageUrl: Uri? = data?.data // data의 data형태로 들어옴
//                    uploadImgName = getName(currentImageUrl)

                    try {
                        bitmap = MediaStore.Images.Media.getBitmap(
                            this.contentResolver,
                            currentImageUrl
                        )

                        iv_aiImg.setImageBitmap(bitmap)


                    } catch (e: Exception) {
                        e.printStackTrace()
                    }
                }
            }
        }
    }

    /**
     * 갤러리 오픈 함수
     */
    fun openGallery() {
        val intent = Intent(Intent.ACTION_PICK)
        intent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*")
        startActivityForResult(intent, REQUEST_OPEN_GALLERY)
    }

    /**
     * 테드 퍼미션 설정
     */
    private fun setPermission() {
        val permission = object : PermissionListener {
            override fun onPermissionGranted() { // 설정해놓은 위험 권한들이 허용되었을 경우 이곳을 수행함.
                Toast.makeText(this@AIActivity, "권한이 혀용 되었습니다.", Toast.LENGTH_SHORT).show()
            }
            override fun onPermissionDenied(deniedPermissions: MutableList<String>?) { // 설정해놓은 위험 권한들 중 거부를 한 경우 이곳을 수행함.
                Toast.makeText(this@AIActivity, "권한이 거부 되었습니다.", Toast.LENGTH_SHORT).show()
            }
        }
        TedPermission.with(this)
            .setPermissionListener(permission)
            .setRationaleMessage("카메라 앱을 사용하시려면 권한을 허용해주세요.")
            .setDeniedMessage("권한을 거부하셨습니다. [앱 설정] -> [권한] 항목에서 허용해주세요.")
            .setPermissions(
                android.Manifest.permission.WRITE_EXTERNAL_STORAGE,
                android.Manifest.permission.CAMERA
            ).check()
    }

}