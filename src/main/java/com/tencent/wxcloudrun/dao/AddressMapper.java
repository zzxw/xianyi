package com.tencent.wxcloudrun.dao;

import com.tencent.wxcloudrun.model.Address;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface AddressMapper {
    List<Address> queryAddressByUser(@Param("userID") String userID);

    Address  queryAddressById(@Param("userID") String userID, @Param("addressNo") int addressNo);

    void deleteAddress(@Param("userID") String userID, @Param("addressNo") int addressNo);

    void updateAddress(Address address);

    void updateDefaultAddress(String userID);

    void createAddress(Address address);
}
