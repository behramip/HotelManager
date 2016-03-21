package cz.fi.muni.pv168.hotelmanager;

import java.util.List;

public interface GuestManager {

    /**
     *
     * @param guest
     * @return
     */
    void createGuest(Guest guest);
    
    /**
     *
     * @param id
     * @return
     */
    Guest findGuestById(Long id);

    /**
     *
     * @param guest
     */
    void deleteGuest(Guest guest);

    /**
     *
     * @return 
     */
    List<Guest> findAllGuests();

    /**
     *
     * @param guest
     * @return
     */
    void updateGuest(Guest guest);

}
