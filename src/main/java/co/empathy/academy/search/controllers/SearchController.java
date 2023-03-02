package co.empathy.academy.search.controllers;

import co.empathy.academy.search.services.ElasticServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@RestController
public class SearchController {
    @Autowired
    private ElasticServiceImpl el;

    @GetMapping("/greet/{name}")
    public String greet(@PathVariable String name) {
        return "Hello " + name;
    }

    @GetMapping("/search")
    public Map search(@RequestParam String query) throws IOException {
        Map<String, Object> map = new HashMap<String, Object>();

        map.put("query", el.search(query));
        map.put("clusterName", el.getClusterName());

        return map;
    }
}
