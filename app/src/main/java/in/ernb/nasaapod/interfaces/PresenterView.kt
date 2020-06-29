package `in`.ernb.nasaapod.interfaces

import android.app.DownloadManager
import com.mythio.retrofitsample.network.NasaAPOD

/**
 * Author Nadeem Bhat ,
 * Created by Nadeem Bhat on Monday, Jun, 2020.
 * Copy Right (c) 1:16 PM.
 * Srinagar,Kashmir
 * ennennbee@gmail.com
 * NASA APOD
 */


interface PresenterView {
    fun updateUI(uiData:NasaAPOD,filename:String)
    fun downloadFile(url:String?,filename: String?)
    fun showMessage(message:String)
}