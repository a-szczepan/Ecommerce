import * as actionTypes from "./shopping-types";

const INITIAL_STATE = {
  user: "",
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

const shopReducer = (state = INITIAL_STATE, action) => {
  switch (action.type) {
    case actionTypes.SET_USER:
      return {
        ...state,
        loading: false,
        user: action.payload,
      };

    case actionTypes.LOAD_ACCOUNT_INFO:
      return {
        ...state,
        loading: false,
        account: action.payload,
      };

    case actionTypes.CREATE_ACCOUNT_INFO:
      return {
        ...state,
        loading: false,
        account: action.payload,
      };
    case actionTypes.DELETE_ACCOUNT_INFO:
      return {
        ...state,
        loading: false,
        account: action.payload,
      };
    case actionTypes.LOAD_SHIPMENT_INFO:
      return {
        ...state,
        loading: false,
        shipping: action.payload,
      };

    case actionTypes.CREATE_SHIPMENT_INFO:
      return {
        ...state,
        loading: false,
        shipping: action.payload,
      };

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

    case actionTypes.LOAD_CART:
      const cart = [];
      const allProducts = state.products;
      allProducts.map((product) => {
        action.payload.map((cart_item) => {
          if (product.id === cart_item.product_id) {
            let newProd = product;
            newProd["quantity"] = cart_item.quantity;
            newProd["cart_id"] = cart_item.id;
            newProd["providerKey"] = cart_item.providerKey;
            cart.push(newProd);
          }
        });
      });
      let sumAfterLoad = 0;
      cart.map((product) => (sumAfterLoad += parseFloat(product.price)));
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
      let product = state.products.filter(
        (product) => product.id === action.payload.product_id
      );
      product = product[0];
      product["quantity"] = action.payload.quantity;
      product["cart_id"] = action.payload.id;
      product["providerKey"] = action.payload.providerKey;
      state.cart.push(product);
      let sum = 0;
      state.cart.map(
        (product) => (sum += parseFloat(product.price) * product.quantity)
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
      let newCart = [];

      if (state.cart.length > 0) {
        console.log("tuu");
        newCart = state.cart.filter(
          (product) => product.cart_id !== action.payload
        );
      } else {
        newCart = [];
      }
      let newSum = 0;
      newCart.map(
        (product) => (newSum += parseFloat(product.price) * product.quantity)
      );
      newSum = new Intl.NumberFormat("pl-PL", {
        style: "currency",
        currency: "PLN",
      }).format(newSum);
      return {
        ...state,
        loading: false,
        cart: newCart,
        cartSum: newSum,
      };
    case actionTypes.CART_QUANTITY_UP:
      state.cart.map((product) =>
        product.cart_id === action.payload
          ? (product.quantity += 1)
          : product.quantity
      );

      let quantityUpSum = 0;
      state.cart.map(
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
      state.cart.map((product) =>
        product.cart_id === action.payload
          ? product.quantity > 0
            ? (product.quantity -= 1)
            : product.quantity
          : product.quantity
      );

      let quantityDownSum = 0;
      state.cart.map(
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
    case actionTypes.REMOVE_FROM_WISHLIST:
      let newWishlistProducts = [];
      if (state.wishlistProducts.length > 0) {
        newWishlistProducts = state.wishlistProducts.filter(
          ({ id }) => id !== action.payload
        );
      } else {
        newWishlistProducts = [];
      }
      return {
        ...state,
        loading: false,
        wishlistProducts: newWishlistProducts,
      };
    case actionTypes.ADD_TO_WISHLIST:
      state.wishlist.push(action.payload);
      return {
        ...state,
        loading: false,
        wishlist: state.wishlist,
      };

    case actionTypes.CREATE_ORDER:
      state.orders.push(action.payload);
      return {
        ...state,
        loading: false,
        orders: state.orders,
        cartSum: state.cartSum,
        cartId: action.payload.id
      };

    case actionTypes.CREATE_PAYMENT:
      state.payments.push(action.payload)
      return {
        ...state,
        loading: false,
        payments: state.payments
      }
    default:
      return state;
  }
};

export default shopReducer;
