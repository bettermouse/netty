/*
 * Copyright 2013-2018 Lilinfeng.
 *  
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *  
 *      http://www.apache.org/licenses/LICENSE-2.0
 *  
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package basic;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;

/**
 * @author lilinfeng
 * @date 2014年2月14日
 * @version 1.0
 */
public class TimeServer {

    public void bind(int port) throws Exception {
	// 配置服务端的NIO线程组 用于网络事件的处理 实际上就是Reactor线程组
	//一个用于接收客户端连接  一个用于网络读写
	EventLoopGroup bossGroup = new NioEventLoopGroup();
	EventLoopGroup workerGroup = new NioEventLoopGroup();
	try {
		//ServerBootstrap 是netty用于启动服务端的辅助启动类
	    ServerBootstrap b = new ServerBootstrap();
	    //设置 绑定 Reactor线程池
		//前面一个是两个都有的,后面的一个是给父构造器的
		ServerBootstrap channel = b.group(bossGroup, workerGroup)
				//ServerSocketChannel
				//设置并绑定服务端
				.channel(NioServerSocketChannel.class);
		channel  //要Channel
		    .option(ChannelOption.SO_BACKLOG, 1024)
				//类似于Reactor类中的Handler 主要处理网络IO
		    .childHandler(new ChildChannelHandler()); //要Handler
	    // 绑定端口，同步等待成功
		ChannelFuture bind = b.bind(port);
		ChannelFuture f = bind.sync();
//		f.channel().eventLoop().schedule(new Runnable() {
//			@Override
//			public void run() {
//				xxx
//			}
//		})
	    // 等待服务端监听端口关闭
	    f.channel().closeFuture().sync();
	} finally {
	    // 优雅退出，释放线程池资源
	    bossGroup.shutdownGracefully();
	    workerGroup.shutdownGracefully();
	}
    }

	/*
	 ChannelInitializer
	 将handle传给 channel
	* */
	                                          /*好相似的操作  */
    private class ChildChannelHandler extends ChannelInitializer<SocketChannel> {
	@Override
	protected void initChannel(SocketChannel arg0) throws Exception {
		//链路创建时,创建并初使化ChannelPipeline
		//它是一个职责链,负责管理执行ChannelHandler
		//ChannelHandler 是提供给用户的定制和扩建关键接口.利用ChannelHander完成定制功能
	    arg0.pipeline().addLast(new TimeServerHandler());
	}

    }

    /**
     * @param args
     * @throws Exception
     */
    public static void main(String[] args) throws Exception {
	int port = 8080;
	if (args != null && args.length > 0) {
	    try {
		port = Integer.valueOf(args[0]);
	    } catch (NumberFormatException e) {
		// 采用默认值
	    }
	}
	new TimeServer().bind(port);
    }
}
