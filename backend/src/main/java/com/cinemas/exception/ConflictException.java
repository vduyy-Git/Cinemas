/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.combinedculture.exception;

/**
 *
 * @author Windows
 */
//409 xung đột dữ liệu/dl đã tồn tại
public class ConflictException extends RuntimeException{
    public ConflictException(String message){
        super(message);
    }
}
