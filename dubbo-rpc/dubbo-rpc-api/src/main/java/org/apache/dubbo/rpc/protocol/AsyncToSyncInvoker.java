/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.apache.dubbo.rpc.protocol;

import org.apache.dubbo.common.URL;
import org.apache.dubbo.remoting.RemotingException;
import org.apache.dubbo.remoting.TimeoutException;
import org.apache.dubbo.rpc.Invocation;
import org.apache.dubbo.rpc.InvokeMode;
import org.apache.dubbo.rpc.Invoker;
import org.apache.dubbo.rpc.Result;
import org.apache.dubbo.rpc.RpcException;
import org.apache.dubbo.rpc.RpcInvocation;

import java.util.concurrent.ExecutionException;

/**
 * This class will work as a wrapper wrapping outside of each protocol invoker.
 * 它的作用是把异步结果转化为同步结果。新的改动中每个调用只要不是oneway方式调用都会先以异步调用开始，然后根据配置的情况如果是同步调用，则会在这个类中进行异步结果转同步的处理。当然，这里先是执行了invoke，然后就进入下一个AbstractInvoker的invoke了。
 * @param <T>
 */
public class AsyncToSyncInvoker<T> implements Invoker<T> {

    private Invoker<T> invoker;

    public AsyncToSyncInvoker(Invoker<T> invoker) {
        this.invoker = invoker;
    }

    @Override
    public Class<T> getInterface() {
        return invoker.getInterface();
    }

    @Override
    public Result invoke(Invocation invocation) throws RpcException {
        Result asyncResult = invoker.invoke(invocation);

        try {
            if (InvokeMode.SYNC == ((RpcInvocation)invocation).getInvokeMode()) {
                asyncResult.get();
            }
        } catch (InterruptedException e) {
            throw new RpcException("Interrupted unexpectedly while waiting for remoting result to return!  method: " + invocation.getMethodName() + ", provider: " + getUrl() + ", cause: " + e.getMessage(), e);
        } catch (ExecutionException e) {
            Throwable t = e.getCause();
            if (t instanceof TimeoutException) {
                throw new RpcException(RpcException.TIMEOUT_EXCEPTION, "Invoke remote method timeout. method: " + invocation.getMethodName() + ", provider: " + getUrl() + ", cause: " + e.getMessage(), e);
            } else if (t instanceof RemotingException) {
                throw new RpcException(RpcException.NETWORK_EXCEPTION, "Failed to invoke remote method: " + invocation.getMethodName() + ", provider: " + getUrl() + ", cause: " + e.getMessage(), e);
            }
        } catch (Throwable e) {
            throw new RpcException(e.getMessage(), e);
        }
        return asyncResult;
    }

    @Override
    public URL getUrl() {
        return invoker.getUrl();
    }

    @Override
    public boolean isAvailable() {
        return invoker.isAvailable();
    }

    @Override
    public void destroy() {
        invoker.destroy();
    }

    public Invoker<T> getInvoker() {
        return invoker;
    }
}
