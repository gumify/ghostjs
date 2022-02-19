package me.gumify.ghostjs

import android.content.Context
import java.io.IOException
import java.io.InputStream

object IO {
    fun readAssetFile(context: Context, inFile: String): String? {
        var tContents: String? = null
        try {
            val stream: InputStream = context.assets.open(inFile)
            val size: Int = stream.available()
            val buffer = ByteArray(size)
            stream.read(buffer)
            stream.close()
            tContents = String(buffer)
        } catch (e: IOException) {
            // Handle exceptions here
        }
        return tContents
    }
}