package com.lfg.poc.opentelemetry.itemservice;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.time.Duration;

import org.hamcrest.core.Every;
import org.junit.Before;
import org.junit.jupiter.api.Test;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.mock.mockito.MockBean;
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

import com.lfg.poc.opentelemetry.dao.model.Item;
import com.lfg.poc.opentelemetry.dao.model.Review;
import com.lfg.poc.opentelemetry.itemservice.dao.ReviewClient;

@AutoConfigureMockMvc
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@ContextConfiguration(classes = { Application.class }, initializers = { IntegrationTest.Initializer.class })
@ActiveProfiles(profiles = "test")
@TestPropertySource("/application-test.yml")
@Testcontainers
public class IntegrationTest {

	@Autowired
	private MockMvc mockMvc;
	
	@MockBean
	private ReviewClient reviewClient;
	
	@Autowired
	private ElasticsearchOperations elasticsearchOperations;
	
	@Before
    public void indexCheck() {
    	assertTrue("Index exists", elasticsearchOperations.indexOps(Item.class).exists());
    	when(this.reviewClient.getById(anyString())).thenReturn(new Review());
    }
    
    @Test
	public void find() throws Exception {
		mockMvc
		.perform(get("/items/1").accept("application/json"))
		.andExpect(status().is(200))
		.andExpect(jsonPath("$.id").exists())
		.andExpect(jsonPath("$.name").exists())
		.andExpect(jsonPath("$.price").exists());
	}
	
	@Test
	public void search() throws Exception {
		mockMvc
		.perform(get("/items/search/findByName?name=Nissan").accept("application/json"))
		.andExpect(status().is(200))
		.andExpect(jsonPath("$[*].id").exists())
		.andExpect(jsonPath("$[*].price").exists())
		.andExpect(jsonPath("$[*].name", Every.everyItem(is("Nissan"))));
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
			
			elasticsearchDumpLoader.dump(esHttpHostAddress, "item", "item-mapping.json", "item-data-lite.json");
			
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