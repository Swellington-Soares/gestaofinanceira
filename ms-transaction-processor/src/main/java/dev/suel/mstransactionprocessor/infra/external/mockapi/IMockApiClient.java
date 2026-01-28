package dev.suel.mstransactionprocessor.infra.external.mockapi;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@FeignClient(
        name = "mock-api",
        url = "${services.external.mock-api}"
)
public interface IMockApiClient {

    @GetMapping("/balances")
    List<UserBalanceInfo> getAll();

    @PostMapping("/balances")
    UserBalanceInfo create(@RequestBody UserBalanceInfoCreate infoCreate);

    @GetMapping("/balances/{balanceId}")
    UserBalanceInfo read(@PathVariable String balanceId);

    @PutMapping("/balances/{balanceId}")
    UserBalanceInfo update(@PathVariable String balanceId, @RequestBody UserBalanceUpdate infoUpdate);

    @DeleteMapping("/balances/{balanceId}")
    UserBalanceInfo delete(@PathVariable String balanceId);

    @GetMapping("/balances?userId={userid}")
    List<UserBalanceInfo> getByUserId(@PathVariable Long userid);

}
