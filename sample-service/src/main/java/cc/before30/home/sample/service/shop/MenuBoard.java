package cc.before30.home.sample.service.shop;

import cc.before30.home.sample.domain.generic.money.Money;
import cc.before30.home.sample.domain.shop.Menu;
import cc.before30.home.sample.domain.shop.Shop;
import lombok.Data;

import java.util.List;
import java.util.stream.Collectors;

/**
 * MenuBoard
 *
 * @author before30
 * @since 2019-06-23
 */

@Data
public class MenuBoard {
    private Long shopId;
    private String shopName;
    private boolean open;
    private Money minOrderAmount;
    private List<MenuItem> menuItems;

    public MenuBoard(Shop shop) {
        this.shopId = shop.getId();
        this.shopName = shop.getName();
        this.open = shop.isOpen();
        this.minOrderAmount = shop.getMinOrderAmount();
        this.menuItems = toMenuItems(shop.getMenus());
    }

    private List<MenuItem> toMenuItems(List<Menu> menus) {
        return menus.stream().map(MenuItem::new).collect(Collectors.toList());
    }

    @Data
    public static class MenuItem {
        private Long menuId;
        private String menuName;
        private Money menuBasePrice;
        private String menuDescription;

        public MenuItem(Menu menu) {
            this.menuId = menu.getId();
            this.menuName = menu.getName();
            this.menuBasePrice = menu.getBasePrice();
            this.menuDescription = menu.getDescription();
        }
    }
}
