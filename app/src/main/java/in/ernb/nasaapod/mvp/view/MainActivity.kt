package `in`.ernb.nasaapod.mvp.view

import `in`.ernb.nasaapod.R
import `in`.ernb.nasaapod.interfaces.PresenterView
import `in`.ernb.nasaapod.mvp.presenter.ActivityPresenter
import `in`.ernb.nasaapod.util.AppPreference
import android.Manifest
import android.app.DatePickerDialog
import android.content.Context
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.util.Log
import android.view.View
import android.webkit.WebChromeClient
import android.webkit.WebSettings
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatImageButton
import androidx.appcompat.widget.AppCompatTextView
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.downloadservice.filedownloadservice.manager.FileDownloadManager
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.mythio.retrofitsample.network.NasaAPOD
import java.io.File
import java.text.SimpleDateFormat
import java.util.*
import java.util.regex.Matcher
import java.util.regex.Pattern


class MainActivity : AppCompatActivity(), PresenterView {

    companion object {
        const val REQUEST_PERMISSION = 1
    }

    val listOfPermissions = arrayOf(
        Manifest.permission.CAMERA,
        Manifest.permission.WRITE_EXTERNAL_STORAGE,
        Manifest.permission.READ_EXTERNAL_STORAGE,
        Manifest.permission.READ_CALENDAR
    )
    private lateinit var fabButton: FloatingActionButton
    private lateinit var calender: AppCompatImageButton
    private lateinit var appPreference: AppPreference
    private val sharedPreferences by lazy {
        getSharedPreferences(
            "NASA_PREFERENCES",
            Context.MODE_PRIVATE
        )
    }
    private val API_KEY = "DEMO_KEY"
    private lateinit var titleTextView: AppCompatTextView
    private lateinit var descTextView: AppCompatTextView
    private lateinit var generalLayout: RelativeLayout
    private lateinit var background: ImageView
    private lateinit var imageLayout: RelativeLayout
    private lateinit var videoLayout: RelativeLayout
    private lateinit var presenter: ActivityPresenter
    private lateinit var zoomImage: ImageView
    private lateinit var webview:WebView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        getSupportActionBar()?.show()
        appPreference = AppPreference(sharedPreferences)
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.M) {
            chechRuntimePermissions()
        }
        generalLayout = findViewById(R.id.general)
        imageLayout = findViewById(R.id.imageRoot)
        videoLayout = findViewById(R.id.videoRoot)
        fabButton = findViewById(R.id.fabButton)
        calender = findViewById(R.id.calender)
        zoomImage = findViewById(R.id.imageTouch)
        webview = findViewById(R.id.videoview)
        titleTextView = findViewById(R.id.title)
        descTextView = findViewById(R.id.descr)
        background = findViewById(R.id.background)
        presenter = ActivityPresenter(this, appPreference)
        Log.e("N-View\t", "Key:\t" + API_KEY)
        presenter._ini(API_KEY)

        calender.setOnClickListener(View.OnClickListener {
            val c = Calendar.getInstance()
            val year = c.get(Calendar.YEAR)
            val month = c.get(Calendar.MONTH)
            val day = c.get(Calendar.DAY_OF_MONTH)
            val pattern = "yyyy-MM-dd"
            val dpd = DatePickerDialog(
                this,
                DatePickerDialog.OnDateSetListener { view, year, monthOfYear, dayOfMonth ->
                    Log.e(
                        "MAV",
                        "Day:->\t" + dayOfMonth + "\tMonth :->\t" + monthOfYear + "\tYear :->\t" + year
                    )

                    val simpleDateFormat = SimpleDateFormat(pattern)
                    val date = simpleDateFormat.format(getDate(c, year, monthOfYear, dayOfMonth))
                    Log.e("N-View", "\tDatePicker\t" + date)
                    presenter._dataOnDate_(API_KEY, date)
                },
                year,
                month,
                day
            )

            dpd.show()
        })
            }

    fun getDate(c: Calendar, y: Int, m: Int, d: Int): Date {
        c.set(y, m, d)
        return c.time
    }

    override fun updateUI(uiData: NasaAPOD, filename: String) {
        Log.e("N-View\t", "updateUI()")
        val url:String
        url = uiData.url
        val id:String
        titleTextView.setText(uiData.title)
        descTextView.setText(uiData.explanation)
        if (uiData.mediatype.equals("image"))
            fabButton.setImageResource(R.drawable.ic_zoom_in_black_24dp)
        else
            fabButton.setImageResource(R.drawable.play_video_24dp)


        //For Thumbnail
        if (uiData.mediatype.equals("image")) {
            Glide.with(this) //1
                .load(uiData.url)
                .skipMemoryCache(true) //2
                .diskCacheStrategy(DiskCacheStrategy.NONE)
                .into(background)

        } else {
             id = getYoutubeVideoId(url)
            Log.e("N-View", "Id is:\t" + id)
            if(!id.equals("")){
            val urlId = "https://img.youtube.com/vi/" + id + "/0.jpg";
            Glide.with(this).load(urlId).into(background);
            }
            else{
                Toast.makeText(applicationContext,"Its not Youtue  Sorry we Cant Generate Thumbnail\n and you cann't play video as well!!!",Toast.LENGTH_LONG).show()
            }
        }
        fabButton.setOnClickListener(View.OnClickListener {
            Log.e("N-View", "Filename :\t" + filename)

            if (uiData.mediatype.equals("image")) {
                generalLayout.visibility = View.GONE
                videoLayout.visibility = View.GONE
                imageLayout.visibility = View.VISIBLE
                val completePath =
                    Environment.getExternalStorageDirectory().toString() + "/" + "NasaAPOD/"+filename
                Log.e("N-View", "CompletePath :\t" + completePath)
                val file = File(completePath)
                if(file.exists())
                {
                val imageUri = Uri.fromFile(file);
                Glide.with(this)
                    .load(imageUri)
                    .into(zoomImage)
                }
                else{
                        zoomImage.setImageResource(R.drawable.fnf)
                }
            } else {
                generalLayout.visibility = View.GONE
                videoLayout.visibility = View.VISIBLE
                imageLayout.visibility = View.GONE
                val id:String
                id = getYoutubeVideoId(url)
                if(!id.equals("")) {
                    Log.e("N-View\t", "Video Url is:\t" + url)
                    Log.e("N-View\t", "Video Id is:\t" + id)
                    setWebViewControls(id, webview)
                }
                else{
                    Toast.makeText(applicationContext,"Its not Youtue Video Sorry we Cant play this",Toast.LENGTH_LONG).show()
                }

            }
        })

    }

    override fun downloadFile(url: String?, filename: String?) {
        url?.let {
            filename?.let { it1 ->
                FileDownloadManager.initDownload(
                    this,
                    it,
                    Environment.getExternalStorageDirectory().toString() + "/" + "NasaAPOD",
                    it1
                )
            }
        }
    }

    private fun chechRuntimePermissions() {
        if (ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            Log.e("N-View\t", "Permissions Enabled Now")
            ActivityCompat.requestPermissions(this, listOfPermissions, REQUEST_PERMISSION)
            appPreference.setPermissionStatus(true)
        }
    }

    override fun showMessage(message:String){
        Toast.makeText(applicationContext,message,Toast.LENGTH_LONG).show()
    }


    fun setWebViewControls(videoId:String,webView_:WebView){
        getSupportActionBar()?.hide()
        val  SINGLE_VIDEO_STRING = "'" + "https://www.youtube.com/embed/" + videoId + "?enablejsapi=1&autoplay=0&loop=1&rel=0&controls=0&playlist=" + videoId + "'" + " frameborder='0' allowfullscreen";
        Log.e("N-View\t","Video Url Info\t"+SINGLE_VIDEO_STRING)
        val htmlPageInfo = getHtmlTemplate(SINGLE_VIDEO_STRING);
        Log.e("N-View\t","HtmlPage Info\t"+htmlPageInfo)
        webView_.getSettings().setAppCacheEnabled(true)
        webView_.getSettings().setJavaScriptEnabled(true)
        webView_.getSettings().setAppCachePath(applicationContext.getCacheDir().getAbsolutePath())
        webView_.getSettings().setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK)
        webView_.getSettings().setMediaPlaybackRequiresUserGesture(false)
        webView_.setWebChromeClient(WebChromeClient())
        webView_.loadDataWithBaseURL(
            "https://www.youtube.com/",
            htmlPageInfo,
            "text/html",
            "utf-8",
            null
        )
        webView_.webViewClient = object : WebViewClient() {
            override fun shouldOverrideUrlLoading(view: WebView?, url: String?): Boolean {
                view?.loadUrl(url)
                return true
            }
        }
}



    //get Video if from the url
    fun getYoutubeVideoId(url: String): String {
        Log.e("N-View\t","getYoutubeVideoId()\t"+url)
        val pattern = "(?<=watch\\?v=|/videos/|embed\\/)[^#\\&\\?]*"
        val compiledPattern: Pattern = Pattern.compile(pattern)
        val matcher: Matcher = compiledPattern.matcher(url)
        return if (matcher.find()) {
            matcher.group()
        } else  {
            Log.e("N-View\t","Pattern is not Youtube\t"+url)
            val pattern_ = "(https?://)?(www.)?(player.)?vimeo.com/([a-z]*/)*([0-9]{6,11})[?]?.*"
            val compiledPattern_: Pattern = Pattern.compile(pattern_)
            val matcher_: Matcher = compiledPattern_.matcher(url)
            if(matcher_.find())
            {
              return  matcher_.group()
            }
            Toast.makeText(applicationContext,"Its not Youtue Video Sorry we Cant play this",Toast.LENGTH_LONG).show()
            return ""
        }
    }


    //For PLaying Full Screen Video i create an html page with controls
    fun getHtmlTemplate(url: String): String? {
        Log.e("N-View\t","getHtmlTemplate()\t"+url)
        return "<!DOCTYPE html>\n" +
                "<html>\n" +
                " <style type=\"text/css\">\n" +
                "        html, body {\n" +
                "            height: 100%;\n" +
                "            width: 100%;\n" +
                "            margin: 0;\n" +
                "            object-fit: fill;\n" +
                "            padding: 0;\n" +
                "            background-color: #000000;\n" +
                "            overflow: hidden;\n" +
                "            position: fixed;\n" +
                "        }\n" +
                "    </style>\n" +
                "<body>\n" +
                "<script>\n" +
                "       var tag = document.createElement('script');\n" +
                "       tag.src = \"https://www.youtube.com/player_api\";\n" +
                "       var firstScriptTag = document.getElementsByTagName('script')[0];\n" +
                "       firstScriptTag.parentNode.insertBefore(tag, firstScriptTag);\n" +
                "       var player;\n" +
                "       function onYouTubePlayerAPIReady() {\n" +
                "            player = new YT.Player('player', {\n" +
                "                    height: '100%',\n" +
                "                    width: '100%',\n" +
                "                    events: {\n" +
                "                       'onReady': onPlayerReady,\n" +
                "                       'onStateChange': onPlayerStateChange\n" +
                "                  },\n" +
                "                  playerVars: {\n" +
                "                        'showinfo': 0,\n" +
                "                        'controls': 0\n" +
                "                        'autoplay': 0,\n" +
                "                                }\n" +
                "                            });\n" +
                "            var arrayplaylist = player.getPlaylist(); \n" +
                "\n" +
                "                        }\n" +
                "                        function onPlayerReady(event) {\n" +
                "                            event.target.playVideo();\n" +
                "\n" +
                "                        }\n" +
                "\n" +
                "                        var done = false;\n" +
                "                        function onPlayerStateChange(event) {\n" +
                "                            if (event.data == YT.PlayerState.PLAYING && !done) {\n" +
                "                                done = true;\n" +
                "                            }\n" +
                "                        }\n" +
                "                        function stopVideo() {\n" +
                "                            player.stopVideo();\n" +
                "                        }\n" +
                "                    </script> \n" +
                "                    <iframe id='player' type='text/html' width='100%' height='100%'\n" +
                "                    src=" + url + "/>\n" +
                "\n" +
                "</body>\n" +
                "</html>"
    }
}
