package me.alexirving.app

import Utils.get
import Utils.getReasourceURL
import Utils.getRequestTemplate
import Utils.kill
import Utils.open
import me.alexirving.lib.copyOver
import me.alexirving.lib.pq
import java.awt.*
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream
import java.nio.file.Files
import java.nio.file.Path
import java.util.*
import kotlin.system.exitProcess

val config = Properties()
val latest = File("latest")

val loop = Timer("Updater")

fun main() {
    val i = TrayIcon(Toolkit.getDefaultToolkit().createImage(getReasourceURL("notification.png")))
    i.toolTip = "BellApp"

    val m = MenuItem("Exit")
    m.addActionListener {
        exitProcess(0)
    }
    i.popupMenu = PopupMenu().apply { add(m) }
    SystemTray.getSystemTray().add(i)




    latest.createNewFile()
    copyOver("settings.properties")
    config.load(FileInputStream("settings.properties"))
    val songFile = File(config.getProperty("SONG_LOC")).absolutePath
    val date = getRequestTemplate(config.getProperty("DATE")).build()
    val song = getRequestTemplate(config.getProperty("SONG")).build()

    fun isLatest(): Boolean = get(date) == latest.readText()
    fun saveLatest() {
        kill(config.getProperty("APP_NAME"))
        FileOutputStream(songFile).use {
            it.write(Base64.getDecoder().decode(get(song)))
        }
        open(
            config.getProperty("APP_LOC")
        )
        Files.write(Path.of("latest"), get(date).toByteArray())
        "Song has been updated successfully".pq("LOG")
    }
    loop.scheduleAtFixedRate(object : TimerTask() {
        override fun run() {
            if (!isLatest()) {
                "Found new song!\nUpdating now!".pq("LOG")
                saveLatest()
            } else
                "No new song yet :(".pq("LOG")
        }
    }, 0L, config.getProperty("DELAY").toLong())
}