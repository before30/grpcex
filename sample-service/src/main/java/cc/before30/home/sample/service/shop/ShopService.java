package cc.before30.home.sample.service.shop;

import cc.before30.home.sample.domain.shop.Shop;
import cc.before30.home.sample.domain.shop.ShopRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


/**
 * ShopService
 *
 * @author before30
 * @since 2019-06-23
 */

@Service
public class ShopService {
    private ShopRepository shopRepository;

    public ShopService(ShopRepository shopRepository) {
        this.shopRepository = shopRepository;
    }

    @Transactional(readOnly = true)
    public MenuBoard getMenuBoard(Long shopId) {
        Shop shop = shopRepository.findById(shopId).orElseThrow(IllegalAccessError::new);
        return new MenuBoard(shop);
    }
}
