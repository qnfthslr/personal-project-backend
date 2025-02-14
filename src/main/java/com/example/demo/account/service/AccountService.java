package com.example.demo.account.service;

import com.example.demo.account.controller.form.request.*;
import com.example.demo.account.controller.form.response.AccountLoginResponseForm;
import com.example.demo.account.controller.form.response.AccountPasswordResponseForm;
import com.example.demo.account.entity.Account;

import java.util.List;

public interface AccountService {
    Account regist(Account account);

    AccountLoginResponseForm login(AccountLoginRequestForm requestForm);

    Boolean checkEmail(String email);

    List<Account> list();

    Boolean delete(AccountUserTokenRequestForm accountUserTokenRequestForm);

    Account modify(AccountModifyRequestForm requestForm);

    AccountPasswordResponseForm passwordFind(AccountPasswordFindRequestForm requestForm);

    String goMypage(AccountGoMypageForm accountId);

    Account accountInfoList(String accountId);

    String userTypeCheck(AccountUserTokenRequestForm accountUserTokenRequestForm);

    Account checkUserToken(AccountUserTokenRequestForm accountUserTokenRequestForm);
}
