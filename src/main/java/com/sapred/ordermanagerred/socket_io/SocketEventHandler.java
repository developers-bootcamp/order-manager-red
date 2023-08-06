package com.sapred.ordermanagerred.socket_io;

import com.corundumstudio.socketio.*;
import com.corundumstudio.socketio.annotation.OnConnect;
import com.corundumstudio.socketio.annotation.OnDisconnect;
import com.corundumstudio.socketio.annotation.OnEvent;
import com.corundumstudio.socketio.listener.*;
import com.sapred.ordermanagerred.model.Order;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class SocketEventHandler {

    private final SocketIOServer socketIOServer;

    @Autowired
    public SocketEventHandler(SocketIOServer socketIOServer) {
        this.socketIOServer = socketIOServer;
    }

    @OnConnect
    public void onConnect(SocketIOClient client) {
        System.out.println("Client connected: " + client.getSessionId());
    }

    @OnDisconnect
    public void onDisconnect(SocketIOClient client) {
        System.out.println("Client disconnected: " + client.getSessionId());
    }

    // Handle custom event for new order
    @OnEvent("new-order")
    public void onNewOrder(SocketIOClient client, Order order) {
        System.out.println("New order received: " + order.getId());

        // Broadcast the new order event to all connected clients
        socketIOServer.getBroadcastOperations().sendEvent("new-order", order);
    }
}

