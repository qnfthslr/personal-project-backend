package com.example.demo.account.controller.form.response;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Getter
@RequiredArgsConstructor
public class AccountLoginResponseForm {

    final private String userToken;
    final private String loginStatus;


}
