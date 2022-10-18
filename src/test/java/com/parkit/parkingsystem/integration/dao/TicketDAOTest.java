package com.parkit.parkingsystem.integration.dao;

import com.parkit.parkingsystem.config.DataBaseConfig;
import com.parkit.parkingsystem.constants.ParkingType;
import com.parkit.parkingsystem.dao.ParkingSpotDAO;
import com.parkit.parkingsystem.dao.TicketDAO;
import com.parkit.parkingsystem.integration.config.DataBaseTestConfig;
import com.parkit.parkingsystem.integration.service.DataBasePrepareService;
import com.parkit.parkingsystem.model.ParkingSpot;
import com.parkit.parkingsystem.model.Ticket;
import com.parkit.parkingsystem.util.InputReaderUtil;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.sql.SQLException;
import java.util.Date;

import static com.mysql.cj.protocol.a.MysqlTextValueDecoder.getTime;
import static junit.framework.Assert.assertTrue;
import static org.mockito.Mockito.when;


@ExtendWith(MockitoExtension.class)
public class TicketDAOTest {

    @Mock
    public DataBaseTestConfig dataBaseTestConfig = new DataBaseTestConfig();
    @Mock
    public static DataBasePrepareService dataBasePrepareService;
    @Mock
    public TicketDAO ticketDAO;

    @Mock
    public static Ticket ticket;

    @Mock
    public static ParkingSpot parkingSpot;

    @BeforeAll
    public static void setUp() {
        dataBasePrepareService = new DataBasePrepareService();

        parkingSpot = new ParkingSpot(1, ParkingType.CAR, false);
        ticket = new Ticket();
        ticket.setId(1);
        ticket.setParkingSpot(parkingSpot);
        ticket.setVehicleRegNumber("ABCDEF");
        ticket.setPrice(15);


    }

    @BeforeEach
    public void setUpPerTest() {

        ticketDAO.dataBaseConfig = dataBaseTestConfig;
        dataBasePrepareService.clearDataBaseEntries();
        ticketDAO = new TicketDAO();


    }


    @Test
    public void saveTicketTest() {



        ticketDAO.saveTicket(ticket);


    }


}
