package org.safecircle.backend.services;

import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.Message;
import com.google.firebase.messaging.Notification;
import org.safecircle.backend.models.Alert;
import org.safecircle.backend.models.Location;
import org.safecircle.backend.models.User;
import org.safecircle.backend.repositories.LocationRepository;
import org.safecircle.backend.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;
import java.util.List;

@Service
public class LocationService {

    private static final BigDecimal EARTH_RADIUS_KM = new BigDecimal("6371.0");
    private static final MathContext MATH_CONTEXT = new MathContext(10, RoundingMode.HALF_UP);
    private final LocationRepository locationRepository;

    public LocationService(LocationRepository locationRepository) {
        this.locationRepository = locationRepository;
    }

    /**
     * Bereken de afstand tussen twee locaties in kilometers met BigDecimal.
     */
    public BigDecimal calculateDistance(BigDecimal latitude1, BigDecimal longitude1,
                                        BigDecimal latitude2, BigDecimal longitude2) {
        // Converteer graden naar radialen
        BigDecimal latDistance = toRadians(latitude2.subtract(latitude1));
        BigDecimal lonDistance = toRadians(longitude2.subtract(longitude1));

        // Haversine-formule
        BigDecimal a = sin(latDistance.divide(new BigDecimal(2), MATH_CONTEXT)).pow(2)
                .add(
                        cos(toRadians(latitude1))
                                .multiply(cos(toRadians(latitude2)))
                                .multiply(sin(lonDistance.divide(new BigDecimal(2), MATH_CONTEXT)).pow(2))
                );

        BigDecimal c = new BigDecimal(2).multiply(atan2(sqrt(a), sqrt(BigDecimal.ONE.subtract(a))));

        return EARTH_RADIUS_KM.multiply(c, MATH_CONTEXT);
    }

    private BigDecimal toRadians(BigDecimal degree) {
        return degree.multiply(new BigDecimal(Math.PI)).divide(new BigDecimal("180"), MATH_CONTEXT);
    }

    private BigDecimal sin(BigDecimal radians) {
        return new BigDecimal(Math.sin(radians.doubleValue()), MATH_CONTEXT);
    }

    private BigDecimal cos(BigDecimal radians) {
        return new BigDecimal(Math.cos(radians.doubleValue()), MATH_CONTEXT);
    }

    private BigDecimal atan2(BigDecimal y, BigDecimal x) {
        return new BigDecimal(Math.atan2(y.doubleValue(), x.doubleValue()), MATH_CONTEXT);
    }

    private BigDecimal sqrt(BigDecimal value) {
        return new BigDecimal(Math.sqrt(value.doubleValue()), MATH_CONTEXT);
    }

    public BigDecimal getDistanceFromLocation(long locationId, BigDecimal targetLatitude, BigDecimal targetLongitude) {
        // Haal de locatie op
        Location location = getLocationById(locationId);
        if (location == null) {
            throw new RuntimeException("Location not found for ID: " + locationId);
        }

        // Bereken de afstand
        return calculateDistance(
                location.getLatitude(), location.getLongitude(), targetLatitude, targetLongitude
        );
    }

    public Location getLocationById(long locationId) {
        if(!locationRepository.existsById(locationId)) {
            return null;
        }
        return getLocationById(locationId);
    }

    public boolean isValidLocationId(long locationId) {
        return locationRepository.existsByLocationId(locationId);
    }

}

