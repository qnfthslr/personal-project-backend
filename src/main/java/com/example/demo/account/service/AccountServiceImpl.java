package com.example.demo.account.service;

import com.example.demo.account.controller.form.request.*;
import com.example.demo.account.controller.form.response.AccountLoginResponseForm;
import com.example.demo.account.controller.form.response.AccountPasswordResponseForm;
import com.example.demo.account.entity.Account;
import com.example.demo.account.repository.AccountRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@Slf4j
@RequiredArgsConstructor
public class AccountServiceImpl implements AccountService{
    final AccountRepository accountRepository;

    // 계정 등록 기능
    @Override
    public Account regist(Account account) {
//        Optional<Account> maybeAccount = accountRepository.findByEmail(account.getEmail());
//        if(maybeAccount.isPresent()) {
//            return null;
//        } // 아이디 중복 확인하면서 확인할 것이기 때문에 구지 안해도 됌
        Account savedAccount = accountRepository.save(account);
        System.out.println(savedAccount);
        return savedAccount;
    }

    // 로그인 기능
    @Override
    public AccountLoginResponseForm login(AccountLoginRequestForm requestForm) {
        Account account = requestForm.toAccount();
        Optional<Account> maybeAccount = accountRepository.findByEmail(account.getEmail());

        if(maybeAccount.isEmpty()){
            return new AccountLoginResponseForm(account.getUserToken(),"WRONG_ID");
        }
        Account savedAccount = maybeAccount.get();
        if (savedAccount.getPassword().equals(account.getPassword())) {
            savedAccount.setUserToken(UUID.randomUUID().toString());
            accountRepository.save(savedAccount);
            return new AccountLoginResponseForm(savedAccount.getUserToken(),"SUCCESS_LOGIN");
        }
        return new AccountLoginResponseForm(account.getUserToken(),"WRONG_PW");
    }

    // 계정 리스트 확인 기능
    @Override
    public Boolean checkEmail(String email) {
        Optional<Account> maybeAccount = accountRepository.findByEmail(email);

        if (maybeAccount.isPresent()){
            return false;
        }else{
            return true;
        }

    }

    @Override
    public List<Account> list() {
        return accountRepository.findAll(Sort.by(Sort.Direction.DESC, "accountId"));
    }

    // 계정 삭제 기능
    @Override
    public Boolean delete(AccountUserTokenRequestForm requestForm) {

        Optional<Account> maybeAccount = accountRepository.findByUserToken(requestForm.getUserToken());

        if (maybeAccount.isEmpty()){
            log.info("에러 발생");
            return false;
        }
        Account existAccount = maybeAccount.get();
        accountRepository.deleteByUserToken(requestForm.getUserToken());
        return true;
    }

    // 계정 수정 기능
    @Override
    public Account modify(AccountModifyRequestForm requestForm) {
        Optional<Account> maybeAccount = accountRepository.findByUserToken(requestForm.getUserToken());

        if (maybeAccount.isEmpty()){
            log.info("없는 계정입니다.");
            return null;
        }
        Account account = maybeAccount.get();

        account.setPassword(requestForm.getPassword());
        account.setAccountName(requestForm.getAccountName());
        account.setAccountBirth(requestForm.getAccountBirth());
        account.setAccountPhone(requestForm.getAccountPhone());
        account.setAccountAddress(requestForm.getAccountAddress());

        return accountRepository.save(account);
    }

    // 비밀번호 찾기 기능
    @Override
    public AccountPasswordResponseForm passwordFind(AccountPasswordFindRequestForm requestForm) {
        Optional<Account> maybeAccount = accountRepository.findByEmail(requestForm.getEmail());
        if(maybeAccount.isEmpty()) {
            log.info("없는 계정 입니다.");
            return new AccountPasswordResponseForm("","WRONG_ID");
        }
        if(!maybeAccount.get().getAccountName().equals(requestForm.getAccountName())){
            log.info("이름이 일치하지 않습니다.");
            return new AccountPasswordResponseForm("","WRONG_NAME");
        }
        if(!maybeAccount.get().getAccountBirth().equals(requestForm.getAccountBirth())){
            log.info("생년월일을 잘못 입력 하셨습니다.");
            return new AccountPasswordResponseForm("","WRONG_BIRTH");
        }

        return new AccountPasswordResponseForm(maybeAccount.get().getPassword(),"SUCCESS_FIND_PASSWORD");
    }

    @Override
    public String goMypage(AccountGoMypageForm accountGoMypageForm) {
        Optional<Account> maybeAccount = accountRepository.findByUserToken(accountGoMypageForm.getUserToken());
        if(maybeAccount.isEmpty()){
            log.info("에러 발생");
            return null;
        }
        if(maybeAccount.get().getPassword().equals(accountGoMypageForm.getPassword())){
            return String.valueOf(maybeAccount.get().getAccountId());
        }else {
            log.info("틀린 비밀번호 입니다.");
            return null;
        }
    }

    @Override
    public Account accountInfoList(String accountId) {

        Optional<Account> maybeAccount = accountRepository.findByAccountId(Long.valueOf(accountId));
        if (maybeAccount.isEmpty()){
            log.info("에러 발생");
            return null;
        }

        return maybeAccount.get();
    }

    @Override
    public String userTypeCheck(AccountUserTokenRequestForm accountUserTokenRequestForm) {

        Optional<Account> maybeAccount = accountRepository.findByUserToken(accountUserTokenRequestForm.getUserToken());
        if(maybeAccount.isEmpty()){
            log.info("에러 발생");
            return "Error";
        }
        if(maybeAccount.get().getUserType().equals("Manager")){
            log.info("매니저 입니다.");
            return "Manager";
        } else if (maybeAccount.get().getUserType().equals(("Account"))) {
            log.info("일반 회원 입니다.");
            return "Account";
        }else {
            return "Error";
        }

    }

    // 받아온 usertoken으로 계정 정보 받아오기
    @Override
    public Account checkUserToken(AccountUserTokenRequestForm accountUserTokenRequestForm) {
        System.out.println(accountUserTokenRequestForm.getUserToken());
        Optional<Account> maybeAccount = accountRepository.findByUserToken(accountUserTokenRequestForm.getUserToken());
        if(maybeAccount.isEmpty()){
            log.info("에러 발생");
            return null;
        }
        return maybeAccount.get();
    }
}
