/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.combinedculture.exception;

/**
 *
 * @author Windows
 */
//401 Chưa đăng nhập hoặc token không hợp lệ
public class UnAuthorizeException extends RuntimeException{
    public UnAuthorizeException(String message){
        super(message);
    }
}
