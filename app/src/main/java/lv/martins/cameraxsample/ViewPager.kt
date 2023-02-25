package lv.martins.cameraxsample

import android.content.ContentUris
import android.content.Context
import android.content.Intent
import android.media.Image
import android.net.Uri
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.view.*
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.viewpager.widget.PagerAdapter
import androidx.viewpager.widget.ViewPager
import lv.martins.cameraxsample.databinding.ActivityViewPagerBinding
import java.util.*

// Container for information about each video.
class ImageObj(val uri: Uri,
                 val name: String
)

class ViewPager : AppCompatActivity() {
    private lateinit var viewBinding: ActivityViewPagerBinding
    lateinit var viewPager: ViewPager
    lateinit var viewPagerAdapter: ViewPagerAdapter
    lateinit var imageList: List<Uri>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewBinding = ActivityViewPagerBinding.inflate(layoutInflater)
        setContentView(viewBinding.root)

        setSupportActionBar(viewBinding.toolbar)

        viewPager = viewBinding.idViewPager
        imageList = ArrayList<Uri>()

        val imgList = mutableListOf<ImageObj>()

        val collection =
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                MediaStore.Images.Media.getContentUri(
                    MediaStore.VOLUME_EXTERNAL
                )
            } else {
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI
            }

        val projection = arrayOf(
            MediaStore.Images.Media._ID,
            MediaStore.Images.Media.DISPLAY_NAME
        )

        // Display in alphabetical order based on their display name.
        val sortOrder = "${MediaStore.Images.Media.DISPLAY_NAME} ASC"

        val query = contentResolver.query(collection, projection, null, null, null)

        query?.use { cursor ->
            // Cache column indices.
            val idColumn = cursor.getColumnIndexOrThrow(MediaStore.Images.Media._ID)
            val nameColumn =
                cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DISPLAY_NAME)

            while (cursor.moveToNext()) {
                // Get values of columns for a given Images.
                val id = cursor.getLong(idColumn)
                val name = cursor.getString(nameColumn)

                val contentUri: Uri = ContentUris.withAppendedId(
                    MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                    id
                )

                // Stores column values and the contentUri in a local object
                // that represents the media file.
                imgList += ImageObj(contentUri, name)
                imageList = imageList + contentUri
            }
        }

//        imageList = imageList + R.drawable.ic_favorite_black_48dp
//        imageList = imageList + R.drawable.ic_launcher_foreground
//        imageList = imageList + R.drawable.ic_launcher_background
//        imageList = imageList + androidx.appcompat.R.drawable.abc_ab_share_pack_mtrl_alpha

        viewPagerAdapter = ViewPagerAdapter(this@ViewPager, imgList)

        // on below line we are setting
        // adapter to our view pager.
        viewPager.adapter = viewPagerAdapter

    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.gallery_menu, menu);
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem) = when (item.itemId) {
        R.id.record -> {
            // User chose the "Settings" item, show the app settings UI...
            val record = Intent(this, AudioRecordTest::class.java)
            startActivity(record)
            true
        }

        R.id.action_camera -> {
            // User chose the "Favorite" action, mark the current item
            // as a favorite...
            finish()
            true
        }
        else -> {
            // If we got here, the user's action was not recognized.
            // Invoke the superclass to handle it.
            super.onOptionsItemSelected(item)
        }
    }
}

class ViewPagerAdapter(val context: Context, val imageList: MutableList<ImageObj>) : PagerAdapter() {
    // on below line we are creating a method
    // as get count to return the size of the list.
    override fun getCount(): Int {
        return imageList.size
    }

    // on below line we are returning the object
    override fun isViewFromObject(view: View, `object`: Any): Boolean {
        return view === `object` as RelativeLayout
    }

    // on below line we are initializing
    // our item and inflating our layout file
    override fun instantiateItem(container: ViewGroup, position: Int): Any {
        // on below line we are initializing
        // our layout inflater.
        val mLayoutInflater =
            context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater

        // on below line we are inflating our custom
        // layout file which we have created.
        val itemView: View = mLayoutInflater.inflate(R.layout.image_view, container, false)

        // on below line we are initializing
        // our image view with the id.
        val imageView: ImageView = itemView.findViewById<View>(R.id.idIVImage) as ImageView
        val textView: TextView = itemView.findViewById<View>(R.id.textView) as TextView

        // on below line we are setting
        // image resource for image view.
//        imageView.setImageResource(imageList.get(position))
        imageView.setImageURI(imageList.get(position).uri)
        textView.text = imageList.get(position).name

        // on the below line we are adding this
        // item view to the container.
        Objects.requireNonNull(container).addView(itemView)

        // on below line we are simply
        // returning our item view.
        return itemView
    }

    // on below line we are creating a destroy item method.
    override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
        // on below line we are removing view
        container.removeView(`object` as RelativeLayout)
    }
}