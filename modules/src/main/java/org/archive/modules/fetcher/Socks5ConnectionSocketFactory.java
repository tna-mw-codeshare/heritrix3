package org.archive.modules.fetcher;

import org.apache.http.HttpHost;
import org.apache.http.conn.socket.PlainConnectionSocketFactory;
import org.apache.http.protocol.HttpContext;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Proxy;
import java.net.Socket;

/**
 * Allow HTTP connections to be made over a SOCKS5 connection.
 */
public class Socks5ConnectionSocketFactory extends PlainConnectionSocketFactory {
    /**
     * Instantiates a new socket to our SOCKS5 proxy.
     *
     * @param context
     * @return
     * @throws IOException
     */
    @Override
    public Socket createSocket(HttpContext context) throws IOException {
        // find our socket address from the context provided from FetchHTTPRequest
        InetSocketAddress socksAddress = (InetSocketAddress) context.getAttribute("socket.address");
        Proxy proxy = new Proxy(Proxy.Type.SOCKS, socksAddress);
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