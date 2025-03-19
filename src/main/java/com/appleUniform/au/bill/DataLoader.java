package com.appleUniform.au.bill;

import com.appleUniform.au.bill.model.Schools;
import com.appleUniform.au.bill.model.Style;
import com.appleUniform.au.bill.model.Worker;
import com.appleUniform.au.bill.repository.SchoolRepository;
import com.appleUniform.au.bill.repository.StyleRepository;
import com.appleUniform.au.bill.repository.WorkerRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

@Component
public class DataLoader implements CommandLineRunner {

    private final WorkerRepository workerRepository;
    private final StyleRepository styleRepository;
    private final SchoolRepository schoolRepository;
    private final ObjectMapper objectMapper;

    public DataLoader(WorkerRepository workerRepository, StyleRepository styleRepository, SchoolRepository schoolRepository, ObjectMapper objectMapper) {
        this.workerRepository = workerRepository;
        this.styleRepository = styleRepository;
        this.schoolRepository = schoolRepository;
        this.objectMapper = objectMapper;
    }

    @Override
    public void run(String... args) throws Exception {
        loadWorkers();
        loadStyles();
        loadSchools();
    }

    private void loadWorkers() throws IOException {
        InputStream workersInputStream = getClass().getClassLoader().getResourceAsStream("seed/workers.json");
        if (workersInputStream == null) {
            throw new IOException("workers.json file not found in classpath");
        }

        List<Worker> workers = objectMapper.readValue(workersInputStream, objectMapper.getTypeFactory().constructCollectionType(List.class, Worker.class));
        workerRepository.saveAll(workers);
    }

    private void loadStyles() throws IOException {
        InputStream stylesInputStream = getClass().getClassLoader().getResourceAsStream("seed/styles.json");
        if (stylesInputStream == null) {
            throw new IOException("styles.json file not found in classpath");
        }

        List<Style> styles = objectMapper.readValue(stylesInputStream, objectMapper.getTypeFactory().constructCollectionType(List.class, Style.class));
        styleRepository.saveAll(styles);
    }
    private void loadSchools() throws IOException {
        InputStream schoolInputStream = getClass().getClassLoader().getResourceAsStream("seed/schools.json");
        if (schoolInputStream == null) {
            throw new IOException("school.json file not found in classpath");
        }

        List<Schools> schools = objectMapper.readValue(schoolInputStream, objectMapper.getTypeFactory().constructCollectionType(List.class, Schools.class));
        schoolRepository.saveAll(schools);
    }
}