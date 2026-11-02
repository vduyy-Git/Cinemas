/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.combinedculture.exception;

/**
 *
 * @author Windows
 */
//404 Không tìm thấy tài nguyên/không tồn tại
public class NotFoundException extends RuntimeException{
    public NotFoundException(String message){
        super(message);
    }
}
