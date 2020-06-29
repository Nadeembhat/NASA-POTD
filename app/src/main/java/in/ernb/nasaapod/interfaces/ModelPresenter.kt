package `in`.ernb.nasaapod.interfaces

import android.app.DownloadManager
import com.mythio.retrofitsample.network.NasaAPOD
import okhttp3.ResponseBody

/**
 * Author Nadeem Bhat ,
 * Created by Nadeem Bhat on Monday, Jun, 2020.
 * Copy Right (c) 12:47 PM.
 * Srinagar,Kashmir
 * ennennbee@gmail.com
 * NASA APOD
 */


interface ModelPresenter {
    fun processData(data:NasaAPOD,filename:String)
    fun downlaodRegister(obj:String?,file:String?)
    fun showToast(message:String )
}