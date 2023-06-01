package com.example.json_view

import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import kotlinx.coroutines.*
import retrofit2.Call
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.http.GET
import java.util.Objects
import retrofit2.converter.gson.GsonConverterFactory

data class Sur (
    val id: Int = 0,
    val c_name: String? = null,
    val p_name: String? = null,
    val basic_risk: String? = null,
    val index_risk: String? = null,
    val vvoz_zapr: Boolean? = null,
    val high_risk: Boolean? = null,
    val sum_risk: Int? = null
);


interface SurService {
    @GET("/api/sur/list")
    fun getSurs() : Call<MutableList<Sur>>
}

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val  clickButton: Button = findViewById(R.id.button);
        clickButton.setOnClickListener {
            Log.d("BUTTONS", "User tapped the Supabutton")

            val editText: EditText = findViewById(R.id.editTextText) as EditText
            val retrofit = Retrofit.Builder()
                .baseUrl("http://" + editText.text)
                .addConverterFactory(GsonConverterFactory.create())
                .build()

            val service = retrofit.create(SurService::class.java)
            GlobalScope.launch {
                val result = service.getSurs()
                if (result != null)
                // Checking the results
                    println(result)

            }

        };
    }


}
