package com.neoutils.nil.core.contract

abstract class Request private  constructor(){
    abstract class Async: Request()
    abstract class Sync: Request()

    companion object
}