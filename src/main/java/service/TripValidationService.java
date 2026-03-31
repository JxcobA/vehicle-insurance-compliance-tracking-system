package service;

import com.insurance.model.Trips;
import java.time.LocalDateTime;


public class TripValidationService {

    public void validateTrips(Trips lastEvent, String newEventType, LocalDateTime newTimestamp){

        if (newEventType == null || newTimestamp == null){
            throw new IllegalArgumentException("Event type and timestamp must be filled");
        }

        if (!newEventType.equals("TRIP_START")&& !newEventType.equals("TRIP_END")){
            throw new IllegalArgumentException("Invalid event type: "+ newEventType);
        }

        if (lastEvent == null){
            if (newEventType.equals("TRIP_END")){
                throw new IllegalArgumentException("Cannot record TRIP_END without prior TRIP_START");
            }
            return;
        }

        if (newTimestamp.isBefore(lastEvent.getEvent_timestamp())){
            throw new IllegalArgumentException("New event timestamp cannot be before last recorded event");
        }

        if (lastEvent.getEvent_type().equals("TRIP_START") && newEventType.equals("TRIP_END")){

        }



    }


}
