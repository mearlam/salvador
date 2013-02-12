package com.salvador.database;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * Encapsulation of JPA query parameters for use with {@link CrudService}
 */
public final class QueryParameters {
    private Map<String, Object> parameters = null;

    private QueryParameters(String name, Object value) {
        this.parameters = new HashMap<String, Object>();
        this.parameters.put(name, value);
    }

    /**
     * Creates a new set of parameters populated with an initial name/value pair
     *
     * @param name  parameter name
     * @param value parameter value
     * @return initial query parameters
     */
    public static QueryParameters withParameters(String name, Object value) {
        return new QueryParameters(name, value);
    }

    /**
     * Adds another name/value pair to the parameters
     *
     * @param name  parameter name
     * @param value parameter value
     * @return updated query parameters
     */
    public QueryParameters and(String name, Object value) {
        this.parameters.put(name, value);
        return this;
    }

    /**
     * Returns all name/value parameter pairs.
     *
     * @return all name/value pairs
     */
    public Set<Map.Entry<String, Object>> entries() {
        return parameters.entrySet();
    }

    @Override
    public String toString() {
        return "QueryParameters{" +
                "parameters=" + parameters +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof QueryParameters)) {
            return false;
        }
        QueryParameters that = (QueryParameters) o;
        return parameters != null ? parameters.equals(that.parameters) : that.parameters == null;
    }

    @Override
    public int hashCode() {
        return parameters != null ? parameters.hashCode() : 0;
    }
}
