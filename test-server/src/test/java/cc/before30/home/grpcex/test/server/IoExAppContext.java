package cc.before30.home.grpcex.test.server;

import lombok.Getter;

import java.time.Duration;

/**
 * IoExAppContext
 *
 * @author before30
 * @since 2019-07-15
 */
@Getter
public class IoExAppContext {

    private Duration cacheExpiry;

    private String cacheNamespace;

    private String shadowCacheNamespace;
}
