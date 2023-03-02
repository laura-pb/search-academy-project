package co.empathy.academy.search.configuration;

import co.empathy.academy.search.services.ElasticService;
import co.empathy.academy.search.services.ElasticServiceImpl;
import co.empathy.academy.search.services.elastic.ElasticRequest;
import co.empathy.academy.search.services.elastic.ElasticRequestImpl;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ElasticConfig {

    @Bean
    public ElasticRequest elasticRequest() {
        return new ElasticRequestImpl();
    }

    @Bean
    public ElasticService elasticService(ElasticRequest elasticRequest) {
        return new ElasticServiceImpl(elasticRequest);
    }
}
