package com.Jinhyy.config;

import io.netty.channel.ChannelOption;
import io.netty.handler.timeout.ReadTimeoutHandler;
import io.netty.handler.timeout.WriteTimeoutHandler;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.web.reactive.function.client.ExchangeStrategies;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.netty.http.client.HttpClient;

@Configuration
public class NaverDaoConfig {
    @Value("${app.naver.host}")
    String host;
    @Bean(name = "naverSearchWebClient")
    public WebClient naverSearchWebClient() {
        final ExchangeStrategies exchangeStrategies = ExchangeStrategies.builder().codecs(config -> config.defaultCodecs().maxInMemorySize(-1)).build();
        final HttpClient tcpClient = HttpClient.create()
                .option(ChannelOption.CONNECT_TIMEOUT_MILLIS, 3000)
                .doOnConnected(connection -> connection.addHandlerFirst(new ReadTimeoutHandler(3)).addHandlerLast(new WriteTimeoutHandler(3)));

        return WebClient.builder()
                .baseUrl("https://"+ host)
                .exchangeStrategies(exchangeStrategies)
                .clientConnector(new ReactorClientHttpConnector(tcpClient))
                .build()
                ;
    }
}
