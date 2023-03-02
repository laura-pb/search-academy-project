package co.empathy.academy.search.services;

import co.empathy.academy.search.services.elastic.ElasticRequest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ElasticServiceTest {

    @Test
    void givenExistingCluster_whenGetClusterName_thenReturnClusterName() throws IOException {
        String expectedClusterName = "cluster-name";

        //Mock for the ElasticRequest
        ElasticRequest client = mock(ElasticRequest.class);

        //We mock call to ElasticRequest getClusterName
        given(client.getClusterName()).willReturn(expectedClusterName);

        //We create instance of ElasticService with the mock and execute method
        ElasticService elasticService = new ElasticServiceImpl(client);
        String clusterName = elasticService.getClusterName();

        //Assert cluster name was correct and mocked client method was called
        assertEquals(expectedClusterName, clusterName);
        verify(client).getClusterName();
    }

    @Test
    void givenQuery_whenSearch_thenReturnResult() {
        String query = "example";
        String expected_result = "result";

        //Mock for the ElasticRequest
        ElasticRequest client = mock(ElasticRequest.class);

        //We mock the call to executeQuery
        given(client.executeQuery(query)).willReturn(expected_result);

        //We create instance of ElasticService with the mock
        ElasticService elasticService = new ElasticServiceImpl(client);
        String result = elasticService.search(query);

        //Result assertion
        assertEquals(expected_result, result);
        //Assert executeQuery call has been made
        verify(client).executeQuery(query);
    }

    @Test
    void givenQuery_whenErrorDuringSearch_thenPropagateException() {
        String query = "example";

        //Mock for the ElasticRequest
        ElasticRequest client = mock(ElasticRequest.class);

        //We simulate that ElasticRequest throws an exception
        given(client.executeQuery(query)).willThrow(RuntimeException.class);

        //We create instance of ElasticService with the mock
        ElasticService elasticService = new ElasticServiceImpl(client);

        assertThrows(RuntimeException.class, () -> elasticService.search(query));
    }

    @Test
    void givenBlankQuery_whenSearch_thenDoNotExecuteQueryAndReturnEmptyString() {
        //Mock for the ElasticRequest
        ElasticRequest client = mock(ElasticRequest.class);
        ElasticService elasticService = new ElasticServiceImpl(client);

        //Execute search with empty query
        String query = "   ";
        String result = elasticService.search(query);

        //Assert that result obtained is empty string
        assertTrue(result.isEmpty());
        //Assert no call to the client was made
        verifyNoInteractions(client);
    }
}
