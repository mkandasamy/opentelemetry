package com.lfg.poc.opentelemetry.productservice;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;

import com.lfg.poc.opentelemetry.dao.model.Item;
import com.lfg.poc.opentelemetry.dao.model.Review;
import com.lfg.poc.opentelemetry.dao.model.Stock;
import com.lfg.poc.opentelemetry.productservice.dao.ItemClient;
import com.lfg.poc.opentelemetry.productservice.dao.StockClient;

@AutoConfigureMockMvc
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@ContextConfiguration(classes = { Application.class }, initializers = { IntegrationTest.Initializer.class })
@ActiveProfiles(profiles = "test")
@TestPropertySource("/application-test.yml")
public class IntegrationTest {

	@Autowired
	private MockMvc mockMvc;
	
	@MockBean
	private ItemClient itemClient;
	
	@MockBean
	private StockClient stockClient;
	
    @Test
	public void find() throws Exception {
    	
    	Review review = Review.builder().id(1).rating(4).reviews(3).build();
		Item item = Item.builder().id(1).name("Chevy").price(50000.00).review(review).build();
		when(this.itemClient.getById(anyString())).thenReturn(item);
		
		Stock stock = Stock.builder().id(1).availability(100).location("New York").build();
		List<Stock> stocks = new ArrayList<>();
		stocks.add(stock);
    	when(this.stockClient.getByName(anyString())).thenReturn(stocks);
    	
		mockMvc
			.perform(get("/products/1").accept("application/json"))
			.andExpect(status().is(200))
			.andExpect(jsonPath("$.id").exists())
			.andExpect(jsonPath("$.item.id").exists())
			.andExpect(jsonPath("$.item.review.id").exists())
			.andExpect(jsonPath("$.stocks[*].id").exists());
	}
    
    static class Initializer implements ApplicationContextInitializer<ConfigurableApplicationContext> {
		public void initialize(ConfigurableApplicationContext configurableApplicationContext) {
            System.setProperty("LOCALHOST_IP", System.getProperty("LOCALHOST_IP", getPrivateIp()));
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