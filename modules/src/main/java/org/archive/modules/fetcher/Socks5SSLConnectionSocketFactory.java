package org.archive.modules.fetcher;

import org.apache.http.HttpHost;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.protocol.HttpContext;

import javax.net.ssl.SSLContext;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Proxy;
import java.net.Socket;

/**
 * Allow SSL connections to be made over a SOCKS5 connection.
 */
public class Socks5SSLConnectionSocketFactory extends SSLConnectionSocketFactory {
    /**
     * Initialise our SSLConnectionSocketFactory with some custom defaults
     *
     * @param context
     */
    public Socks5SSLConnectionSocketFactory(SSLContext context) {
        // whilst in development, disable certificate verification
        // TODO: make decision as to a sensible default for this
        super(context, ALLOW_ALL_HOSTNAME_VERIFIER);
    }

    /**
     * Instantiates a new socket to our SOCKS5 proxy.
     *
     * @param context
     * @return
     * @throws IOException
     */
    @Override
    public Socket createSocket(HttpContext context) throws IOException {
        InetSocketAddress socketAddress = (InetSocketAddress) context.getAttribute("socket.address");
        Proxy proxy = new Proxy(Proxy.Type.SOCKS, socketAddress);
        return new Socket(proxy);
    }

    /**
     * Make a connection via our SOCKS5 proxy
     *
     * @param connectTimeout
     * @param socket
     * @param host
     * @param remoteAddress
     * @param localAddress
     * @param context
     * @return
     * @throws IOException
     */
    @Override
    public Socket connectSocket(int connectTimeout, Socket socket, HttpHost host, InetSocketAddress remoteAddress, InetSocketAddress localAddress, HttpContext context) throws IOException {
        // get an unresolved version of our remote address
        InetSocketAddress unresolvedRemoteAddress = InetSocketAddress.createUnresolved(host.getHostName(), remoteAddress.getPort());
        return super.connectSocket(connectTimeout, socket, host, unresolvedRemoteAddress, localAddress, context);
    }
}