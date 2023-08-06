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
import org.springframework.stereotype.Controller;




    @Configuration
    public class SocketConfigClass {

        @Value("${host}")
        private String host;

        @Value("${socket.port}")
        private int port;

        @Bean
        public SocketIOServer socketIOServer() {
            com.corundumstudio.socketio.Configuration socketIOConfig = new com.corundumstudio.socketio.Configuration();
            socketIOConfig.setHostname(host);
            socketIOConfig.setPort(port);
//            socketIOConfig.set
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

//            socketIOConfig.setPackagePrefix("/socket.io");

            SocketIOServer socketIOServer = new SocketIOServer(socketIOConfig);

//            socketIOServer.path("/socket.io");
//            socketIOServer.start();
            socketIOServer.start();
            return socketIOServer;
        }

        @Bean
        public SpringAnnotationScanner springAnnotationScanner(SocketIOServer socketServer) {
            return new SpringAnnotationScanner(socketServer);
        }
    }

