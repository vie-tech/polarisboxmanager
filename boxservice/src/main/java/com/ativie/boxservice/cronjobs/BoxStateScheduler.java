package com.ativie.boxservice.cronjobs;


import com.ativie.boxservice.enums.BoxState;
import com.ativie.boxservice.model.Box;
import com.ativie.boxservice.repository.BoxRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.List;

@Component
@RequiredArgsConstructor
@Slf4j
public class BoxStateScheduler {

    private final BoxRepository boxRepository;

    @Scheduled(fixedRate = 30000)
    public void processLoadingBoxes() {
        log.info("Task running....");
        Instant now = Instant.now();
        List<Box> loadingBoxes = boxRepository.findByState(BoxState.LOADING);

        for (Box box : loadingBoxes) {
            if (box.getLastStateChange() != null && box.getLastStateChange().isBefore(now.minusSeconds(60))) {

                if (box.getItemsLoaded() == null || box.getItemsLoaded().isEmpty()) {
                    box.setState(BoxState.IDLE);
                } else {
                    box.setState(BoxState.LOADED);
                }

                box.setLastStateChange(now);
                boxRepository.save(box);
            }
        }
    }

    @Scheduled(fixedRate = 60000)
    public void processDeliveries() {
        log.info("Delivery running");
        List<Box> loadedBoxes = boxRepository.findByState(BoxState.LOADED);

        for (Box box : loadedBoxes) {
            box.setBatteryCapacity(Math.max(0,
                    box.getBatteryCapacity() - 25.0));

            box.setState(BoxState.IDLE);
            box.getItemsLoaded().clear();
            box.setCurrentWeightCapacity(0.0);
            box.setLastStateChange(Instant.now());

            boxRepository.save(box);
        }
    }

    @Scheduled(fixedRate = 60000)
    public void chargeIdleBoxes() {
        List<Box> idleBoxes = boxRepository.findByState(BoxState.IDLE);

        for (Box box : idleBoxes) {
            if (box.getBatteryCapacity() < 100.0 && box.getState() == BoxState.IDLE) {
                double newBattery = Math.min(100.0, box.getBatteryCapacity() + 10.0);
                box.setBatteryCapacity(newBattery);
                box.setCharging(true);
                box.setLastStateChange(Instant.now());
                boxRepository.save(box);
            } else if (box.isCharging()) {
                box.setCharging(false);
                boxRepository.save(box);
            }
        }
    }
}
