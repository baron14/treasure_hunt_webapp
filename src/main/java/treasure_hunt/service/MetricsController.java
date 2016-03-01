package treasure_hunt.service;


import java.util.ArrayList;

import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import treasure_hunt.service.pojo.Metric;
import treasure_hunt_webapp.dao.MetricDao;

@RestController
public class MetricsController {

	MetricDao dao = new MetricDao();
  
    @RequestMapping(path ="/metrics",method = RequestMethod.POST)
    public String greeting(@RequestBody Metric metric) {
    	dao.saveMetrics(metric);
    	return "hey";
    }
    
    
    @RequestMapping(path ="/metrics")
    public ArrayList<Metric> greeting() {
    	return dao.getMetrics();

    }
}

