package cz.fi.muni.pv168.hotelmanager;

import java.util.List;

public interface RoomManager {

	/**
	 * 
	 * @param room
         * @return 
	 */
	Room createRoom(Room room);

	/**
	 * 
	 * @param room
	 */
	void deleteRoom(Room room);

	/**
	 * 
	 * @param id
         * @return 
	 */
	Room findRoomById(Long id);

	List<Room> findAllRooms();

	/**
	 * 
	 * @param room
         * @return 
	 */
	Room updateRoom(Room room);

}