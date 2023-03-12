package com.tolisso.lab1.feign;

import com.tolisso.lab1.dto.RequestDto;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

public interface Api {

    List<RequestDto> mkRequest();
}
