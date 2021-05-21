package model;

import java.time.LocalDateTime;

public final class Route {
    private final Long id;
    private final Station departureStation;
    private final Station arrivalStation;
    private final LocalDateTime departureTime;
    private final LocalDateTime arrivalTime;
    private final String trainName;
    private final Integer totalSeats;
    private final Double pricePerSeat;

    public static Builder builder() {
        return new Builder(null);
    }

    public static Builder builder(Route route) {
        return new Builder(route);
    }

    private Route(Builder builder) {
        this.id = builder.id;
        this.departureStation = builder.departureStation;
        this.arrivalStation = builder.arrivalStation;
        this.departureTime = builder.departureTime;
        this.arrivalTime = builder.arrivalTime;
        this.trainName = builder.trainName;
        this.totalSeats = builder.totalSeats;
        this.pricePerSeat = builder.pricePerSeat;
    }

    public Long getId() {
        return id;
    }

    public Station getDepartureStation() {
        return departureStation;
    }

    public Station getArrivalStation() {
        return arrivalStation;
    }

    public LocalDateTime getDepartureTime() {
        return departureTime;
    }

    public LocalDateTime getArrivalTime() {
        return arrivalTime;
    }

    public String getTrainName() {
        return trainName;
    }

    public Integer getTotalSeats() {
        return totalSeats;
    }

    public Double getPricePerSeat() {
        return pricePerSeat;
    }

        public static class Builder {
            private Long id;
            private Station departureStation;
            private Station arrivalStation;
            private LocalDateTime departureTime;
            private LocalDateTime arrivalTime;
            private String trainName;
            private Integer totalSeats;
            private Double pricePerSeat;

        private Builder(Route route) {
            if (route == null) {
                return;
            }
            this.id = route.id;
            this.departureStation = route.departureStation;
            this.arrivalStation = route.arrivalStation;
            this.departureTime = route.departureTime;
            this.arrivalTime = route.arrivalTime;
            this.trainName = route.trainName;
            this.totalSeats = route.totalSeats;
            this.pricePerSeat = route.pricePerSeat;
        }

        public Builder id(Long id) {
            this.id = id;
            return this;
        }

        public Builder departureStation(Station departureStation) {
            this.departureStation = departureStation;
            return this;
        }

        public Builder arrivalStation(Station arrivalStation) {
            this.arrivalStation = arrivalStation;
            return this;
        }

        public Builder departureTime(LocalDateTime departureTime) {
            this.departureTime = departureTime;
            return this;
        }

        public Builder arrivalTime(LocalDateTime arrivalTime) {
            this.arrivalTime = arrivalTime;
            return this;
        }

        public Builder trainName(String trainName) {
            this.trainName = trainName;
            return this;
        }

        public Builder totalSeats(Integer totalSeats) {
            this.totalSeats = totalSeats;
            return this;
        }

        public Builder pricePerSeat(Double pricePerSeat) {
            this.pricePerSeat = pricePerSeat;
            return this;
        }

        public Route build(){
            return new Route(this);
        }
    }
}
