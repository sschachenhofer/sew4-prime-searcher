package primesearcher;

import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import java.time.LocalTime;
import java.util.Collection;
import java.util.concurrent.ConcurrentSkipListSet;

@Component
public class PrimeCalculator implements ApplicationRunner {

    private ConcurrentSkipListSet<Long> primes;
    private boolean calculating; //Set this to false to stop the calculation
    private LocalTime startup;
    private LocalTime lastPrimeDiscovered;

    /**
     * Calculates primes until the method stop() is called. All calculated primes are added to a collection that can be
     * obtained through the method getPrimes().
     *
     * @param args The application arguments (whatever that is...)
     * @author Simon Schachenhofer
     * @since 2019-04-11
     */
    @Override
    public void run(ApplicationArguments args) {
        this.startup = LocalTime.now();

        this.primes = new ConcurrentSkipListSet<>();

        long current = 2; //Start calculating at 2, the first prime number

        //Loop to calculate primes
        while (this.calculating) {

            /*
             * Set to false as soon as a another natural number is found that divides the current number in whole
             * numbers.
             */
            boolean isPrime = true;

            /*
             * Inner loop to check whether the current number (l) is dividable by and other natural number (m) > 1 in
             * whole numbers.
             * The loop starts at l = current - 1 (because a number is dividable by itself in whole numbers) and ends
             * at 2.
             */
            for (long l = current - 1; l > 1; l--) {
                if (current % l == 0) {
                    isPrime = false;
                    break;
                }
            }

            //Add current number to the list if it is a prime
            if (isPrime) {
                this.primes.add(current);
                this.lastPrimeDiscovered = LocalTime.now();
            }

            current++;
        }
    }

    /**
     * Sets the private boolean parameter to false to stop the calculation of new primes and thereby end the thread.
     * @author Simon Schachenhofer
     * @since 2019-04-11
     */
    public void stopCalculation() {
        this.calculating = false;
    }

    /**
     * Returns the set of calculated primes
     * @author Simon Schachenhofer
     * @since 2019-04-11
     */
    public Collection<Long> getPrimes() {
        return this.primes;
    }

    /**
     * Returns the list of all calculated primes as a string
     *
     * @param separator a String value that is placed inbetween the primes
     * @author Simon Schachenhofer
     * @since 2019-04-11
     */
    public String getPrimesString(String separator) {
        StringBuilder sb = new StringBuilder();

        for (Long l : this.primes) {
            sb.append(l).append(separator);
        }

        String s = sb.toString();
        return sb.substring(0, sb.length() - 2); //Cuts away the last comma
    }

    /**
     * Returns the list of all calculated primes as a string, separated by commas.
     *
     * @return A String in the format "2, 3, 5, 7, 11, " and so on
     * @author Simon Schachenhofer
     * @since 2019-04-11
     */
    public String getPrimesString() {
        return this.getPrimesString(", ");
    }

    /**
     * Returns the most recently calculated prime
     *
     * @author Simon Schachenhofer
     * @since 2019-04-11
     */
    public long getLatestPrime() {
        return this.primes.last();
    }

    /**
     * Returns the startup time
     */
    public LocalTime getStartupTime() {
        return this.startup;
    }


    //TODO: Methoden zum Abrufen der Zeitpunkte (startup und lastPrimeDiscovered
}