import * as actionTypes from "./shopping-types";

const INITIAL_STATE = {
  products: [],
  wishlist: [],
  wishlistProducts: [],
  loading: false,
};

const shopReducer = (state = INITIAL_STATE, action) => {
  switch (action.type) {
    case actionTypes.LOAD_PRODUCTS:
      return {
        ...state,
        loading: false,
        wishlist: state.wishlist,
        products: action.payload,
        wishlistProducts: state.wishlistProducts,
      };
    case actionTypes.ADD_TO_CART:
      return {};
    case actionTypes.REMOVE_FROM_CART:
      return {};
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
    default:
      //jesli nic sie nie zmieni
      return state;
  }
};

export default shopReducer;
