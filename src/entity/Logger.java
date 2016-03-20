package entity;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;

public class Logger {
    private static Logger instance;
    
    private BufferedWriter _writer;
    
    private Logger() {
        try {
            this._writer = new BufferedWriter(
                    new FileWriter(new File("tmp/log.txt"))
                    );
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    
    public static Logger getInstance() {
        if (instance == null) {
            instance = new Logger();
        }
        return instance;
    }
    
    public void info(String information) {
        try {
            this.writeTimestamps();
            this._writer.write(information);
            this._writer.newLine();
            this._writer.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    private void writeTimestamps() {
        try {
            this._writer.write(String.format("[%s]: ",
                    LocalDateTime.now()
            ));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
