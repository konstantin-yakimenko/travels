package ru.travels.repositories;

import org.tarantool.SocketChannelProvider;
import org.tarantool.TarantoolClient;
import org.tarantool.TarantoolClientConfig;
import org.tarantool.TarantoolClientImpl;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.SocketChannel;

public class Tarantool {
    private final TarantoolClient client;

    public static final Integer SPACE_USER = 512;
    public static final Integer SPACE_LOCATION = 513;
    public static final Integer SPACE_VISIT = 514;

    public Tarantool(String username, String password) {
        client = TryExecutor.execute(() -> this.create(username, password));
    }

    public TarantoolClientImpl create(String username, String password) {
        TarantoolClientConfig config = new TarantoolClientConfig();
        config.username = username;
        config.password = password;

        SocketChannelProvider socketChannelProvider = new SocketChannelProvider() {
            @Override
            public SocketChannel get(int retryNumber, Throwable lastError) {
                if (lastError != null) {
                    lastError.printStackTrace(System.out);
                }
                try {
                    return SocketChannel.open(new InetSocketAddress("localhost", 3301));
                } catch (IOException e) {
                    throw new IllegalStateException(e);
                }
            }
        };

        return new TarantoolClientImpl(socketChannelProvider, config);
    }

    public TarantoolClient getClient() {
        return client;
    }
}
