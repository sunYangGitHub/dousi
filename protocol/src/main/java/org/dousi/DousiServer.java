package org.dousi;

import org.dousi.api.Handler;
import org.dousi.api.Server;
import org.dousi.config.ServerConfig;
import org.dousi.netty.NettyTransportFactory;
import java.util.concurrent.CopyOnWriteArrayList;


/**
 * The class that represents a dousi server.
 */
public class DousiServer {

  /**
   * The service handlers.
   * Note that CopyOnWriteArrayList might not be efficient too much when writing,
   * but this is not a writing-bundle collection.
   */
  private CopyOnWriteArrayList<Handler> serviceHandlers = new CopyOnWriteArrayList<>();

  private ServerConfig serverConfig;

  public DousiServer(ServerConfig serverConfig) {
    this.serverConfig = serverConfig;
  }

  /**
   * Register the service into this exporter.
   *
   * @param interfaceClass The interface that we want to export.
   * @param serviceObject The object that this service implementation.
   */
  public <T> void registerService(Class<T> interfaceClass, T serviceObject) {
    serviceHandlers.add(new HandlerDelegate(new ServerImpl<T>(serviceObject, interfaceClass)));
  }

  public void run() {
    Server server = NettyTransportFactory.getInstance().createServer(serverConfig, serviceHandlers);
    server.open();
  }
}
