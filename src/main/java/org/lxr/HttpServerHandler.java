package org.lxr;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.*;
import io.netty.util.CharsetUtil;

public class HttpServerHandler extends SimpleChannelInboundHandler<FullHttpRequest> {
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, FullHttpRequest request) throws Exception {
        System.out.println("request.method():"+request.method());
        System.out.println("request.uri:"+request.uri());

        ByteBuf jsonBuf = request.content();
        String jsonStr = jsonBuf.toString(CharsetUtil.UTF_8);

        System.out.println("request.content:"+request.content());
        System.out.println("jsonStr"+jsonStr);

        String rsp = "{\"a\":1}";
        ByteBuf rspBuf = Unpooled.copiedBuffer(rsp.getBytes());
        HttpResponse response = new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.OK, rspBuf);
        response.headers().set("Content-Type","text/json");
        response.headers().set("Content-Lenght",rspBuf.readableBytes());


        ctx.writeAndFlush(response).addListener(ChannelFutureListener.CLOSE);
    }
}
