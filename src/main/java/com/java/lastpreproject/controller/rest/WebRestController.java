package com.java.lastpreproject.controller.rest;

import com.java.lastpreproject.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import javax.servlet.http.HttpServletRequest;
import java.util.Collections;
import java.util.List;

@RestController("/")
public class WebRestController {

    private RestTemplate restTemplate;

    @Autowired
    public WebRestController(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    @GetMapping(value = "/get/users")
    public ResponseEntity<List<User>> getUsersList() {
        final String url = "http://91.241.64.178:7081/api/users";
        final String delUrl = "http://91.241.64.178:7081/api/users/3";
        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
        HttpEntity<User> entity = new HttpEntity<>(headers);
        ResponseEntity<List<User>> resp = restTemplate.exchange(url, HttpMethod.GET, entity,
                new ParameterizedTypeReference<>() {});
        String sessId = resp.getHeaders().getFirst("Set-Cookie");
        System.out.println(resp);

        User user = new User(3L, "James", "Brown", (byte) 3);

        headers.set("Cookie", sessId);
        HttpEntity<User> postEntity = new HttpEntity<>(user, headers);
        ResponseEntity<String> postResp = restTemplate.exchange(url, HttpMethod.POST, postEntity, String.class);
        System.out.println(postResp);

        HttpEntity<User> putEntity = new HttpEntity<>(new User(3L, "Thomas", "Shelby", (byte) 3), headers);

        ResponseEntity<String> putResp = restTemplate.exchange(url, HttpMethod.PUT, putEntity, String.class);
        System.out.println(putResp);

        HttpEntity<User> deleteEntity = new HttpEntity<>(headers);

        ResponseEntity<String> deleteResp = restTemplate.exchange(delUrl, HttpMethod.DELETE, deleteEntity, String.class, 3);
        System.out.println(deleteResp);
        return resp;
    }

}