package co.empathy.academy.search.controllers;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

@SpringBootTest
@AutoConfigureMockMvc
public class SearchControllerTest {
    @Autowired
    private MockMvc mvc;

    @Test
    void givenName_whenGreet_thenGreetingWithName() throws Exception {
        mvc.perform(MockMvcRequestBuilders.get("/greet/{name}", "laura"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().string("Hello laura"));
    }

    @Test
    void givenQueryValue_whenSearch_thenReturnQueryValueAndCluster() throws Exception {
        mvc.perform(MockMvcRequestBuilders.get("/search?query=example"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().json(
                        "{'query':'example', 'clusterName':'docker-cluster'}"
                ));
    }
}
