package net.cozyvanilla.cozylib.util.java;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.UnknownHostException;

public class AddressUtils {

    private AddressUtils() {}

    /**
     * Converts an InetSocketAddress to its string IP representation.
     *
     * @param ip the address to convert
     * @return the IP address as a string, or "(null)" if input is null
     */
    @NotNull
    public static String toString(InetSocketAddress ip) {
        if (ip != null) {
            return ip.getAddress().getHostAddress();
        }

        return "(null)";
    }

    /**
     * Converts an InetSocketAddress to its raw byte representation.
     *
     * @param ip the address to convert
     * @return the byte array of the IP address, or null if input is null
     */
    @Nullable
    public static byte[] toBytes(InetSocketAddress ip) {
        return ip.getAddress().getAddress();
    }

    /**
     * Creates an InetSocketAddress from a byte array.
     *
     * @param bytes the byte array representing the IP address
     * @return the created InetSocketAddress, or null if conversion fails
     */
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
