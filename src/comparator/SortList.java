
package comparator;

import entities.Tenant;
import java.util.Comparator;

/**
 *
 * This class returns two sorting methods for sorting objects
 * within a list.
 */
public class SortList {
    /**
     * sort by lastName in Ascending order use .reverse to sort in descending
     * order
     */
    public Comparator<Tenant> sortByLastName = (Tenant o1, Tenant o2) -> o1.getLastName().compareTo(o2.getLastName());
    /**
     * sort the tenant list by date of birth use .reverse to sort in descending
     * order
     */
    public Comparator<Tenant> sortByDateOfBirth = (Tenant o1, Tenant o2) -> o1.getDateOfBirth().compareTo(o2.getDateOfBirth());

}
