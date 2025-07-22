package nro.server;

import lombok.Getter;
import lombok.Setter;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

/**
 * 
 */
@Setter
@Getter
public class GameConfig {

    public boolean isOpenPrisonPlanet;
    public boolean isOpenSuperMarket;
    public String event;
    
    public GameConfig() {
//        load();
    }

    public void load() {
        Properties properties = new Properties();
        try {
            properties.load(new FileInputStream("config/game.properties"));
            properties.forEach((key, value) -> {
                System.out.println("[Gameconfig]: " + key + " : " + value);
            });
            isOpenPrisonPlanet = Boolean.parseBoolean(properties.getProperty("open.prisonplanet"));
            isOpenSuperMarket = Boolean.parseBoolean(properties.getProperty("open.supermarket"));
            event = String.valueOf(properties.getProperty("event"));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
