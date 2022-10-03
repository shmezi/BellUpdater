package me.alexirving.launcher

import Utils.get
import Utils.getRequestTemplate
import com.google.gson.Gson
import com.google.gson.JsonObject
import me.alexirving.lib.copyOver
import me.alexirving.lib.pq
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream
import java.net.URL
import java.nio.file.Files
import java.nio.file.Path
import java.util.*

val config = Properties()
val latest = File("updated")

val loop = Timer("Updater")
var bellVote: Process? = null
val gson = Gson()

fun main() {
    latest.createNewFile()
    copyOver("updater.properties")
    config.load(FileInputStream("updater.properties"))

    val date = getRequestTemplate(config.getProperty("GITHUB")).build()

    fun isLatest(): Boolean = gson.fromJson(get(date), JsonObject::class.java).getAsJsonArray("assets")
        .get(0).asJsonObject.get("updated_at").asString == latest.readText()

    fun saveLatest() {
        bellVote?.destroy()

        val assets = gson.fromJson(get(date), JsonObject::class.java).getAsJsonArray("assets")
            .get(0).asJsonObject

        FileOutputStream("BellVotes.jar").write(URL(assets.get("browser_download_url").asString).readBytes())
        Files.write(Path.of("updated"), assets.get("updated_at").asString.toByteArray())
        "Bell votes has been updated!".pq("LOG")
        bellVote = Runtime.getRuntime().exec("java -jar BellVotes.jar")
    }
    if (isLatest()) {
        bellVote = Runtime.getRuntime().exec("java -jar BellVotes.jar")
    }

    loop.scheduleAtFixedRate(object : TimerTask() {
        override fun run() {
            if (!isLatest()) {
                "Found new Bell votes application!\nUpdating now!".pq("LOG")
                saveLatest()
            } else
                "No new app yet :(".pq("LOG")
        }
    }, 0L, config.getProperty("DELAY").toLong())
}
