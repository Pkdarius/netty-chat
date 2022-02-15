package org.example.handler;

import io.netty.channel.Channel;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

public class Channels {
    static private final ConcurrentMap<String, Channel> channels = new ConcurrentHashMap<>();

    private Channels() {}

    public static Map<String, Channel> getChannels() {
        return channels;
    }
}
