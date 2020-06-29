package `in`.ernb.nasaapod.mvp.presenter

import `in`.ernb.nasaapod.mvp.Model.ActivityModel
import `in`.ernb.nasaapod.interfaces.ModelPresenter
import `in`.ernb.nasaapod.interfaces.PresenterView
import `in`.ernb.nasaapod.interfaces.ViewPresenter
import `in`.ernb.nasaapod.util.AppPreference
import android.os.Environment
import android.util.Log
import com.mythio.retrofitsample.network.NasaAPOD
import java.io.File

/**
 * Author Nadeem Bhat ,
 * Created by Nadeem Bhat on Monday, Jun, 2020.
 * Copy Right (c) 12:43 PM.
 * Srinagar,Kashmir
 * ennennbee@gmail.com
 * NASA APOD
 */


class ActivityPresenter(var presenterView: PresenterView,var preferences:AppPreference) :ViewPresenter ,ModelPresenter{



    override fun _ini(key:String) {
        Log.e("PATH\t","_ini:\t"+Environment.DIRECTORY_DOWNLOADS.toString())

        if(!preferences.getDirStatus()){

            val folder = File(Environment.getExternalStorageDirectory().toString() + "/" + "NasaAPOD")
            if (!folder.exists()) {
                folder.mkdirs()
            }
        }
        val model = ActivityModel(this)
        model.getApiData(key)
    }

    override fun _dataOnDate_(key:String,cal :String) {
        Log.e("N-Presenter\t","_dataOnDate_()")
        Log.e("N-Presenter\t","Key:\t"+key+"\tDate:\t"+cal)
        val model = ActivityModel(this)
        model.getDataOnDate(key,cal)
    }

    override fun processData(data: NasaAPOD,filename:String) {
        Log.e("N-Presenter\t","processData()")
        presenterView.updateUI(data,filename)
    }

    override fun downlaodRegister(obj: String?, file: String?) {
        if(!file.equals("video")){
        presenterView.downloadFile(obj,file)
        }
    }


        override fun showToast(message:String ){
            presenterView.showMessage(message)
        }


    }