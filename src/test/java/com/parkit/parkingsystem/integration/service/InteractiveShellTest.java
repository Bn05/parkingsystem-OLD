package com.parkit.parkingsystem.integration.service;

import com.parkit.parkingsystem.service.InteractiveShell;
import com.parkit.parkingsystem.service.ParkingService;
import com.parkit.parkingsystem.util.InputReaderUtil;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
class InteractiveShellTest {

    @Mock
    private  InputReaderUtil inputReaderUtil;

    @Mock
    private  ParkingService parkingService;

    @Test
    public  void loadInterfaceIncomingVehiculeTest() {

    }
    // TODO :  Test a faire !

}