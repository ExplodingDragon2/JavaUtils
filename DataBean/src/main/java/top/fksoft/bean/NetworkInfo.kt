package top.fksoft.bean

import java.net.Inet6Address
import java.net.InetAddress

/**
 * # 网络主机信息
 *
 * 此数据类用于记录网络主机信息，
 *
 * @property ip String ip地址
 * @property port Int 主机端口号
 * @property hostName String? 主机网络名称 （如果有）
 * @constructor
 */
data class NetworkInfo (val ip:String,val port:Int){

    var hostName:String = ip

    /**
     * # 判断当前网络主机地址是否为 IPv6 地址
     *
     * @return Boolean 是否为 IPv6 地址
     */
    fun isIpv6Host() =  InetAddress.getByName(ip) is Inet6Address

    override fun toString(): String {
        return "$hostName:$port"
    }

}
