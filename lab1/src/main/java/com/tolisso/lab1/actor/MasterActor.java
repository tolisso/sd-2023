package com.tolisso.lab1.actor;

import akka.actor.ActorRef;
import akka.actor.Props;
import akka.actor.UntypedActor;
import com.tolisso.lab1.dto.RequestDto;
import com.tolisso.lab1.dto.Result;
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
import java.util.concurrent.*;
import java.util.stream.Collectors;

@RequiredArgsConstructor
public class MasterActor extends UntypedActor {

    private final List<Api> apis;
    private final Result<List<RequestDto>> result;

    private List<ActorRef> actors;

    private List<RequestDto> resultRequestDtos = new ArrayList<>();
    private int actorsDone = 0;

    @Override
    public void postStop() {
        System.out.println(self().path() + " was stopped");
        actors.forEach(actor -> getContext().stop(actor));
    }

    @Override
    public void onReceive(Object message) {
        if (message.equals("get")) {
            actors = new ArrayList<>();
            for (int i = 0; i < apis.size(); i++) {
                String name = "child" + i;
                System.out.println("Create child: " + name);
                var actor = getContext().actorOf(Props.create(ChildActor.class), name);
                actor.tell(apis.get(i), getSelf());
                actors.add(actor);
            }
        } else if (message instanceof List) {
            val topRequestsFromApi = (List<RequestDto>) message;
            actorsDone++;
            resultRequestDtos.addAll(topRequestsFromApi);
            if (actorsDone == apis.size()) {
                result.set(getTopRequests(resultRequestDtos));
            }
        }
    }

    private List<RequestDto> getTopRequests(List<RequestDto> rawResults) {
        Map<String, Integer> requestToNumberOfSearches = rawResults.stream()
                .collect(Collectors.toMap(RequestDto::getRequest, RequestDto::getNumOfSearches, Integer::sum));

        return requestToNumberOfSearches.entrySet().stream()
                .sorted(Map.Entry.<String, Integer>comparingByValue().reversed())
                .limit(5)
                .map(entry -> new RequestDto(entry.getKey(), entry.getValue()))
                .collect(Collectors.toList());
    }

}
