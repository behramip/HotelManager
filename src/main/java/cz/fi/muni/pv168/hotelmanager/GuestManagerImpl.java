/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.fi.muni.pv168.hotelmanager;

import java.util.List;
import java.sql.*;
import java.time.Clock;
import static java.time.Clock.system;
import java.time.LocalDate;
import static java.time.ZoneId.systemDefault;
import java.util.ArrayList;
import javax.sql.DataSource;

/**
 *
 * @author xdubnick
 */
class GuestManagerImpl implements GuestManager {

    private final DataSource dataSource;

    public GuestManagerImpl(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Override
    public void createGuest(Guest guest) throws ServiceFailureException {
        validate(guest);
        if (guest.getId() != null) {
            throw new IllegalArgumentException("guest id is already set");
        }
        Long id = null;
        try (
                Connection connection = dataSource.getConnection();
                PreparedStatement st = connection.prepareStatement(
                        "INSERT INTO GUEST (name,surname,born,cellNumber) VALUES (?,?,?,?)",
                        Statement.RETURN_GENERATED_KEYS)) {

            st.setString(1, guest.getName());
            st.setString(2, guest.getSurname());
            st.setDate(3, Date.valueOf(guest.getBorn()));
            st.setString(4, guest.getCellNumber());

            int addedRows = st.executeUpdate();
            if (addedRows != 1) {
                throw new ServiceFailureException("Internal Error: More rows ("
                        + addedRows + ") inserted when trying to insert guest " + guest);
            }

            ResultSet keyRS = st.getGeneratedKeys();
            id = getKey(keyRS, guest);
            guest.setId(id);

        } catch (SQLException ex) {
            throw new ServiceFailureException("Error when inserting guest " + guest, ex);
        } catch (Exception ex) {
            throw new ServiceFailureException("Error when inserting guest " + guest, ex);
        }

    }

    private void validate(Guest guest) throws IllegalArgumentException {
        if (guest == null) {
            throw new IllegalArgumentException("guest is null");
        }
        Clock clock = system(systemDefault());
        if (guest.getBorn().isAfter(LocalDate.now(clock))) {
            throw new IllegalArgumentException("date of born is in future.");
        }
    }

    private Long getKey(ResultSet keyRS, Guest guest) throws ServiceFailureException, SQLException {
        if (keyRS.next()) {
            if (keyRS.getMetaData().getColumnCount() != 1) {
                throw new ServiceFailureException("Internal Error: Generated key"
                        + "retriving failed when trying to insert guest " + guest
                        + " - wrong key fields count: " + keyRS.getMetaData().getColumnCount());
            }
            Long result = keyRS.getLong(1);
            if (keyRS.next()) {
                throw new ServiceFailureException("Internal Error: Generated key"
                        + "retriving failed when trying to insert guest " + guest
                        + " - more keys found");
            }
            return result;
        } else {
            throw new ServiceFailureException("Internal Error: Generated key"
                    + "retriving failed when trying to insert guest " + guest
                    + " - no key found");
        }
    }

    @Override
    public Guest findGuestById(Long id) throws ServiceFailureException {
        try (
                Connection connection = dataSource.getConnection();
                PreparedStatement st = connection.prepareStatement(
                        "SELECT NAME,SURNAME,BORN,CELLNUMBER FROM GUEST WHERE ID = ?")) {
  
            st.setLong(1, id);
           // st.setObject(1, new Long(id));
            ResultSet rs = st.executeQuery();

            if (rs.next()) {
                Guest guest = new Guest(rs.getString("NAME"), rs.getString("SURNAME"), rs.getDate("BORN").toLocalDate(), rs.getString("CELLNUMBER"));
                guest.setId(id); //tohle nevim jestli tak muze byt 
                if (rs.next()) {
                    throw new ServiceFailureException(
                            "Internal error: More entities with the same id found ");
                }

                return guest;
            } else {
                return null;
            }

        } catch (SQLException ex) {
            throw new ServiceFailureException(
                    "Error when retrieving guest with id " + id, ex);
        }
    }

    @Override
    public void deleteGuest(Guest guest) {
        if (guest == null) {
            throw new IllegalArgumentException("guest is null");
        }
        if (guest.getId() == null) {
            throw new IllegalArgumentException("guest id is null");
        }
        try (
                Connection connection = dataSource.getConnection();
                PreparedStatement st = connection.prepareStatement(
                        "DELETE FROM GUEST WHERE ID = ?")) {

            st.setLong(1, guest.getId());

            int count = st.executeUpdate();
            if (count == 0) {
                throw new ServiceFailureException("Guest " + guest + " was not found in database!");
            } else if (count != 1) {
                throw new ServiceFailureException("Invalid deleted rows count detected (one row should be updated): " + count);
            }
        } catch (SQLException ex) {
            throw new ServiceFailureException(
                    "Error when updating guest " + guest, ex);
        }
    }

    @Override
    public List<Guest> findAllGuests() {
        try (
                Connection connection = dataSource.getConnection();
                PreparedStatement st = connection.prepareStatement(
                        "SELECT NAME,SURNAME,BORN,CELLNUMBER FROM GUEST")) {

            ResultSet rs = st.executeQuery();

            List<Guest> result = new ArrayList<>();
            while (rs.next()) {
                result.add(new Guest(rs.getString("NAME"), rs.getString("SURNAME"), rs.getDate("BORN").toLocalDate(), rs.getString("CELLNUMBER")));
            }
            return result;

        } catch (SQLException ex) {
            throw new ServiceFailureException(
                    "Error when retrieving all guests", ex);
        }
    }

    @Override
    public void updateGuest(Guest guest) {
        validate(guest);
        if (guest.getId() == null) {
            throw new IllegalArgumentException("guest id is null");
        }
        try (
                Connection connection = dataSource.getConnection();
                PreparedStatement st = connection.prepareStatement(
                        "UPDATE GUEST SET NAME = ?, SURNAME = ?, BORN = ?, CELLNUMBER = ? WHERE ID = ?")) {

            st.setString(1, guest.getName());
            st.setString(2, guest.getSurname());
            st.setDate(3, Date.valueOf(guest.getBorn()));
            st.setString(4, guest.getCellNumber());
            st.setLong(5, guest.getId());

            int count = st.executeUpdate();
            if (count == 0) {
                throw new ServiceFailureException("Guest " + guest + " was not found in database!");
            } else if (count != 1) {
                throw new ServiceFailureException("Invalid updated rows count detected (one row should be updated): " + count);
            }
        } catch (SQLException ex) {
            throw new ServiceFailureException(
                    "Error when updating guest " + guest, ex);
        }

    }

}
