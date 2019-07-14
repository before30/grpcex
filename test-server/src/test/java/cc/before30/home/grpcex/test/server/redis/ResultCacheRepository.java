package cc.before30.home.grpcex.test.server.redis;

import cc.before30.home.grpcex.test.server.IoExAppContext;
import cc.before30.home.grpcex.test.server.io.IoExRequest;
import org.apache.commons.lang3.StringUtils;
import org.springframework.cache.CacheManager;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;

import javax.annotation.Nonnull;
import java.time.Duration;

/**
 * ResultCacheRepository
 *
 * @author before30
 * @since 2019-07-14
 */
public class ResultCacheRepository {
    public static Duration DEFAULT_EXPIRY = Duration.ofHours(2);

    private final RedisClient redisClient;
    private final RedisClient shadowRedisClient;
    private final Duration cacheExpiry;

    public ResultCacheRepository(@Nonnull IoExAppContext context,
                                 @Nonnull RedisClient client,
                                 @Nonnull RedisClient shadowRedisClient) {
        this.redisClient = client;
        this.shadowRedisClient = shadowRedisClient;
        this.cacheExpiry = context.getCacheExpiry();
    }

    private String getMessageKey(@Nonnull final IoExRequest request) {
        return request.toString();
    }

    public String getResult(@Nonnull final IoExRequest request) {
        return null;
    }

    public void setResult(@Nonnull final IoExRequest request, @Nonnull String response) {
//        redisClient.store()
        // if (shadowRedisClient != null && dualwrite)
    }
}
