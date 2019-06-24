package cc.before30.home.sample.config;

import org.springframework.boot.actuate.info.Info;
import org.springframework.boot.actuate.info.InfoContributor;
import org.springframework.stereotype.Component;

/**
 * SampleAppInfoContributor
 *
 * @author before30
 * @since 2019-06-24
 */
@Component
public class SampleAppInfoContributor implements InfoContributor {

    @Override
    public void contribute(Info.Builder builder) {
    }
}
