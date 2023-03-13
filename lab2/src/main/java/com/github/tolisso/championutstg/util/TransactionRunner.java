package com.github.tolisso.championutstg.util;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.function.Supplier;

@Component
public class TransactionRunner {
    @Transactional
    public void doInTransaction(Runnable r) {
        r.run();
    }
}
