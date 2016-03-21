/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.fi.muni.pv168.hotelmanager;

import java.sql.Connection;
import java.sql.SQLException;
import java.time.Clock;
import static java.time.Clock.system;
import java.time.Instant;
import java.time.LocalDate;
import java.time.Month;
import java.time.ZoneId;
import static java.time.ZoneId.systemDefault;
import java.util.List;
import javax.sql.DataSource;
import org.apache.derby.jdbc.EmbeddedDataSource;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
/**
 *
 * @author Filip Dubnička <dubnickaf@gmail.com>
 */
public class GuestManagerImplTest {

    private GuestManagerImpl manager;
    private static final LocalDate NOW = LocalDate.of(2015, Month.MARCH, 1);
    private DataSource dataSource;

    @Before
    public void setUp() throws SQLException {
        EmbeddedDataSource ds = new EmbeddedDataSource();
        //we will use in memory database
        ds.setDatabaseName("memory:guestmgr-test");
        ds.setCreateDatabase("create");
        dataSource = ds;
        try (Connection connection = dataSource.getConnection()) {
            connection.prepareStatement("CREATE TABLE GUEST ( "
                    + "ID BIGINT PRIMARY KEY GENERATED ALWAYS AS IDENTITY, "
                    + "NAME VARCHAR(255), "
                    + "SURNAME VARCHAR(255), "
                    + "BORN DATE, "
                    + "CELLNUMBER VARCHAR(255) )").executeUpdate();
        }           //creating table, to not to change functionality of original table
        manager = new GuestManagerImpl(dataSource);
    }

    @After
    public void tearDown() throws SQLException {
        try (Connection connection = dataSource.getConnection()) {
            connection.prepareStatement("DROP TABLE GUEST").executeUpdate();
        }
    }

    /**
     * Test of createGuest method, of class GuestManagerImpl.
     */
    @Test
    public void testCreateGuest() {
        Guest guest = new Guest("John", "Strongman", LocalDate.of(2014, Month.MARCH, 15), "+421099099099");
        manager.createGuest(guest);
        assertNotNull(guest.getId());
        assertEquals(guest.getName(), "John");
        assertEquals(guest.getSurname(), "Strongman");
        assertEquals(guest.getBorn(), LocalDate.of(2014, Month.MARCH, 15));
        assertEquals(guest.getCellNumber(), "+421099099099");

    }

    /**
     * Test of createGuest method, of class GuestManagerImpl.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testThrowingExceptionWhenCreatingGuestWithId() {
        Guest guest = new Guest("John", "Strongman", LocalDate.of(2014, Month.MARCH, 15), "+421099099099");
        guest.setId((long) 1);
        manager.createGuest(guest);
    }

    /**
     * Test of createGuest method, of class GuestManagerImpl.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testThrowingExceptionWhenGuestThatWasNotBornYet() {
        Clock clock = system(systemDefault());
        Guest guest = new Guest("John", "Strongman", LocalDate.now(clock).plusDays(1), "+421099099099");
        manager.createGuest(guest);
    }

    /**
     * Test of findGuestById method, of class GuestManagerImpl.
     */
    @Test
    public void testFindGuestById() {
        Guest guest = new Guest("John", "Strongman", LocalDate.of(2014, Month.MARCH, 15), "+421099099099");
        manager.createGuest(guest);
        Long id = guest.getId();
        Guest result = manager.findGuestById(id);
        assertEquals(guest, result);
    }

    /**
     * Test of deleteGuest method, of class GuestManagerImpl.
     */
    @Test
    public void testDeleteGuest() {
        Guest g1 = new Guest("John", "Strongman", LocalDate.of(2014, Month.MARCH, 15), "+421099099099");
        Guest g2 = new Guest("Josh", "Strongman", LocalDate.of(2014, Month.MARCH, 14), "+420099099099");
        manager.createGuest(g1);
        manager.createGuest(g2);

        assertNotNull(manager.findGuestById(g1.getId()));
        assertNotNull(manager.findGuestById(g2.getId()));

        manager.deleteGuest(g1);

        assertNull(manager.findGuestById(g1.getId()));
        assertNotNull(manager.findGuestById(g2.getId()));
        manager.deleteGuest(g2);
    }

