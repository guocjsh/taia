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
package io.github.guocjsh.i18n;

import io.github.guocjsh.i18n.filter.I18nFilter;
import io.github.guocjsh.i18n.props.TaiaI18nProperties;
import io.github.guocjsh.i18n.resolver.TaiaLocaleResolver;
import io.github.guocjsh.i18n.util.I18nUtil;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @Author iron.guo
 * @Date 2023/2/1
 * @Description
 */
@Configuration
public class I18nAutoConfiguration {

    @Bean
    public void soutLogo(){
        String logo="  ___________      .__          \n" +
                "  \\__    ___/____  |__|____     \n" +
                "    |    |  \\__  \\ |  \\__  \\    \n" +
                "    |    |   / __ \\|  |/ __ \\_  \n" +
                "    |____|  (____  /__(____  /  \n" +
                "                 \\/        \\/    \n" +
                " \n" +
                "http://www.isanlife.com (v1.0.1)";
        System.out.println(logo);
    }

    @Bean
    public I18nFilter getFilter(){
        return new I18nFilter();
    }

    @Bean
    public TaiaI18nProperties getProperties(){
        return new TaiaI18nProperties();
    }

    @Bean
    public TaiaLocaleResolver getResolver(){
        return new TaiaLocaleResolver();
    }

    @Bean
    public I18nUtil getUtil(){
        return new I18nUtil(getResolver());
    }


}
