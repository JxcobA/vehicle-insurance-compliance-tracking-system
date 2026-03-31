package setup;

import config.DatabaseConnection;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

public class Databaseinitialiser {

    public static void initialiseDatabase() {
        String createVehiclesTable = """
            CREATE TABLE IF NOT EXISTS vehicles (
                registration_number VARCHAR(20) PRIMARY KEY,
                make VARCHAR(50),
                model VARCHAR(50),
                created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
            );
            """;

        String createPoliciesTable = """
            CREATE TABLE IF NOT EXISTS insurance_policies (
                id SERIAL PRIMARY KEY,
                registration_number VARCHAR(20) NOT NULL,
                policy_type VARCHAR(30) NOT NULL,
                issue_date DATE NOT NULL,
                expiry_date DATE NOT NULL,
                is_active BOOLEAN DEFAULT TRUE,
                CONSTRAINT fk_policy_vehicle
                    FOREIGN KEY (registration_number)
                    REFERENCES vehicles(registration_number)
                    ON DELETE CASCADE,
                CONSTRAINT chk_policy_dates
                    CHECK (expiry_date >= issue_date)
            );
            """;

        String createTripEventsTable = """
            CREATE TABLE IF NOT EXISTS trip_events (
                id SERIAL PRIMARY KEY,
                registration_number VARCHAR(20) NOT NULL,
                event_type VARCHAR(20) NOT NULL,
                event_timestamp TIMESTAMP NOT NULL,
                location VARCHAR(100),
                CONSTRAINT fk_trip_vehicle
                    FOREIGN KEY (registration_number)
                    REFERENCES vehicles(registration_number)
                    ON DELETE CASCADE,
                CONSTRAINT chk_event_type
                    CHECK (event_type IN ('TRIP_START', 'TRIP_END'))
            );
            """;

        String createIndexes = """
            CREATE INDEX IF NOT EXISTS idx_policy_registration
            ON insurance_policies(registration_number);

            CREATE INDEX IF NOT EXISTS idx_policy_expiry
            ON insurance_policies(expiry_date);

            CREATE INDEX IF NOT EXISTS idx_trip_registration
            ON trip_events(registration_number);

            CREATE INDEX IF NOT EXISTS idx_trip_registration_timestamp
            ON trip_events(registration_number, event_timestamp DESC);
            """;

        try (Connection connection = DatabaseConnection.getConnection();
             Statement statement = connection.createStatement()) {

            statement.execute(createVehiclesTable);
            statement.execute(createPoliciesTable);
            statement.execute(createTripEventsTable);
            statement.execute(createIndexes);

            System.out.println("Database tables created successfully.");

        } catch (SQLException e) {
            System.out.println("Error initializing database: " + e.getMessage());
            e.printStackTrace();
        }
    }
}

