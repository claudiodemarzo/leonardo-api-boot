package it.leonardo.leonardoapiboot.handlers;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.server.support.DefaultHandshakeHandler;

import java.security.Principal;
import java.util.Map;

public class WebSocketHandshakeHandler extends DefaultHandshakeHandler{

    private static final Log log = LogFactory.getLog(WebSocketHandshakeHandler.class);

    @Override
    protected Principal determineUser(ServerHttpRequest request, WebSocketHandler wsHandler, Map<String, Object> attributes) {
        log.info("Tentativo di connessione da: " + request.getRemoteAddress());
        if(!attributes.containsKey("userID")) {
            log.info("Connessione non autorizzata");
            return null;
        }
        return new StompPrincipal(attributes.get("userID").toString());
    }

    public static class StompPrincipal implements Principal {
        private final String name;
        public StompPrincipal(String name){
            this.name = name;
        }

        public String getName(){
            return name;
        }
    }
}
