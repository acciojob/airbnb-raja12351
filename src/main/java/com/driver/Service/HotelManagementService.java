package com.driver.Service;

import com.driver.Exception.AadharMandatory;
import com.driver.Exception.HotelAlreadyExist;
import com.driver.Exception.HotelNameRequired;
import com.driver.Repository.HotelManagementRepository;
import com.driver.model.Hotel;
import com.driver.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

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
}
