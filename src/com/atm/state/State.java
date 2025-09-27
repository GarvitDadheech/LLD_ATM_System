package com.atm.state;

import com.atm.ATMOrchestrator;

public interface State {
    void handle(ATMOrchestrator atm);
}
