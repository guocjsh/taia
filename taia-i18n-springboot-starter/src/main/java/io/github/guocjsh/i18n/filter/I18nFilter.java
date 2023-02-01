/**
 * Copyright (c) 2021-2028, iron.guo 郭成杰 (jiedreams@sina.com).
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
package io.github.guocjsh.i18n.filter;

import io.github.guocjsh.i18n.context.I18nContextHandler;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

/**
 * @Author iron.guo
 * @Date 2023/1/31
 * @Description
 */
public class I18nFilter implements Filter {

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        Filter.super.init(filterConfig);
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        doI18nFilter((HttpServletRequest)request);
        chain.doFilter(request, response);
    }

    private void doI18nFilter(HttpServletRequest request){
        String lang = request.getHeader("lang");
        I18nContextHandler.setLang(lang);
    }

    @Override
    public void destroy() {
        Filter.super.destroy();
    }
}
