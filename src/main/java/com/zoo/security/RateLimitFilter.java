package com.zoo.security;

import io.github.bucket4j.Bandwidth;
import io.github.bucket4j.Bucket;
import io.github.bucket4j.Refill;
import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.graphql.GraphQlProperties;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.time.Duration;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
@Order(5) // SecurityFilterChain'den sonra ama controller'dan önce
public class RateLimitFilter implements Filter {

    @Value("${ratelimit.animals.capacity:60}")
    private long capacity;

    @Value("${ratelimit.animals.refill:60}")
    private long refill;

    private final Map<String, Bucket> buckets = new ConcurrentHashMap<String, Bucket>();

    private Bucket newBucket() {
        Bandwidth limit = Bandwidth.classic(capacity, Refill.greedy(refill, Duration.ofMinutes(1)));
        return Bucket.builder().addLimit(limit).build();
    }

    private String clientKey(HttpServletRequest request) {
        String xf = request.getHeader("X-Forwarded-For");
        if (xf != null && !xf.isEmpty()) {
            return xf.split(",")[0].trim();
        }
        return request.getRemoteAddr();
    }

    private boolean isAnimalsPublicGet(HttpServletRequest req) {
        String uri = req.getRequestURI();
        String method = req.getMethod();
        return "GET".equalsIgnoreCase(method) && (uri.equals("/api/v1/animals") || uri.startsWith("/api/v1/animals/"));
    }

    @Override
    public void doFilter(jakarta.servlet.ServletRequest sreq,
                         jakarta.servlet.ServletResponse sres,
                         FilterChain chain) throws IOException, ServletException {

        HttpServletRequest req = (HttpServletRequest) sreq;
        HttpServletResponse res = (HttpServletResponse) sres;

        if (!isAnimalsPublicGet(req)) {
            chain.doFilter(sreq, sres);
            return;
        }

        String key = clientKey(req);
        Bucket bucket = buckets.computeIfAbsent(key, k -> newBucket());

        if (bucket.tryConsume(1)) {
            long tokens = bucket.getAvailableTokens();
            res.setHeader("X-RateLimit-Remaining", String.valueOf(tokens));
            chain.doFilter(sreq, sres);
        } else {
            // 429 Too Many Requests
            res.setStatus(429);
            res.setContentType("application/json");
            res.setHeader("Retry-After", "60"); // saniye
            String body = "{\"error\":\"too_many_requests\",\"message\":\"Rate limit exceeded. Try again later.\"}";
            res.getWriter().write(body);
        }
    }
}
