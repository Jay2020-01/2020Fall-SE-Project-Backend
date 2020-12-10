package com.backend.gateway.filter;

import com.alibaba.fastjson.JSON;
import com.backend.gateway.feign.AuthService;
import com.backend.gateway.properties.IgnoreUrlsProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class AuthorizeGatewayFilter implements GlobalFilter, Ordered {

    @Autowired
    private AuthService authService;

    @Autowired
    private IgnoreUrlsProperties ignoreUrlsProperties;

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        ServerHttpRequest request = exchange.getRequest();
        String token = request.getHeaders().getFirst("Authorization");
        String path = request.getPath().value();
        Map<String, Object> map = new HashMap<>();
        System.out.println("请求后端路径：" + path);
        try {
            if (isIgnoreHttpUrls(path)) {
                return chain.filter(exchange);
            }
            if (token != null) {
                int code = authService.checkToken(token);
                if (code == 200) {// 校验成功
                    return chain.filter(exchange);
                } else if (code == 201) {
                    System.out.println("token已过期！");
                    map.put("msg", "token已过期！");
                    return unauthorizedResponse(exchange, map);
                } else if (code == 202) {
                    System.out.println("token认证失败！");
                    map.put("msg", "token认证失败！");
                    return unauthorizedResponse(exchange, map);
                }
                else {
                    System.out.println("认证接口调用异常！");
                    map.put("msg", "认证接口调用异常！");
                    return unauthorizedResponse(exchange, map);
                }
            } else {
                System.out.println("请确保认证信息不为空！");
                map.put("msg", "请确保认证信息不为空！");
                return unauthorizedResponse(exchange, map);
            }
        } catch (Exception e) {
            System.out.println("网关异常：" + e);
            map.put("msg", "网关异常：" + e);
            return unauthorizedResponse(exchange, map);
        }
    }

    /**
     * 过滤网关
     */
    private boolean isIgnoreHttpUrls(String servletPath) {
        boolean rt = false;
        List<String> ignoreHttpUrls = ignoreUrlsProperties.getHttpUrls();
        for (String ihu : ignoreHttpUrls) {
            if (servletPath.contains(ihu)) {
                System.out.println("网关过滤:"+servletPath);
                rt = true;
                break;
            }
        }
        return rt;
    }

    /**
     * 认证失败时，返回json数据
     */
    private Mono<Void> unauthorizedResponse(ServerWebExchange exchange, Map<String,Object> ajaxResult){
        ServerHttpResponse originalResponse = exchange.getResponse();
        originalResponse.setStatusCode(HttpStatus.UNAUTHORIZED);
        originalResponse.getHeaders().add("Content-Type", "application/json;charset=UTF-8");
        byte[] response = null;
        try{
            response = JSON.toJSONString(ajaxResult).getBytes("UTF-8");
        }catch (UnsupportedEncodingException e){
            e.printStackTrace();
        }
        DataBuffer buffer = originalResponse.bufferFactory().wrap(response);
        return originalResponse.writeWith(Flux.just(buffer));
    }

    @Override
    public int getOrder() {
        return -100;
    }

}
