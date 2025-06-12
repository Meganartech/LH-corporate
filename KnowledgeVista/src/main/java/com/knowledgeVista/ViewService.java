package com.knowledgeVista;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.knowledgeVista.Course.Test.Repository.ViewRepository;

import jakarta.annotation.PostConstruct;
import jakarta.transaction.Transactional;

@Service
public class ViewService {

    @Autowired
    private ViewRepository viewRepository;

    @PostConstruct  // This will run after the Spring Boot app starts
    @Transactional
    public void initializeView() {
        try {
            viewRepository.createCombinedView();
        } catch (Exception e) {
            System.err.println("❌ Error Creating SQL View: " + e.getMessage());
        }
    }
}

