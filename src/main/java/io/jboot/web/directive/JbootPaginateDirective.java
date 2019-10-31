/**
 * Copyright (c) 2015-2019, Michael Yang 杨福海 (fuhai999@gmail.com).
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
package io.jboot.web.directive;

import com.jfinal.plugin.activerecord.Page;
import com.jfinal.template.Env;
import com.jfinal.template.io.Writer;
import com.jfinal.template.stat.Scope;
import io.jboot.utils.StrUtil;
import io.jboot.web.controller.JbootControllerContext;
import io.jboot.web.directive.base.PaginateDirectiveBase;

import javax.servlet.http.HttpServletRequest;

public class JbootPaginateDirective extends PaginateDirectiveBase {

    private static final String URL_PAGE_INFO = "page=";
    private static final String URL_QMARK = "?";
    private static final String URL_AMARK = "&";

    @Override
    protected String getUrl(int pageNumber, Env env, Scope scope, Writer writer) {
        HttpServletRequest request = JbootControllerContext.get().getRequest();
        String queryString = request.getQueryString();

        String url = request.getRequestURI();

        if (StrUtil.isNotBlank(queryString)) {
            url = url.concat(URL_QMARK).concat(queryString);
        }

        /**
         * 锚链接
         */
        String anchor = getPara("anchor", scope, "");


        int index = url.indexOf(URL_PAGE_INFO);

        /**
         * 已经有 page=xx 参数了
         */
        if (index != -1) {
            StringBuilder sb = new StringBuilder();
            sb.append(url, 0, index).append(URL_PAGE_INFO).append(pageNumber);
            int idx = url.indexOf(URL_AMARK, index);
            if (idx != -1) {
                sb.append(url.substring(idx));
            }
            return sb.append(anchor).toString();
        }

        /**
         * 没有 page=xx 参数，但是已经有 query 请求
         */
        if (StrUtil.isNotBlank(queryString)) {
            StringBuilder sb = new StringBuilder(url);
            return sb.append(URL_AMARK)
                    .append(URL_PAGE_INFO)
                    .append(pageNumber)
                    .append(anchor)
                    .toString();
        }

        /**
         * 没有 query 请求
         */
        else {
            StringBuilder sb = new StringBuilder(url);
            return sb.append(URL_QMARK)
                    .append(URL_PAGE_INFO)
                    .append(pageNumber)
                    .append(anchor)
                    .toString();
        }

    }


    @Override
    protected Page<?> getPage(Env env, Scope scope, Writer writer) {
        String pageAttr = getPara("pageAttr", scope, "page");
        return JbootControllerContext.get().getAttr(pageAttr);
    }

}