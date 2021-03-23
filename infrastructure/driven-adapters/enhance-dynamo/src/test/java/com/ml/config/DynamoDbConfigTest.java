package com.ml.config;

import static org.junit.Assert.assertNotNull;

import org.junit.Test;

public class DynamoDbConfigTest {

	@Test
	public void getDynamoDbEnhancedAsyncClientShouldReturnNotNull() {
		DynamoDbConfig config = new DynamoDbConfig();
		config.setDynamoDbEndPointUrl("http://localhost:8000");
		assertNotNull(config.getDynamoDbEnhancedAsyncClient());
	}

}
