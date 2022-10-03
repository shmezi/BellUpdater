import java.awt.Desktop
import java.io.File
import java.net.URI
import java.net.http.HttpClient
import java.net.http.HttpRequest
import java.net.http.HttpResponse.BodyHandlers

object Utils {
    val client: HttpClient = HttpClient.newHttpClient()
    private val requestTemplate: HttpRequest.Builder =
        HttpRequest.newBuilder()
            .GET()
            .setHeader("User-Agent", "Mozilla/5.0")

    fun getRequestTemplate(url: String) = requestTemplate.copy().uri(URI(url))

    fun kill(name: String) = Runtime.getRuntime().exec("taskkill /F /IM $name")
    fun open(location: String) = Desktop.getDesktop().open(File(location))

    fun get(request: HttpRequest): String = client.send(request, BodyHandlers.ofString()).body()



}