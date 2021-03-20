package com.ml.api;

import static org.springframework.web.reactive.function.server.RequestPredicates.GET;
import static org.springframework.web.reactive.function.server.RequestPredicates.POST;
import static org.springframework.web.reactive.function.server.RouterFunctions.route;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerResponse;

import com.ml.model.util.Constants;

@Configuration
public class Router {

	@Bean
	public RouterFunction<ServerResponse> routerFunction(Handler handler) {
		return route(POST(Constants.ROUTE_VERIFY_IS_MUTANT), handler::verifyHumanIsMutant)
				.andRoute(GET(Constants.ROUTE_GET_STATS), handler::getStats);
	}
}
