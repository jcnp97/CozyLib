package net.cozyvanilla.cozylib.common.interfaces;

import net.cozyvanilla.cozylib.integrations.economy.ExcellentEconomyDeposit;
import net.cozyvanilla.cozylib.integrations.economy.ExcellentEconomyGet;
import net.cozyvanilla.cozylib.integrations.economy.ExcellentEconomyWithdraw;

public interface ExcellentEconomyAPI {
    ExcellentEconomyGet get();
    ExcellentEconomyDeposit deposit();
    ExcellentEconomyWithdraw withdraw();
}