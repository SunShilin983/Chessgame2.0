package org.ssl.controller;

import javafx.application.Application;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class App
{
    private static Logger logger = LogManager.getLogger(LogManager.ROOT_LOGGER_NAME);

    public static void main( String[] args )
    {
        logger.trace("Log4j2 output:  Chess game is started");

        Application.launch(ChessGame.class, args);
    }
}
