package me.gumify.ghostjs

import android.annotation.SuppressLint
import android.content.Context
import android.webkit.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.json.JSONObject

@SuppressLint("SetJavaScriptEnabled")
class Ghostjs(private val context: Context) {
    private val webView = WebView(context)
    private var urlLoadCallback: ((error: Boolean) -> Unit)? = null

    init {
        webView.webChromeClient = WebChromeClient()
        webView.webViewClient = object : WebViewClient() {
            override fun onPageFinished(view: WebView?, url: String?) {
                super.onPageFinished(view, url)
                urlLoadCallback?.invoke(false)
                urlLoadCallback = null
            }

            override fun onReceivedError(
                view: WebView?,
                request: WebResourceRequest?,
                error: WebResourceError?
            ) {
                super.onReceivedError(view, request, error)
                urlLoadCallback?.invoke(true)
                urlLoadCallback = null
            }
        }
        webView.settings.apply {
            javaScriptEnabled = true
        }
        webView.loadData("""
            <!DOCTYPE html>
            <html>
            <head>
            </head>
            <body>
            </body>
            </html>
        """.trimIndent(), "text/html", "UTF-8")
    }

    fun loadUrl(url: String, callback: (error: Boolean) -> Unit) {
        urlLoadCallback = callback
        webView.loadUrl(url)
    }

    private suspend fun evaluator(script: String): String? {
        var output: String? = null
        var isDone = false
        CoroutineScope(Dispatchers.Main).launch {
            webView.evaluateJavascript("""
            (function() {
                try {
                    var __GHOST_DATA = (function() {
                        $script
                    })();
                    return {output: __GHOST_DATA, type: __GHOST_DATA.constructor.name};
                } catch(e) {
                    return e.message;
                }
            })();
        """.trimIndent()) {
                try {
                    output = it
                } catch (e: Exception) {}
                isDone = true
            }
        }
        while (!isDone) {
            delay(100)
        }
        return output
    }

    fun execute(script: String) {
        webView.evaluateJavascript(script) {}
    }

    fun executeFile(path: String) {
        try {
            execute(IO.readAssetFile(context, path)!!)
        } catch (e: NullPointerException) {
            throw FileReadException(path)
        }
    }

    suspend fun <T> eval(script: String): T? {
        val e = evaluator(script) ?: return null
        try {
            val json = JSONObject(e)
            val tmpOutput = json.getString("output")
            val output: Any = when (json.getString("type")) {
                "Number" -> {
                    if (tmpOutput.contains(".")) {
                        json.getDouble("output")
                    } else {
                        json.getInt("output")
                    }
                }
                "Array" -> json.getJSONArray("output")
                "Boolean" -> json.getBoolean("output")
                "Object" -> json.getJSONObject("output")
                else -> json.getString("output")
            }
            return output as T
        } catch (ex: Exception) {
            throw JavaScriptException(e)
        }
    }
}