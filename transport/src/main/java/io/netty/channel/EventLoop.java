/*
 * Copyright 2013 The Netty Project
 *
 * The Netty Project licenses this file to you under the Apache License,
 * version 2.0 (the "License"); you may not use this file except in compliance
 * with the License. You may obtain a copy of the License at:
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations
 * under the License.
 */

package io.netty.channel;

import io.netty.util.concurrent.EventExecutor;

/**
 * Will handle all the I/O operations for a {@link Channel} once registered.
 *
 * One {@link EventLoop} instance will usually handle more than one {@link Channel} but this may depend on
 * implementation details and internals.
 *
 * 一旦注册，将处理通道的所有I / O操作。一个EventLoop实例通常处理超过一个channel
 * 但这可能取决于实现细节和内部
 */
public interface EventLoop extends EventExecutor, EventLoopGroup {
    @Override
    EventLoopGroup parent();

    @Override
    EventLoop unwrap();

    /**
     * Creates a new default {@link ChannelHandlerInvoker} implementation that uses this {@link EventLoop} to
     * invoke event handler methods.
     * 创建一个新的默认ChannelHandlerInvoker实现，该实现使用此EventLoop来调用事件处理程序方法
     */
    ChannelHandlerInvoker asInvoker();
}
