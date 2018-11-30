package com.miracle.distributed.workerid;

import lombok.extern.slf4j.Slf4j;

import org.springframework.web.client.RestTemplate;


/**
 * Unit test for simple App.
 */
@Slf4j
public class AppTest 
{
    public static void main(String[] args)
    {
    	final String fetchWorkerIdUrl = "http://10.0.17.31:8080/api/workerId/order";
    	final String renewWorkerIdUrl = "http://10.0.17.31:8080/api/workerId/order";
    	RestTemplate restTemplate = new RestTemplate();
    	Integer workerId = restTemplate.getForObject(fetchWorkerIdUrl, Integer.class);
    	log.info("Got distributed workerId {}", workerId);
    	
    	Integer renewedWorkerId = restTemplate.postForObject(renewWorkerIdUrl, workerId, Integer.class);
    	log.info("Renewed distributed workerId {}", renewedWorkerId);
    }
}
