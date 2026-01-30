package dev.suel.mstransactionprocessor.infra.service;

import dev.suel.mstransactionprocessor.application.gateway.BalanceServicePort;
import dev.suel.mstransactionprocessor.infra.external.mockapi.IMockApiClient;
import dev.suel.mstransactionprocessor.infra.external.mockapi.UserBalanceInfo;
import dev.suel.mstransactionprocessor.infra.external.mockapi.UserBalanceInfoCreate;
import dev.suel.mstransactionprocessor.infra.external.mockapi.UserBalanceUpdate;
import feign.FeignException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
@RequiredArgsConstructor
public class BalanceServiceImpl implements BalanceServicePort {

    private final IMockApiClient mockApiClient;

    private UserBalanceInfo getOrCreateUserBalanceInfo(Long userId) {
        try {
            return mockApiClient.getByUserId(userId)
                    .stream().findFirst()
                    .orElseGet(() -> mockApiClient.create(new UserBalanceInfoCreate(userId)));
        } catch (FeignException fe) {
            if (fe.status() == 404) {
                return mockApiClient.create(new UserBalanceInfoCreate(userId));
            } else {
                throw fe;
            }
        }
    }




    @Override
    public void updateBalance(Long userId, BigDecimal amount) {
        UserBalanceInfo  userBalanceInfo = getOrCreateUserBalanceInfo(userId);
        mockApiClient.update(userBalanceInfo.id(), new UserBalanceUpdate(amount.toPlainString()));
    }

    @Override
    public BigDecimal getBalance(Long userId) {
        UserBalanceInfo  userBalanceInfo = getOrCreateUserBalanceInfo(userId);
        return new BigDecimal( userBalanceInfo.balance() );
    }
}
