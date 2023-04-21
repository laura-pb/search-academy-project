package co.empathy.academy.search.configuration;

import co.empathy.academy.search.services.ElasticService;
import co.empathy.academy.search.services.ElasticServiceImpl;
import co.empathy.academy.search.services.FavoriteService;
import co.empathy.academy.search.services.FavoriteServiceImpl;
import co.empathy.academy.search.services.elastic.ElasticRequest;
import co.empathy.academy.search.services.elastic.ElasticRequestImpl;
import co.empathy.academy.search.services.tmdb.TMDBRequest;
import co.empathy.academy.search.services.tmdb.TMDBRequestImpl;
import org.jobrunr.jobs.mappers.JobMapper;
import org.jobrunr.storage.InMemoryStorageProvider;
import org.jobrunr.storage.StorageProvider;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;

@Configuration
@EnableAsync
public class MainConfig {

    @Bean
    public ElasticRequest elasticRequest() {
        return new ElasticRequestImpl();
    }

    @Bean
    public ElasticService elasticService(ElasticRequest elasticRequest) {
        return new ElasticServiceImpl(elasticRequest);
    }

    @Bean
    public TMDBRequest tmdbRequest() { return  new TMDBRequestImpl();
    }

    @Bean
    public FavoriteService favoriteService(TMDBRequest tmdbRequest) {
        return new FavoriteServiceImpl(tmdbRequest);
    }

    // JobRunr in-memory data store.
    @Bean
    public StorageProvider storageProvider(JobMapper jobMapper) {
        InMemoryStorageProvider storageProvider = new InMemoryStorageProvider();
        storageProvider.setJobMapper(jobMapper);
        return storageProvider;
    }
}
