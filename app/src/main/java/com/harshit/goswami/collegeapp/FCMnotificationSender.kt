package com.harshit.goswami.collegeapp

import android.app.Activity
import android.content.Context
import com.android.volley.*
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import org.json.JSONException
import org.json.JSONObject


class FCMnotificationSender(
    private val userFcmToken: String,
    private val title: String,
    private val body: String,
    private  val type: String,
    private val imageUrl: String,
//    private val mContext: Context,
    private  val mActivity: Activity
) {
    private var requestQueue: RequestQueue? = null
    private val postUrl = "https://fcm.googleapis.com/fcm/send"
    private val fcmServerKey =
        "AAAADYQwRi4:APA91bFZ8u2gi60OPR1MaIky5-y6X-BgPBIJuK1tEZsbSF1DnGvVotR2e0Tj4_KjR8A4wnyeAAY8h_mOcYJOsgeKh52VRNrBwk_8Pf8W4xU8ClHZ_-Gnn5b_Bwj0gEyTqKbAzInTYztZ"
    fun sendNotifications() {

        requestQueue = Volley.newRequestQueue(mActivity)
        val mainObj = JSONObject()
        try {
            mainObj.put("to", userFcmToken)
            val notiObject = JSONObject()
            notiObject.put("title", title)
            notiObject.put("message", body)
            notiObject.put("type", type)
            if (type == "BIGPIC") notiObject.put("imageUrl", imageUrl)
                // enter icon that exists in drawable only
            mainObj.put("data", notiObject)
            val request: JsonObjectRequest = object : JsonObjectRequest(
                Request.Method.POST,
                postUrl,
                mainObj,
                Response.Listener<JSONObject?> {
                    // code run is got response
                },
                Response.ErrorListener {
                    // code run is got error
                }) {
                @Throws(AuthFailureError::class)
                override fun getHeaders(): Map<String, String> {
                    val header: MutableMap<String, String> = HashMap()
                    header["content-type"] = "application/json"
                    header["authorization"] = "key=$fcmServerKey"
                    return header
                }
            }
            requestQueue!!.add(request)
        } catch (e: JSONException) {
            e.printStackTrace()
        }
    }
}

