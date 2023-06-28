package com.driver.Repository;

import com.driver.model.Booking;
import com.driver.model.Hotel;
import com.driver.model.User;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

@Repository
public class HotelManagementRepository {

    private HashMap<String, Hotel> hotelData = new HashMap<>();
    private HashMap<Integer, User> userData = new HashMap<>();
    private HashMap<String, Booking> bookingData = new HashMap<>();

    private HashMap<Integer,List<Booking>> userBookLink = new HashMap<>();

    public Boolean findByName(String hotelName) {
        for(String name : hotelData.keySet()){
            if(hotelName.equals(name)){
                return true;
            }
        }
        return false;
    }

    public void addHotel(Hotel hotel) {
        hotelData.put(hotel.getHotelName(), hotel);
    }

    public void adduser(User user) {
        userData.put(user.getaadharCardNo(), user);
    }

    public List<String> getHotelWithMostFacilities() {
        int maxfacilities = 0;
        String hotelName = "";
        List<String> ans = new ArrayList<>();
        for(String hotel : hotelData.keySet()){
            if(hotelData.get(hotel).getFacilities().size() > maxfacilities){
                maxfacilities = hotelData.get(hotel).getFacilities().size();
                hotelName = hotel;
            }
        }
        for(String hotel : hotelData.keySet()){
            if(hotelData.get(hotel).getFacilities().size()==maxfacilities){
                ans.add(hotel);
            }
        }

        return ans;
    }

    public Hotel findHotel(String hotelName) {
        for(String hotel : hotelData.keySet()){
            if(hotelName.equals(hotel)){
                return hotelData.get(hotel);
            }
        }
        return null;
    }

    public void bookRoom(Booking booking) {
        bookingData.put(booking.getBookingId(), booking);
    }

    public User findUser(int aadharNo) {
        for(int aadhar : userData.keySet()){
            if(aadhar == aadharNo){
                return userData.get(aadhar);
            }
        }
        return null;
    }

    public void addLink(int aadharNo, Booking booking) {
        userBookLink.computeIfAbsent(aadharNo, k -> new ArrayList<>());

        userBookLink.get(aadharNo).add(booking);
    }

    public int getBookings(Integer aadharCard) {
        return userBookLink.get(aadharCard).size();
    }
}
