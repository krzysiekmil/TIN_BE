package pjwstk.s20124.tin.services;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import pjwstk.s20124.tin.model.InfoChange;
import pjwstk.s20124.tin.model.InfoChangeType;
import pjwstk.s20124.tin.repository.InfoChangeRepository;

import java.time.Instant;
import java.util.Date;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class InfoChangeService {

    private final InfoChangeRepository infoChangeRepository;


    @Async
    public void createInfoChangeRegistry(InfoChange infoChange){
        if(InfoChangeType.EVENT_ATTENDING_STATUS_CHANGE.equals(infoChange.getType())){
            Optional<InfoChange> optionalInfoChange= infoChangeRepository.findInfoChangeByUserAndEventRelatedToAndType(infoChange.getUser(), infoChange.getEventRelatedTo(), InfoChangeType.EVENT_ATTENDING_STATUS_CHANGE);

            if(optionalInfoChange.isPresent()){
                infoChange = optionalInfoChange.get();
                infoChange.setLastModifiedDate(Date.from(Instant.now()));
            }
        }

        infoChangeRepository.save(infoChange);
    }
}
