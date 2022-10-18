package com.parkit.parkingsystem.integration.service;

import com.parkit.parkingsystem.constants.ParkingType;
import com.parkit.parkingsystem.dao.ParkingSpotDAO;
import com.parkit.parkingsystem.dao.TicketDAO;
import com.parkit.parkingsystem.model.ParkingSpot;
import com.parkit.parkingsystem.model.Ticket;
import com.parkit.parkingsystem.service.ParkingService;
import com.parkit.parkingsystem.util.InputReaderUtil;
import net.bytebuddy.implementation.bind.annotation.IgnoreForBinding;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.platform.commons.function.Try;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ParkingServiceTest {
    @Mock
    private static ParkingService parkingService;

    @Mock
    private static InputReaderUtil inputReaderUtil;
    @Mock
    private static ParkingSpotDAO parkingSpotDAO;
    @Mock
    private static TicketDAO ticketDAO;

    @Mock
    private static Ticket ticket;

    ParkingSpot parkingSpot = new ParkingSpot(1, ParkingType.CAR, false);

    @BeforeEach
    public void setUpPerTest() {
        try {
            lenient().when(inputReaderUtil.readVehicleRegistrationNumber()).thenReturn("ABCDEF");
            Ticket ticket = new Ticket();
            ticket.setInTime(new Date(System.currentTimeMillis() - (60 * 60 * 1000)));
            ticket.setParkingSpot(parkingSpot);
            ticket.setVehicleRegNumber("ABCDEF");
            lenient().when(ticketDAO.getTicket(anyString())).thenReturn(ticket);
            lenient().when(ticketDAO.updateTicket(any(Ticket.class))).thenReturn(true);
            lenient().when(parkingSpotDAO.updateParking(any(ParkingSpot.class))).thenReturn(true);
            parkingService = new ParkingService(inputReaderUtil, parkingSpotDAO, ticketDAO);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Failed to set up test mock objects");
        }
    }


    @Test
    public void processIncomingVehicleTest() {

        when(inputReaderUtil.readSelection()).thenReturn(1);
        when(parkingSpotDAO.getNextAvailableSlot(any(ParkingType.class))).thenReturn(1);
        when(ticketDAO.saveTicket(any())).thenReturn(true);

        parkingService.processIncomingVehicle();

        verify(ticketDAO, Mockito.times(1)).recurringUsers(any());

    }


    @Test
    public void processIncomingVehicleParkingSpotNullTest() {

        when(inputReaderUtil.readSelection()).thenReturn(3);


        parkingService.processIncomingVehicle();

        verify(ticketDAO, Mockito.times(0)).updateTicket(any());
    }

    // Condition impossible a atteindre ! Correction code ? Si parkingSpot.getId< 0 alors parkingSpot!=null

    @Test
    public void processIncomingVehicleParkingSpotIdNullTest() {

        when(inputReaderUtil.readSelection()).thenReturn(1);
        when(parkingSpotDAO.getNextAvailableSlot(any())).thenReturn(0);

        parkingService.processIncomingVehicle();

        verify(ticketDAO, Mockito.times(0)).updateTicket(any());
    }


    @Test
    public void processIncomingVehicleRecurringUsersFalseTest() {

        when(inputReaderUtil.readSelection()).thenReturn(1);
        when(parkingSpotDAO.getNextAvailableSlot(any(ParkingType.class))).thenReturn(1);
        lenient().when(ticketDAO.recurringUsers(anyString())).thenReturn(true);
        when(ticketDAO.saveTicket(any())).thenReturn(true);
        parkingService.processIncomingVehicle();

        verify(ticketDAO, Mockito.times(1)).recurringUsers(any());
    }

    @Test
    public void processIncomingVehicleErrorTest() {

        when(inputReaderUtil.readSelection()).thenReturn(2);
        when(parkingSpotDAO.getNextAvailableSlot(any(ParkingType.class))).thenReturn(1);

        parkingService.processIncomingVehicle();

        verify(ticketDAO, Mockito.times(0)).recurringUsers(any());


    }

    @Test
    public void processIncomingVehicleBikeTest() {

        when(inputReaderUtil.readSelection()).thenReturn(2);
        when(parkingSpotDAO.getNextAvailableSlot(any(ParkingType.class))).thenReturn(1);
        when(ticketDAO.saveTicket(any())).thenReturn(true);

        parkingService.processIncomingVehicle();

        verify(ticketDAO, Mockito.times(1)).recurringUsers(any());
    }


    @Test
    public void getNextParkingNumberIfAvailableParkingFull() {
        when(inputReaderUtil.readSelection()).thenReturn(1);
        when(parkingSpotDAO.getNextAvailableSlot(ParkingType.CAR)).thenReturn(0);


        parkingService.getNextParkingNumberIfAvailable();

        assertFalse(parkingSpot.isAvailable());
    }


    @Test
    public void getNextParkingNumberIfAvailableErrorInputTypeVehicule() {

        when(inputReaderUtil.readSelection()).thenReturn(3);

        assertNull(parkingService.getNextParkingNumberIfAvailable());
    }


    @Test
    public void processExitingVehicleTest() {

        parkingService.processExitingVehicle();
        verify(parkingSpotDAO, Mockito.times(1)).updateParking(any(ParkingSpot.class));
    }

    @Test
    public void processExitingVehicleWithRecurringUsersTest() {

        when(ticketDAO.recurringUsers(anyString())).thenReturn(true);

        parkingService.processExitingVehicle();
        verify(parkingSpotDAO, Mockito.times(1)).updateParking(any(ParkingSpot.class));
        verify(ticketDAO, Mockito.times(1)).recurringUsers(anyString());
    }


    @Test
    public void processExitingVehicleUpdateTicketFalseTest() {

        when(ticketDAO.updateTicket(any(Ticket.class))).thenReturn(false);

        parkingService.processExitingVehicle();

        assertFalse(parkingSpot.isAvailable());


    }


}