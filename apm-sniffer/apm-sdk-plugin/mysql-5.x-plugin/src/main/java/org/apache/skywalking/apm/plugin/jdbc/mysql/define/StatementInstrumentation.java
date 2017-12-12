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
 *
 */


package org.apache.skywalking.apm.plugin.jdbc.mysql.define;

import net.bytebuddy.description.method.MethodDescription;
import net.bytebuddy.matcher.ElementMatcher;
import org.apache.skywalking.apm.agent.core.plugin.interceptor.InstanceMethodsInterceptPoint;
import org.apache.skywalking.apm.agent.core.plugin.interceptor.enhance.ClassInstanceMethodsEnhancePluginDefine;
import org.apache.skywalking.apm.agent.core.plugin.interceptor.ConstructorInterceptPoint;
import org.apache.skywalking.apm.agent.core.plugin.match.ClassMatch;

import static net.bytebuddy.matcher.ElementMatchers.named;
import static org.apache.skywalking.apm.plugin.jdbc.mysql.define.MultiClassNameMatch.byMultiClassMatch;

/**
 * {@link StatementInstrumentation} intercepts the following methods in the {@link
 * com.mysql.jdbc.StatementImpl} and {@link com.mysql.cj.jdbc.StatementImpl}class.
 * 1. execute <br/>
 * 2. executeQuery <br/>
 * 3. executeUpdate <br/>
 * 4. executeLargeUpdate <br/>
 * 5. addBatch <br/>
 * 6. executeBatchInternal <br/>
 * 7. executeUpdateInternal <br/>
 * 8. executeQuery <br/>
 * 9. executeBatch <br/>
 *
 * @author zhangxin
 */
public class StatementInstrumentation extends ClassInstanceMethodsEnhancePluginDefine {
    private static final String STATEMENT_CLASS_NAME = "com.mysql.jdbc.StatementImpl";
    private static final String SERVICE_METHOD_INTERCEPTOR = "org.apache.skywalking.apm.plugin.jdbc.mysql.StatementExecuteMethodsInterceptor";
    public static final String MYSQL6_STATEMENT_CLASS_NAME = "com.mysql.cj.jdbc.StatementImpl";

    @Override protected ConstructorInterceptPoint[] getConstructorsInterceptPoints() {
        return new ConstructorInterceptPoint[0];
    }

    @Override protected InstanceMethodsInterceptPoint[] getInstanceMethodsInterceptPoints() {
        return new InstanceMethodsInterceptPoint[] {
            new InstanceMethodsInterceptPoint() {
                @Override public ElementMatcher<MethodDescription> getMethodsMatcher() {
                    return named("execute")
                        .or(named("executeQuery"))
                        .or(named("executeUpdate"))
                        .or(named("executeLargeUpdate"))
                        .or(named("addBatch"))
                        .or(named("executeBatchInternal"))
                        .or(named("executeUpdateInternal"))
                        .or(named("executeQuery"))
                        .or(named("executeBatch"));
                }

                @Override public String getMethodsInterceptor() {
                    return SERVICE_METHOD_INTERCEPTOR;
                }

                @Override public boolean isOverrideArgs() {
                    return false;
                }
            }
        };
    }

    @Override protected ClassMatch enhanceClass() {
        return byMultiClassMatch(STATEMENT_CLASS_NAME, MYSQL6_STATEMENT_CLASS_NAME);
    }
}
