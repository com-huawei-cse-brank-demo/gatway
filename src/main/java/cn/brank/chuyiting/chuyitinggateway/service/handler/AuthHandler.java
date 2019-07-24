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

package cn.brank.chuyiting.chuyitinggateway.service.handler;

import cn.brank.chuyiting.chuyitinggateway.service.EdgeConst;
import org.apache.servicecomb.core.Handler;
import org.apache.servicecomb.core.Invocation;
import org.apache.servicecomb.core.definition.MicroserviceMeta;
import org.apache.servicecomb.provider.pojo.Invoker;
import org.apache.servicecomb.provider.springmvc.reference.RestTemplateBuilder;
import org.apache.servicecomb.swagger.invocation.AsyncResponse;
import org.apache.servicecomb.swagger.invocation.InvocationType;
import org.apache.servicecomb.swagger.invocation.exception.InvocationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.client.RestTemplate;

import javax.ws.rs.core.Response.Status;

public class AuthHandler implements Handler {
  private static Logger LOGGER = LoggerFactory.getLogger(AuthHandler.class);

  private static Auth auth;

  static {
    auth = Invoker.createProxy("auth", "auth", Auth.class);
  }

  @Override
  public void init(MicroserviceMeta microserviceMeta, InvocationType invocationType) {
  }

  @Override
  public void handle(Invocation invocation, AsyncResponse asyncResp) throws Exception {
    System.out.println("enter handle");
    if (invocation.getHandlerContext().get(EdgeConst.ENCRYPT_CONTEXT) != null) {
      invocation.next(asyncResp);
      return;
    }
    if(invocation.getOperationMeta().getMethod().getName().equals("auth")){
      invocation.next(asyncResp);
      return;
    }
    System.out.println(invocation.getRequestEx().getRequestURI());
    if (invocation.getRequestEx().getRequestURI().substring(1,5).equals("auth")){
      invocation.next(asyncResp);
      return;
    }
    String jwt = invocation.getRequestEx().getHeader("Authorization");
    jwt = jwt==null?invocation.getRequestEx().getParameter("Authorization"):jwt;

    auth.auth(jwt).whenComplete((succ, e) -> doHandle(invocation, asyncResp, succ, e));
  }

  protected void doHandle(Invocation invocation, AsyncResponse asyncResp, Boolean authSucc, Throwable authException) {
    System.out.println("enter doHandle");
    if (authException != null) {
      asyncResp.consumerFail(new InvocationException(Status.UNAUTHORIZED, (Object) authException.getMessage()));
      throw new InvocationException(Status.UNAUTHORIZED,"auth failed");
    }

    if (!authSucc) {
      asyncResp.consumerFail(new InvocationException(Status.UNAUTHORIZED, (Object) "auth failed"));
      throw new InvocationException(Status.UNAUTHORIZED,"auth failed");
    }

    LOGGER.debug("auth success.");
    try {
      invocation.next(asyncResp);
    } catch (Throwable e) {
      asyncResp.consumerFail(e);
    }
  }
}
