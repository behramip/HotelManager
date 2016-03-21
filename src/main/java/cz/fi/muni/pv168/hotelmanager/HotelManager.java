package cz.fi.muni.pv168.hotelmanager;

import java.util.List;

public interface HotelManager {

	/**
	 * 
	 * @param guest
         * @return 
	 */
	Room findRoomByGuest(Guest guest);

	/**
	 * 
	 * @param room
         * @return 
	 */
	List<Guest> getGuestsInRoom(Room room);

	/**
	 * 
	 * @param room
	 * @param guest
	 */
	void reserveRoomByName(Room room, Guest guest);

	/**
	 * 
	 * @param room
	 * @param guestList
	 */
	void accommodateGuests(Room room, List<Guest> guestList);

	/**
	 * 
	 * @param room
	 * @param guestList
	 */
	void updateGuestsInRoom(Room room, List<Guest> guestList);

	/**
	 * 
	 * @param room
	 */
	void clearRoom(Room room);

	List<Room> getFreeRooms();

}