package sample.persistence;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * A mapping store for content values used by the {@link SQLiteDatabase}.
 *
 * @author ISchwarz
 */
public class ContentValues {

    private static final Logger LOGGER = LogManager.getLogger(ContentValues.class);
    private final Map<String, Object> keyValueMap = new HashMap<>();


    public ContentValues() {
    }

    public ContentValues(final ContentValues from) {
        putAll(from);
    }

    public void putNull(final String key) {
        putObject(key, null);
    }

    public void put(final String key, final String value) {
        putObject(key, value);
    }

    public void put(final String key, final Integer value) {
        putObject(key, value);
    }

    public void put(final String key, final Byte value) {
        putObject(key, value);
    }

    public void put(final String key, final Float value) {
        putObject(key, value);
    }

    public void put(final String key, final Short value) {
        putObject(key, value);
    }

    public void put(final String key, final byte[] value) {
        putObject(key, value);
    }

    public void put(final String key, final Double value) {
        putObject(key, value);
    }

    public void put(final String key, final Long value) {
        putObject(key, value);
    }

    public void put(final String key, final Boolean value) {
        putObject(key, value);
    }

    public void putAll(final ContentValues others) {
        keyValueMap.putAll(others.keyValueMap);
    }

    private void putObject(final String key, final Object value) {
        keyValueMap.put(key, value);
    }

    public Object get(String key) {
        return keyValueMap.get(key);
    }

    public String getAsString(final String key) {
        Object value = keyValueMap.get(key);
        return value != null ? value.toString() : null;
    }

    public Long getAsLong(String key) {
        Object value = keyValueMap.get(key);
        try {
            return value != null ? ((Number) value).longValue() : null;
        } catch (ClassCastException e) {
            if (value instanceof CharSequence) {
                try {
                    return Long.valueOf(value.toString());
                } catch (NumberFormatException e2) {
                    LOGGER.error("Cannot parse Long value for " + value + " at key " + key, e2);
                    return null;
                }
            } else {
                LOGGER.error("Cannot cast value for " + key + " to a Long: " + value, e);
                return null;
            }
        }
    }

    public Integer getAsInteger(String key) {
        Object value = keyValueMap.get(key);
        try {
            return value != null ? ((Number) value).intValue() : null;
        } catch (ClassCastException e) {
            if (value instanceof CharSequence) {
                try {
                    return Integer.valueOf(value.toString());
                } catch (NumberFormatException e2) {
                    LOGGER.error("Cannot parse Integer value for " + value + " at key " + key, e2);
                    return null;
                }
            } else {
                LOGGER.error("Cannot cast value for " + key + " to a Integer: " + value, e);
                return null;
            }
        }
    }

    /**
     * Gets a value and converts it to a Short.
     *
     * @param key the value to get
     * @return the Short value, or null if the value is missing or cannot be converted
     */
    public Short getAsShort(final String key) {
        Object value = keyValueMap.get(key);
        try {
            return value != null ? ((Number) value).shortValue() : null;
        } catch (ClassCastException e) {
            if (value instanceof CharSequence) {
                try {
                    return Short.valueOf(value.toString());
                } catch (NumberFormatException e2) {
                    LOGGER.error("Cannot parse Short value for " + value + " at key " + key, e2);
                    return null;
                }
            } else {
                LOGGER.error("Cannot cast value for " + key + " to a Short: " + value, e);
                return null;
            }
        }
    }

    /**
     * Gets a value and converts it to a Byte.
     *
     * @param key the value to get
     * @return the Byte value, or null if the value is missing or cannot be converted
     */
    public Byte getAsByte(String key) {
        Object value = keyValueMap.get(key);
        try {
            return value != null ? ((Number) value).byteValue() : null;
        } catch (ClassCastException e) {
            if (value instanceof CharSequence) {
                try {
                    return Byte.valueOf(value.toString());
                } catch (NumberFormatException e2) {
                    LOGGER.error("Cannot parse Byte value for " + value + " at key " + key, e2);
                    return null;
                }
            } else {
                LOGGER.error("Cannot cast value for " + key + " to a Byte: " + value, e);
                return null;
            }
        }
    }

    /**
     * Gets a value and converts it to a Double.
     *
     * @param key the value to get
     * @return the Double value, or null if the value is missing or cannot be converted
     */
    public Double getAsDouble(String key) {
        Object value = keyValueMap.get(key);
        try {
            return value != null ? ((Number) value).doubleValue() : null;
        } catch (ClassCastException e) {
            if (value instanceof CharSequence) {
                try {
                    return Double.valueOf(value.toString());
                } catch (NumberFormatException e2) {
                    LOGGER.error("Cannot parse Double value for " + value + " at key " + key, e2);
                    return null;
                }
            } else {
                LOGGER.error("Cannot cast value for " + key + " to a Double: " + value, e);
                return null;
            }
        }
    }

    /**
     * Gets a value and converts it to a Float.
     *
     * @param key the value to get
     * @return the Float value, or null if the value is missing or cannot be converted
     */
    public Float getAsFloat(String key) {
        Object value = keyValueMap.get(key);
        try {
            return value != null ? ((Number) value).floatValue() : null;
        } catch (ClassCastException e) {
            if (value instanceof CharSequence) {
                try {
                    return Float.valueOf(value.toString());
                } catch (NumberFormatException e2) {
                    LOGGER.error("Cannot parse Float value for " + value + " at key " + key, e2);
                    return null;
                }
            } else {
                LOGGER.error("Cannot cast value for " + key + " to a Float: " + value, e);
                return null;
            }
        }
    }

    /**
     * Gets a value and converts it to a Boolean.
     *
     * @param key the value to get
     * @return the Boolean value, or null if the value is missing or cannot be converted
     */
    public Boolean getAsBoolean(String key) {
        Object value = keyValueMap.get(key);
        try {
            return (Boolean) value;
        } catch (ClassCastException e) {
            if (value instanceof CharSequence) {
                return Boolean.valueOf(value.toString());
            } else if (value instanceof Number) {
                return ((Number) value).intValue() != 0;
            } else {
                LOGGER.error("Cannot cast value for " + key + " to a Boolean: " + value, e);
                return null;
            }
        }
    }

    /**
     * Gets a value that is a byte array. Note that this method will not convert
     * any other types to byte arrays.
     *
     * @param key the value to get
     * @return the byte[] value, or null is the value is missing or not a byte[]
     */
    public byte[] getAsByteArray(String key) {
        Object value = keyValueMap.get(key);
        if (value instanceof byte[]) {
            return (byte[]) value;
        } else {
            return null;
        }
    }


    public boolean containsKey(final String key) {
        return keyValueMap.containsKey(key);
    }

    public Object remove(final String key) {
        return keyValueMap.remove(key);
    }

    public void clear() {
        keyValueMap.clear();
    }

    public Set<Map.Entry<String, Object>> valueSet() {
        return keyValueMap.entrySet();
    }

    public Set<String> keySet() {
        return keyValueMap.keySet();
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (String name : keyValueMap.keySet()) {
            String value = getAsString(name);
            if (sb.length() > 0) sb.append(" ");
            sb.append(name).append("=").append(value);
        }
        return sb.toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        final ContentValues that = (ContentValues) o;
        return keyValueMap.equals(that.keyValueMap);
    }

    @Override
    public int hashCode() {
        return keyValueMap.hashCode();
    }
}
