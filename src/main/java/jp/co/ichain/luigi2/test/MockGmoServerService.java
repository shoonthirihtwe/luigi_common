package jp.co.ichain.luigi2.test;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.InetSocketAddress;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;

/**
 * GMOモックサーバーサービス
 *
 * @author : [VJP] タン
 * @createdAt : 2021-09-15
 * @updatedAt : 2021-09-15
 */
public class MockGmoServerService {
  HttpServer httpServer;

  public MockGmoServerService() throws IOException {
    InetSocketAddress address = new InetSocketAddress(8000);
    httpServer = HttpServer.create(address, 0);
  }

  public void mock(String url, String response) {
    HttpHandler httpHandler = new HttpHandler() {
      @Override
      public void handle(HttpExchange exchange) throws IOException {
        byte[] res = response.getBytes();
        exchange.sendResponseHeaders(HttpURLConnection.HTTP_OK, res.length);
        exchange.getResponseBody().write(res);
        exchange.close();
      }
    };

    httpServer.createContext(url, httpHandler);
  }

  public void start() {
    httpServer.start();
  }

  public void stop() {
    httpServer.stop(0);
  }
}
