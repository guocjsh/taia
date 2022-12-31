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
package io.github.guocjsh.cloud.alibaba.sentinel.rule;

import com.alibaba.csp.sentinel.datasource.ReadableDataSource;
import com.alibaba.csp.sentinel.datasource.nacos.NacosDataSource;
import com.alibaba.csp.sentinel.slots.block.flow.FlowRule;
import com.alibaba.csp.sentinel.slots.block.flow.FlowRuleManager;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import io.github.guocjsh.cloud.alibaba.sentinel.props.SentinelDataSourceProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;

import java.util.List;

/**
 * Sentinel自动持久化
 *
 * <p>
 * 通过控制台设置规则后将规则推送到统一的规则中心，<br>
 * 通过插件实现Sentinel自动持久化
 *
 * @author iron.guo
 *
 */
public class LoadNamespaceRules implements ApplicationRunner {

    @Autowired
    private SentinelDataSourceProperties properties;

    @Override
    public void run(ApplicationArguments args) throws Exception {
        ReadableDataSource<String, List<FlowRule>> flowRuleDataSource = new NacosDataSource<>(this.properties.getServerAddr(), this.properties.getGroupId(), this.properties.getDataId(),
                source -> JSON.parseObject(source, new TypeReference<List<FlowRule>>() {
                }));
        FlowRuleManager.register2Property(flowRuleDataSource.getProperty());
    }
}
