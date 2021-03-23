package com.ml.api;

import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.web.reactive.function.server.RequestPredicates.GET;
import static org.springframework.web.reactive.function.server.RequestPredicates.POST;
import static org.springframework.web.reactive.function.server.RequestPredicates.accept;
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
		return route(POST(Constants.ROUTE_VERIFY_IS_MUTANT).and(accept(APPLICATION_JSON)), handler::verifyHumanIsMutant)
				.andRoute(GET(Constants.ROUTE_GET_CHECKUP_STAT).and(accept(APPLICATION_JSON)), handler::getCheckUpStat)
				.andRoute(GET(Constants.ROUTE_HEALTH), handler::health);
	}
}
