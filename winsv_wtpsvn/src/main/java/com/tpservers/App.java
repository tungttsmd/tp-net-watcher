package com.tpservers;

import com.tpservers.Services.Facade.EventService;
import com.tpservers.Routes.RouteBootstrap;
import com.tpservers.Routes.RouteDispatcher.RouteDispatcher;
import com.tpservers.Services.Service;
import io.github.cdimascio.dotenv.Dotenv;

import java.nio.file.Path;

import tungtt.Console.Console;
import tungtt.PackageHandler.Downloader;
import tungtt.PackageHandler.Unzipper;

public class App {
    static {
        Dotenv dotenv = Dotenv.configure()
                .directory(".")
                .ignoreIfMissing()
                .load();

        dotenv.entries().forEach(e -> System.setProperty(e.getKey(), e.getValue()));
    }

    public static void main(String[] args) {
        
        // A. Boot services
        Service.boot();

        // B. Đăng ký route
        RouteBootstrap.init();

        // C. Mở cổng
        RouteDispatcher.boot();

        // D. Bật dịch vụ Event Processor
        EventService.boot();
    }

}
