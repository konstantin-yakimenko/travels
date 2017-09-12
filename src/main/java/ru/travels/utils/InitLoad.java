package ru.travels.utils;

import com.google.gson.Gson;
import com.google.gson.stream.JsonReader;
import org.apache.ant.compress.taskdefs.Unzip;
import ru.travels.model.Entity;
import ru.travels.repositories.LocationRepository;
import ru.travels.repositories.UserRepository;
import ru.travels.repositories.VisitRepository;
import ru.travels.repositories.load.LoadLocations;
import ru.travels.repositories.load.LoadUsers;
import ru.travels.repositories.load.LoadVisits;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Arrays;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class InitLoad {
    private static final String OUTPUT_FOLDER_PATH = "/opt/myapp/data";
    private static final File DATA_ZIP_FILE = new File("/tmp/data/data.zip");
    private static final File OUTPUT_FOLDER = new File(OUTPUT_FOLDER_PATH);

    private final LocationRepository locationRepository;
    private final UserRepository userRepository;
    private final VisitRepository visitRepository;

    public InitLoad(LocationRepository locationRepository, UserRepository userRepository, VisitRepository visitRepository) {
        this.locationRepository = locationRepository;
        this.userRepository = userRepository;
        this.visitRepository = visitRepository;
    }

    private void setNow() {
        Long now = loadNow();
        locationRepository.setNow(ZonedDateTime.ofInstant(Instant.ofEpochMilli(now), ZoneId.systemDefault()));
    }

    private Long loadNow() {
        try {
            Files.copy(Paths.get("/tmp/data/options.txt"), Paths.get("/opt/myapp/data/options.txt"));

            File file = Arrays.stream(OUTPUT_FOLDER.listFiles())
                    .filter(f->f.getName().equalsIgnoreCase("options.txt"))
                    .findFirst().get();
            BufferedReader reader = new BufferedReader(new FileReader(file));
            String line = reader.readLine();
            return Long.parseLong(line) * 1000;
        } catch (Exception e) {
            e.printStackTrace();
            return 0L;
        }
    }

    public void loadArchData() {
        try {
            unzipfile();
            setNow();
            loadFiles();
        } catch (Exception e) {
            System.out.println("e = " + e);
        } finally {
            System.gc();
        }
    }

    private void loadFiles() throws Exception {
        System.out.println("Start import data");
        File[] outputDirectory = OUTPUT_FOLDER.listFiles();
        if (outputDirectory != null) {
            ExecutorService exec = Executors.newFixedThreadPool(20);
            for (File jsonFile : outputDirectory) {
                if (jsonFile.getName().equalsIgnoreCase("options.txt")) {
                    continue;
                }
                exec.execute(() -> {
                    try {
                        String entity = jsonFile.getName().substring(0, jsonFile.getName().indexOf('_'));
                        switch (Entity.byName(entity)) {
                            case USERS:
                                loadUsers(jsonFile);
                                break;
                            case VISITS:
                                loadVisits(jsonFile);
                                break;
                            case LOCATIONS:
                                loadLocations(jsonFile);
                                break;
                        }
                    } catch (Exception e) {
                        System.out.println("Error import data " + e);
                    }
                });
            }
            exec.shutdown();
            exec.awaitTermination(7, TimeUnit.MINUTES);
            System.out.println("Import data has been finished");
        }
    }

    private void loadLocations(File jsonFile) throws Exception {
        Gson gson = new Gson();
        JsonReader reader = new JsonReader(new InputStreamReader(new FileInputStream(jsonFile), "UTF-8"));
        LoadLocations locations = gson.fromJson(reader, LoadLocations.class);
        locationRepository.addBatch(locations);
    }

    private void loadVisits(File jsonFile) throws Exception {
        Gson gson = new Gson();
        JsonReader reader = new JsonReader(new InputStreamReader(new FileInputStream(jsonFile), "UTF-8"));
        LoadVisits visits = gson.fromJson(reader, LoadVisits.class);
        visitRepository.addBatch(visits);
    }

    private void loadUsers(File jsonFile) throws Exception {
        Gson gson = new Gson();
        JsonReader reader = new JsonReader(new InputStreamReader(new FileInputStream(jsonFile), "UTF-8"));
        LoadUsers users = gson.fromJson(reader, LoadUsers.class);
        userRepository.addBatch(users);
    }

    private void unzipfile() throws Exception {
        System.out.println("Unzip files");
        Unzip unzipper = new Unzip();
        if( !OUTPUT_FOLDER.exists() ) {
            OUTPUT_FOLDER.mkdirs();
        }
        unzipper.setSrc(DATA_ZIP_FILE);
        unzipper.setDest(OUTPUT_FOLDER);
        unzipper.execute();
    }
}
