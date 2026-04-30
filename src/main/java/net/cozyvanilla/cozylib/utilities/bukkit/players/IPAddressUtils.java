package net.cozyvanilla.cozylib.utilities.bukkit.players;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.UnknownHostException;

public class IPAddressUtils {

    @NotNull
    public static String toString(InetSocketAddress ip) {
        if (ip != null) {
            return ip.getAddress().getHostAddress();
        }

        return "(null)";
    }

    @Nullable
    public static byte[] toBytes(InetSocketAddress ip) {
        return ip.getAddress().getAddress();
    }

    @Nullable
    public static InetSocketAddress fromBytes(@NotNull byte[] bytes) {
        try {
            InetAddress ip = InetAddress.getByAddress(bytes);
            return new InetSocketAddress(ip, 0);
        } catch (UnknownHostException e) {
            return null;
        }
    }
}
