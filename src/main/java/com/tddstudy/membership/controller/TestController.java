package com.tddstudy.membership.controller;

import org.hibernate.internal.util.ZonedDateTimeComparator;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.ZonedDateTime;
import org.springframework.http.MediaType;

@RestController
public class TestController{

    @GetMapping(value = "/healthcheck")
    public ResponseEntity healthcheck(@RequestParam("format") String format) {
        return healthcheckService(format);
    }

    private ResponseEntity healthcheckService(String format) {
        final HealthcheckDto healthcheckDto = makeHealthcheckResponse(format);

        if (healthcheckDto.getStatus().equals("400"))
            return ResponseEntity.badRequest().body(null);

        return ResponseEntity.ok().body(healthcheckDto);
    }

    private HealthcheckDto makeHealthcheckResponse(String format) {
        if (format.equals("short"))
            return new HealthcheckDto("OK");

        if (format.equals("full"))
            return new HealthcheckDto("OK", ZonedDateTime.now());

        return new HealthcheckDto("400");
    }

    public static class HealthcheckDto {
        private String status;
        private ZonedDateTime currentTime;

        public HealthcheckDto(String status) {
            this.status = status;
        }

        public HealthcheckDto(String status, ZonedDateTime currentTime) {
            this.status = status;
            this.currentTime = currentTime;
        }

        public String getStatus() {
            return status;
        }

        public ZonedDateTime getCurrentTime() {
            return currentTime;
        }
    }
}


