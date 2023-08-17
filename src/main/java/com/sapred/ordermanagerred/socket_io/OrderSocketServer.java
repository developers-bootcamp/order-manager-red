package com.sapred.ordermanagerred.socket_io;
import com.corundumstudio.socketio.Configuration;
import com.corundumstudio.socketio.SocketIOClient;
import com.corundumstudio.socketio.SocketIOServer;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
//import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

@Component
@Scope("singleton")
@Slf4j
public class OrderSocketServer {
    private static Map<String, Set<SocketIOClient>> companyIdToClientsMap = new ConcurrentHashMap<>();

    @Value("${host}")
    private String host;

    @Value("${socket.port}")
    private int port;

    @Bean
    public SocketIOServer socketIOServer() {
        com.corundumstudio.socketio.Configuration config = new com.corundumstudio.socketio.Configuration();
        config.setHostname(host);
        config.setPort(port);
        SocketIOServer server = new SocketIOServer(config);

        server.addConnectListener(client -> {
            log.info("Client connected: " + client.getSessionId());
            String companyId="11";
            joinCompany(client, companyId);
        });
        server.addDisconnectListener(client -> {
            log.info("Client disconnected: " + client.getSessionId());
            String companyId = "11"; // Assuming you know the company ID
            companyIdToClientsMap.get(companyId).remove(client);
        });

//server.on('connection', (client) => {}


//        server.addEventListener("new-order", String.class, (client, data, ackSender) -> {
//            log.info("new-order event accured " );
//            String companyId = data; // Assume data contains the companyId
//            joinCompany(client, companyId);
//        });

        server.start();
        return server;

    }


    private void joinCompany(SocketIOClient client, String companyId) {
        companyIdToClientsMap.computeIfAbsent(companyId, key -> ConcurrentHashMap.newKeySet()).add(client);
    }

    public static void sendNewOrderToCompany (String companyId, String message) {
        Set<SocketIOClient> clients = companyIdToClientsMap.get(companyId);
        if (clients != null) {
            for (SocketIOClient client : clients) {
                client.sendEvent("message", message);
            }
        }
    }
}
