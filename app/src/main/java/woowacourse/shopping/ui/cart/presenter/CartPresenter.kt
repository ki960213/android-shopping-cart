package woowacourse.shopping.ui.cart.presenter

import woowacourse.shopping.domain.CartItem
import woowacourse.shopping.repository.CartItemRepository
import woowacourse.shopping.ui.cart.CartContract
import woowacourse.shopping.ui.cart.uistate.CartItemUIState
import java.lang.IllegalStateException
import kotlin.properties.Delegates

class CartPresenter(
    private val view: CartContract.View,
    private val cartItemRepository: CartItemRepository,
) : CartContract.Presenter {

    private var _currentPage by Delegates.observable(0) { _, _, new ->
        showCartItems(new, selectedCartItems, true)
        showPageUI(new)
        showAllSelectionUI(new, selectedCartItems)
    }
    private var selectedCartItems: Set<CartItem> by Delegates.observable(setOf()) { _, _, new ->
        showAllSelectionUI(_currentPage, new)
        showOrderUI(new)
    }

    override val currentPage: Int
        get() = _currentPage

    override val selectedCartItemIds: List<Long>
        get() = selectedCartItems.map {
            it.id ?: throw IllegalStateException("아이디가 부여되지 않은 장바구니 아이템은 선택될 수 없습니다.")
        }

    override fun restoreCurrentPage(currentPage: Int) {
        this._currentPage = currentPage
    }

    override fun restoreSelectedCartItems(cartItemIds: List<Long>) {
        this.selectedCartItems = cartItemRepository.findAllByIds(cartItemIds).toSet()
        showCartItems(_currentPage, selectedCartItems, false)
    }

    override fun onLoadCartItemsOfNextPage() {
        _currentPage++
    }

    override fun onLoadCartItemsOfPreviousPage() {
        _currentPage--
    }

    override fun onLoadCartItemsOfLastPage() {
        _currentPage = (cartItemRepository.countAll() - 1) / PAGE_SIZE + 1
    }

    override fun onDeleteCartItem(cartItemId: Long) {
        cartItemRepository.deleteById(cartItemId)
        selectedCartItems = selectedCartItems.filter { it.id != cartItemId }.toSet()
        showPageUI(_currentPage)
        showCartItems(_currentPage, selectedCartItems, false)
    }

    override fun onChangeSelectionOfCartItem(cartItemId: Long, isSelected: Boolean) {
        val cartItem = cartItemRepository.findById(cartItemId) ?: return

        selectedCartItems = if (isSelected) {
            selectedCartItems + cartItem
        } else {
            selectedCartItems - cartItem
        }
    }

    override fun onChangeSelectionOfAllCartItems(isSelected: Boolean) {
        fun getCartItemsOfCurrentPage(): List<CartItem> {
            val offset = (_currentPage - 1) * PAGE_SIZE
            return cartItemRepository.findAllOrderByAddedTime(PAGE_SIZE, offset)
        }

        val cartItemsOfCurrentPage = getCartItemsOfCurrentPage()
        if (isSelected) {
            selectedCartItems = selectedCartItems + cartItemsOfCurrentPage
            showCartItems(_currentPage, selectedCartItems, false)
            return
        }
        if (cartItemsOfCurrentPage.all { it in selectedCartItems }) {
            selectedCartItems = selectedCartItems - cartItemsOfCurrentPage.toSet()
            showCartItems(_currentPage, selectedCartItems, false)
        }
    }

    override fun onPlusCount(cartItemId: Long) {
        val cartItem = cartItemRepository.findById(cartItemId) ?: return
        cartItem.plusCount()
        cartItemRepository.updateCountById(cartItemId, cartItem.count)
        if (cartItem in selectedCartItems) {
            selectedCartItems = selectedCartItems - cartItem + cartItem
        }
        showCartItems(_currentPage, selectedCartItems, false)
    }

    override fun onMinusCount(cartItemId: Long) {
        val cartItem = cartItemRepository.findById(cartItemId) ?: return
        cartItem.minusCount()
        cartItemRepository.updateCountById(cartItemId, cartItem.count)
        if (cartItem in selectedCartItems) {
            selectedCartItems = selectedCartItems - cartItem + cartItem
        }
        showCartItems(_currentPage, selectedCartItems, false)
    }

    private fun showAllSelectionUI(currentPage: Int, selectedCartItems: Set<CartItem>) {
        val cartItems = getCartItemsOf(currentPage)
        view.setStateOfAllSelection(
            cartItems.all { it in selectedCartItems } && cartItems.isNotEmpty()
        )
    }

    private fun getCartItemsOf(page: Int): List<CartItem> {
        val offset = (page - 1) * PAGE_SIZE
        return cartItemRepository.findAllOrderByAddedTime(PAGE_SIZE, offset)
    }

    private fun showCartItems(page: Int, selectedCartItems: Set<CartItem>, initScroll: Boolean) {
        val cartItems = getCartItemsOf(page)
        val cartItemUIStates =
            cartItems.map { CartItemUIState.create(it, it in selectedCartItems) }
        view.setCartItems(cartItemUIStates, initScroll)
    }

    private fun showOrderUI(selectedCartItems: Set<CartItem>) {
        view.setOrderPrice(selectedCartItems.sumOf(CartItem::getOrderPrice))
        view.setOrderCount(selectedCartItems.size)
    }

    private fun showPageUI(currentPage: Int) {
        if (getMaxPage() <= 1) {
            view.setPageUIVisibility(false)
            return
        }
        view.setPageUIVisibility(true)
        view.setStateThatCanRequestPreviousPage(currentPage > 1)
        view.setStateThatCanRequestNextPage(currentPage < getMaxPage())
        view.setPage(currentPage)
    }

    private fun getMaxPage(): Int = (cartItemRepository.countAll() - 1) / PAGE_SIZE + 1

    companion object {
        private const val PAGE_SIZE = 5
    }
}
