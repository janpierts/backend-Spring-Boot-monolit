package com.rj.esqueleto.admin.crud.application.dto;

import com.rj.esqueleto.common.utils.helperEndpoints;

public record InsertUpdate_Crud_Model(Long id, String name, String email) {
    public void validate(){
        String mssg="";
        if(name == null || name.isBlank()) throw new IllegalArgumentException("Name is required.");
        if(name.length()>255 || !helperEndpoints.isAlphabeticWithSpaces(name)) {
            if(name.length()>255) mssg+="Name is too long(max character required: '255').";
            if(!helperEndpoints.isAlphabeticWithSpaces(name)) mssg+=" | Name required(only Alphabetic with spaces)";
            throw new IllegalArgumentException(mssg);
        }
        if(email == null || email.isBlank()) throw new IllegalArgumentException("Email is required.");
        if(email.length()>255 || !helperEndpoints.isValidEmail(email)){
            if(email.length()>255) mssg+="Email is too long(max character required: '255').";
            if(!helperEndpoints.isValidEmail(email)) mssg+=" | Email reuired(email format).";
            throw new IllegalArgumentException(mssg);
        }
    }
}
