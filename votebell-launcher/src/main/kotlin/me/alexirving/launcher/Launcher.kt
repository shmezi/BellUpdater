package me.alexirving.launcher

import Utils.get
import Utils.getReasourceURL
import Utils.getRequestTemplate
import com.google.gson.Gson
import com.google.gson.JsonObject
import me.alexirving.lib.copyOver
import me.alexirving.lib.pq
import java.awt.MenuItem
import java.awt.PopupMenu
import java.awt.SystemTray.getSystemTray
import java.awt.Toolkit.getDefaultToolkit
import java.awt.TrayIcon
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream
import java.net.URL
import java.nio.file.Files
import java.nio.file.Path
import java.util.*
import kotlin.system.exitProcess

val config = Properties()
val latest = File("updated")

val loop = Timer("Updater")
var bellVote: Process? = null
val gson = Gson()

fun main() {
    val i = TrayIcon(getDefaultToolkit().createImage(getReasourceURL("bells.png")))
    i.toolTip = "BellLauncher"

    val m = MenuItem("Exit")
    m.addActionListener {
        bellVote?.destroy()
        exitProcess(0)
    }

    i.popupMenu = PopupMenu().apply { add(m) }
    getSystemTray().add(i)


    latest.createNewFile()
    copyOver("updater.properties")
    config.load(FileInputStream("updater.properties"))

    val date = getRequestTemplate(config.getProperty("GITHUB")).build()

    fun isLatest(): Boolean = try {
        gson.fromJson(get(date), JsonObject::class.java).getAsJsonArray("assets")
            .get(0).asJsonObject.get("updated_at").asString == latest.readText()
    } catch (_: NullPointerException) {
        true
    }

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
        "Started bell app!".pq()
    }

    loop.scheduleAtFixedRate(object : TimerTask() {
        override fun run() {
            if (!isLatest()) {
                "Found new Bell votes application!\nUpdating now!".pq("LOG")
                saveLatest()
            } else
                "No new app yet :(".pq("LOG")
        }
    }, config.getProperty("DELAY").toLong(), config.getProperty("DELAY").toLong())
}
