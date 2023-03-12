package com.tolisso.lab1.feign;

import com.tolisso.lab1.dto.RequestDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@FeignClient(value = "api2", url = "${api2}")
public interface Api2 extends Api {

    @GetMapping("/api2")
    List<RequestDto> mkRequest();
}
