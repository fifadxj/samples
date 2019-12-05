package sample.redis.config;

public interface Cache {
    Boolean put(String key, String value);

    Boolean put(String key, String value, int seconds);

    String get(String key);

    Boolean del(String key);

    Boolean lock(String key, String value, int seconds);

    Boolean unlock(String key, String value);
}
