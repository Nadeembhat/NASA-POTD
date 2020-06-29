package `in`.ernb.nasaapod.interfaces

import android.app.DownloadManager
import java.util.*

/**
 * Author Nadeem Bhat ,
 * Created by Nadeem Bhat on Monday, Jun, 2020.
 * Copy Right (c) 12:42 PM.
 * Srinagar,Kashmir
 * ennennbee@gmail.com
 * NASA APOD
 */


interface PresenterModel {
   fun getApiData(key:String)
   fun getDataOnDate(key:String, date:String)

}