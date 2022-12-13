package kr.ac.kumoh.s20180488.w1401customlist

import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.collection.LruCache
import com.android.volley.toolbox.ImageLoader
import com.android.volley.toolbox.Volley
import kr.ac.kumoh.s20180488.w1401customlist.databinding.ActivityCryptoBinding

class CryptoActivity : AppCompatActivity() {
    private lateinit var binding: ActivityCryptoBinding

    companion object {
        const val KEY_NAME = "CryptoName"
        const val KEY_FULLNAME = "CryptoFullname"
        const val KEY_IMAGE = "CryptoImage"
    }

    private lateinit var imageLoader: ImageLoader
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCryptoBinding.inflate(layoutInflater)
        setContentView(binding.root)

        imageLoader = ImageLoader(
            Volley.newRequestQueue(this),
            object : ImageLoader.ImageCache {
                private val cache = LruCache<String, Bitmap>(100)
                override fun getBitmap(url: String): Bitmap? {
                    return cache.get(url)
                }
                override fun putBitmap(url: String, bitmap: Bitmap) {
                    cache.put(url, bitmap)
                }
            })

        binding.textName.text = intent.getStringExtra(KEY_NAME)
        binding.imageCoin.setImageUrl(intent.getStringExtra(KEY_IMAGE), imageLoader)
        binding.textFullname.text = intent.getStringExtra(KEY_FULLNAME)
        binding.btnImplicitIntent.setOnClickListener{
            var uri =  Uri.parse("https://www.binance.com/en/trade/${binding.textName.text}_BUSD?_from=markets&theme=dark&type=spot")
            if (binding.textName.text == "USDT"){
                uri = Uri.parse("https://www.binance.com/en/trade/BUSD_${binding.textName.text}?_from=markets&theme=dark&type=spot")
            }
            val intent = Intent(Intent.ACTION_VIEW, uri)
            startActivity(intent)
        }
    }
}