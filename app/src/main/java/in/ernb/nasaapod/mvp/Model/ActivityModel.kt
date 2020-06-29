package `in`.ernb.nasaapod.mvp.Model

import `in`.ernb.nasaapod.interfaces.ModelPresenter
import `in`.ernb.nasaapod.interfaces.PresenterModel
import android.util.Log
import android.webkit.URLUtil
import com.mythio.retrofitsample.network.NasaAPOD
import com.mythio.retrofitsample.network.NasaApi
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


/**
 * Author Nadeem Bhat ,
 * Created by Nadeem Bhat on Monday, Jun, 2020.
 * Copy Right (c) 12:46 PM.
 * Srinagar,Kashmir
 * ennennbee@gmail.com
 * NASA APOD
 */


class ActivityModel(var presenter: ModelPresenter) : PresenterModel {


    private var data: NasaAPOD? = null

    override fun getApiData(key: String) {
        Log.e("N-Model\t", "Key:\t" + key + "")
        var file:String?
        var fileUrl:String?
        val api = NasaApi.retrofitService.getData(key)
        api.enqueue(object : Callback<NasaAPOD> {
            override fun onFailure(call: Call<NasaAPOD>, t: Throwable) {
                Log.d("ActivityModel", "Failed :" + t.message)
            }
            override fun onResponse(call: Call<NasaAPOD>, response: Response<NasaAPOD>) {
                Log.e("N-Model", "Success :\t")
                if (response.isSuccessful) {
                    data = response.body()
                    if (response.body()?.mediatype.equals("image")) {
                        fileUrl = response.body()?.hdurl
                    }
                    else {
                        fileUrl = response.body()?.url
                    }

                    Log.e("N-Model", "\tFile Url is\t" + fileUrl)
                    if (!response.body()?.mediatype.equals("video")) {
                        presenter.showToast("Please Wait while File is Downloaded...\nDon't press The Button Untill Download Finishes")
                        file = URLUtil.guessFileName(fileUrl, null, null)

                        //file = url?.substringBeforeLast("/")
                        Log.e("N-Model", "\tFileName is\t" + file)
                    } else {
                        file = "Video"
                    }
                    presenter.downlaodRegister(fileUrl, file)
                    data?.let {
                        if (file != null) {
                            presenter.processData(it, file!!)
                        }
                    }
                }
               else {
                    presenter.showToast("Response  is Unsuccessfull\n"+response.message())
                }
            }
        })

    }

    override fun getDataOnDate(key: String, date: String) {
        Log.e("N-Model\t", "date:\t" + date + "")
        var file:String?
        var fileUrl:String?
        val api = NasaApi.retrofitService.getData(key, date)
        api.enqueue(object : Callback<NasaAPOD> {
            override fun onFailure(call: Call<NasaAPOD>, t: Throwable) {
                Log.d("N-Model", "Failed :" + t.message)
            }
            override fun onResponse(call: Call<NasaAPOD>, response: Response<NasaAPOD>) {
                Log.e("N-Model", "Success :\t")
                if(response.isSuccessful){
                data = response.body()
                if(response.body()?.mediatype.equals("image")){
                    fileUrl = response.body()?.hdurl
                }
                else{
                    fileUrl = response.body()?.url
                }
                if(fileUrl ==null)

                Log.e("N-Model","\tFile Url is\t"+fileUrl)
                if(!response.body()?.mediatype.equals("video")) {
                    presenter.showToast("Please Wait while File is Downloaded...\nDon't press The Button Untill Download Finishes")
                    file = URLUtil.guessFileName(fileUrl, null, null)

                    //file = url?.substringBeforeLast("/")
                    Log.e("N-Model", "\tFileName is\t" + file)
                }
                else{
                    file = ""
                }
                presenter.downlaodRegister(fileUrl,file)
                data?.let {
                    if (file != null) {
                        presenter.processData(it, file!!)
                    }
                }
            }
                else {
                    presenter.showToast("Response  is Unsuccessfull\n"+response.message())
                }
            }
        })



    }
}