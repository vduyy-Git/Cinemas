/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.combinedculture.exception;

/**
 *
 * @author Windows
 */
//403 Có đăng nhập rồi nhưng không có quyền
public class ForBiddenException extends RuntimeException{
    public ForBiddenException(String message){
        super(message);
    }
}
