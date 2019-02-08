package braj.androiddev

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.text.Html
import android.view.View
import kotlinx.android.synthetic.main.activity_main.*
import okhttp3.*
import org.jetbrains.anko.longToast
import org.json.JSONObject
import java.io.IOException

class MainActivity : AppCompatActivity() {

    val url = "https://api.icndb.com/jokes/random"

    var okHttpClient: OkHttpClient = OkHttpClient()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        next_Button.setOnClickListener({
            loadRandomFacts()
        })
    }

    private fun loadRandomFacts() {
        runOnUiThread { progressBar.visibility = View.VISIBLE }

        val request: Request = Request.Builder().url(url).build()

        okHttpClient.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                longToast(getString(R.string.message_no_internet))
            }

            override fun onResponse(call: Call?, response: Response?) {
                val data = response?.body()?.string()

                val text = (JSONObject(data).getJSONObject("value").get("joke")).toString()

                runOnUiThread {
                    facts_textView.text = Html.fromHtml(text)
                    progressBar.visibility = View.GONE
                }
            }
        })

    }
}
