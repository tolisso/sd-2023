package com.tolisso.lab1.feign;

import com.tolisso.lab1.dto.RequestDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@FeignClient(value = "api3", url = "${api3}")
public interface Api3 extends Api {

    @GetMapping("/api3")
    List<RequestDto> mkRequest();
}
