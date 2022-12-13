package kr.ac.kumoh.s20180488.w1401customlist

import android.app.Application
import android.graphics.Bitmap
import android.widget.Toast
import androidx.collection.LruCache
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.toolbox.ImageLoader
import com.android.volley.toolbox.JsonArrayRequest
import com.android.volley.toolbox.Volley
import kr.ac.kumoh.s20180488.w1401customlist.databinding.ActivityCryptoBinding
import org.json.JSONArray
import org.json.JSONObject
import java.net.URLEncoder

class CryptoViewModel(application: Application) : AndroidViewModel(application) {
    data class Crypto(var id: Int, var name: String, var fullname: String, var price: Double, var image: String)

    companion object {
        const val QUEUE_TAG = "CryptoVolleyRequest"
        const val SERVER_URL = "https://expresssongdb-ntcon.run.goorm.io/"
    }

    private val coins = ArrayList<Crypto>()
    private val _list = MutableLiveData<ArrayList<Crypto>>()
    val list: LiveData<ArrayList<Crypto>>
        get() = _list

    private var queue: RequestQueue
    val imageLoader: ImageLoader

    init {
        _list.value = coins
        queue = Volley.newRequestQueue(getApplication())
        imageLoader = ImageLoader(queue,
            object : ImageLoader.ImageCache {
                private val cache = LruCache<String, Bitmap>(100)
                override fun getBitmap(url: String): Bitmap? {
                    return cache.get(url)
                }
                override fun putBitmap(url: String, bitmap: Bitmap) {
                    cache.put(url, bitmap)
                }
            })
    }
    fun getImageUrl(i: Int): String = "${SERVER_URL}/image/" + URLEncoder.encode(coins[i].image, "utf-8")

    fun requestCrypto() {
        val request = JsonArrayRequest(
            Request.Method.GET,
            "${SERVER_URL}/crypto",
            null,
            {
                //Toast.makeText(getApplication(), it.toString(), Toast.LENGTH_LONG).show()
                coins.clear()
                parseJson(it)
                _list.value = coins
            },
            {
                Toast.makeText(getApplication(), it.toString(), Toast.LENGTH_LONG).show()
            }
        )

        request.tag = QUEUE_TAG
        queue.add(request)
    }

    private fun parseJson(items: JSONArray) {
        for (i in 0 until items.length()) {
            val item: JSONObject = items[i] as JSONObject
            val id = item.getInt("id")
            val name = item.getString("name")
            val fullname = item.getString("fullname")
            val price = item.getDouble("price")
            val image = item.getString("image")

            coins.add(Crypto (id, name, fullname, price ,image))
        }
    }

    override fun onCleared() {
        super.onCleared()
        queue.cancelAll(QUEUE_TAG)
    }
}