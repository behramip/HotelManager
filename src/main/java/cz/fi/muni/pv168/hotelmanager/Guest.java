package cz.fi.muni.pv168.hotelmanager;

import java.time.LocalDate;
import java.util.Objects;

public class Guest {

	public Long id;
	public String name;
	public String surname;
	public LocalDate born;
	public String cellNumber;

	public Guest(String name, String surname, LocalDate born, String cellNumber) {
        //this.id = AutoIncrement; TODO
        this.name = name;
        this.surname = surname;
        this.born = born;
        this.cellNumber = cellNumber;
    }

    @Override
    public String toString() {
        return "Guest{" + "id=" + id + ", name=" + name + ", surname=" + surname + ", born=" + born + ", cellNumber=" + cellNumber + '}';
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 53 * hash + Objects.hashCode(this.id);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Guest other = (Guest) obj;
        return Objects.equals(this.id, other.id);
    }

    /**
     * @return the id
     */
    public Long getId() {
        return id;
    }

 

    /**
     * @param id the id to set
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * @param name the name to set
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * @return the surname
     */
    public String getSurname() {
        return surname;
    }

    /**
     * @param surname the surname to set
     */
    public void setSurname(String surname) {
        this.surname = surname;
    }

    /**
     * @return the born
     */
    public LocalDate getBorn() {
        return born;
    }

    /**
     * @param born the born to set
     */
    public void setBorn(LocalDate born) {
        this.born = born;
    }

    /**
     * @return the cellNumber
     */
    public String getCellNumber() {
        return cellNumber;
    }

    /**
     * @param cellNumber the cellNumber to set
     */
    public void setCellNumber(String cellNumber) {
        this.cellNumber = cellNumber;
    }

}