package com.vandunxg.trackee.users.application.adapter;

public interface VerificationCodeAdapter {

    void verifyVerificationToken(String token, String userId);
}
