package usth.ict.group20.imdb.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import usth.ict.group20.imdb.R
import usth.ict.group20.imdb.models.Celebrity
import com.bumptech.glide.Glide

// This adapter takes a list of Celebrity objects
class CelebrityAdapter(private val celebrities: List<Celebrity>) :
    RecyclerView.Adapter<CelebrityAdapter.CelebrityViewHolder>() {

    // The ViewHolder now holds references to both an ImageView and a TextView
    class CelebrityViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val imageView: ImageView = view.findViewById(R.id.celebrity_image)
        val nameTextView: TextView = view.findViewById(R.id.celebrity_name)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CelebrityViewHolder {
        // Inflate the new item_celebrity_small.xml layout
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_celebrity_small, parent, false)
        return CelebrityViewHolder(view)
    }

    override fun getItemCount(): Int {
        return celebrities.size
    }

    override fun onBindViewHolder(holder: CelebrityViewHolder, position: Int) {
        val celebrity = celebrities[position]


        holder.nameTextView.text = celebrity.name
        holder.imageView.setImageResource(celebrity.imageResId)
    }

}