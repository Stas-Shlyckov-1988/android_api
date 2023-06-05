package com.example.json_view

import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.google.gson.GsonBuilder
import com.google.gson.JsonParser
import kotlinx.coroutines.*
import java.net.HttpURLConnection
import java.net.URL

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val  clickButton: Button = findViewById(R.id.button);
        clickButton.setOnClickListener {
            Log.d("BUTTONS", "User tapped the Supabutton")
            GlobalScope.launch(Dispatchers.IO) {
                val editText: EditText = findViewById(R.id.editTextText) as EditText
                val url = URL(editText.text.toString())
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
                        val prettyJson = JsonParser.parseString(response)
                        //Log.d("Pretty Printed JSON :", prettyJson)
                        val textView: TextView = findViewById(R.id.content) as TextView

                        var textData :String = ""
                        for (obj in prettyJson.asJsonArray) {
                            textData += "ID: "
                            textData += obj.asJsonObject.get("id")
                            textData += "\n"

                            textData += "Country: "
                            textData += obj.asJsonObject.get("c_name")
                            textData += "\n"

                            textData += "Production: "
                            textData += obj.asJsonObject.get("p_name")
                            textData += "\n"

                            textData += "Sum risk: "
                            textData += obj.asJsonObject.get("sum_risk")
                            textData += "\n"
                        }
                        textView.text = textData
                    }
                } else {
                    Log.e("HTTPURLCONNECTION_ERROR", responseCode.toString())
                }
            }
        };
    }


}

