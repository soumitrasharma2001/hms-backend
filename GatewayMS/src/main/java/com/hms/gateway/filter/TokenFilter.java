package com.hms.gateway.filter;



import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.HttpHandler;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;

@Component
public class TokenFilter extends AbstractGatewayFilterFactory<TokenFilter.Config>{
	private static final String SECRET_KEY="uhtf1hPnXtYspX9lS61C25mXAJD/p6f64mwyKUG369DIdtEI/im36jpPlnM4n3r0MtW3uejnIiIAFYrb/kYqMA==";
	public TokenFilter() {
		super(Config.class);
	}
	public static class Config{}
	
	@Override
	public GatewayFilter apply(Config config) {
		return (exchange,chain)->{
			String path=exchange.getRequest().getPath().toString();
			if(path.equals("/user/login")||path.equals("/user/register")) {
				return chain.filter(exchange.mutate().request(r->r.header("X-Secret-Key", "SECRET")).build());
			}
			HttpHeaders header=exchange.getRequest().getHeaders();
			if(!header.containsHeader(HttpHeaders.AUTHORIZATION)) {
				throw new RuntimeException("Authorization header is missing");
			}
			String authHeader=header.getFirst(HttpHeaders.AUTHORIZATION);
			if(authHeader==null||!authHeader.startsWith("Bearer ")) {
				throw new RuntimeException("Authorization header is invalid");
			}
			String token=authHeader.substring(7);
			try {
				Claims claims=Jwts.parser().setSigningKey(SECRET_KEY).parseClaimsJws(token).getBody();
				Long userId = claims.get("id", Long.class);
                String role = claims.get("role", String.class);
                Long profileId = role.equals("ADMIN")?-1L:claims.get("profileId", Long.class);
				exchange=exchange.mutate().request(request -> request.headers(headers -> {
					headers.add("X-Secret-Key", "SECRET");
					headers.add("X-User-Id", userId.toString());
                    headers.add("X-Role", role);
                    headers.add("X-Profile-Id", profileId.toString());
                })).build();
			}catch(Exception e) {
				System.out.println(e.getMessage());
				throw new RuntimeException("Token is invalid");
			}
			return chain.filter(exchange);
		};
	}
}
