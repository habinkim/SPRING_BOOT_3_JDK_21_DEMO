package com.habin.demo.account.adapter.input.rest;

import com.habin.demo.base.ControllerBaseTest;
import com.habin.demo.dummy.DummyService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.security.NoSuchAlgorithmException;

class LoginControllerTest extends ControllerBaseTest {

    @Autowired
    private DummyService dummyService;

    @Transactional
    @Test
    @Order(1)
    @DisplayName("로그인, 성공")
    void loginSuccess() throws NoSuchAlgorithmException {
        dummyService.initUser();
    }

}
