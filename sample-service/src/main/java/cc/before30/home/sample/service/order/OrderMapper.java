package cc.before30.home.sample.service.order;

import cc.before30.home.sample.domain.order.Order;
import cc.before30.home.sample.domain.order.OrderLineItem;
import cc.before30.home.sample.domain.order.OrderOption;
import cc.before30.home.sample.domain.order.OrderOptionGroup;
import cc.before30.home.sample.domain.shop.Menu;
import cc.before30.home.sample.domain.shop.MenuRepository;
import cc.before30.home.sample.domain.shop.Shop;
import cc.before30.home.sample.domain.shop.ShopRepository;
import org.springframework.stereotype.Component;

import java.util.stream.Collectors;

import static java.util.stream.Collectors.toList;

/**
 * OrderMapper
 *
 * @author before30
 * @since 2019-06-23
 */

@Component
public class OrderMapper {
    private MenuRepository menuRepository;
    private ShopRepository shopRepository;

    public OrderMapper(MenuRepository menuRepository, ShopRepository shopRepository) {
        this.menuRepository = menuRepository;
        this.shopRepository = shopRepository;
    }

    public Order mapFrom(Cart cart) {
        Shop shop = shopRepository.findById(cart.getShopId())
                .orElseThrow(IllegalArgumentException::new);

        return new Order(
                cart.getUserId(),
                shop.getId(),
                cart.getCartLineItem()
                .stream()
                .map(this::toOrderLineItem)
                .collect(toList()));
    }

    private OrderLineItem toOrderLineItem(Cart.CartLineItem cartLineItem) {
        Menu menu = menuRepository.findById(cartLineItem.getMenuId())
                .orElseThrow(IllegalArgumentException::new);

        return new OrderLineItem(
                menu.getId(),
                cartLineItem.getName(),
                cartLineItem.getCount(),
                cartLineItem.getGroups()
                        .stream()
                        .map(this::toOrderOptionGroup)
                        .collect(Collectors.toList()));
    }

    private OrderOptionGroup toOrderOptionGroup(Cart.CartOptionGroup cartOptionGroup) {
        return new OrderOptionGroup(
                cartOptionGroup.getName(),
                cartOptionGroup.getOptions()
                        .stream()
                        .map(this::toOrderOption)
                        .collect(Collectors.toList()));
    }

    private OrderOption toOrderOption(Cart.CartOption cartOption) {
        return new OrderOption(
                cartOption.getName(),
                cartOption.getPrice());
    }
}
