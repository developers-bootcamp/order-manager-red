package com.sapred.ordermanagerred.socket_io;


import com.corundumstudio.socketio.AuthorizationListener;
//import com.corundumstudio.socketio.Configuration;
import com.corundumstudio.socketio.HandshakeData;
import com.corundumstudio.socketio.SocketConfig;
import com.corundumstudio.socketio.SocketIOServer;
import com.corundumstudio.socketio.annotation.SpringAnnotationScanner;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

    @Configuration
    public class SocketConfigClass {

        @Value("${host}")
        private String host;

        @Value("${port}")
        private int port;

        @Bean
        public SocketIOServer socketIOServer() {
            com.corundumstudio.socketio.Configuration socketIOConfig = new com.corundumstudio.socketio.Configuration();
            socketIOConfig.setHostname(host);
            socketIOConfig.setPort(port);

            SocketConfig socketConfig = new SocketConfig();
            socketConfig.setReuseAddress(true);
            socketIOConfig.setSocketConfig(socketConfig);

            socketIOConfig.setAuthorizationListener(new AuthorizationListener() {
                @Override
                public boolean isAuthorized(HandshakeData data) {
                    // Implement your authorization logic here
                    return true; // For demo purposes, allow all connections
                }
            });

            return new SocketIOServer(socketIOConfig);
        }

        @Bean
        public SpringAnnotationScanner springAnnotationScanner(SocketIOServer socketServer) {
            return new SpringAnnotationScanner(socketServer);
        }
    }

