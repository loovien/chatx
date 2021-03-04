package com.example.chat;

import com.example.chat.configs.Opts;
import com.example.chat.configs.TcpOps;
import com.example.chat.dto.AuthDTO;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.group.ChannelGroup;
import io.netty.channel.group.DefaultChannelGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.util.concurrent.GlobalEventExecutor;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.concurrent.ConcurrentHashMap;

@Slf4j
@Getter
@Setter
@Component
public class ChatInitializr {

    private final Opts opts;

    private final TcpOps tcpOps;

    private EventLoopGroup master;

    private EventLoopGroup worker;

    private final ChannelGroup channels = new DefaultChannelGroup(GlobalEventExecutor.INSTANCE);

    private final ConcurrentHashMap<Integer, AuthDTO> userSocket = new ConcurrentHashMap<>();

    public ChatInitializr(Opts opts, TcpOps tcpOps) {
        this.opts = opts;
        this.tcpOps = tcpOps;
        this.master = new NioEventLoopGroup(opts.getMasterThreadNum());
        this.worker = new NioEventLoopGroup(opts.getWorkThreadNum());
    }

}
