package cc.before30.home.sample.domain.shop;

import lombok.Builder;
import lombok.Data;

import java.util.List;

/**
 * OptionGroup
 *
 * @author before30
 * @since 2019-06-23
 */

@Data
public class OptionGroup {
    private String name;
    private List<Option> options;

    @Builder
    public OptionGroup(String name, List<Option> options) {
        this.name = name;
        this.options = options;
    }
}
