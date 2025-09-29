package com.ativie.boxservice.service;


import com.ativie.boxservice.dto.CreateBoxRequest;
import com.ativie.boxservice.model.Box;
import com.ativie.boxservice.repository.BoxRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class BoxService {

    private final BoxRepository boxRepository;

    public Box createNewBox(CreateBoxRequest request) {
        Box box = Box.builder()
                .name(request.name())
                .userPublicId(request.userPublicId())
                .build();
      return boxRepository.save(box);
    }
}
