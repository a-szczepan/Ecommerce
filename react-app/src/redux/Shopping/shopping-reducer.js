import * as actionTypes from "./shopping-types";

const INITIAL_STATE = {
  products: [],
  wishlist: [],
  wishlistProducts: [],
  cart: [],
  cartSum: 0,
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

    case actionTypes.LOAD_CART:
      const cart = []
      const allProducts = state.products
        allProducts.map(product => {
          action.payload.map( cart_item => {
                if (product.id === cart_item.product_id){
                    let newProd = product
                    newProd["quantity"] = cart_item.quantity
                    newProd["cart_id"] = cart_item.id
                    newProd["user_id"] = cart_item.user_id
                    cart.push(newProd)
                }
              }
          )
        })
      return {
        ...state,
        loading: false,
        cart: cart
      };

    case actionTypes.ADD_TO_CART:
      let product = state.products.filter(product=> product.id === action.payload.product_id)
        product=product[0]
        product["quantity"] = action.payload.quantity
        product["cart_id"] = action.payload.id
        product["user_id"] = action.payload.user_id
      state.cart.push(product);
      return {
        ...state,
        loading: false,
        cart: state.cart,
      };
    case actionTypes.REMOVE_FROM_CART:
      let newCart = []
        if(state.cart.length > 1 ){
          newCart = state.cart.filter(product => product.id !== action.payload)
        } else {
          newCart = []
        }
      return {
        ...state,
        loading: false,
        cart: newCart
      };

    case actionTypes.UPDATE_CART_SUM:
      let updatedSum = 0
          state.cart.map(x=> updatedSum+=x.price)
        console.log(updatedSum)
      return {
        ...state,
        loading: false,
        cartSum: updatedSum
      }
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
      return state;
  }
};

export default shopReducer;
