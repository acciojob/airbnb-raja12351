package com.driver.Service;

import com.driver.Exception.*;
import com.driver.Repository.HotelManagementRepository;
import com.driver.model.Booking;
import com.driver.model.Facility;
import com.driver.model.Hotel;
import com.driver.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.UUID;

@Service
public class HotelManagementService {

    @Autowired
    HotelManagementRepository hotelManagementRepository;

    public String addHotel(Hotel hotel) throws HotelNameRequired, HotelAlreadyExist {

        if(hotel.getHotelName()==null){
            throw new HotelNameRequired("Hotel name is required while adding into the database.");
        }
        if(hotelManagementRepository.findByName(hotel.getHotelName())){
            throw new HotelAlreadyExist("Hotel is already present in the database.");
        }

        hotelManagementRepository.addHotel(hotel);

        return "Hotel is added successfully in the database.";
    }

    public Integer addUser(User user) throws AadharMandatory{

        if(user.getaadharCardNo()==0){
            throw new AadharMandatory("Aadhar number is mandatory in the details.");
        }

        hotelManagementRepository.adduser(user);
        return user.getaadharCardNo();
    }

    public String getHotelWithMostFacilities() {
        List<String> list = hotelManagementRepository.getHotelWithMostFacilities();
        Collections.sort(list, String.CASE_INSENSITIVE_ORDER);

        return list.get(0);
    }

    public int bookRoom(Booking booking) throws HotelNotFound, UserNotFound {
        if(!hotelManagementRepository.findByName(booking.getHotelName())){
            throw new HotelNotFound("Hotel is not present in the database");
        }

        int aadharNo = booking.getBookingAadharCard();
        User user = hotelManagementRepository.findUser(aadharNo);
        if(user == null){
            throw new UserNotFound("User is not present int the database");
        }

        Hotel hotel = hotelManagementRepository.findHotel(booking.getHotelName());

        int pricePerNight = hotel.getPricePerNight();

        if(hotel.getAvailableRooms() < booking.getNoOfRooms()){
            return -1;
        }
        booking.setBookingId(UUID.randomUUID().toString());

        booking.setAmountToBePaid(pricePerNight * booking.getNoOfRooms());

        hotelManagementRepository.bookRoom(booking);
        hotelManagementRepository.addLink(aadharNo,booking);

        return booking.getAmountToBePaid();
    }

    public int getBookings(Integer aadharCard) {
        return hotelManagementRepository.getBookings(aadharCard);
    }

    public Hotel update(List<Facility> newFacilities, String hotelName) {
        Hotel hotel = hotelManagementRepository.findHotel(hotelName);

        List<Facility> facilities = hotel.getFacilities();

        for(Facility facility : newFacilities){
            if(!facilities.contains(facility)){
                facilities.add(facility);
            }
        }

        hotel.setFacilities(facilities);
        hotelManagementRepository.addHotel(hotel);

        return hotel;
    }
}
