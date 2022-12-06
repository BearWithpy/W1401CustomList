package kr.ac.kumoh.s20180488.w1401customlist

import android.app.Application
import android.widget.Toast
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.toolbox.JsonArrayRequest
import com.android.volley.toolbox.Volley
import org.json.JSONArray
import org.json.JSONObject

class CryptoViewModel(application: Application) : AndroidViewModel(application) {
    data class Crypto(var id: Int, var name: String, var fullname: String, var price: Double, var image: String)

    companion object {
        const val QUEUE_TAG = "CryptoVolleyRequest"
    }

    private val coins = ArrayList<Crypto>()
    private val _list = MutableLiveData<ArrayList<Crypto>>()
    val list: LiveData<ArrayList<Crypto>>
        get() = _list

    private var queue: RequestQueue

    init {
        _list.value = coins
        queue = Volley.newRequestQueue(getApplication())
    }

    fun requestSong() {
        // NOTE: 서버 주소는 본인의 서버 IP 사용할 것
        val url = "https://expresssongdb-ntcon.run.goorm.io/crypto"

        // Array를 반환할 경우에는 JsonObjectRequest 대신 JsonArrayRequest 사용
        val request = JsonArrayRequest(
            Request.Method.GET,
            url,
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