/**
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

package org.yx.sumk.dubbo.filter;

import com.google.common.collect.Maps;
import org.apache.dubbo.common.constants.CommonConstants;
import org.apache.dubbo.common.extension.Activate;
import org.apache.dubbo.common.utils.StringUtils;
import org.apache.dubbo.rpc.Filter;
import org.apache.dubbo.rpc.Invocation;
import org.apache.dubbo.rpc.Invoker;
import org.apache.dubbo.rpc.Result;
import org.apache.dubbo.rpc.RpcContext;
import org.apache.dubbo.rpc.RpcException;
import org.yx.common.context.ActionContext;
import org.yx.util.CollectionUtil;

import java.util.Map;

/**
 * @author : wjiajun
 */
@Activate(
        group = {CommonConstants.CONSUMER, CommonConstants.PROVIDER},
        order = -1
)
public class TraceFilter implements Filter {

    public final static String SUMK_TRACE_PREFIX = "sumk.";

    public final static String ACT = SUMK_TRACE_PREFIX + "act";
    public final static String TRACE_ID = SUMK_TRACE_PREFIX + "traceId";
    public final static String SPAN_ID = SUMK_TRACE_PREFIX + "spanId";
    public final static String IS_TEST = SUMK_TRACE_PREFIX + "isTest";
    public final static String USER_ID = SUMK_TRACE_PREFIX + "userId";

    @Override
    public Result invoke(Invoker<?> invoker, Invocation invocation) throws RpcException {
        RpcContext rpcContext = RpcContext.getContext();
        ActionContext sumkContext = ActionContext.current();

        boolean isConsumer = rpcContext.isConsumerSide();

        if (isConsumer) {
            // sumk rpc context 到 dubbo attachments
            rpcContext.setAttachments(convertSumkAttachments(sumkContext.attachmentView()));
            rpcContext.setAttachment(ACT, sumkContext.act());
            rpcContext.setAttachment(TRACE_ID, sumkContext.traceId());
            rpcContext.setAttachment(SPAN_ID, sumkContext.spanId());
            rpcContext.setAttachment(IS_TEST, sumkContext.isTest());
            rpcContext.setAttachment(USER_ID, sumkContext.userId());

            rpcContext.getObjectAttachments().forEach((k, v) -> {
                if (!k.contains(SUMK_TRACE_PREFIX)) {
                    return;
                }
                if (!invocation.getAttachments().containsKey(k)) {
                    invocation.getObjectAttachments().put(k, v);
                }
            });
        } else {
            // dubbo attachments - rpcContext
            Map<String, Object> attachments = Maps.newHashMap(rpcContext.getObjectAttachments());

            attachments.remove(ACT);
            attachments.remove(TRACE_ID);
            attachments.remove(SPAN_ID);
            attachments.remove(IS_TEST);
            attachments.remove(USER_ID);

            ActionContext.newContext(rpcContext.getAttachment(ACT), rpcContext.getAttachment(TRACE_ID), rpcContext.getAttachment(SPAN_ID), rpcContext.getAttachment(USER_ID), (Boolean) rpcContext.getObjectAttachment(IS_TEST),
                    convertSumkAttachments(attachments));
        }
        return invoker.invoke(invocation);
    }

    private Map<String, String> convertSumkAttachments(Map<String, ?> attachments) {
        Map<String, String> result = Maps.newHashMap();
        if(CollectionUtil.isEmpty(attachments)) {
            return result;
        }

        attachments.forEach((k, v) -> {
            if (k.contains(SUMK_TRACE_PREFIX)) {
                result.put(k.replace(SUMK_TRACE_PREFIX, StringUtils.EMPTY_STRING), (String) v);
            } else {
                result.put(SUMK_TRACE_PREFIX + k, (String) v);
            }
        });
        return result;
    }
}