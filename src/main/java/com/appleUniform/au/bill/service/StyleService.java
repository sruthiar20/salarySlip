package com.appleUniform.au.bill.service;


import com.appleUniform.au.bill.model.Style;
import com.appleUniform.au.bill.repository.StyleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class StyleService {

    @Autowired
    private StyleRepository styleRepository;

    public Style addStyle(Style style) {
        return styleRepository.save(style);
    }

    public List<Style> getAllStyles() {
        return styleRepository.findAll();
    }

    public Style getStyleById(String id) {
        return styleRepository.findById(id).orElse(null);
    }
}