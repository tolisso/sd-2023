package com.tolisso.lab1.actor;

import akka.actor.ActorRef;
import akka.actor.Props;
import akka.actor.UntypedActor;
import com.tolisso.lab1.dto.RequestDto;
import com.tolisso.lab1.dto.Result;
import com.tolisso.lab1.feign.Api1;
import com.tolisso.lab1.feign.Api2;
import com.tolisso.lab1.feign.Api3;
import lombok.RequiredArgsConstructor;
import lombok.val;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;

@RequiredArgsConstructor
public class MasterActor extends UntypedActor {

    private final Api1 api1;
    private final Api2 api2;
    private final Api3 api3;
    private final RecursiveAction invokeWhenDone;
    private final Result<List<RequestDto>> result;

    private ActorRef actor1;
    private ActorRef actor2;
    private ActorRef actor3;

    volatile private List<RequestDto> result1;
    volatile private List<RequestDto> result2;
    volatile private List<RequestDto> result3;

    @Override
    public void postStop() {
        System.out.println(self().path() + " was stopped");
        getContext().stop(actor1);
        getContext().stop(actor2);
        getContext().stop(actor3);
    }

    @Override
    public void onReceive(Object message) {
        if (message.equals("get")) {
            String name1 = "child1";
            System.out.println("Create child: " + name1);
            actor1 = getContext().actorOf(Props.create(ChildActor.class), name1);
            actor1.tell(api1, getSelf());

            String name2 = "child2";
            System.out.println("Create child: " + name2);
            actor2 = getContext().actorOf(Props.create(ChildActor.class), name2);
            actor2.tell(api2, getSelf());

            String name3 = "child3";
            System.out.println("Create child: " + name3);
            actor3 = getContext().actorOf(Props.create(ChildActor.class), name3);
            actor3.tell(api3, getSelf());
        } else if (message instanceof List) {
            val res = (List<RequestDto>) message;
            if (getSender().equals(actor1)) {
                result1 = res;
            } else if (getSender().equals(actor2)) {
                result2 = res;
            } else if (getSender().equals(actor3)) {
                result3 = res;
            }
            if (result1 != null && result2 != null && result3 != null) {
                List<RequestDto> joinedRes = new ArrayList<>();
                joinedRes.addAll(result1);
                joinedRes.addAll(result2);
                joinedRes.addAll(result3);
                result.setRes(joinedRes);
                invokeWhenDone.fork();
            }
        }
    }


}
