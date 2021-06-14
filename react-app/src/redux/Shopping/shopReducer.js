import * as actionTypes from "./shopping-types";

const INITIAL_STATE = {
  user: "",
  categories: [],
  currentCategory: "all",
  account: "create",
  shipping: "create",
  products: [],
  wishlist: [],
  wishlistProducts: [],
  cart: [],
  cartSum: 0,
  cartId: 0,
  orders: [],
  payments: [],
  loading: false,
};

const shopReducer = (state = INITIAL_STATE, action = {}) => {
  switch (action.type) {
    case actionTypes.LOAD_CART:
      const cart = [];
      const allProducts = state.products;
      allProducts.forEach((product) => {
        action.payload.forEach((cart_item) => {
          if (product.id === cart_item.product_id) {
            let loadCartNew = {
              ...product,
              quantity: cart_item.quantity,
              cart_id: cart_item.id,
              providerKey: cart_item.providerKey,
            };
            cart.push(loadCartNew);
          }
        });
      });
      let sumAfterLoad = 0;
      cart.forEach(
        (product) =>
          (sumAfterLoad += parseFloat(product.price * product.quantity))
      );
      sumAfterLoad = new Intl.NumberFormat("pl-PL", {
        style: "currency",
        currency: "PLN",
      }).format(sumAfterLoad);
      return {
        ...state,
        loading: false,
        cart: cart,
        cartSum: sumAfterLoad,
      };
    case actionTypes.ADD_TO_CART:
      let filteredProd = state.products.filter(
        (product) => product.id === action.payload.product_id
      );
      filteredProd = filteredProd[0];
      let addToCartProd = {
        ...filteredProd,
        quantity: action.payload.quantity,
        cart_id: action.payload.id,
        providerKey: action.payload.providerKey,
      };
      state.cart.push(addToCartProd);
      let sum = 0;
      state.cart.forEach(
        (cartItem) => (sum += parseFloat(cartItem.price) * cartItem.quantity)
      );
      sum = new Intl.NumberFormat("pl-PL", {
        style: "currency",
        currency: "PLN",
      }).format(sum);
      return {
        ...state,
        loading: false,
        cart: state.cart,
        cartSum: sum,
      };
    case actionTypes.REMOVE_FROM_CART:
      const cartWithoutItem = removeFromCart(state, action.payload);
      let newSum = 0;
      newCart.forEach(
        (product) => (newSum += parseFloat(product.price) * product.quantity)
      );
      newSum = new Intl.NumberFormat("pl-PL", {
        style: "currency",
        currency: "PLN",
      }).format(newSum);
      return {
        ...state,
        loading: false,
        cart: cartWithoutItem,
        cartSum: newSum,
      };
    case actionTypes.CART_QUANTITY_UP:
      state.cart.forEach((product) =>
        product.cart_id === action.payload
          ? product.quantity++
          : product.quantity
      );
      let quantityUpSum = 0;
      state.cart.forEach(
        (product) =>
          (quantityUpSum += parseFloat(product.price) * product.quantity)
      );
      quantityUpSum = new Intl.NumberFormat("pl-PL", {
        style: "currency",
        currency: "PLN",
      }).format(quantityUpSum);

      return {
        ...state,
        loading: false,
        cart: state.cart,
        cartSum: quantityUpSum,
      };
    case actionTypes.CART_QUANTITY_DOWN:
      state.cart.forEach((product) =>
        product.cart_id === action.payload
          ? product.quantity > 0
            ? product.quantity--
            : product.quantity
          : product.quantity
      );
      let quantityDownSum = 0;
      state.cart.forEach(
        (product) =>
          (quantityDownSum += parseFloat(product.price) * product.quantity)
      );
      quantityDownSum = new Intl.NumberFormat("pl-PL", {
        style: "currency",
        currency: "PLN",
      }).format(quantityDownSum);

      return {
        ...state,
        loading: false,
        cart: state.cart,
        cartSum: quantityDownSum,
      };

    case actionTypes.REMOVE_FROM_WISHLIST:
      const wishlistWithoutItem = removeFromWishlist(state, action.payload);
      return {
        ...state,
        loading: false,
        wishlistProducts: wishlistWithoutItem,
      };
    case actionTypes.ADD_TO_WISHLIST:
      state.wishlist.push(action.payload);
      return {
        ...state,
        loading: false,
        wishlist: state.wishlist,
      };

    case actionTypes.LOAD_PAYMENTS:
    case actionTypes.CREATE_PAYMENT:
      state.payments.push(action.payload);
      return {
        ...state,
        loading: false,
        payments: state.payments,
      };

    case actionTypes.CREATE_ORDER:
      state.orders.push(action.payload);
      return {
        ...state,
        loading: false,
        orders: state.orders,
        cartSum: state.cartSum,
        cartId: action.payload.id,
      };

    case actionTypes.LOAD_CATEGORIES:
      state.categories.push(action.payload);
      return {
        ...state,
        loading: false,
        categories: action.payload,
        currentCategory: "all",
      };

    case actionTypes.SET_CURRENT_CATEGORY:
      return {
        ...state,
        loading: false,
        currentCategory: action.payload,
      };
    case actionTypes.SET_USER:
    case actionTypes.LOG_OUT:
      return {
        ...state,
        loading: false,
        user: action.payload,
      };
    case actionTypes.LOAD_ACCOUNT_INFO:
    case actionTypes.CREATE_ACCOUNT_INFO:
    case actionTypes.DELETE_ACCOUNT_INFO:
      return {
        ...state,
        loading: false,
        account: action.payload,
      };
    case actionTypes.LOAD_SHIPMENT_INFO:
    case actionTypes.CREATE_SHIPMENT_INFO:
    case actionTypes.DELETE_SHIPMENT_INFO:
      return {
        ...state,
        loading: false,
        shipping: action.payload,
      };

    case actionTypes.LOAD_PRODUCTS:
      return {
        ...state,
        loading: false,
        wishlist: state.wishlist,
        products: action.payload,
        wishlistProducts: state.wishlistProducts,
      };
    case actionTypes.LOAD_WISHLIST:
      return {
        ...state,
        loading: false,
        wishlist: action.payload,
        products: state.products,
        wishlistProducts: state.wishlistProducts,
      };
    case actionTypes.LOAD_WISHLIST_PRODUCTS:
      return {
        ...state,
        loading: false,
        wishlistProducts: action.payload,
      };
    case actionTypes.LOAD_ORDERS:
      return {
        ...state,
        loading: false,
        orders: action.payload,
      };
    default:
      return state;
  }
};
/*UTILS*/
const removeFromWishlist = (state, action) => {
  if (state.wishlistProducts.length > 0) {
    return state.wishlistProducts.filter(({ id }) => id !== action);
  } else {
    return [];
  }
};

const removeFromCart = (state, action) => {
  if (state.cart.length > 0) {
    return state.cart.filter((product) => product.cart_id !== action.payload);
  } else {
    return [];
  }
};

export default shopReducer;
