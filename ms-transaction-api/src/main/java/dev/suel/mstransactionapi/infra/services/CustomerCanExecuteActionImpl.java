package dev.suel.mstransactionapi.infra.services;

import dev.suel.mstransactionapi.application.gateway.CustomerCanExecuteActionPort;
import dev.suel.mstransactionapi.infra.external.ICustomerClient;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@RequiredArgsConstructor
@Service("CustomerCanExecuteService")
public class CustomerCanExecuteActionImpl implements CustomerCanExecuteActionPort {

    private final ICustomerClient customerClient;

    @Override
    public boolean isAllowed() {
        try {
            return customerClient.checkCustomer().status() == 200;
        } catch (Exception e) {
            return false;
        }
    }
}
