package ua.shtain.opensky.core.network.http_client

interface HttpClientApi {

    fun <Api> getApi(_class: Class<Api>): Api

}