package licenta.applicationserver.services;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

public class RaspberryRESTService {
    public void sendDummyData(Integer value) {
        RestTemplate restTemplate = new RestTemplate();

        String url = "http://192.168.1.130:5000/data";
        String jsonPayload = "{\"value\": " + value + "}";
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<String> requestEntity = new HttpEntity<>(jsonPayload, headers);

        ResponseEntity<String> response = restTemplate.postForEntity(url, requestEntity, String.class);
        System.out.println("Response from RPi5: " + response.getBody());
    }
}
