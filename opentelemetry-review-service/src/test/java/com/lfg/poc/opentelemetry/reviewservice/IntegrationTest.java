package com.lfg.poc.opentelemetry.reviewservice;
import static org.junit.Assert.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.time.Duration;

import org.junit.Before;
import org.junit.jupiter.api.Test;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.util.TestPropertyValues;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.testcontainers.elasticsearch.ElasticsearchContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import com.lfg.poc.opentelemetry.dao.model.Review;

@AutoConfigureMockMvc
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@ContextConfiguration(classes = { Application.class }, initializers = { IntegrationTest.Initializer.class })
@ActiveProfiles(profiles = "test")
@TestPropertySource("/application-test.yml")
@Testcontainers
public class IntegrationTest {

	@Autowired
	private MockMvc mockMvc;
	
	@Autowired
	private ElasticsearchOperations elasticsearchOperations;
	
    @Before
    public void indexCheck() {
    	assertTrue("Index exists", elasticsearchOperations.indexOps(Review.class).exists());
    }
    
    @Test
	public void find() throws Exception {
		mockMvc
		.perform(get("/reviews/1").accept("application/json"))
		.andExpect(status().is(200))
		.andExpect(jsonPath("$.id").exists())
		.andExpect(jsonPath("$.rating").exists())
		.andExpect(jsonPath("$.reviews").exists());
	}
	
	
	@Container
	public static ElasticsearchContainer elasticsearchContainer = new ElasticsearchContainer(
			"docker.elastic.co/elasticsearch/elasticsearch:7.5.0").withStartupTimeout(Duration.ofSeconds(600));
	
	@Container
	public static ElasticsearchDumpLoader elasticsearchDumpLoader = ElasticsearchDumpLoader.getInstance("data/")
			.withStartupTimeout(Duration.ofSeconds(600));
	
	static class Initializer implements ApplicationContextInitializer<ConfigurableApplicationContext> {
		public void initialize(ConfigurableApplicationContext configurableApplicationContext) {
			
			System.setProperty("LOCALHOST_IP", System.getProperty("LOCALHOST_IP", getPrivateIp()));
			
			String esHttpHostAddress = elasticsearchContainer.getHttpHostAddress().replace("localhost", getPrivateIp());
			LoggerFactory.getLogger("Initializer.initialize")
				.info("elasticsearchContainer.getHttpHostAddress: " + elasticsearchContainer.getHttpHostAddress());
			
			elasticsearchDumpLoader.dump(esHttpHostAddress, "review", "review-mapping.json", "review-data-lite.json");
			
			TestPropertyValues
				.of("lfg.opentelemetry.service.elastic-search-url=" + esHttpHostAddress)
				.applyTo(configurableApplicationContext.getEnvironment());
		}
	}
	
	private static String getPrivateIp() {
		try {
			return InetAddress.getLocalHost().getHostAddress().trim();
		} catch (UnknownHostException e) {
			return "localhost";
		}
	}

}