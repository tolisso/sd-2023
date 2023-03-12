package com.tolisso.lab1.feign;

import com.tolisso.lab1.dto.RequestDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@FeignClient(value = "api1", url = "${api1}")
public interface Api1 extends Api {

    @GetMapping("/api1")
    List<RequestDto> mkRequest();
}
