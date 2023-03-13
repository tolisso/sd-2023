package com.tolisso.lab1.actor;

import akka.actor.UntypedActor;
import com.tolisso.lab1.actor.exception.EscalateException;
import com.tolisso.lab1.dto.RequestDto;
import com.tolisso.lab1.feign.Api;
import scala.Option;

import java.util.List;

public class ChildActor extends UntypedActor {

    @Override
    public void postStop() {
        System.out.println(self().path() + " was stopped");
    }

    @Override
    public void postRestart(Throwable cause) {
        System.out.println(self().path() + " was restarted after: " + cause);
    }

    @Override
    public void preRestart(Throwable cause, Option<Object> message) {
        System.out.println(self().path() + " is dying because of " + cause);
    }

    @Override
    public void onReceive(Object message) {
        if (message instanceof Api api) {
            final List<RequestDto> result;
            try {
                result = api.mkRequest();
            } catch (RuntimeException exc) {
                throw new EscalateException(exc);
            }
            getContext().parent().tell(result, getSelf());
        }
    }
}
