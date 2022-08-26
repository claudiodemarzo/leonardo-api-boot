package it.leonardo.leonardoapiboot.configuration;

import com.fasterxml.jackson.databind.ObjectMapper;
import it.leonardo.leonardoapiboot.controller.ChatWSController;
import it.leonardo.leonardoapiboot.entity.Notifica;
import it.leonardo.leonardoapiboot.handler.WebSocketHandshakeHandler;
import it.leonardo.leonardoapiboot.service.ChatroomService;
import it.leonardo.leonardoapiboot.service.UtenteService;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
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

import java.util.List;

@Configuration
@EnableWebSocketMessageBroker
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {
    private static Log log = LogFactory.getLog(WebSocketConfig.class);

    @Autowired
    private UtenteService utenteService;

    @Autowired
    private ChatroomService chatroomService;

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
                        if (session.getAttributes().containsKey("userID")) {
                            log.info("Utente collegato: " + session.getAttributes().get("userID"));
                            utenteService.setOnlineStatus(true, Integer.parseInt(session.getAttributes().get("userID").toString()));
                            chatroomService.getByUtenteMit(utenteService.findById(Integer.parseInt(session.getAttributes().get("userID").toString())).get()).forEach(c -> {
                                int id = c.getUtenteDest().getUtenteId();
                                ChatWSController.sendNotification(String.valueOf(id), new Notifica(Notifica.TipoNotifica.internal, "", "{\"context\" : \"userStatusUpdate\", \"value\" : {\"userID\" : " + session.getAttributes().get("userID").toString() + ", \"status\" : true}}", null), null);
                            });
                        } else {
                            log.warn("Connessione non autorizzata");
                            session.close(CloseStatus.NOT_ACCEPTABLE);
                        }
                        super.afterConnectionEstablished(session);
                    }

                    @Override
                    public void afterConnectionClosed(WebSocketSession session, CloseStatus closeStatus) throws Exception {
                        if (CloseStatus.NOT_ACCEPTABLE != closeStatus) {
                            log.info("Utente " + session.getAttributes().get("userID") + " disconnesso da WS");
                            utenteService.setOnlineStatus(false, Integer.parseInt(session.getAttributes().get("userID").toString()));
                            chatroomService.getByUtenteMit(utenteService.findById(Integer.parseInt(session.getAttributes().get("userID").toString())).get()).forEach(c -> {
                                int id = c.getUtenteDest().getUtenteId();
                                ChatWSController.sendNotification(String.valueOf(id), new Notifica(Notifica.TipoNotifica.internal, "", "{\"context\" : \"userStatusUpdate\", \"value\" : {\"userID\" : " + session.getAttributes().get("userID").toString() + ", \"status\" : false}}", null), null);
                            });
                        }
                        super.afterConnectionClosed(session, closeStatus);
                    }

                    @Override
                    public void handleMessage(WebSocketSession session, WebSocketMessage<?> message) throws Exception {
                        super.handleMessage(session, message);
                    }
                };
            }
        });
        WebSocketMessageBrokerConfigurer.super.configureWebSocketTransport(registry);
    }
}
