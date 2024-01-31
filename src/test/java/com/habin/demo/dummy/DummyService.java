package com.habin.demo.dummy;


import com.habin.demo.account.application.port.input.usecase.RegisterUseCase;
import com.habin.demo.base.AbstractIntegrationTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class DummyService extends AbstractIntegrationTest {

    @Autowired
    private RegisterUseCase registerUseCase;

}
