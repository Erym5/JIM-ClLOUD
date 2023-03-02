package cn.tojintao.cloud.filter;//package cn.tojintao.cloud.filter;
//

import cn.tojintao.cloud.constant.Constant;
import org.casbin.jcasbin.main.Enforcer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import static cn.tojintao.cloud.constant.Constant.CASBIN_USER_PREFIX;

@Component
public class CustomAuthFilter implements GlobalFilter, Ordered {
    @Autowired
    private Enforcer enforcer;
    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        String userId = exchange.getRequest().getHeaders().getFirst(Constant.CustomRequestHeaders.X_USER_ID);
        String requestURI = exchange.getRequest().getURI().toString();
        String method = exchange.getRequest().getMethodValue();
        String friendId = exchange.getRequest().getHeaders().getFirst("friendId");
        enforcer.loadPolicy();
        boolean enforce = enforcer.enforce(CASBIN_USER_PREFIX + userId, requestURI, method);
        if (enforce) {
            return chain.filter(exchange);
        }
        return null;
    }

    @Override
    public int getOrder() {
        return 1;
    }
}
//