package service;

import com.insurance.model.Trips;
import java.time.LocalDateTime;
import java.util.List;

public class TripValidationService {

    public void validateTrips(Trips lastEvent, String newEventType, LocalDateTime newTimestamp){

        if (newEventType == null || newTimestamp == null){
            throw new IllegalArgumentException("Event type and timestamp must be filled");
        }

        if (!newEventType)



    }


}
