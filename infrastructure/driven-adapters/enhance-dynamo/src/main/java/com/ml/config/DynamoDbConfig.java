package com.ml.config;

import java.net.URI;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

import lombok.Setter;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbEnhancedAsyncClient;
import software.amazon.awssdk.services.dynamodb.DynamoDbAsyncClient;
import software.amazon.awssdk.services.dynamodb.DynamoDbAsyncClientBuilder;

@Setter
@Configuration
public class DynamoDbConfig {

	@Value("${dynamodb.endpoint:}")
	private String dynamoDbEndPointUrl;

	@Bean
	public DynamoDbAsyncClient getDynamoDbAsyncClient() {
		DynamoDbAsyncClientBuilder builder = DynamoDbAsyncClient.builder();
		if (dynamoDbEndPointUrl != null && !dynamoDbEndPointUrl.isEmpty()) {
			builder.endpointOverride(URI.create(dynamoDbEndPointUrl));
		}
		return builder.build();
	}

	@Bean
	@Primary
	public DynamoDbEnhancedAsyncClient getDynamoDbEnhancedAsyncClient() {
		return DynamoDbEnhancedAsyncClient.builder().dynamoDbClient(getDynamoDbAsyncClient()).build();
	}

}
