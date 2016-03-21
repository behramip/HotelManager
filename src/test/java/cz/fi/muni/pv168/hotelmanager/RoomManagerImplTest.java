/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.fi.muni.pv168.hotelmanager;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import javax.sql.DataSource;
import org.apache.derby.jdbc.EmbeddedDataSource;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

/**
 *
 * @author Patrik
 */
public class RoomManagerImplTest {

    private RoomManagerImpl roomManager;
    private DataSource dataSource;


    @Before
    public void setUp() throws SQLException {
        EmbeddedDataSource ds = new EmbeddedDataSource();
        //we will use in memory database
        ds.setDatabaseName("memory:roommgr-test");
        ds.setCreateDatabase("create");
        dataSource = ds;
        try (Connection connection = dataSource.getConnection()) {
            connection.prepareStatement("CREATE TABLE ROOM ("
                    + "ID BIGINT PRIMARY KEY GENERATED ALWAYS AS IDENTITY,"
                    + "NUMBER INTEGER,"
                    + "CAPACITY INTEGER )").executeUpdate();
        }           //creating table, to not to change functionality of original table
        roomManager = new RoomManagerImpl(dataSource);
    }

    @After
    public void tearDown() throws SQLException {
        try (Connection connection = dataSource.getConnection()) {
            connection.prepareStatement("DROP TABLE ROOM").executeUpdate();
        }
    }

    @Rule
    public ExpectedException exceptedException = ExpectedException.none();

    @Test
    public void createRoomWithNull() {
        exceptedException.expect(IllegalArgumentException.class);
        roomManager.createRoom(null);
    }

    @Test
    public void findRoomById() {
        Room room1 = initiateRoom(5, 45, new Long(1));

        roomManager.createRoom(room1);

        Room room2 = roomManager.findRoomById(room1.getId());

        Assert.assertEquals(room1, room2);
    }

    @Test
    public void findAllRooms() {
        Room room1 = initiateRoom(5, 45, new Long(1));
        Room room2 = initiateRoom(4, 40, new Long(2));
        Room room3 = initiateRoom(2, 21, new Long(3));

        roomManager.createRoom(room1);
        roomManager.createRoom(room2);
        roomManager.createRoom(room3);

        List<Room> rooms = Arrays.asList(room1, room2, room3);
        List<Room> roomList = roomManager.findAllRooms();

        Assert.assertEquals(rooms.size(), roomList.size());

        //pro jednotlive atributy
        Assert.assertEquals(rooms, roomList);
    }

    @Test
    public void updateRoomWithWrongAttributes() {
        Room room = initiateRoom(5, 45, new Long(1));
        roomManager.createRoom(room);

        room.setCapacity(-2);
        exceptedException.expect(IllegalArgumentException.class);
        roomManager.updateRoom(room);

        room.setCapacity(-0);
        exceptedException.expect(IllegalArgumentException.class);
        roomManager.updateRoom(room);

        room.setId(new Long(-1));
        exceptedException.expect(IllegalArgumentException.class);
        roomManager.updateRoom(room);

        room.setNumber(-450);
        exceptedException.expect(IllegalArgumentException.class);
        roomManager.updateRoom(room);
    }

    public Room initiateRoom(int capacity, int roomNumber, Long id) {
        Room room = new Room();
        room.setCapacity(capacity);
        room.setNumber(roomNumber);
        room.setId(id);

        return room;
    }

}
