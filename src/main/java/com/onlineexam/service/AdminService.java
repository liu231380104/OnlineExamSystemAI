package com.onlineexam.service;

import com.onlineexam.entity.Admin;

import java.util.List;

public interface AdminService{

    public List<Admin> findAll();

    public Admin findById(Integer adminId);

    public int deleteById(int adminId);

    public int update(Admin admin);

    public int add(Admin admin);

    Object resetPsw(Integer adminId, String newPsw, String oldPsw);
}
