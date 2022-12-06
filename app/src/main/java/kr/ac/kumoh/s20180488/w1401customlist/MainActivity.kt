package kr.ac.kumoh.s20180488.w1401customlist

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
//import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.toolbox.NetworkImageView
import kr.ac.kumoh.s20180488.w1401customlist.databinding.ActivityMainBinding


class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    //private lateinit var model: SongViewModel
    private lateinit var view: CryptoViewModel
    //private val songAdapter = SongAdapter()
    private val crytoAdapter = CryptoAdapter()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

//        model = ViewModelProvider(this)[SongViewModel::class.java]
//
//        binding.list.apply {
//            layoutManager = LinearLayoutManager(applicationContext)
//            setHasFixedSize(true)
//            itemAnimator = DefaultItemAnimator()
//            adapter = songAdapter
//        }
//
//        model.list.observe(this) {
//            // 좀더 구체적인 이벤트를 사용하라고 warning 나와서 변경함
//            //songAdapter.notifyDataSetChanged()
//            //Log.i("size", "${model.list.value?.size ?: 0}")
//
//            // Changed가 아니라 Inserted
//            songAdapter.notifyItemRangeInserted(0,
//                model.list.value?.size ?: 0)
//        }
//
//        model.requestSong()

        view = ViewModelProvider(this)[CryptoViewModel::class.java]

        binding.list.apply {
            layoutManager = LinearLayoutManager(applicationContext)
            setHasFixedSize(true)
            itemAnimator = DefaultItemAnimator()
            adapter = crytoAdapter
        }

        view.list.observe(this) {
            // 좀더 구체적인 이벤트를 사용하라고 warning 나와서 변경함
            //songAdapter.notifyDataSetChanged()
            //Log.i("size", "${model.list.value?.size ?: 0}")

            // Changed가 아니라 Inserted
            crytoAdapter.notifyItemRangeInserted(0,
                view.list.value?.size ?: 0)
        }

        view.requestCrypto()
    }
    inner class CryptoAdapter: RecyclerView.Adapter<CryptoAdapter.ViewHolder>() {
        inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
            val txText1: TextView = itemView.findViewById<TextView>(R.id.text1)
            val txText2: TextView = itemView.findViewById<TextView>(R.id.text2)

            val niImage: NetworkImageView = itemView.findViewById<NetworkImageView>(R.id.image)

            init {
                niImage.setDefaultImageResId(android.R.drawable.ic_menu_report_image)
            }
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
            val view = layoutInflater.inflate(R.layout.item_song,
                parent,
                false)
            return ViewHolder(view)
        }

        override fun onBindViewHolder(holder: ViewHolder, position: Int) {
            holder.txText1.text = view.list.value?.get(position)?.fullname
            holder.txText2.text = view.list.value?.get(position)?.name

            holder.niImage.setImageUrl(view.getImageUrl(position), view.imageLoader)
        }

        override fun getItemCount() = view.list.value?.size ?: 0
    }

//    inner class SongAdapter: RecyclerView.Adapter<SongAdapter.ViewHolder>() {
//        inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
//            val txText1: TextView = itemView.findViewById<TextView>(R.id.text1)
//            val txText2: TextView = itemView.findViewById<TextView>(R.id.text2)
//
//            val niImage: NetworkImageView = itemView.findViewById<NetworkImageView>(R.id.image)
//
//            init {
//                niImage.setDefaultImageResId(android.R.drawable.ic_menu_report_image)
//            }
//        }
//
//        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
//            val view = layoutInflater.inflate(R.layout.item_song,
//                parent,
//                false)
//            return ViewHolder(view)
//        }
//
//        override fun onBindViewHolder(holder: ViewHolder, position: Int) {
//            holder.txText1.text = model.list.value?.get(position)?.title
//            holder.txText2.text = model.list.value?.get(position)?.singer
//
//            holder.niImage.setImageUrl(model.getImageUrl(position), model.imageLoader)
//        }
//
//        override fun getItemCount() = model.list.value?.size ?: 0
//    }
}