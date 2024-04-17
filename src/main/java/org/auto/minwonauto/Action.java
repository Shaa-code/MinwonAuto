package org.auto.minwonauto;

@FunctionalInterface
interface Action {
    void execute() throws Exception;
}