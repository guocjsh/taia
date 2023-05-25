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
package io.github.guocjsh.excel.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.lang.reflect.Field;

/**
 * @Author iron.guo
 * @Date 2022/5/22
 * @Description
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true)
@Builder
public class ExcelPropsEntity {
    /**
     * excelModel字段Field
     */
    private Field fieldEntity;

    /**
     * excel列名称
     */
    private String value;

    /**
     * 日期格式 默认 yyyy-MM-dd HH:mm:ss
     *
     * @return
     */
    private String dateFormat;

    /**
     * BigDecimal精度 默认:-1(默认不开启BigDecimal格式化)
     *
     * @return
     */
    private Integer scale;

    /**
     * 正则表达式校验
     *
     * @return
     */
    private String regex;

    /**
     * 正则表达式校验失败返回的错误信息,regex配置后生效
     *
     * @return
     */
    private String regexMessage;


}
