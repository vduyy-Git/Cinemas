/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.combinedculture.exception;

/**
 *
 * @author Windows
 */
//500 lỗi hệ thống/server
public class GlobalException extends RuntimeException{
    public GlobalException(String message){
        super(message);
    }
}