    /**
     * Test of updateGuest method, of class GuestManagerImpl.
     */
    @Test
    public void testUpdateGuestsName() {
        Guest guest = new Guest("John", "Strongman", LocalDate.of(2014, Month.MARCH, 15), "+421099099099");
        manager.createGuest(guest);

        String newName = "Josh";
        guest.setName(newName);
        manager.updateGuest(guest);
        assertEquals(newName, guest.getName());
        assertEquals(guest.getSurname(), "Strongman");
        assertEquals(guest.getBorn(), LocalDate.of(2014, Month.MARCH, 15));
        assertEquals(guest.getCellNumber(), "+421099099099");
    }

    /**
     * Test of updateGuest method, of class GuestManagerImpl.
     */
    @Test
    public void testUpdateGuestsSurname() {
        Guest guest = new Guest("John", "Strongman", LocalDate.of(2014, Month.MARCH, 15), "+421099099099");
        Long id = guest.getId();
        manager.createGuest(guest);

        guest.setSurname("Weakman");
        manager.updateGuest(guest);
        assertEquals("Weakman", guest.getSurname());
        assertEquals(guest.getName(), "John");
        assertEquals(guest.getBorn(), LocalDate.of(2014, Month.MARCH, 15));
        assertEquals(guest.getCellNumber(), "+421099099099");
    }

    /**
     * Test of updateGuest method, of class GuestManagerImpl.
     */
    @Test
    public void testUpdateGuestsBorndate() {
        Guest guest = new Guest("John", "Strongman", LocalDate.of(2014, Month.MARCH, 15), "+421099099099");
        Long id = guest.getId();
        manager.createGuest(guest);

        guest.setBorn(LocalDate.of(2011, Month.DECEMBER, 13));
        manager.updateGuest(guest);
        assertEquals(LocalDate.of(2011, Month.DECEMBER, 13), guest.getBorn());
        assertEquals(guest.getName(), "John");
        assertEquals(guest.getSurname(), "Strongman");
        assertEquals(guest.getCellNumber(), "+421099099099");
    }

    /**
     * Test of updateGuest method, of class GuestManagerImpl.
     */
    @Test
    public void testUpdateGuestsCellNumber() {
        Guest guest = new Guest("John", "Strongman", LocalDate.of(2014, Month.MARCH, 15), "+421099099099");
        Long id = guest.getId();
        manager.createGuest(guest);

        guest.setCellNumber("+5");
        manager.updateGuest(guest);
        assertEquals("+5", guest.getCellNumber());
        assertEquals(guest.getName(), "John");
        assertEquals(guest.getSurname(), "Strongman");
        assertEquals(guest.getBorn(), LocalDate.of(2014, Month.MARCH, 15));
    }

    /**
     * Test of updateGuest method, of class GuestManagerImpl.
     */
    @Test(expected = ServiceFailureException.class)
    public void testUpdateGuestIdIsWrong() {
        Guest guest = new Guest("John", "Strongman", LocalDate.of(2014, Month.MARCH, 15), "+421099099099");
        manager.createGuest(guest);

        Long newId = (long) 12;
        guest.setId(newId);

        manager.updateGuest(guest);
    }

    /**
     * Test of findAllGuests method, of class GuestManagerImpl.
     */
    @Test
    public void testfindAllGuests() {
        assertEquals(manager.findAllGuests().size(), 0);

        Guest guest = new Guest("John", "Strongman", LocalDate.of(2014, Month.MARCH, 15), "+421099099099");
        manager.createGuest(guest);
        guest = new Guest("John", "Strongman", LocalDate.of(2014, Month.MARCH, 15), "+421099099099");
        manager.createGuest(guest);
        guest = new Guest("John", "Strongman", LocalDate.of(2014, Month.MARCH, 15), "+421099099099");
        manager.createGuest(guest);

        assertEquals(manager.findAllGuests().size(), 3);

        manager.deleteGuest(guest);

        assertEquals(manager.findAllGuests().size(), 2); // tady byla 0, nejsem si jistej jestli jsi to myslel tak ze po smaznuti jednoho zustanou 2 
        
    }

}
