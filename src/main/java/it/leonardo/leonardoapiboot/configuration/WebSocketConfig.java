package it.leonardo.leonardoapiboot.configuration;

import com.fasterxml.jackson.databind.ObjectMapper;
import it.leonardo.leonardoapiboot.handlers.WebSocketHandshakeHandler;
import it.leonardo.leonardoapiboot.service.UtenteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.converter.DefaultContentTypeResolver;
import org.springframework.messaging.converter.MappingJackson2MessageConverter;
import org.springframework.messaging.converter.MessageConverter;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.util.MimeTypeUtils;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.WebSocketMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketTransportRegistration;
import org.springframework.web.socket.handler.WebSocketHandlerDecorator;
import org.springframework.web.socket.handler.WebSocketHandlerDecoratorFactory;
import org.springframework.web.socket.server.support.HttpSessionHandshakeInterceptor;

import java.util.HashMap;
import java.util.List;

@Configuration
@EnableWebSocketMessageBroker
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {

    public static HashMap<Integer, WebSocketSession> users = new HashMap<>();
    @Autowired
    private UtenteService utenteService;

    @Override
    public void configureMessageBroker(MessageBrokerRegistry config) {
        config.enableSimpleBroker("/topic");
        config.setApplicationDestinationPrefixes("/app");
        config.setUserDestinationPrefix("/user");
    }

    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        registry.addEndpoint("/wsock")
                .setAllowedOrigins("https://leonardostart.tk")
                .addInterceptors(new HttpSessionHandshakeInterceptor())
                .setHandshakeHandler(new WebSocketHandshakeHandler())
                .withSockJS();
    }


    @Override
    public boolean configureMessageConverters(List<MessageConverter> messageConverters) {
        DefaultContentTypeResolver resolver = new DefaultContentTypeResolver();
        resolver.setDefaultMimeType(MimeTypeUtils.APPLICATION_JSON);
        MappingJackson2MessageConverter converter = new MappingJackson2MessageConverter();
        converter.setObjectMapper(new ObjectMapper());
        converter.setContentTypeResolver(resolver);
        messageConverters.add(converter);
        return false;
}

    @Override
    public void configureWebSocketTransport(WebSocketTransportRegistration registry) {
        registry.addDecoratorFactory(new WebSocketHandlerDecoratorFactory() {
            @Override
            public WebSocketHandler decorate(WebSocketHandler handler) {
                return new WebSocketHandlerDecorator(handler) {
                    @Override
                    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
                        if(session.getAttributes().containsKey("userID")){
                            String username = utenteService.findById((int)session.getAttributes().get("userID")).get().getUsername();
                            System.out.println(username+" connesso");
                            users.put((int)session.getAttributes().get("userID"),session);
                        }else{
                            System.out.println("Connessione rifiutata");
                            session.close(CloseStatus.NOT_ACCEPTABLE);
                        }
                        System.out.println(users.keySet());
                        super.afterConnectionEstablished(session);
                    }
                    @Override
                    public void afterConnectionClosed(WebSocketSession session, CloseStatus closeStatus) throws Exception {
                        if (CloseStatus.NOT_ACCEPTABLE != closeStatus){
                            users.remove(session.getAttributes().get("userID"));
                            System.out.println("RIP "+utenteService.findById((int)session.getAttributes().get("userID")).get().getUsername());
                        }
                        System.out.println(users.keySet());
                        super.afterConnectionClosed(session,closeStatus);
                    }

                    @Override
                    public void handleMessage(WebSocketSession session, WebSocketMessage<?> message) throws Exception {
                        System.out.println(message.getPayload());
                        super.handleMessage(session, message);
                    }
                };
            }
        });
        WebSocketMessageBrokerConfigurer.super.configureWebSocketTransport(registry);
    }
}
