/**
 * Copyright (c) 2015-2021, Michael Yang 杨福海 (fuhai999@gmail.com).
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package io.jboot.components.gateway;

import com.jfinal.handler.Handler;
import com.jfinal.kit.LogKit;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author michael yang (fuhai999@gmail.com)
 * @Date: 2020/3/22
 */
public class JbootGatewayHandler extends Handler {

    @Override
    public void handle(String target, HttpServletRequest request, HttpServletResponse response, boolean[] isHandled) {
        JbootGatewayConfig config = JbootGatewayManager.me().matchingConfig(request);
        if (config == null) {
            next.handle(target, request, response, isHandled);
            return;
        }

        try {
            new GatewayInvocation(config, request, response).invoke();
        } finally {
            isHandled[0] = true;
            flushBuffer(response);
        }
    }

    private static void flushBuffer(HttpServletResponse resp) {
        try {
            resp.flushBuffer();
        } catch (IOException e) {
            LogKit.error(e.toString(), e);
        }
    }
}
