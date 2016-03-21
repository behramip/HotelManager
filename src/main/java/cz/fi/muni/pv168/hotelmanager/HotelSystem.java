/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.fi.muni.pv168.hotelmanager;

import java.time.LocalDate;
import java.time.Month;
import javax.activation.DataSource;
import org.apache.derby.jdbc.EmbeddedDataSource;

/**
 *
 * @author x409062
 */
public class HotelSystem {
    
     GuestManagerImpl guestMan;
    
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        /*DataSource dataSource;
        GuestManagerImpl guestMan = null;
        EmbeddedDataSource ds = new EmbeddedDataSource();
        //we will use in memory database
        ds.setDatabaseName("memory:guestmgr-test");
        ds.setCreateDatabase("create");

        dataSource = (DataSource) ds;
        
        guestMan = new GuestManagerImpl((javax.sql.DataSource) dataSource);
        
        Guest guest = new Guest("John","Newman",LocalDate.of(1992, Month.MARCH, 3),"+420771265454");
        
        guestMan.createGuest(guest);*/
        
    }
    
}
