package com.tolisso.lab1.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.RecursiveAction;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

@NoArgsConstructor
@AllArgsConstructor
public class Result<T> {
    private T res;
    private final DoNothingFuture evaluatedWhenSet = new DoNothingFuture();

    public void set(T res) {
        this.res = res;
        evaluatedWhenSet.fork();
    }

    public T get(long timeout, TimeUnit unit) throws ExecutionException, InterruptedException, TimeoutException {
        evaluatedWhenSet.get(timeout, unit);
        return res;
    }

    private static class DoNothingFuture extends RecursiveAction {

        @Override
        protected void compute() {
        }
    }
}
