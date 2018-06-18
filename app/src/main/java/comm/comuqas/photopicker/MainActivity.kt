package comm.comuqas.photopicker

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.graphics.BitmapFactory
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.View
import com.tbruyelle.rxpermissions2.RxPermissions
import kotlinx.android.synthetic.main.activity_main.*
import java.io.File


class MainActivity : AppCompatActivity(), View.OnClickListener {
    private val galleryRequestCode = 1
    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.iv -> {
                RxPermissions(this)
                        .request(Manifest.permission.READ_EXTERNAL_STORAGE)
                        .subscribe({ granted ->
                            if (granted) {
                                // All requested permissions are granted
                                val galleryIntent = Intent(Intent.ACTION_GET_CONTENT)
                                galleryIntent.type = "image/*"
                                startActivityForResult(galleryIntent, galleryRequestCode)
                            } else {
                                // At least one permission is denied
                            }
                        })
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        iv.setOnClickListener(this)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == galleryRequestCode && resultCode == Activity.RESULT_OK) {
            val imageUrl = data?.data
            iv.setImageURI(imageUrl)
            Log.e("asdf", "imageUrl " + imageUrl!!.path)

            val options = BitmapFactory.Options()
            options.inJustDecodeBounds = true
            BitmapFactory.decodeFile(File(imageUrl.path).absolutePath, options)
            val imageHeight = options.outHeight
            val imageWidth = options.outWidth
            if (imageHeight > 4096 || imageWidth > 4096) {
                val opts = BitmapFactory.Options()
                opts.inSampleSize = 4
                val bitmap = BitmapFactory.decodeFile(imageUrl.toString(), opts)
                iv.setImageBitmap(bitmap)
            } else {
                /*Picasso.get()
                        .load(File(imageUrl.path)) // Uri of the picture
                        .into(iv)*/
            }//for vivo phone

            val imageFile = File(MyImageFilePath.getPath(applicationContext, imageUrl))
            /*try {
                contentResolver.openInputStream(imageUrl)
                val asdf = Compressor(this).setMaxWidth(640)
                        .setMaxHeight(480)
                        .setQuality(75)
                        .setDestinationDirectoryPath(externalCacheDir!!.absolutePath)
                        .compressToFile(imageFile)
            } catch (e: IOException) {
                e.printStackTrace()
            }*/

        }
    }
}
