package com.tolisso.lab1.actor;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.Props;
import com.tolisso.lab1.dto.RequestDto;
import com.tolisso.lab1.dto.Result;
import com.tolisso.lab1.exception.ActorException;
import com.tolisso.lab1.feign.Api;
import com.tolisso.lab1.feign.Api1;
import com.tolisso.lab1.feign.Api2;
import com.tolisso.lab1.feign.Api3;
import lombok.RequiredArgsConstructor;
import lombok.val;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.RecursiveAction;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class ActorLauncher {

    private final Api1 api1;
    private final Api2 api2;
    private final Api3 api3;

    public List<RequestDto> getTopRequests() {
        ActorSystem system = ActorSystem.create("MySystem");
        val result = new Result<List<RequestDto>>();
        List<Api> apis = List.of(api1, api2, api3);
        ActorRef parent = system.actorOf(
                Props.create(MasterActor.class, apis, result), "parent");

        parent.tell("get", ActorRef.noSender());

        try {
            return result.get(500, TimeUnit.MILLISECONDS);
        } catch (TimeoutException | ExecutionException | InterruptedException e) {
            throw new ActorException(e);
        } finally {
            system.stop(parent);
        }
    }
}
