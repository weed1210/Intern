package dxc.assignment.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

@Configuration
public class RetrofitDefaultConfig {
	@Bean("retrofit")
	public Retrofit retrofit() {
		return new Retrofit.Builder()
				.baseUrl("http://localhost:9090/Application/")
				.addConverterFactory(GsonConverterFactory.create())
				.build();
	}
}
