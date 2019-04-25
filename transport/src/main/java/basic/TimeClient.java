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

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.util.concurrent.Future;
import io.netty.util.concurrent.GenericFutureListener;

import java.util.concurrent.TimeUnit;

/**
 * @author lilinfeng
 * @date 2014年2月14日
 * @version 1.0
 */
public class TimeClient {

    public void connect(int port, String host) throws Exception {
	// 配置客户端NIO线程组
	EventLoopGroup group = new NioEventLoopGroup();
	try {
	    Bootstrap b = new Bootstrap();
	    b.group(group).channel(NioSocketChannel.class)
		    .option(ChannelOption.TCP_NODELAY, true)
		    .handler(new ChannelInitializer<SocketChannel>() {




			//在Channel在初始化的时候,将 ChannelHandler 设置以 Pipeline中
			@Override
			public void initChannel(SocketChannel ch)
				throws Exception {
				//其作用是当创建NIOSocketChannel成功之后,在进行初始化时,
				//将它的ChannelHandler设置到ChannelPipeline 用于处理网络I/O处理

			    ch.pipeline().addLast(new TimeClientHandler());
			}
		    });

	    // 发起异步连接操作
		ChannelFuture connect = b.connect(host, port).sync();

//		connect.channel().closeFuture().
//				addListener(new ChannelFutureListener() {
//					@Override
//					public void operationComplete(ChannelFuture future) throws Exception {
//						future.channel().eventLoop().schedule(new Runnable() {
//							@Override
//							public void run() {
//								//这一段代码必须要不停的掉用
//								System.out.println("AAAAAA");
//							}
//						}, 5, TimeUnit.SECONDS);
//					}
//				}).sync();

				//.channel().closeFuture().sync();

		//ChannelFuture f = connect.sync();

	    // 当客户端链路关闭
		//connect.channel().closeFuture().sync();
//		new Thread(new Runnable() {
//			@Override
//			public void run() {
//				while (true){
//					try {
//						Thread.sleep(10000);
//					} catch (InterruptedException e) {
//						e.printStackTrace();
//					}
//					System.out.println("i want to gg");
//				}
//			}
//		}).start();

	} finally {
	    // 优雅退出，释放NIO线程组
	 //   group.shutdownGracefully();
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
	new TimeClient().connect(port, "127.0.0.1");
    }
}
