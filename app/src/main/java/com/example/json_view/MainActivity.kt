package com.example.json_view

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.fasterxml.jackson.annotation.JsonProperty
import com.google.gson.GsonBuilder
import com.google.gson.JsonParser
import kotlinx.coroutines.*
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.http.GET
import retrofit2.http.Path
import java.net.HttpURLConnection
import java.net.URL

data class Sur(
    @JsonProperty("id") val id: Long,
    @JsonProperty("c_name") val cName: String,
    @JsonProperty("p_name") val pName: String
)

interface GitHubService {
    @GET("api/sur/list")
    fun getSurs(): Call<List<Sur>>
}

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        //getReprofit()
        val  clickButton: Button = findViewById(R.id.button);
        clickButton.setOnClickListener {
            Log.d("BUTTONS", "User tapped the Supabutton")
            GlobalScope.launch(Dispatchers.IO) {
                val editText: EditText = findViewById(R.id.editTextText) as EditText
                val url = URL("http://" + editText.text + "/api/sur/list")
                val httpURLConnection = url.openConnection() as HttpURLConnection
                httpURLConnection.setRequestProperty("Accept", "application/json") // The format of response we want to get from the server
                httpURLConnection.requestMethod = "GET"
                httpURLConnection.doInput = true
                httpURLConnection.doOutput = false
                // Check if the connection is successful
                val responseCode = httpURLConnection.responseCode
                if (responseCode == HttpURLConnection.HTTP_OK) {
                    val response = httpURLConnection.inputStream.bufferedReader()
                        .use { it.readText() }  // defaults to UTF-8
                    withContext(Dispatchers.Main) {

                        // Convert raw JSON to pretty JSON using GSON library
                        val gson = GsonBuilder().setPrettyPrinting().create()
                        val prettyJson = gson.toJson(JsonParser.parseString(response))
                        Log.d("Pretty Printed JSON :", prettyJson)
                        val textView: TextView = findViewById(R.id.content) as TextView
                        textView.text = prettyJson
                    }
                } else {
                    Log.e("HTTPURLCONNECTION_ERROR", responseCode.toString())
                }
            }
        };
    }


}

fun getReprofit() :GitHubService
{
    val retrofit = Retrofit.Builder()
        .baseUrl("http://192.168.1.145/")
        .build()

    val service: GitHubService = retrofit.create(GitHubService::class.java)
    return service
}
