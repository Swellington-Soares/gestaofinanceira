package dev.suel.mstransactionapi.dto;

import java.math.BigDecimal;

public record ExpenseByMonth(
         int year,
         int month,
         BigDecimal totalAmount)
{
}
